package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.texture.TextureRenderers;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.texture.render.RenderingContext;
import ru.enzhine.rnb.world.Fluid;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.Material;
import ru.enzhine.rnb.world.WorldImpl;
import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class FloodableBlock extends TransparentBlock implements Floodable, Ticking {

    private static final TextureRenderer<RenderingContext>[] fluidRenderers;
    private static final RenderingContext[] fluidRendererContexts;

    static {
        Fluid[] globalFluids = Fluid.values();
        fluidRenderers = new TextureRenderer[globalFluids.length];
        fluidRendererContexts = new RenderingContext[globalFluids.length];

        for (Fluid fluid : globalFluids) {
            var fluidRenderer = TextureRenderers.getFluidTextureRenderer(fluid);
            fluidRenderers[fluid.getId()] = fluidRenderer;
            fluidRendererContexts[fluid.getId()] = fluidRenderer.newContext();
        }
    }

    private final byte[] fluids;
    private boolean isStill;
    private byte totalFluid;

    public FloodableBlock(String sprite, String spriteBG, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this(TextureRenderers.getTextureRenderer(sprite), TextureRenderers.getTextureRenderer(spriteBG), loc, bt, material, biomeType);
    }

    public FloodableBlock(TextureRenderer<RenderingContext> textureRenderer, TextureRenderer<RenderingContext> textureBGRenderer, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        super(textureRenderer, textureBGRenderer, loc, bt, material, biomeType);

        this.fluids = new byte[Fluid.values().length];
        this.totalFluid = 0;
        this.isStill = false;
    }

    @Override
    public byte getTotalLevel() {
        return totalFluid;
    }

    @Override
    public byte getLevel(Fluid fluid) {
        return fluids[fluid.getId()];
    }

    @Override
    public void setLevel(Fluid fluid, byte level) {
        if (level < 0 || level > Floodable.MAX_FLOOD_LEVEL) {
            throw new RuntimeException(String.format("Setting level %d is out of range", level));
        }
        int idx = fluid.getId();
        if (totalFluid - fluids[idx] + level > Floodable.MAX_FLOOD_LEVEL) {
            throw new RuntimeException(String.format("Setting level %d exceeds maximum fluid level %d", level, Floodable.MAX_FLOOD_LEVEL));
        }
        this.totalFluid -= fluids[idx];
        fluids[idx] = level;
        this.totalFluid += fluids[idx];

        this.isStill = false;
    }

    @Override
    public byte[] levels() {
        return fluids;
    }

    @Override
    public void batch(SpriteBatch batch, ShapeDrawer drawer, Viewport viewport) {
        super.batch(batch, drawer, viewport);

        if (totalFluid == 0) {
            return;
        }
        int startingLevel = 0;
        for (int i = 0; i < fluids.length; i++) {
            int lvl = fluids[i];
            if (lvl == 0) {
                continue;
            }
            var fluidRenderer = fluidRenderers[i];
            var fluidContext = fluidRendererContexts[i];

            fluidRenderer.render(
                    fluidContext,
                    batch,
                    loc.getBlockX() * WorldImpl.BLOCK_PIXEL_SIZE,
                    loc.getBlockY() * WorldImpl.BLOCK_PIXEL_SIZE + startingLevel,
                    0,
                    0,
                    WorldImpl.BLOCK_PIXEL_SIZE,
                    lvl);
            startingLevel += lvl;
        }
    }

    public byte tryTransmitFluid(Floodable other, Fluid fluid, boolean banSmall) {
        if (other == null) {
            return 0;
        }
        if (other.getTotalLevel() == MAX_FLOOD_LEVEL) {
            return 0;
        }
        int current = this.getLevel(fluid);
        int toTransmit = Math.min(MAX_FLOOD_LEVEL - other.getLevel(fluid), current <= 1 ? current : current / 2);
        if (banSmall && toTransmit <= 3) {
            return 0;
        }
        if (toTransmit != 0) {
            other.setLevel(fluid, (byte) (other.getLevel(fluid) + toTransmit));
            this.setLevel(fluid, (byte) (this.getLevel(fluid) - toTransmit));
        }
        return (byte) toTransmit;
    }

    @Override
    public boolean isStill() {
        return isStill;
    }

    @Override
    public void onTick() {
        if (isStill) {
            return;
        }

        boolean shouldGetStill = true;
        for (int id = 0; id < fluids.length; id++) {
            Fluid fluid = Fluid.getById(id);
            byte lvl = fluids[id];
            if (lvl == 0) {
                continue;
            }

            Floodable bottomFloodable = atBottomFloodable();
            if (bottomFloodable != null && tryTransmitFluid(bottomFloodable, fluid, false) != 0) {
                shouldGetStill = false;
                continue;
            }
            Floodable leftFloodable = atLeftFloodable();
            if (leftFloodable != null && tryTransmitFluid(leftFloodable, fluid, true) != 0) {
                shouldGetStill = false;
                continue;
            }
            Floodable rightFloodable = atRightFloodable();
            if (rightFloodable != null && tryTransmitFluid(rightFloodable, fluid, true) != 0) {
                shouldGetStill = false;
                continue;
            }
        }
//        if (shouldGetStill) {
//            this.isStill = true;
//        }
    }

    private Floodable atBottomFloodable() {
        var bottom = this.loc.getChunk().get(this.loc.getChunkLocalX(), this.loc.getChunkLocalY() - 1);
        if (bottom == null) {
            bottom = this.loc.getWorld().getBlock(this.loc.getBlockX(), this.loc.getBlockY() - 1, false);
        }
        if (bottom instanceof Floodable) {
            return (Floodable) bottom;
        }
        return null;
    }

    private Floodable atLeftFloodable() {
        var left = this.loc.getChunk().get(this.loc.getChunkLocalX() - 1, this.loc.getChunkLocalY());
        if (left == null) {
            left = this.loc.getWorld().getBlock(this.loc.getBlockX() - 1, this.loc.getBlockY(), false);
        }
        if (left instanceof Floodable) {
            return (Floodable) left;
        }
        return null;
    }

    private Floodable atRightFloodable() {
        var right = this.loc.getChunk().get(this.loc.getChunkLocalX() + 1, this.loc.getChunkLocalY());
        if (right == null) {
            right = this.loc.getWorld().getBlock(this.loc.getBlockX() + 1, this.loc.getBlockY(), false);
        }
        if (right instanceof Floodable) {
            return (Floodable) right;
        }
        return null;
    }
}
