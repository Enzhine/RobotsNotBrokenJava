package ru.enzhine.rnb.world.robot.module.base;

public interface PowerModule extends RobotModule {
    float getMaxLevel();

    float getCurrentLevel();

    float chargeBy(float delta);

    boolean isCharged();
}
