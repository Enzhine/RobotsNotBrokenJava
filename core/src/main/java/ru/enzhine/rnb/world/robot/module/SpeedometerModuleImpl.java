package ru.enzhine.rnb.world.robot.module;

import org.graalvm.polyglot.HostAccess;
import ru.enzhine.rnb.world.block.base.Ticking;
import ru.enzhine.rnb.world.entity.base.PhysicalEntity;
import ru.enzhine.rnb.world.robot.RobotController;
import ru.enzhine.rnb.world.robot.module.base.BasicRobotModule;
import ru.enzhine.rnb.world.robot.module.base.PowerModule;
import ru.enzhine.rnb.world.robot.module.base.RobotModuleType;
import ru.enzhine.rnb.world.robot.module.base.SpeedometerModule;

public class SpeedometerModuleImpl extends BasicRobotModule implements SpeedometerModule, Ticking {

    private final PowerModule mpm;

    private final float tickDischarge;

    public SpeedometerModuleImpl(RobotController robotController, RobotModuleType type, float tickDischarge) {
        super(robotController, type);
        this.mpm = robotController.findModule(PowerModule.class);

        assert tickDischarge < 0f;
        this.tickDischarge = tickDischarge;
    }

    @HostAccess.Export
    @Override
    public double getX() {
        var ent = (PhysicalEntity) this.robotController;
        return ent.getActualVelocity().x;
    }

    @HostAccess.Export
    @Override
    public double getY() {
        var ent = (PhysicalEntity) this.robotController;
        return ent.getActualVelocity().y;
    }

    @Override
    public void onTick() {
        mpm.chargeBy(tickDischarge);
    }
}
