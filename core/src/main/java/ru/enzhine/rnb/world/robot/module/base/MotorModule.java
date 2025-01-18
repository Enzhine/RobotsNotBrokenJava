package ru.enzhine.rnb.world.robot.module.base;

public interface MotorModule extends RobotModule {
    void setSpeed(double x);

    double getMaxSpeed();
}
