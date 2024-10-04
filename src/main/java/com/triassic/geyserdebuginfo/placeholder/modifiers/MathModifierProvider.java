package com.triassic.geyserdebuginfo.placeholder.modifiers;

import com.triassic.geyserdebuginfo.placeholder.ModifierProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MathModifierProvider extends ModifierProvider {

    @Override
    public List<String> getModifiers() {
        return Arrays.asList("round", "floor");
    }

    @Override
    public String onRequest(final String modifier, @NotNull final String params) {
        return switch (modifier) {
            case "round" -> round(params);
            case "floor" -> floor(params);
            default -> null;
        };
    }

    private static String round(String value) {
        return applyNumericOperation(value, Math::round);
    }

    private static String floor(String value) {
        return applyNumericOperation(value, Math::floor);
    }

    private static String applyNumericOperation(String value, Function<Double, Number> operation) {
        try {
            double number = Double.parseDouble(value);
            return String.valueOf(operation.apply(number));
        } catch (NumberFormatException e) {
            return value;
        }
    }
}
