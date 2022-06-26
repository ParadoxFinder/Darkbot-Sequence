# Conditions

## OnDestroyed
* Description: Runs the `step` after the player is destroyed.
* Parameters: None

## OnPreviousKillerSpotted
* Description: Runs the `step` after an enemy player is spotted that
  previously destroyed the player. This is meant to reduce deaths from
  players who kill bots.
* Parameters: None

## OnTimeReacheed
* Description: Runs the `step` after the specified time is reached.
* Parameters:
  * `int hour` - Hour of the day from 0 to 24. Both 0 and 24 work for midnight.
  * `int minute` - Minute of the hour.