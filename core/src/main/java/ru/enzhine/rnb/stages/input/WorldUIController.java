package ru.enzhine.rnb.stages.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import ru.enzhine.rnb.stages.WorldStage;
import ru.enzhine.rnb.utils.MathUtils;
import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.World;
import ru.enzhine.rnb.world.WorldImpl;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.Rendering;
import ru.enzhine.rnb.world.entity.Robot;
import ru.enzhine.rnb.world.entity.base.PhysicalEntity;
import ru.enzhine.rnb.world.entity.base.Entity;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ListIterator;

public class WorldUIController implements Rendering {

    private final Viewport viewport;
    private final World world;
    private TextButton codeButton;

    @Getter
    private Block currentBlock;
    @Getter
    private PhysicalEntity currentEntity;
    @Getter
    private Block selectedBlock;
    @Getter
    private PhysicalEntity selectedEntity;

    private final float secondsThreshold;
    private float secondsAccumulation;


    public WorldUIController(Viewport viewport, World world, TextButton codeButton) {
        this.viewport = viewport;
        this.world = world;
        this.codeButton = codeButton;

        this.secondsThreshold = 1f;
        this.secondsAccumulation = 0f;
    }

    public void update(float deltaTime) {
        secondsAccumulation += deltaTime;
        if (secondsAccumulation > secondsThreshold) {
            secondsAccumulation = 0;
        }
    }

    public void mouseMoved(int screenX, int screenY) {
        currentBlock = null;
        currentEntity = null;

        var proj = viewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
        var blockX = MathUtils.blockPos(proj.x / WorldImpl.BLOCK_PIXEL_SIZE);
        var blockY = MathUtils.blockPos(proj.y / WorldImpl.BLOCK_PIXEL_SIZE);

        Block block = world.getBlock(blockX, blockY, false);
        if (block == null) {
            return;
        }
        Chunk chunk = block.getLocation().getChunk();
        for (ListIterator<Entity> it = chunk.getEntities(); it.hasNext(); ) {
            Entity e = it.next();

            if (e instanceof PhysicalEntity ce && ce.getBoundingBox().collides(proj.x, proj.y)) {
                currentEntity = ce;
                break;
            }
        }
        if (currentEntity == null) {
            currentBlock = block;
        }
    }

    public void mouseClicked(int screenX, int screenY, int pointer, int button) {
        mouseMoved(screenX, screenY);

        if (button == Input.Buttons.LEFT) {
            if (currentEntity != null) {
                selectedEntity = currentEntity;
                selectedBlock = null;
            } else if (currentBlock != null) {
                selectedBlock = currentBlock;
                selectedEntity = null;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, ShapeDrawer drawer, Viewport viewport) {
        outlineSelected(drawer);
        outlineCursor(drawer);
        updateUI();
    }

    public void outlineSelected(ShapeDrawer shapeDrawer) {
        var progress = secondsAccumulation / secondsThreshold;
        var scale = viewport.getWorldWidth() / viewport.getScreenWidth() * getZoom();

        if (selectedEntity != null) {
            var bb = selectedEntity.getBoundingBox();
            shapeDrawer.rectangle(
                    (float) (bb.getX() * WorldImpl.BLOCK_PIXEL_SIZE),
                    (float) (bb.getY() * WorldImpl.BLOCK_PIXEL_SIZE),
                    bb.getPxWidth(),
                    bb.getPxHeight(),
                    MathUtils.bilerp(Color.WHITE, Color.BLACK, progress),
                    4f * scale
            );
        } else if (selectedBlock != null) {
            var blockLoc = selectedBlock.getLocation();
            shapeDrawer.rectangle(
                    (float) (blockLoc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE),
                    (float) (blockLoc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE),
                    WorldImpl.BLOCK_PIXEL_SIZE,
                    WorldImpl.BLOCK_PIXEL_SIZE,
                    MathUtils.bilerp(Color.WHITE, Color.BLACK, progress),
                    4f * scale
            );
        }
    }

    public void outlineCursor(ShapeDrawer shapeDrawer) {
        var scale = viewport.getWorldWidth() / viewport.getScreenWidth() * getZoom();

        if (currentEntity != null) {
            var bb = currentEntity.getBoundingBox();
            shapeDrawer.rectangle(
                    (float) (bb.getX() * WorldImpl.BLOCK_PIXEL_SIZE),
                    (float) (bb.getY() * WorldImpl.BLOCK_PIXEL_SIZE),
                    bb.getPxWidth(),
                    bb.getPxHeight(),
                    Color.WHITE,
                    2f * scale
            );
        } else if (currentBlock != null) {
            var blockLoc = currentBlock.getLocation();
            shapeDrawer.rectangle(
                    (float) (blockLoc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE),
                    (float) (blockLoc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE),
                    WorldImpl.BLOCK_PIXEL_SIZE,
                    WorldImpl.BLOCK_PIXEL_SIZE,
                    Color.WHITE,
                    2f * scale
            );
        }
    }

    private void updateUI() {
        if (selectedEntity instanceof Robot) {
            codeButton.setVisible(true);
        } else {
            codeButton.setVisible(false);
        }
    }

    private float getZoom() {
        return ((OrthographicCamera) viewport.getCamera()).zoom;
    }
}
