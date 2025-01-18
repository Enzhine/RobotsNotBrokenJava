package ru.enzhine.rnb.world.robot;

import ru.enzhine.rnb.world.robot.module.base.RobotModule;

import java.util.concurrent.ExecutorService;

public interface RobotController {
    <T extends RobotModule> T findModule(Class<T> clazz);

    void registerModule(RobotModule robotModule);

    <T extends RobotModule> void eraseModule(Class<T> clazz);

    ExecutorService getExecutorService();

    ScriptExecutor getScriptExecutor();

    boolean isEnabled();

    boolean canBootUp();

    boolean bootUp();

    void shutDown();

    void callAsyncVoid(String funcName, Object... args);
}
