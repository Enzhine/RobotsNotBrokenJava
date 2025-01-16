package ru.enzhine.rnb.world.robot.module;

public interface PowerModule extends RobotModule {
    float getMaxLevel();

    float getCurrentLevel();

    float chargeBy(float delta);

    boolean isCharged();
}
