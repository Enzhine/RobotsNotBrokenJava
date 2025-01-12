package ru.enzhine.rnb.world.robot.module;

public interface MutablePowerModule extends PowerModule {
    float chargeBy(float delta);

    boolean isCharged();
}
