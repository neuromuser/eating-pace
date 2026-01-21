package com.neuromuser.eatingpace.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.joml.Matrix4f;

public class EatingProgressHudConfigurable implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player == null || !player.isUsingItem()) {
            return;
        }

        ItemStack usingItem = player.getActiveItem();
        if (!usingItem.isFood()) {
            return;
        }

        int maxUseTicks = usingItem.getMaxUseTime();
        int usedTicks = player.getItemUseTimeLeft();
        float progress = 1.0f - ((float) usedTicks / (float) maxUseTicks);

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int[] pos = EatingProgressConfig.getScreenPosition(screenWidth, screenHeight);
        int centerX = pos[0];
        int centerY = pos[1];

        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();

        int outlineThickness = (int) EatingProgressConfig.OUTLINE_WIDTH;

        drawDonutSegment(matrices, centerX, centerY,
                EatingProgressConfig.CIRCLE_INNER_RADIUS,
                EatingProgressConfig.CIRCLE_OUTER_RADIUS + outlineThickness,
                0f, 360f,
                EatingProgressConfig.OUTLINE_R,
                EatingProgressConfig.OUTLINE_G,
                EatingProgressConfig.OUTLINE_B,
                EatingProgressConfig.OUTLINE_A);

        drawDonutSegment(matrices, centerX, centerY,
                EatingProgressConfig.CIRCLE_INNER_RADIUS,
                EatingProgressConfig.CIRCLE_OUTER_RADIUS,
                0f, 360f,
                EatingProgressConfig.EMPTY_R,
                EatingProgressConfig.EMPTY_G,
                EatingProgressConfig.EMPTY_B,
                EatingProgressConfig.EMPTY_A);

        if (progress > 0) {
            drawDonutSegment(matrices, centerX, centerY,
                    EatingProgressConfig.CIRCLE_INNER_RADIUS,
                    EatingProgressConfig.CIRCLE_OUTER_RADIUS,
                    -90f, -90f + (360f * progress),
                    EatingProgressConfig.FILL_R,
                    EatingProgressConfig.FILL_G,
                    EatingProgressConfig.FILL_B,
                    EatingProgressConfig.FILL_A);
        }

        if (EatingProgressConfig.CIRCLE_INNER_RADIUS > 0) {
            drawDonutSegment(matrices, centerX, centerY,
                    EatingProgressConfig.CIRCLE_INNER_RADIUS - outlineThickness,
                    EatingProgressConfig.CIRCLE_INNER_RADIUS,
                    0f, 360f,
                    EatingProgressConfig.OUTLINE_R,
                    EatingProgressConfig.OUTLINE_G,
                    EatingProgressConfig.OUTLINE_B,
                    EatingProgressConfig.OUTLINE_A);
        }

        matrices.pop();
    }

    private void drawDonutSegment(MatrixStack matrices, int centerX, int centerY,
                                  int innerRadius, int outerRadius,
                                  float startAngle, float endAngle,
                                  int r, int g, int b, int a) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

        float angleRange = endAngle - startAngle;
        int segmentCount = (int) (EatingProgressConfig.CIRCLE_SEGMENTS * Math.abs(angleRange) / 360f);
        segmentCount = Math.max(segmentCount, 1);

        for (int i = 0; i <= segmentCount; i++) {
            float angle = startAngle + (angleRange * i / segmentCount);
            float rad = (float) Math.toRadians(angle);

            float cosAngle = (float) Math.cos(rad);
            float sinAngle = (float) Math.sin(rad);

            float xOuter = centerX + cosAngle * outerRadius;
            float yOuter = centerY + sinAngle * outerRadius;
            bufferBuilder.vertex(matrix, (int)(xOuter + 0.5f), (int)(yOuter + 0.5f), 0).color(r, g, b, a).next();

            float xInner = centerX + cosAngle * innerRadius;
            float yInner = centerY + sinAngle * innerRadius;
            bufferBuilder.vertex(matrix, (int)(xInner + 0.5f), (int)(yInner + 0.5f), 0).color(r, g, b, a).next();
        }

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}