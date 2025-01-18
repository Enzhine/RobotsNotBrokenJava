package ru.enzhine.rnb.world.robot.module.base;

import lombok.RequiredArgsConstructor;
import ru.enzhine.rnb.world.robot.RobotController;

@RequiredArgsConstructor
public abstract class BasicRobotModule implements RobotModule {

    protected final RobotController robotController;
    protected final RobotModuleType robotModuleType;
    protected boolean enabled = false;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public RobotModuleType getType() {
        return robotModuleType;
    }
}
