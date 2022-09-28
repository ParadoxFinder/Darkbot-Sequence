package eu.paradox.darkbot.implementation.condition.time;

import eu.paradox.darkbot.base.BaseCondition;

import java.util.Calendar;
import java.util.Date;

public class OnTimeReached extends BaseCondition {
    /**
     * Hour of the day to trigger the condition
     */
    private int hour;

    /**
     * Minute of the hour to trigger the condition.
     */
    private int minute;

    /**
     * Required time to be reached in milliseconds.
     */
    private long requiredTime;

    /**
     * Handles the step being parsed.
     */
    @Override
    public void onParse() {
        if (!this.parameters.containsKey("hour")) {
            throw new RuntimeException("Missing parameter \"hour\".");
        }
        this.hour = this.getParameterInt("hour");
        if (!this.parameters.containsKey("minute")) {
            throw new RuntimeException("Missing parameter \"minute\".");
        }
        this.minute = this.getParameterInt("minute");
    }

    /**
     * Resets the condition.
     */
    @Override
    public void reset() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, this.hour);
        calendar.set(Calendar.MINUTE, this.minute);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        this.requiredTime = calendar.getTimeInMillis();
    }

    /**
     * Returns if the condition is true.
     *
     * @return If the condition is true.
     */
    @Override
    public boolean isTrue() {
        return System.currentTimeMillis() > this.requiredTime;
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "OnTimeReached[hour=" + this.hour + ",minute=" + this.minute + "]";
    }
}
