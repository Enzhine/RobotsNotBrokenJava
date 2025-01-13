package ru.enzhine.rnb.world.robot.module;

import ru.enzhine.rnb.world.robot.RobotController;

public class MutablePowerModuleImpl extends BasicRobotModule implements MutablePowerModule {

    private float powerLevelCurrent;
    private final float powerLevelMax;

    public MutablePowerModuleImpl(RobotController robotController, float maxLevel, float initialCharge) {
        super(robotController);

        this.powerLevelMax = maxLevel;
        this.powerLevelCurrent = initialCharge;
    }

    @Override
    public float chargeBy(float delta) {
        if (powerLevelCurrent + delta > powerLevelMax) {
            var overflow = powerLevelCurrent + delta - powerLevelMax;
            powerLevelCurrent = powerLevelMax;

            return overflow;
        } else if (powerLevelCurrent + delta < 0) {
            var underflow = powerLevelCurrent + delta;
            powerLevelCurrent = 0;

            return underflow;
        } else {
            powerLevelCurrent += delta;

            if (powerLevelCurrent == 0f) {
                robotController.shutDown();
            }
            return 0f;
        }
    }

    @Override
    public boolean isCharged() {
        return powerLevelCurrent >= powerLevelMax;
    }

    @Override
    public float getMaxLevel() {
        return powerLevelMax;
    }

    @Override
    public float getCurrentLevel() {
        return powerLevelCurrent;
    }
}
