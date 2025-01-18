package ru.enzhine.rnb.world.robot.module;

import org.graalvm.polyglot.HostAccess;
import ru.enzhine.rnb.utils.MathUtils;
import ru.enzhine.rnb.world.BoundingBox;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.Ticking;
import ru.enzhine.rnb.world.entity.base.Entity;
import ru.enzhine.rnb.world.entity.base.PhysicalEntity;
import ru.enzhine.rnb.world.robot.RobotController;
import ru.enzhine.rnb.world.robot.module.base.BasicRobotModule;
import ru.enzhine.rnb.world.robot.module.base.CollisionModule;
import ru.enzhine.rnb.world.robot.module.base.PowerModule;
import ru.enzhine.rnb.world.robot.module.base.RobotModuleType;

public class CollisionModuleImpl extends BasicRobotModule implements CollisionModule, Ticking {

    private final PowerModule mpm;
    private final float dist;
    private final float tickDischarge;

    public CollisionModuleImpl(RobotController robotController, RobotModuleType type, float checkDistance, float tickDischarge) {
        super(robotController, type);
        this.mpm = robotController.findModule(PowerModule.class);
        assert checkDistance > 0f;
        this.dist = checkDistance;
        assert tickDischarge < 0f;
        this.tickDischarge = tickDischarge;
    }

    @HostAccess.Export
    @Override
    public boolean isWallBy(int side) {
        if (!isEnabled()) {
            return false;
        }

        var entity = (PhysicalEntity) robotController;
        BoundingBox newBB;
        switch (side) {
            case 0 -> newBB = entity.getBoundingBox().translated(0, dist);
            case 1 -> newBB = entity.getBoundingBox().translated(dist, 0);
            case 2 -> newBB = entity.getBoundingBox().translated(0, -dist);
            case 3 -> newBB = entity.getBoundingBox().translated(-dist, 0);
            default -> {
                return false;
            }
        }

        var bottomLeft = getAt(entity, newBB.leftX(), newBB.bottomY());
        var topLeft = getAt(entity, newBB.leftX(), newBB.topY());
        var topRight = getAt(entity, newBB.rightX(), newBB.topY());
        var bottomRight = getAt(entity, newBB.rightX(), newBB.bottomY());

        return !bottomLeft.isPenetrable() || !topLeft.isPenetrable() ||
                !topRight.isPenetrable() || !bottomRight.isPenetrable();
    }

    private Block getAt(Entity entity, double x, double y) {
        var loc = entity.getLocation();

        if (loc.getChunk().contains(x, y)) {
            return new Location(x, y, loc.getChunk()).getBlock();
        } else {
            return loc.getWorld().getBlock(MathUtils.blockPos(x), MathUtils.blockPos(y), true);
        }
    }

    @Override
    public void onTick() {
        mpm.chargeBy(tickDischarge);
    }
}
