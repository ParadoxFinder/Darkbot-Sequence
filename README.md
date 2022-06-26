# Darkbot Sequence
Darkbot Sequence is a plugin for [Darkbot](https://github.com/darkbot-reloaded/DarkBot)
that makes the player follow a sequence of steps.

## Warning: Beta Software
The plugin works but is missing a lot of features and
is clunky to use. The sequence is defined in a JSON
file and can only be changed by reloading the plugins.

# Setup
In order to use the plugin, there must be a `sequence.json`
file in the root directory of Darkbot. The JSON file must be
a dictionary with the keys being name of the sequences and
the values being a step in a sequence. The sequence names matter
for being invoked either by the plugin directly or by other steps.
By default `"main"` is used unless configured otherwise in
the settings.

There are 2 main schemas: steps and conditions. Steps run
a step of a sequence while a condition will take over the
current step if it becomes true. The schemas are the following:
* `Step`
  * `String name` - Type of the step, such as `RunModule`.
  * `Optional<Map<String, Object>> parameters` - Optional parameters
    for the step. The parameters depend on the step.
      * For all steps, if `output` is a parameter, the value will be
        printed when the step is started.
  * `Optional<List<Condition>> conditionSteps` - Optional list
    of conditions that can take over the step.
  * `Optional<Step> nextStep` - Next step to run after the current
    one completes. 
* `Condition`
  * `String name` - Type of the step, such as `OnTimeReached`.
  * `Optional<Map<String, Object>> parameters` - Optional parameters
    for the condition. The parameters depend on the condition.
  * `Optional<Step> step` - Step to run if the condition becomes true.

See [docs/Steps.md](docs/Steps.md) and [docs/Conditions.md](docs/Conditions.md)
for the options.

## Example
The following example will run the `Palladium Module` until 8:00 AM.
```json
"main": {
    "name": "RunModule",
    "parameters": {
      "module": "Palladium Module",
      "output": "Starting Palladium bot."
    },
    {
        "name": "OnTimeReached",
        "parameters": {
            "hour": 8,
            "minute": 0
        },
        "step": {
            "name": "Logout",
            "parameters": {
                "output": "End time was reached. Stopping.",
            },
            "nextStep": {
                "name": "Stop",
            }
        }
    }
}
```