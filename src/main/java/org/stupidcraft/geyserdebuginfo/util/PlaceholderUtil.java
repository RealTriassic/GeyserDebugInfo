package org.stupidcraft.geyserdebuginfo.util;

public class PlaceholderUtil {

    public static String setModifiers(String modifier, String value) {
        return switch (modifier) {
            case "uppercase" -> value.toUpperCase();
            case "lowercase" -> value.toLowerCase();
            case "capitalize" -> capitalize(value);
            case "floor" -> floor(value);
            default -> value;
        };
    }

    private static String capitalize(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1).toLowerCase();
    }

    private static String floor(String value) {
        try {
            double number = Double.parseDouble(value);
            return String.valueOf((int) Math.floor(number));
        } catch (NumberFormatException e) {
            return value;
        }
    }
}
