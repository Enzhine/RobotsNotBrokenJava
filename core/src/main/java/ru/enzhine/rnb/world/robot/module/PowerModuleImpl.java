package ru.enzhine.rnb.world.robot.module;

import org.graalvm.polyglot.HostAccess;
import ru.enzhine.rnb.world.robot.RobotController;

public class PowerModuleImpl extends BasicRobotModule implements PowerModule, RobotModule {

    private float powerLevelCurrent;
    private final float powerLevelMax;

    public PowerModuleImpl(RobotController robotController, float maxLevel, float initialCharge) {
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
        } else if (powerLevelCurrent + delta <= 0) {
            var underflow = powerLevelCurrent + delta;
            powerLevelCurrent = 0f;
            robotController.shutDown();

            return underflow;
        } else {
            powerLevelCurrent += delta;

            return 0f;
        }
    }

    @Override
    public boolean isCharged() {
        return powerLevelCurrent >= powerLevelMax;
    }

    @HostAccess.Export
    @Override
    public float getMaxLevel() {
        return powerLevelMax;
    }

    @HostAccess.Export
    @Override
    public float getCurrentLevel() {
        return powerLevelCurrent;
    }
}
