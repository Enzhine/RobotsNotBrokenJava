package ru.enzhine.rnb.world.entity.base;

import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.entity.RobotImpl;
import ru.enzhine.rnb.world.robot.RobotModuleFactoryImpl;
import ru.enzhine.rnb.world.robot.module.base.RobotModuleType;

public class EntityFactoryImpl implements EntityFactory {
    @Override
    public Entity makeEntity(EntityType entityType, double x, double y, Chunk c) {
        Location newLoc = new Location(x, y, c);
        return switch (entityType) {
            case ROBOT -> {
                var robotModuleFactory = new RobotModuleFactoryImpl();
                var inventory = new ArrayInventory(1);
                var robot = new RobotImpl(newLoc, inventory);

                robot.registerModule(robotModuleFactory.makeRobotModule(RobotModuleType.BATTERY_V1, robot));
                robot.registerModule(robotModuleFactory.makeRobotModule(RobotModuleType.MOTOR_V1, robot));
                robot.registerModule(robotModuleFactory.makeRobotModule(RobotModuleType.SPEEDOMETER, robot));
                robot.registerModule(robotModuleFactory.makeRobotModule(RobotModuleType.CPU, robot));

                yield robot;
            }
        };
    }
}
