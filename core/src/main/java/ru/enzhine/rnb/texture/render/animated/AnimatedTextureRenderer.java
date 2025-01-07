package ru.enzhine.rnb.texture.render.animated;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;
import ru.enzhine.rnb.Main;
import ru.enzhine.rnb.texture.exception.TextureRendererException;
import ru.enzhine.rnb.texture.outline.AverageOutlineColor;
import ru.enzhine.rnb.texture.outline.OutlineColorStrategy;
import ru.enzhine.rnb.texture.preprocessor.TexturePreprocessor;
import ru.enzhine.rnb.texture.render.TextureRenderer;
import ru.enzhine.rnb.texture.render.TextureRendererProperties;
import ru.enzhine.rnb.utils.TextureUtils;
import space.earlygrey.shapedrawer.ShapeDrawer;

@Builder
@Jacksonized
public class AnimatedTextureRenderer implements TextureRenderer<AnimatedRenderingContext> {

    @NonNull
    private final TexturePreprocessor src;
    @NonNull
    @Builder.Default
    private TextureRendererProperties properties = TextureRendererProperties.builder()
            .uWrap(Texture.TextureWrap.ClampToEdge)
            .vWrap(Texture.TextureWrap.ClampToEdge)
            .build();
    @NonNull
    @Builder.Default
    private OutlineColorStrategy outline = AverageOutlineColor.builder().build();

    @NonNull
    private final Integer width;
    @NonNull
    private final Integer height;
    @NonNull
    private final Float seconds;
    @NonNull
    private final RenderingOrder order;
    @NonNull
    private final RenderingLoop loop;

    private Texture preparedTexture;
    private Color preparedLowColor;
    private Color preparedOutlineColor;
    private int xScaled;
    private int yScaled;
    private int totalFrames;

    @Override
    public void prepare() {
        if (preparedTexture != null) {
            throw new TextureRendererException("Already prepared");
        }

        preparedTexture = src.process();
        preparedLowColor = TextureUtils.getAverageColor(preparedTexture);
        preparedTexture.setWrap(properties.getUWrap(), properties.getVWrap());
        this.xScaled = preparedTexture.getWidth() / width;
        this.yScaled = preparedTexture.getHeight() / height;
        this.totalFrames = this.xScaled * this.yScaled;
        preparedOutlineColor = outline.getColor(preparedTexture);
    }

    @Override
    public AnimatedRenderingContext newContext() {
        int startFrame = order == RenderingOrder.INWARDS ? 0 : this.totalFrames - 1;
        return new AnimatedRenderingContext(
                startFrame,
                0f,
                this.seconds / this.totalFrames,
                order == RenderingOrder.INWARDS,
                false
        );
    }

    @Override
    public Color getOutlineColor(AnimatedRenderingContext context) {
        return preparedOutlineColor;
    }

    @Override
    public void render(AnimatedRenderingContext context, SpriteBatch spriteBatch, float x, float y, int srcX, int srcY, int width, int height) {
        updateContext(context, Main.getDeltaTime());
        int offX = xOffsetByFrame(context.getCurrentFrame()) * this.width;
        if (preparedTexture.getUWrap() == Texture.TextureWrap.Repeat) {
            offX += srcX;
        }
        int offY = yOffsetByFrame(context.getCurrentFrame()) * this.height;
        if (preparedTexture.getVWrap() == Texture.TextureWrap.Repeat) {
            offY += srcY;
        }
        spriteBatch.draw(preparedTexture, x, y, offX, offY, this.width, this.height);
    }

    @Override
    public void renderLow(AnimatedRenderingContext context, SpriteBatch spriteBatch, ShapeDrawer shapeDrawer, float x, float y, int width, int height) {
        shapeDrawer.filledRectangle(x, y, width, height, preparedLowColor);
    }

    private void updateContext(AnimatedRenderingContext context, float deltaTime) {
        if (context.isFinished()) {
            return;
        }
        context.setAccumulation(context.getAccumulation() + deltaTime);
        if (context.getAccumulation() < context.getFrameThreshold()) {
            return;
        } else {
            context.setAccumulation(context.getFrameThreshold() - context.getAccumulation());
        }
        if (context.isInwards()) {
            if (context.getCurrentFrame() == this.totalFrames - 1) {
                if (loop == RenderingLoop.NONE) {
                    context.setFinished(true);
                    context.setAccumulation(0f);
                } else if (loop == RenderingLoop.FLIP) {
                    context.setCurrentFrame(this.totalFrames - 2);
                    context.setInwards(false);
                } else {
                    context.setCurrentFrame(0);
                }
            } else {
                context.setCurrentFrame(context.getCurrentFrame() + 1);
            }
        } else {
            if (context.getCurrentFrame() == 0) {
                if (loop == RenderingLoop.NONE) {
                    context.setFinished(true);
                    context.setAccumulation(0f);
                } else if (loop == RenderingLoop.FLIP) {
                    context.setCurrentFrame(1);
                    context.setInwards(true);
                } else {
                    context.setCurrentFrame(this.totalFrames - 1);
                }
            } else {
                context.setCurrentFrame(context.getCurrentFrame() - 1);
            }
        }

    }

    private int xOffsetByFrame(int frame) {
        return frame % this.xScaled;
    }

    private int yOffsetByFrame(int frame) {
        return frame / this.yScaled;
    }
}
