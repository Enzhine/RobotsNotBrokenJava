package ru.enzhine.rnb.world.robot.module.base;

public enum RobotModuleType {
    BATTERY_V1("battery"),
    MOTOR_V1("motor"),
    COLLIDER_V1("collider"),
    SPEEDOMETER("speedometer"),
    CPU("cpu");

    public final String placeholder;
    RobotModuleType(String placeholder) {
        this.placeholder = placeholder;
    }
}
