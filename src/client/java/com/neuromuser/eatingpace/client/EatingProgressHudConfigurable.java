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
    private static final int CIRCLE_OUTER_RADIUS = 10;
    private static final int CIRCLE_INNER_RADIUS = 5;
    private static final int CIRCLE_SEGMENTS = 20;

    private static final int OUTLINE_WIDTH = 1;

    private static final int EMPTY_R = 255;
    private static final int EMPTY_G = 80;
    private static final int EMPTY_B = 80;
    private static final int EMPTY_A = 0;

    private static final int FILL_R = 80;
    private static final int FILL_G = 255;
    private static final int FILL_B = 80;
    private static final int FILL_A = 240;

    private static final int OUTLINE_R = 220;
    private static final int OUTLINE_G = 220;
    private static final int OUTLINE_B = 220;
    private static final int OUTLINE_A = 220;

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player == null || !player.isUsingItem()) return;

        ItemStack usingItem = player.getActiveItem();
        if (!usingItem.isFood()) return;

        int maxUseTicks = usingItem.getMaxUseTime();
        int usedTicks = player.getItemUseTimeLeft();
        float progress = 1.0f - ((float) usedTicks / maxUseTicks);

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();

        drawDonutSegment(
                matrices,
                centerX, centerY,
                CIRCLE_INNER_RADIUS,
                CIRCLE_OUTER_RADIUS,
                0f, 360f,
                EMPTY_R, EMPTY_G, EMPTY_B, EMPTY_A
        );

        if (progress > 0f) {
            drawDonutSegment(
                    matrices,
                    centerX, centerY,
                    CIRCLE_INNER_RADIUS,
                    CIRCLE_OUTER_RADIUS,
                    -90f,
                    -90f + (360f * progress),
                    FILL_R, FILL_G, FILL_B, FILL_A
            );
        }

        drawOutline(
                matrices,
                centerX, centerY,
                CIRCLE_INNER_RADIUS,
                CIRCLE_OUTER_RADIUS,
                OUTLINE_WIDTH,
                OUTLINE_R, OUTLINE_G, OUTLINE_B, OUTLINE_A
        );

        matrices.pop();
    }


    private void drawDonutSegment(
            MatrixStack matrices,
            int centerX, int centerY,
            int innerRadius, int outerRadius,
            float startAngle, float endAngle,
            int r, int g, int b, int a
    ) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

        float angleRange = endAngle - startAngle;
        int segments = Math.max(1,
                (int) (CIRCLE_SEGMENTS * Math.abs(angleRange) / 360f));

        for (int i = 0; i <= segments; i++) {
            float angle = startAngle + angleRange * i / segments;
            float rad = (float) Math.toRadians(angle);

            float cos = (float) Math.cos(rad);
            float sin = (float) Math.sin(rad);

            buffer.vertex(matrix,
                    centerX + cos * outerRadius,
                    centerY + sin * outerRadius,
                    0
            ).color(r, g, b, a).next();

            buffer.vertex(matrix,
                    centerX + cos * innerRadius,
                    centerY + sin * innerRadius,
                    0
            ).color(r, g, b, a).next();
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private void drawOutline(
            MatrixStack matrices,
            int centerX, int centerY,
            int innerRadius, int outerRadius,
            int thickness,
            int r, int g, int b, int a
    ) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        if (outerRadius > 0) {
            buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
            drawRingOutline(buffer, matrix, centerX, centerY, outerRadius, thickness, r, g, b, a);
            BufferRenderer.drawWithGlobalProgram(buffer.end());
        }

        if (innerRadius > 0) {
            buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
            drawRingOutline(buffer, matrix, centerX, centerY, innerRadius, -thickness, r, g, b, a);
            BufferRenderer.drawWithGlobalProgram(buffer.end());
        }

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private void drawRingOutline(
            BufferBuilder buffer,
            Matrix4f matrix,
            int centerX, int centerY,
            int radius, int thickness,
            int r, int g, int b, int a
    ) {
        int outer = thickness < 0 ? radius - thickness : radius;
        int inner = thickness < 0 ? radius : radius - thickness;

        for (int i = 0; i <= CIRCLE_SEGMENTS; i++) {
            float angle = (float) i / CIRCLE_SEGMENTS * 360f;
            float rad = (float) Math.toRadians(angle);

            float cos = (float) Math.cos(rad);
            float sin = (float) Math.sin(rad);

            buffer.vertex(matrix,
                    centerX + cos * outer,
                    centerY + sin * outer,
                    0
            ).color(r, g, b, a).next();

            buffer.vertex(matrix,
                    centerX + cos * inner,
                    centerY + sin * inner,
                    0
            ).color(r, g, b, a).next();
        }
    }
}
