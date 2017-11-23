/**
 * 
 */
package net.sasu.lib.gate.estimator;

import java.util.concurrent.TimeUnit;

import net.sasu.lib.gate.time.Timer;

/**
 * Base estimator for handling common tasks
 * 
 * @author Sasu
 *
 */
public abstract class BaseEstimator<T> implements Estimator<T> {

	private long remainingWorkUnits;
	private Timer timer;

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
		this.timer = Timer.getInstanceAndStart();
	}

	@Override
	public void stop() {
		this.timer.stop();
	}

	/**
	 * Initializes the number of remaining work units
	 * 
	 * @param remainingWorkUnitsArg
	 */
	public void initializeRemainingWorkUnits(long remainingWorkUnitsArg) {
		this.remainingWorkUnits = remainingWorkUnitsArg;
	}

}
