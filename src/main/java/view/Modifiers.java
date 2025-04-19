package view;

import java.util.Map;
import java.util.HashMap;

public class Modifiers {
    public static final Map<String, Double> BURGER_MODS = new HashMap<String, Double>() {{
        put("No Lettuce", 0.0);
        put("Extra Patty", 1.50);
        put("Add Cheese", 0.75);
        put("No Pickles", 0.0);
        put("Extra Tomato", 0.50);
    }};

    public static final Map<String, Double> FRIES_MODS = new HashMap<String, Double>() {{
        put("Jumbo Size", 1.00);
        put("Animal Style", 1.50);
    }};
}
