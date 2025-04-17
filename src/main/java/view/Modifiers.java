package view;

import java.util.Map;

public class Modifiers {
    public static final Map<String, Double> BURGER_MODS = Map.of(
        "No Lettuce", 0.0,
        "Extra Patty", 1.50,
        "Add Cheese", 0.75,
        "No Pickles", 0.0,
        "Extra Tomato", 0.50
    );

    public static final Map<String, Double> FRIES_MODS = Map.of(
        "Jumbo Size", 1.00,
        "Animal Style", 1.50
    );
}
