package ru.enzhine.rnb.world.robot.module;

import org.graalvm.polyglot.HostAccess;
import ru.enzhine.rnb.world.block.base.Ticking;
import ru.enzhine.rnb.world.entity.base.PhysicalEntity;
import ru.enzhine.rnb.world.robot.RobotController;

public class TickingMotorModuleImpl extends BasicRobotModule implements MotorModule, Ticking {

    private final PowerModule mpm;

    private float speed;
    private final float absSpeed;
    private final float moveFactor;
    private final float disFactor;

    public TickingMotorModuleImpl(RobotController robotController, float absSpeed, float moveFactor, float dischargeFactor) {
        super(robotController);
        this.mpm = robotController.findModule(PowerModule.class);
        assert this.mpm != null;

        this.speed = 0f;
        this.absSpeed = absSpeed;
        this.moveFactor = moveFactor;
        assert dischargeFactor < 0f;
        this.disFactor = dischargeFactor;
    }

    @HostAccess.Export
    @Override
    public void setSpeed(double x) {
        if (!isEnabled()) {
            return;
        }

        speed = Math.max(-absSpeed, Math.min((float) x, absSpeed));
    }

    @HostAccess.Export
    @Override
    public double maxSpeed() {
        return absSpeed;
    }

    @Override
    public void onTick() {
        var robot = (PhysicalEntity) robotController;

        robot.appendVelocity(speed * moveFactor, 0f);
        mpm.chargeBy(Math.abs(speed) * disFactor);
    }
}
