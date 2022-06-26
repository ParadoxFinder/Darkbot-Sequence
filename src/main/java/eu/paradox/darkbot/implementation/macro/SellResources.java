package eu.paradox.darkbot.implementation.macro;

import eu.paradox.darkbot.data.StepEntry;

import java.util.HashMap;

public class SellResources {
    /**
     * Parses a step entry into.
     *
     * @param baseEntry Entry to parse from.
     * @return The new parsed step entry.
     */
    public static StepEntry parse(StepEntry baseEntry) {
        // Set up the open and sell steps.
        StepEntry clickSellResources = new StepEntry("ClickSellResources", null, baseEntry.nextStep, null);
        StepEntry openSellWait = new StepEntry("Wait", new HashMap<String, Object>() {{
            put("delay", 3);
        }}, clickSellResources, null);
        StepEntry openSell = new StepEntry("OpenSell", null, openSellWait, null);

        // Set up the move steps.
        StepEntry goToSellLocation = new StepEntry("GoToSellPosition", null, openSell, null);
        StepEntry stopModule = new StepEntry("StopModule", null, goToSellLocation, null);
        StepEntry goToSellMap = new StepEntry("GoToSellMap", baseEntry.parameters, stopModule, null);

        // Return the first step.
        return goToSellMap;
    }
}
