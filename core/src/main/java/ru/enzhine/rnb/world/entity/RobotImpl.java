package ru.enzhine.rnb.world.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.render.PlacedRendering;
import ru.enzhine.rnb.texture.TextureRenderers;
import ru.enzhine.rnb.texture.render.stateful.StatefulRenderingContext;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.WorldImpl;
import ru.enzhine.rnb.world.block.base.Ticking;
import ru.enzhine.rnb.world.entity.base.BasicEntity;
import ru.enzhine.rnb.world.BoundingBox;
import ru.enzhine.rnb.world.entity.base.EntityType;
import ru.enzhine.rnb.world.entity.base.Inventory;
import ru.enzhine.rnb.world.item.base.ItemStack;
import ru.enzhine.rnb.world.robot.GraalJavaScriptExecutor;
import ru.enzhine.rnb.world.robot.ScriptExecutor;
import ru.enzhine.rnb.world.robot.module.base.*;
import space.earlygrey.shapedrawer.ShapeDrawer;

import javax.script.ScriptException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class RobotImpl extends BasicEntity implements Robot {

    private static final int RS_ENABLED = 0;
    private static final int RS_DISABLED = 1;
    private static final int RS_IDLE = 2;

    private final List<RobotModule> modules;
    private final ScriptExecutor scriptExecutor;
    private final Inventory inv;

    private ExecutorService executorService;
    private boolean enabled;
    private ItemStack handItem;

    public RobotImpl(Location location, Inventory inv) {
        super(TextureRenderers.getTextureRenderer("entity/robot.json"), EntityType.ROBOT, location, new BoundingBox(0d, 0d, 11, 11));

        this.inv = inv;
        this.modules = new LinkedList<>();
        this.scriptExecutor = new GraalJavaScriptExecutor();
        this.enabled = false;
        resetExecutorService();
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }

    private void resetExecutorService() {
        executorService = Executors.newCachedThreadPool();
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

    private void putModules() {
        var alreadyPut = new LinkedHashSet<String>();

        for (RobotModule robotModule : modules) {
            var placeholder = robotModule.getType().placeholder;

            if (alreadyPut.add(placeholder)) {
                scriptExecutor.inject(placeholder, robotModule);
            } else {
                throw new RuntimeException("Executor can not contain modules with same placeholder");
            }
        }
    }

    @Override
    public boolean canBootUp() {
        var pool = (ThreadPoolExecutor) executorService;
        return !enabled && scriptExecutor.isOnceExecuted() && pool.getActiveCount() == 0;
    }

    @Override
    public boolean bootUp() {
        if (!canBootUp()) {
            return false;
        }
        resetExecutorService();
        putModules();

        toggleRobotSystem(true);
        executorService.submit(() -> {
            try {
                scriptExecutor.invoke("onBoot");
            } catch (ScriptException | NoSuchMethodException e) {
                System.out.println(e);
                toggleRobotSystem(false);
            }
        });

        return true;
    }

    @Override
    public void shutDown() {
        if (!enabled) {
            return;
        }

        final var future = executorService.submit(() -> {
            try {
                scriptExecutor.invoke("onShutdown");
            } catch (ScriptException | NoSuchMethodException ignored) {
            }
        });
        executorService.submit(() -> {
            try {
                future.get(1, TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException | TimeoutException ignored) {
            }
            try {
                getScriptExecutor().interrupt(Duration.of(1, ChronoUnit.SECONDS));
                executorService.shutdown();
            } catch (TimeoutException ignored) {
            }
        });
        toggleRobotSystem(false);
    }

    @Override
    public void callAsyncVoid(String funcName, Object... args) {
        executorService.submit(() -> {
            try {
                scriptExecutor.invoke(funcName, args);
            } catch (ScriptException | NoSuchMethodException ignored) {
            }
        });
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
            if (!robotModule.isEnabled()) {
                return;
            }
            if (robotModule instanceof Ticking tickingModule) {
                tickingModule.onTick();
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }


    @Override
    public ItemStack getHandItem() {
        return handItem;
    }

    @Override
    public void pickHandItem(int idx) {
        var hand = handItem;
        handItem = inv.getAt(idx);
        inv.setAt(idx, hand);
    }

    @Override
    public void render(SpriteBatch batch, ShapeDrawer drawer, Viewport viewport) {
        super.render(batch, drawer, viewport);
        if (handItem != null && handItem instanceof PlacedRendering item) {
            var center = 0.5d - getBoundingBox().getPxWidth() / 2d / WorldImpl.BLOCK_PIXEL_SIZE;
            item.renderAt(loc.getX() - center, loc.getY(), batch, drawer, viewport);
        }
    }
}
