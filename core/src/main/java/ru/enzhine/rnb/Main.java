//package ru.enzhine.rnb;
//
//import com.badlogic.gdx.ApplicationAdapter;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Pixmap;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.math.Vector3;
//import com.badlogic.gdx.utils.ScreenUtils;
//import com.badlogic.gdx.utils.viewport.ExtendViewport;
//import lombok.Getter;
//import org.openjdk.nashorn.api.scripting.NashornScriptEngine;
//import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
//import ru.enzhine.rnb.editor.CodeEditor;
//import ru.enzhine.rnb.editor.CodeEditorInputProcessor;
//import ru.enzhine.rnb.world.Location;
//import ru.enzhine.rnb.world.WorldImpl;
//import ru.enzhine.rnb.world.block.base.*;
//import ru.enzhine.rnb.world.entity.Robot;
//import ru.enzhine.rnb.world.entity.base.EntityType;
//import space.earlygrey.shapedrawer.ShapeDrawer;
//
//import java.time.Duration;
//import java.time.Instant;
//
//public class Main extends ApplicationAdapter {
//    SpriteBatch batch;
//    ShapeDrawer drawer;
//    Texture singleColorTexture;
//    ExtendViewport ev;
//    WorldImpl w;
//    Robot r;
//    CodeEditor ce;
//    BitmapFont font;
//
//    @Override
//    public void create() {
//        ce = new CodeEditor(0, 0);
//        w = new WorldImpl(10, 43, 0.2f, 0.2f);
//        ev = new ExtendViewport(32 * WorldImpl.BLOCK_PIXEL_SIZE, 32 * WorldImpl.BLOCK_PIXEL_SIZE);
//        long radius = 10;
//        w.genRangeChunks(-(2 * radius), 2 * radius, -radius, radius);
//        w.setBlock(BlockType.AIR, 4L, 4L);
//        w.setBlock(BlockType.AIR, 5L, 4L);
//        w.setBlock(BlockType.AIR, 4L, 5L);
//        w.setBlock(BlockType.AIR, 5L, 5L);
//        this.r = (Robot) w.summonEntity(EntityType.ROBOT, 5d, 4d);
//        NashornScriptEngineFactory sefactory = new NashornScriptEngineFactory();
//        NashornScriptEngine engine = (NashornScriptEngine) sefactory.getScriptEngine(cName -> cName.startsWith("java.util"));
////        NashornScriptEngine engine = (NashornScriptEngine) sefactory.getScriptEngine(new String[]{}, this.getClass().getClassLoader(), cName -> true);
//        engine.put("robot", this.r);
//
//        Gdx.input.setInputProcessor(new CodeEditorInputProcessor(ce, engine));
//
//        font = new BitmapFont();
//        batch = new SpriteBatch();
//        batch.setColor(Color.WHITE);
//        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
//        pixmap.setColor(Color.WHITE);
//        pixmap.drawPixel(0, 0);
//        this.singleColorTexture = new Texture(pixmap);
//        this.drawer = new ShapeDrawer(this.batch, new TextureRegion(this.singleColorTexture, 0, 0, 1, 1));
//        pixmap.dispose();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        super.resize(width, height);
//        ev.update(width, height);
//    }
//
//    private Vector3 getCurrentPos() {
//        return ev.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
//    }
//
//    private void drawBlockInfo() {
//        var proj = getCurrentPos();
//        var loc = new Location((double) proj.x / WorldImpl.BLOCK_PIXEL_SIZE, (double) proj.y / WorldImpl.BLOCK_PIXEL_SIZE, w.getChunk(0L, 0L, false));
//        var chunk = w.getChunk(loc.getBlockX(), loc.getBlockY(), false);
//        var block = (OpaqueBlock) w.getBlock(loc.getBlockX(), loc.getBlockY(), false);
//        if (chunk != null) {
//            drawer.line(
//                    chunk.getOffsetX() * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    (chunk.getOffsetY() + 1) * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    (chunk.getOffsetX() + 1) * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    (chunk.getOffsetY() + 1) * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    Color.RED,
//                    2f
//            );
//            drawer.line(
//                    chunk.getOffsetX() * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    chunk.getOffsetY() * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    (chunk.getOffsetX() + 1) * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    chunk.getOffsetY() * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    Color.RED,
//                    2f
//            );
//            drawer.line(
//                    chunk.getOffsetX() * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    chunk.getOffsetY() * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    chunk.getOffsetX() * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    (chunk.getOffsetY() + 1) * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    Color.RED,
//                    2f
//            );
//            drawer.line(
//                    (chunk.getOffsetX() + 1) * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    chunk.getOffsetY() * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    (chunk.getOffsetX() + 1) * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    (chunk.getOffsetY() + 1) * w.chunkSize() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    Color.RED,
//                    2f
//            );
//        }
//        drawer.line(
//                loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE,
//                (loc.getBlockY() + 1) * WorldImpl.BLOCK_PIXEL_SIZE,
//                (loc.getBlockX() + 1) * WorldImpl.BLOCK_PIXEL_SIZE,
//                (loc.getBlockY() + 1) * WorldImpl.BLOCK_PIXEL_SIZE,
//                Color.WHITE
//        );
//        drawer.line(
//                loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE,
//                loc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE,
//                (loc.getBlockX() + 1) * WorldImpl.BLOCK_PIXEL_SIZE,
//                loc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE,
//                Color.WHITE
//        );
//        drawer.line(
//                loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE,
//                loc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE,
//                loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE,
//                (loc.getBlockY() + 1) * WorldImpl.BLOCK_PIXEL_SIZE,
//                Color.WHITE
//        );
//        drawer.line(
//                (loc.getBlockX() + 1) * WorldImpl.BLOCK_PIXEL_SIZE,
//                loc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE,
//                (loc.getBlockX() + 1) * WorldImpl.BLOCK_PIXEL_SIZE,
//                (loc.getBlockY() + 1) * WorldImpl.BLOCK_PIXEL_SIZE,
//                Color.WHITE
//        );
//        if (chunk != null) {
//            font.draw(
//                    batch,
//                    String.format("G(%d;%d) L(%d;%d)", loc.getBlockX(), loc.getBlockY(), loc.getChunkLocalX(), loc.getChunkLocalY()),
//                    loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    (loc.getBlockY() - 1) * WorldImpl.BLOCK_PIXEL_SIZE
//            );
//            font.draw(
//                    batch,
//                    block.getType().toString(),
//                    loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    loc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE
//            );
//            font.draw(
//                    batch,
//                    String.format("C(%d;%d) " + block.getBiome(), chunk.getOffsetX(), chunk.getOffsetY()),
//                    loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE,
//                    (loc.getBlockY() - 2) * WorldImpl.BLOCK_PIXEL_SIZE
//            );
//        }
//    }
//
//    private void drawFPS() {
//        batch.begin();
//        var proj = ev.getCamera().unproject(new Vector3(1, 1, 0));
//        font.draw(batch, "FPS " + Gdx.graphics.getFramesPerSecond(), proj.x, proj.y);
//        batch.end();
//    }
//
//    private final Vector3 lastPos = new Vector3(0, 0, 0);
//
//    private void trySetAir() {
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            var proj = getCurrentPos();
//            var loc = new Location((double) proj.x / WorldImpl.BLOCK_PIXEL_SIZE, (double) proj.y / WorldImpl.BLOCK_PIXEL_SIZE, w.getChunk(0L, 0L, false));
////            Block b = w.getBlock(loc.getBlockX(), loc.getBlockY(), false);
//            w.setBlock(BlockType.AIR, loc.getBlockX(), loc.getBlockY());
//        }
//    }
//
//    private void tryTick() {
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
//            var proj = getCurrentPos();
//            var loc = new Location((double) proj.x / WorldImpl.BLOCK_PIXEL_SIZE, (double) proj.y / WorldImpl.BLOCK_PIXEL_SIZE, w.getChunk(0L, 0L, false));
//            Block b = w.getBlock(loc.getBlockX(), loc.getBlockY(), false);
//            if (b == null) {
//                return;
//            }
//            if (b instanceof Ticking) {
//                ((Ticking) b).onTick();
//            }
//        }
//    }
//
//    private void tryMove() {
//        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
//            var scaleX = ev.getWorldWidth() / ev.getScreenWidth();
//            var scaleY = ev.getWorldHeight() / ev.getScreenHeight();
//            ev.getCamera().translate((lastPos.x - Gdx.input.getX()) * scaleX, (Gdx.input.getY() - lastPos.y) * scaleY, 0);
//        }
//        lastPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//    }
//
//    private static Instant last = Instant.now();
//    @Getter
//    private static float deltaTime = 0f;
//
//    @Override
//    public void render() {
//        tryMove();
//        tryTick();
//        trySetAir();
//        ScreenUtils.clear(0, 0, 0, 1);
//        ev.apply();
//        batch.setProjectionMatrix(ev.getCamera().combined);
//        batch.begin();
//        w.batch(batch, drawer, ev);
//        drawBlockInfo();
//        batch.end();
//        drawFPS();
//        w.onTick();
//
//        var toBe = Instant.now();
//        deltaTime = Duration.between(last, toBe).getNano() / 1e9f;
//        last = Instant.now();
//        ce.render(batch);
//    }
//
//    @Override
//    public void dispose() {
//        ce.dispose();
//        batch.dispose();
//        font.dispose();
//    }
//}
