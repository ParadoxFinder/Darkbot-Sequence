# Steps

## `Empty`
* Description: Does nothing and does not invoke `nextStep`. Only
  `conditions` can be run.
* Parameters: None

## `RunModule`
* Description: Runs a module and does not invoke `nextStep`. Only
  `conditions` can be run.
* Parameters:
  * `String module` - Name of the module to run.

## `RunStep`
* Description: Runs a step in `sequence.json`. `nextStep` is invoked
  after the steps complete. `conditions` is not supported.
* Parameters:
  * `String step` - Name of the step in `sequence.json` to run.

## `StopModule`
* Description: Stops the current module.
* Parameters: None

## `Wait`
* Description: Waits for a given delay to pass before invoking `nextStep`.
  `conditions` can be used to interrupt the delay.
* Parameters:
  * `int delay` - Seconds to wait before moving to the next step.

## `GoToMap`
* Description: Makes the player go to a specific map.
* Parameters:
  * `String map` - Name of the map to go to.

## `GoToSellMap`
* Description: Makes the player go to a map with an ore trade location.
* Parameters:
  * `Optional<List<String>> maps` - Optional whitelist of maps to go to. Intended
    for requiring `"5-2"` as the destination.

## `GoToPosition`
* Description: Makes the player go to a specific position on the current map.
* Parameters:
  * `int x` - X position to go to.
  * `int y` - Y position to go to.

## `GoToSellPosition`
* Description: Makes the player go to the ore trade location on the
  current map.
* Parameters: None

## `OpenSell`
* Description: Opens the ore trade UI. May behave unpredicably if it
  isn't possible.
* Parameters: None

## `ClickSellResources`
* Description: Sells the current ores. May behave unpredicably if the ore
  trade isn't open.
* Parameters: None

## `Logout`
* Description: Logs out the player.
* Parameters: None

## `Stop`
* Description: Stops Darkbot. `nextStep` can still be run, but won't until
  the bot is started again.
* Parameters: None

## `SellResources`
* Description: Sends the player to a map with an ore trade, moves the player
  to the ore trade location, opens the ore trade window, and sells the ores.
  * `Optional<List<String>> maps` - Optional whitelist of maps to go to. Intended
    for requiring `"5-2"` as the destination.