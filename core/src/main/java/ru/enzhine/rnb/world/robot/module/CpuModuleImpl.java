package ru.enzhine.rnb.world.robot.module;

import org.graalvm.polyglot.HostAccess;
import ru.enzhine.rnb.world.robot.RobotController;
import ru.enzhine.rnb.world.robot.module.base.BasicRobotModule;
import ru.enzhine.rnb.world.robot.module.base.CpuModule;
import ru.enzhine.rnb.world.robot.module.base.RobotModuleType;

public class CpuModuleImpl extends BasicRobotModule implements CpuModule {

    public CpuModuleImpl(RobotController robotController, RobotModuleType type) {
        super(robotController, type);
    }

    @HostAccess.Export
    @Override
    public void wait(double seconds) throws InterruptedException {
        Thread.sleep((long) (seconds * 1000));
    }
}
