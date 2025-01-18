package ru.enzhine.rnb.world.robot.module;

import org.graalvm.polyglot.HostAccess;
import ru.enzhine.rnb.world.block.base.Ticking;
import ru.enzhine.rnb.world.robot.RobotController;
import ru.enzhine.rnb.world.robot.module.base.BasicRobotModule;
import ru.enzhine.rnb.world.robot.module.base.PowerModule;
import ru.enzhine.rnb.world.robot.module.base.RobotModuleType;

public class PowerModuleImpl extends BasicRobotModule implements PowerModule, Ticking {

    private float powerLevelCurrent;
    private final float powerLevelMax;
    private final float tickDischarge;

    public PowerModuleImpl(RobotController robotController, RobotModuleType type, float maxLevel, float initialCharge, float tickDischarge) {
        super(robotController, type);
        assert tickDischarge < 0f;
        this.tickDischarge = tickDischarge;

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

    @Override
    public void onTick() {
        chargeBy(tickDischarge);
    }
}
