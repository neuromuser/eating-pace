package com.neuromuser.eatingpace.client;

public class EatingProgressConfig {
    public static int CIRCLE_X_OFFSET = 0;
    public static int CIRCLE_Y_OFFSET = 0;
    public static AnchorPoint ANCHOR = AnchorPoint.CENTER;

    public static int CIRCLE_OUTER_RADIUS = 5;
    public static int CIRCLE_INNER_RADIUS = 0;
    public static int CIRCLE_SEGMENTS = 40;

    public static float OUTLINE_WIDTH = 2.0f;

    public static int EMPTY_R = 255;
    public static int EMPTY_G = 80;
    public static int EMPTY_B = 80;
    public static int EMPTY_A = 0;

    public static int FILL_R = 80;
    public static int FILL_G = 255;
    public static int FILL_B = 80;
    public static int FILL_A = 255;

    public static int OUTLINE_R = 255;
    public static int OUTLINE_G = 255;
    public static int OUTLINE_B = 255;
    public static int OUTLINE_A = 255;

    public enum AnchorPoint {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CENTER,
        HOTBAR_RIGHT,
        HOTBAR_LEFT,
        HOTBAR_CENTER
    }

    public static int[] getScreenPosition(int screenWidth, int screenHeight) {
        return switch (ANCHOR) {
            case TOP_LEFT -> new int[]{CIRCLE_X_OFFSET, CIRCLE_Y_OFFSET};
            case TOP_RIGHT -> new int[]{screenWidth - CIRCLE_X_OFFSET, CIRCLE_Y_OFFSET};
            case BOTTOM_LEFT -> new int[]{CIRCLE_X_OFFSET, screenHeight - CIRCLE_Y_OFFSET};
            case BOTTOM_RIGHT -> new int[]{screenWidth - CIRCLE_X_OFFSET, screenHeight - CIRCLE_Y_OFFSET};
            case CENTER -> new int[]{screenWidth / 2, screenHeight / 2};
            case HOTBAR_RIGHT -> new int[]{screenWidth / 2 + CIRCLE_X_OFFSET, screenHeight - CIRCLE_Y_OFFSET};
            case HOTBAR_LEFT -> new int[]{screenWidth / 2 - CIRCLE_X_OFFSET, screenHeight - CIRCLE_Y_OFFSET};
            case HOTBAR_CENTER -> new int[]{screenWidth / 2, screenHeight - CIRCLE_Y_OFFSET};
        };
    }
}