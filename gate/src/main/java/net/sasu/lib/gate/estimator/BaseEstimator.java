/**
 * 
 */
package net.sasu.lib.gate.estimator;

import java.util.concurrent.TimeUnit;

import net.sasu.lib.timer.NanosecondTimer;
import net.sasu.lib.timer.TimeTeller;
import net.sasu.lib.timer.Timer;

/**
 * Base estimator for handling common tasks
 * 
 * @author Sasu
 *
 */
public abstract class BaseEstimator implements Estimator<BaseEstimator> {

    private long totalWorkUnits;
    private long remainingWorkUnits;
    private Timer timer;

    public BaseEstimator init(long remainingWorkUnits, Timer timer){
        final BaseEstimator init = this.initAndStart(remainingWorkUnits);
        init.setTimer(timer);
        return init;
    }

    @Override
    public void completeWorkUnits(long workUnitsCompleted) {
        if (workUnitsCompleted < 0) {
            throw new IllegalArgumentException("workUnitsCompleted may not be negative");
        }

        this.remainingWorkUnits = this.remainingWorkUnits - workUnitsCompleted;
        if (this.remainingWorkUnits < 0) {
            throw new IllegalStateException(
                    "More work than available completed. Remaining work units: " + this.remainingWorkUnits);
        }
    }

    @Override
    public long getElapsedTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.timer.getElapsedTimeRaw(), TimeUnit.NANOSECONDS);
    }

    @Override
    public String getElapsedTimeAsString() {
        return timer.getElapsedTime();
    }

    @Override
    public void start() {
        if(this.timer == null) {
            this.timer = new NanosecondTimer();
        }
        this.timer.start();
    }

    @Override
    public void stop() {
        this.timer.stop();
    }

    /**
     * Initializes the number of remaining work units
     * 
     * @param totalWorkUnitsArg
     */
    public void initializeRemainingWorkUnits(long totalWorkUnitsArg) {
        this.remainingWorkUnits = totalWorkUnitsArg;
        this.totalWorkUnits = totalWorkUnitsArg;
    }

    public long getRemainingWorkUnits() {
        return this.remainingWorkUnits;
    }

    public long getTotalWorkUnits() {
        return totalWorkUnits;
    }

    @Override
    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    @Override
    public String getRemainingTimeAsString() {
        TimeUnit timeUnit = TimeUnit.NANOSECONDS;
        final long remainingTime = this.getRemainingTime(timeUnit);
        return TimeTeller.outputElapsedTime(remainingTime, timeUnit);
    }
}