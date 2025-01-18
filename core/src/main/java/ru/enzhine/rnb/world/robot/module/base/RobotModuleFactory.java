package ru.enzhine.rnb.world.robot.module.base;

import ru.enzhine.rnb.world.robot.RobotController;

public interface RobotModuleFactory {
    RobotModule makeRobotModule(RobotModuleType robotModuleType, RobotController robotController);
}
