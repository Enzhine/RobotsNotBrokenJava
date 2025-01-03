package ru.enzhine.rnb.world.block.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.texture.TextureCache;
import ru.enzhine.rnb.texture.Textures;
import ru.enzhine.rnb.world.Fluid;
import ru.enzhine.rnb.world.Location;
import ru.enzhine.rnb.world.Material;

public abstract class FloodableBlock extends TransparentBlock implements Floodable, Ticking {

    private final byte[] fluids;
    private boolean isStill;
    private byte totalFluid;

    public FloodableBlock(String sprite, String spriteBG, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this(Textures.getTexture(sprite), Textures.getTexture(spriteBG), loc, bt, material, biomeType);
    }

    public FloodableBlock(TextureCache textureCache, TextureCache textureBGCache, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        this(textureCache.getOriginTexture(), textureBGCache.getOriginTexture(), textureCache.getOutlineColor(), loc, bt, material, biomeType);
    }

    public FloodableBlock(Texture blockTexture, Texture blockBGTexture, Color blockOutlineColor, Location loc, BlockType bt, Material material, BiomeType biomeType) {
        super(blockTexture, blockBGTexture, blockOutlineColor, loc, bt, material, biomeType);

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
    public void batchRender(SpriteBatch batch, Viewport viewport) {
        super.batchRender(batch, viewport);

        if (totalFluid == 0) {
            return;
        }
        int startingLevel = 0;
        for (int i = 0; i < fluids.length; i++) {
            int lvl = fluids[i];
            if (lvl == 0) {
                continue;
            }
            Texture tex = Textures.getFluidTexture(Fluid.getById(i)).getOriginTexture();
            batch.draw(
                    tex,
                    loc.getBlockX() * TEXTURE_WH,
                    loc.getBlockY() * TEXTURE_WH + startingLevel,
                    0,
                    0,
                    TEXTURE_WH,
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
