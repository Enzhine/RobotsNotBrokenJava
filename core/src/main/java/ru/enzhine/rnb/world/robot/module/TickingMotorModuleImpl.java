package ru.enzhine.rnb.world.robot.module;

import ru.enzhine.rnb.world.entity.base.PhysicalEntity;
import ru.enzhine.rnb.world.robot.RobotController;

public class TickingMotorModuleImpl extends BasicRobotModule implements TickingMotorModule {

    private final MutablePowerModule mpm;

    private float speed;
    private final float absSpeed;
    private final float moveFactor;
    private final float disFactor;

    public TickingMotorModuleImpl(RobotController robotController, float absSpeed, float moveFactor, float dischargeFactor) {
        super(robotController);
        this.mpm = robotController.findModule(MutablePowerModule.class);
        assert this.mpm != null;

        this.speed = 0f;
        this.absSpeed = absSpeed;
        this.moveFactor = moveFactor;
        assert dischargeFactor < 0f;
        this.disFactor = dischargeFactor;
    }

    @Override
    public void setSpeed(float x) {
        if (!isEnabled()) {
            return;
        }

        speed = Math.max(-absSpeed, Math.min(x, absSpeed));
    }

    @Override
    public float maxSpeed() {
        return absSpeed;
    }

    @Override
    public void onTick() {
        var robot = (PhysicalEntity) robotController;

        robot.appendVelocity(speed * moveFactor, 0f);
        mpm.chargeBy(Math.abs(speed) * disFactor);
    }
}
