/* The GUI was coded using generative AI. */
/* 
 * Modifiers.java
 * This class defines the modifications needed for the restaurant.
 * 
 * */

package view;

import java.util.Map;
import java.util.HashMap;

// MODIFICATIONS
public class Modifiers {
    public static final Map<String, Double> BURGER_MODS = new HashMap<String, Double>() {
		private static final long serialVersionUID = 1L;

	{
        put("No Lettuce", 0.0);
        put("Extra Patty", 1.50);
        put("Add Cheese", 0.75);
        put("No Pickles", 0.0);
        put("Extra Tomato", 0.50);
    }};

    public static final Map<String, Double> FRIES_MODS = new HashMap<String, Double>() {
		private static final long serialVersionUID = 1L;

	{
        put("Jumbo Size", 1.00);
        put("Animal Style", 1.50);
    }};
}
