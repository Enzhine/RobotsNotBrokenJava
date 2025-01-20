package ru.enzhine.rnb.world.robot;

import ru.enzhine.rnb.world.robot.module.*;
import ru.enzhine.rnb.world.robot.module.base.RobotModule;
import ru.enzhine.rnb.world.robot.module.base.RobotModuleType;

public class RobotModuleFactoryImpl implements RobotModuleFactory {
    @Override
    public RobotModule makeRobotModule(RobotModuleType robotModuleType, RobotController robotController) {
        return switch (robotModuleType) {
            case BATTERY_V1 -> new PowerModuleImpl(robotController, robotModuleType, 100f, 100f, -0.001f);
            case MOTOR_V1 -> new MotorModuleImpl(robotController, robotModuleType, 1f, 0.75f, -0.2f, -0.001f);
            case COLLIDER_V1 -> new CollisionModuleImpl(robotController, robotModuleType, 0.05f, -0.001f);
            case SPEEDOMETER -> new SpeedometerModuleImpl(robotController, robotModuleType, -0.001f);
            case CPU -> new CpuModuleImpl(robotController, robotModuleType);
        };
    }
}
