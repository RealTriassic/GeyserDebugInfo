package com.triassic.geyserdebuginfo.placeholder.modifiers;

import com.triassic.geyserdebuginfo.placeholder.ModifierProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TextModifierProvider extends ModifierProvider {

    @Override
    public List<String> getModifiers() {
        return Arrays.asList("uppercase", "lowercase");
    }

    @Override
    public String onRequest(final String modifier, @NotNull final String params) {
        return switch (modifier) {
            case "uppercase" -> params.toUpperCase();
            case "lowercase" -> params.toLowerCase();
            default -> null;
        };
    }
}
