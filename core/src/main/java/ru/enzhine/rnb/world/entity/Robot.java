package ru.enzhine.rnb.world.entity;

import ru.enzhine.rnb.texture.TextureRenderers;
import ru.enzhine.rnb.texture.render.stateful.StatefulRenderingContext;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.WorldImpl;
import ru.enzhine.rnb.world.block.base.Ticking;
import ru.enzhine.rnb.world.entity.base.BasicEntity;
import ru.enzhine.rnb.world.BoundingBox;
import ru.enzhine.rnb.world.entity.base.EntityType;
import ru.enzhine.rnb.world.robot.GraalJavaScriptExecutor;
import ru.enzhine.rnb.world.robot.RobotController;
import ru.enzhine.rnb.world.robot.ScriptExecutor;
import ru.enzhine.rnb.world.robot.module.MutablePowerModuleImpl;
import ru.enzhine.rnb.world.robot.module.RobotModule;
import ru.enzhine.rnb.world.robot.module.TickingMotorModuleImpl;
import ru.enzhine.rnb.world.robot.module.open.MotorModule;
import ru.enzhine.rnb.world.robot.module.open.PowerModule;

import javax.script.ScriptException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class Robot extends BasicEntity implements RobotController, Ticking {

    private final List<RobotModule> modules;
    private final ScriptExecutor scriptExecutor;
    private final ExecutorService executorService;

    private boolean enabled;

    private static final int RS_ENABLED = 0;
    private static final int RS_DISABLED = 1;
    private static final int RS_IDLE = 2;

    public Robot(Location location) {
        super(TextureRenderers.getTextureRenderer("entity/robot.json"), EntityType.ROBOT, location, new BoundingBox(-5d / WorldImpl.BLOCK_PIXEL_SIZE, 0d, 11, 11));

        this.modules = new LinkedList<>();
        this.scriptExecutor = new GraalJavaScriptExecutor();
        this.executorService = Executors.newCachedThreadPool();
        this.enabled = false;

        registerModule(new MutablePowerModuleImpl(this, 100f, 100f));
        registerModule(new TickingMotorModuleImpl(this, 1f, 0.75f, -0.01f));
    }

    @Override
    public <T extends RobotModule> T findModule(Class<T> clazz) {
        for (RobotModule module : modules) {
            if (clazz.isInstance(module)) {
                return (T) module;
            }
        }
        return null;
    }

    @Override
    public void registerModule(RobotModule robotModule) {
        Class<?> clazz = robotModule.getClass();

        for (RobotModule module : modules) {
            if (module.getClass().equals(clazz)) {
                throw new RuntimeException(String.format("Module %s of class %s already registered", robotModule, clazz));
            }
        }

        modules.add(robotModule);
    }

    @Override
    public <T extends RobotModule> void eraseModule(Class<T> clazz) {
        modules.removeIf(module -> module.getClass().equals(clazz));
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public ScriptExecutor getScriptExecutor() {
        return scriptExecutor;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean canBootUp() {
        var pool = (ThreadPoolExecutor) executorService;
        return scriptExecutor.isOnceExecuted() && pool.getActiveCount() == 0;
    }

    @Override
    public boolean bootUp() {
        if (!canBootUp()) {
            return false;
        }
        putModules();

        toggleRobotSystem(true);
        executorService.submit(() -> {
            try {
                scriptExecutor.invoke("onBoot");
            } catch (ScriptException | NoSuchMethodException e) {
                toggleRobotSystem(false);
            }
            setRenderingStateIdling();
        });

        return true;
    }

    @Override
    public void shutDown() {
        if (!enabled) {
            return;
        }

        executorService.shutdown();
        final var future = executorService.submit(() -> {
            try {
                scriptExecutor.invoke("onShutdown");
            } catch (ScriptException | NoSuchMethodException ignored) {
            }
        });
        executorService.submit(() -> {
            try {
                future.get(1, TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException ignored) {
            } catch (TimeoutException e) {
                future.cancel(true);
            }
        });
        toggleRobotSystem(false);
    }

    private void toggleRobotSystem(boolean enabled) {
        this.enabled = enabled;
        for (RobotModule robotModule : modules) {
            robotModule.setEnabled(enabled);
        }

        updateRenderingState();
    }

    private void updateRenderingState() {
        var context = (StatefulRenderingContext) rendererContext;

        if (enabled) {
            context.setCurrentState(RS_ENABLED);
        } else {
            context.setCurrentState(RS_DISABLED);
        }
    }

    private void setRenderingStateIdling() {
        var context = (StatefulRenderingContext) rendererContext;

        context.setCurrentState(RS_IDLE);
    }

    @Override
    public void onTick() {
        for (RobotModule robotModule : modules) {
            if(!robotModule.isEnabled()) {
                return;
            }
            if (robotModule instanceof Ticking tickingModule) {
                tickingModule.onTick();
            }
        }
    }

    private void putModules() {
        for (RobotModule robotModule : modules) {
            if (robotModule instanceof MotorModule) {
                scriptExecutor.inject("motor", robotModule);
            }else if (robotModule instanceof PowerModule) {
                scriptExecutor.inject("battery", robotModule);
            }
        }
    }
}
