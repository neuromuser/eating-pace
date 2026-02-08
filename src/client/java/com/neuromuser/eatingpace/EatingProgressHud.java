package com.neuromuser.eatingpace;

import com.mojang.blaze3d.systems.RenderSystem;
import com.neuromuser.eatingpace.config.ConfigManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.joml.Matrix4f;

public class EatingProgressHud implements HudRenderCallback {
    private static final int CIRCLE_OUTER_RADIUS = 10;
    private static final int CIRCLE_INNER_RADIUS = 5;
    private static final int CIRCLE_SEGMENTS = 40;
    private static final int OUTLINE_WIDTH = 1;

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player == null || !player.isUsingItem()) return;

        ItemStack usingItem = player.getActiveItem();
        if (!usingItem.contains(DataComponentTypes.FOOD)) return;

        int maxUseTicks = ConfigManager.getEatingTime(usingItem);
        int usedTicks = player.getItemUseTimeLeft();
        float progress = 1.0f - ((float) usedTicks / maxUseTicks);

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();

        drawDonutSegment(matrices, centerX, centerY, 0f, 360f, 255, 80, 100);

        if (progress > 0f) {
            drawDonutSegment(matrices, centerX, centerY, -90f, -90f + (360f * progress), 80, 255, 240);
        }

        drawOutline(matrices, centerX, centerY);

        matrices.pop();
    }

    private void drawDonutSegment(
            MatrixStack matrices,
            int centerX, int centerY,
            float startAngle, float endAngle,
            int r, int g, int a
    ) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

        float angleRange = endAngle - startAngle;
        int segments = Math.max(1, (int) (CIRCLE_SEGMENTS * Math.abs(angleRange) / 360f));

        for (int i = 0; i <= segments; i++) {
            float angle = startAngle + angleRange * i / segments;
            float rad = (float) Math.toRadians(angle);
            float cos = (float) Math.cos(rad);
            float sin = (float) Math.sin(rad);

            buffer.vertex(matrix, centerX + cos * CIRCLE_OUTER_RADIUS, centerY + sin * CIRCLE_OUTER_RADIUS, 0)
                    .color(r, g, 80, a); 

            buffer.vertex(matrix, centerX + cos * CIRCLE_INNER_RADIUS, centerY + sin * CIRCLE_INNER_RADIUS, 0)
                    .color(r, g, 80, a);
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private void drawOutline(MatrixStack matrices, int centerX, int centerY) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        Tessellator tessellator = Tessellator.getInstance();

        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        drawRing(buffer, matrix, centerX, centerY, CIRCLE_OUTER_RADIUS, true);
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        drawRing(buffer, matrix, centerX, centerY, CIRCLE_INNER_RADIUS, false);
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private void drawRing(BufferBuilder buffer, Matrix4f matrix, int centerX, int centerY, int radius, boolean outer) {
        int outerRadius = outer ? radius : radius + OUTLINE_WIDTH;
        int innerRadius = outer ? radius - OUTLINE_WIDTH : radius;

        for (int i = 0; i <= CIRCLE_SEGMENTS; i++) {
            float angle = (float) i / CIRCLE_SEGMENTS * 360f;
            float rad = (float) Math.toRadians(angle);
            float cos = (float) Math.cos(rad);
            float sin = (float) Math.sin(rad);

            buffer.vertex(matrix, centerX + cos * outerRadius, centerY + sin * outerRadius, 0)
                    .color(220, 220, 220, 220);

            buffer.vertex(matrix, centerX + cos * innerRadius, centerY + sin * innerRadius, 0)
                    .color(220, 220, 220, 220);
        }}
    private int calculateMaxUseTime(ItemStack stack) {
        return ConfigManager.getEatingTime(stack);
    }
}