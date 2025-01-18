package ru.enzhine.rnb.world.robot.module.base;

public interface RobotModule {
    RobotModuleType getType();

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
