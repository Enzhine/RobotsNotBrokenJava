package ru.enzhine.rnb.stages.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import ru.enzhine.rnb.utils.MathUtils;
import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.World;
import ru.enzhine.rnb.world.WorldImpl;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.entity.base.Entity;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ListIterator;

public class WorldUIController {

    private final Viewport viewport;
    private final World world;

    @Getter
    private Block currentBlock;
    @Getter
    private Entity currentEntity;

    @Getter
    private Block selectedBlock;
    @Getter
    private Entity selectedEntity;

    private final float secondsThreshold;
    private float secondsAccumulation;

    public WorldUIController(Viewport viewport, World world) {
        this.viewport = viewport;
        this.world = world;

        this.secondsThreshold = 1f;
        this.secondsAccumulation = 0f;
    }

    public void update(float deltaTime) {
        secondsAccumulation += deltaTime;
        if (secondsAccumulation > secondsThreshold) {
            secondsAccumulation = 0;
        }

        currentBlock = null;
        currentEntity = null;

        var proj = getMouseWorldPos();
        var blockX = MathUtils.blockPos(proj.x / WorldImpl.BLOCK_PIXEL_SIZE);
        var blockY = MathUtils.blockPos(proj.y / WorldImpl.BLOCK_PIXEL_SIZE);

        Block block = world.getBlock(blockX, blockY, false);
        if (block == null) {
            return;
        }
        Chunk chunk = block.getLocation().getChunk();
        for (ListIterator<Entity> it = chunk.getEntities(); it.hasNext(); ) {
            Entity e = it.next();

            if (e.getBoundingBox().collides(proj.x, proj.y)) {
                currentEntity = e;
                break;
            }
        }
        if (currentEntity == null) {
            currentBlock = block;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (currentEntity != null) {
                selectedEntity = currentEntity;
                selectedBlock = null;
            } else if (currentBlock != null) {
                selectedBlock = currentBlock;
                selectedEntity = null;
            }
        }
    }

    public void onRender(ShapeDrawer shapeDrawer) {
        outlineCursor(shapeDrawer);
        outlineSelected(shapeDrawer);
    }

    public void outlineSelected(ShapeDrawer shapeDrawer) {
        var progress = secondsAccumulation / secondsThreshold;
        if (selectedEntity != null) {
            var bb = selectedEntity.getBoundingBox();
            shapeDrawer.rectangle(
                    (float) (bb.getX() * WorldImpl.BLOCK_PIXEL_SIZE),
                    (float) (bb.getY() * WorldImpl.BLOCK_PIXEL_SIZE),
                    bb.getPxWidth(),
                    bb.getPxHeight(),
                    MathUtils.bilerp(Color.WHITE, Color.BLACK, progress),
                    0.4f
            );
        } else if (selectedBlock != null) {
            var blockLoc = selectedBlock.getLocation();
            shapeDrawer.rectangle(
                    (float) (blockLoc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE),
                    (float) (blockLoc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE),
                    WorldImpl.BLOCK_PIXEL_SIZE,
                    WorldImpl.BLOCK_PIXEL_SIZE,
                    MathUtils.bilerp(Color.WHITE, Color.BLACK, progress),
                    0.4f
            );
        }
    }

    public void outlineCursor(ShapeDrawer shapeDrawer) {
        if (currentEntity != null) {
            var bb = currentEntity.getBoundingBox();
            shapeDrawer.rectangle(
                    (float) (bb.getX() * WorldImpl.BLOCK_PIXEL_SIZE),
                    (float) (bb.getY() * WorldImpl.BLOCK_PIXEL_SIZE),
                    bb.getPxWidth(),
                    bb.getPxHeight(),
                    Color.WHITE,
                    0.4f
            );
        } else if (currentBlock != null) {
            var blockLoc = currentBlock.getLocation();
            shapeDrawer.rectangle(
                    (float) (blockLoc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE),
                    (float) (blockLoc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE),
                    WorldImpl.BLOCK_PIXEL_SIZE,
                    WorldImpl.BLOCK_PIXEL_SIZE,
                    Color.WHITE,
                    0.4f
            );
        }
    }

    private Vector3 getMouseWorldPos() {
        return viewport.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }
}
