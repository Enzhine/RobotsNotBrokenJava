package ru.enzhine.rnb.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import ru.enzhine.rnb.editor.CodeEditor;
import ru.enzhine.rnb.editor.CodeEditorInputProcessor;
import ru.enzhine.rnb.server.RepeatingThreadExecutor;
import ru.enzhine.rnb.stages.input.WorldUIController;
import ru.enzhine.rnb.stages.input.MoveController;
import ru.enzhine.rnb.stages.input.ZoomController;
import ru.enzhine.rnb.stages.ui.HelpRenderer;
import ru.enzhine.rnb.world.WorldImpl;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.entity.Robot;
import ru.enzhine.rnb.world.entity.base.EntityType;
import ru.enzhine.rnb.world.item.base.ItemStackFactoryImpl;
import ru.enzhine.rnb.world.item.base.ItemType;
import ru.enzhine.rnb.world.robot.RobotController;
import space.earlygrey.shapedrawer.ShapeDrawer;

import javax.script.ScriptException;
import java.util.Random;

public class WorldStage extends Stage {

    public static final int TPS = 10;

    private final SpriteBatch batch;
    private final ShapeDrawer shapeDrawer;
    private final Texture shapeDrawerTexture;

    private final ExtendViewport worldViewport;
    private final ExtendViewport hudViewport;

    private final ZoomController zoomController;
    private final MoveController moveController;
    private final WorldUIController worldUIController;

    private final RepeatingThreadExecutor serverThread;
    private final WorldImpl w;

    private final HelpRenderer helpRenderer;

    public WorldStage(SpriteBatch batch) {
        this.batch = batch;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        shapeDrawerTexture = new Texture(pixmap);
        shapeDrawer = new ShapeDrawer(this.batch, new TextureRegion(shapeDrawerTexture, 0, 0, 1, 1));
        pixmap.dispose();

        worldViewport = new ExtendViewport(32 * WorldImpl.BLOCK_PIXEL_SIZE, 32 * WorldImpl.BLOCK_PIXEL_SIZE);
        hudViewport = new ExtendViewport(32 * WorldImpl.BLOCK_PIXEL_SIZE, 32 * WorldImpl.BLOCK_PIXEL_SIZE);
        setViewport(hudViewport);

        zoomController = new ZoomController((OrthographicCamera) worldViewport.getCamera(), 0.06f, 1f);
        moveController = new MoveController(worldViewport);

        w = new WorldImpl(10, 43, 0.2f, 0.2f);
        initWorld();
        serverThread = new RepeatingThreadExecutor(w::onTick, TPS);
        serverThread.execute();

        initUI();

        worldUIController = new WorldUIController(worldViewport, w, bootButton, codeButton, shutButton);
        helpRenderer = new HelpRenderer(serverThread, worldUIController);
        helpRenderer.setShowFps(true);
        helpRenderer.setShowTps(true);
        helpRenderer.setShowSelectionDetails(true);
    }

    private void initWorld() {
        long radius = 3;
        w.genRangeChunks(-2 * radius, 2 * radius, -2 * radius, 2 * radius);
//        w.genRangeChunks(0L, 0L, 0L, 0L);

        w.setBlock(BlockType.AIR, 4L, 4L);
        w.setBlock(BlockType.AIR, 5L, 4L);
        w.setBlock(BlockType.AIR, 4L, 5L);
        w.setBlock(BlockType.AIR, 5L, 5L);
        var robot = (Robot) w.summonEntity(EntityType.ROBOT, 5d, 4d);

        var itemStackFactory = new ItemStackFactoryImpl();
        var rand = new Random();
        robot.getInventory().setAt(0, itemStackFactory.makeItemstack(ItemType.COAL_ORE_SHARD, 1, rand));
        robot.pickHandItem(0);
        focus(robot.getLocation().getX().floatValue() * WorldImpl.BLOCK_PIXEL_SIZE, robot.getLocation().getY().floatValue() * WorldImpl.BLOCK_PIXEL_SIZE, 0.5f, 2f);
    }

    private Table table;
    private CodeEditor codeEditor;

    private TextButton codeButton;
    private TextButton bootButton;
    private TextButton shutButton;

    private void initUI() {
        table = new Table();
        table.setFillParent(true);
        table.align(Align.center);
        addActor(table);

        codeEditor = new CodeEditor();
        codeEditor.setVisible(false);
        table.add(codeEditor);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont();
        style.fontColor = Color.WHITE;

        codeButton = new TextButton("CODE", style);
        codeButton.align(Align.bottomRight);
        codeButton.setVisible(false);
        codeButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                codeEditor.setVisible(true);
                var codeEditorListener = new CodeEditorInputProcessor(() -> {
                    var robotController = (RobotController) worldUIController.getSelectedEntity();
                    var se = robotController.getScriptExecutor();
                    try {
                        var cs = se.compileScript(codeEditor.getText());
                        se.execute(cs);
                    } catch (ScriptException e) {
                        throw new RuntimeException(e);
                    }
                }, Gdx.input.getInputProcessor(), codeEditor);
                Gdx.input.setInputProcessor(codeEditorListener);
            }
        });

        bootButton = new TextButton("BOOT UP", style);
        bootButton.align(Align.bottomRight);
        bootButton.setVisible(false);
        bootButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                var robotController = (RobotController) worldUIController.getSelectedEntity();

                robotController.bootUp();
            }
        });

        shutButton = new TextButton("SHUT DOWN", style);
        shutButton.align(Align.bottomRight);
        shutButton.setVisible(false);
        shutButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                var robotController = (RobotController) worldUIController.getSelectedEntity();

                robotController.shutDown();
                robotController.getScriptExecutor().reset();
            }
        });

        var tempTable = new Table();
        tempTable.add(bootButton).row();
        tempTable.add(codeButton).row();
        tempTable.add(shutButton).row();
        tempTable.align(Align.right);

        table.add(tempTable).growX();
//        table.setDebug(true);
        addActor(table);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!super.touchDown(screenX, screenY, pointer, button)) {
            worldUIController.mouseClicked(screenX, screenY, pointer, button);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        worldUIController.mouseMoved(screenX, screenY);

        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        zoomController.onScroll(amountY);

        return super.scrolled(amountX, amountY);
    }

    public void resize(int width, int height) {
        worldViewport.update(width, height);
        hudViewport.update(width, height, true);
    }

    public void update() {
        float td = Gdx.graphics.getDeltaTime();
        moveController.sync(td);
        worldUIController.update(td);
    }

    @Override
    public void draw() {
        zoomController.sync(Gdx.graphics.getDeltaTime());
        batch.begin();

        worldViewport.apply();
        batch.setProjectionMatrix(worldViewport.getCamera().combined);
        w.render(batch, shapeDrawer, worldViewport);
        worldUIController.render(batch, shapeDrawer, worldViewport);

        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        helpRenderer.render(batch, shapeDrawer, hudViewport);

        batch.end();
        super.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeDrawerTexture.dispose();
        stopServerThread();

        super.dispose();
    }

    private void stopServerThread() {
        this.serverThread.cancel();
    }

    public void focus(float wX, float wY, float zoom, float seconds) {
        moveController.focusAt(wX, wY, seconds);
        zoomController.zoomTo(zoom, seconds);
    }
}
