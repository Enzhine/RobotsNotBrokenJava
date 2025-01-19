package ru.enzhine.rnb.world.robot.module.base;

public interface CpuModule extends RobotModule {
    void wait(double seconds) throws InterruptedException;
}
