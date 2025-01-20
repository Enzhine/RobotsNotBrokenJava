package ru.enzhine.rnb.world.robot;

import ru.enzhine.rnb.world.robot.module.base.RobotModule;
import ru.enzhine.rnb.world.robot.module.base.RobotModuleType;

public interface RobotModuleFactory {
    RobotModule makeRobotModule(RobotModuleType robotModuleType, RobotController robotController);
}
