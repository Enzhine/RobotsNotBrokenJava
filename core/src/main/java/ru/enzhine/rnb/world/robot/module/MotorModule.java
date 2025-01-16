package ru.enzhine.rnb.world.robot.module;

public interface MotorModule extends RobotModule {
    void setSpeed(double x);

    double maxSpeed();
}
