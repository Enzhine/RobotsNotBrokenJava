package ru.enzhine.rnb.world.robot.module;

import ru.enzhine.rnb.world.robot.module.open.PowerModule;

public interface MutablePowerModule extends PowerModule, RobotModule {
    float chargeBy(float delta);

    boolean isCharged();
}
