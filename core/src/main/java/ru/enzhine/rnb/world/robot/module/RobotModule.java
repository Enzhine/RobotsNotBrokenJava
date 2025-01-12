package ru.enzhine.rnb.world.robot.module;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.enzhine.rnb.world.robot.RobotController;

@RequiredArgsConstructor
public abstract class RobotModule {

    protected final RobotController robotController;
    @Getter
    @Setter
    protected boolean enabled = false;

    final void throwNotEnabled() {
        if (!enabled) {
            throw new RuntimeException("Module is disabled");
        }
    }
}
