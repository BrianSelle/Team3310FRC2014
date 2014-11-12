package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * @author rhhs
 */
public abstract class ExtraTimeoutCommandBase extends CommandBase {

    protected double m_extraTimeout = -1;
    private double m_startExtraTime;

    public ExtraTimeoutCommandBase() {
    }

    /**
     * Sets the timeout of this command.
     *
     * @param seconds the timeout (in seconds)
     * @throws IllegalArgumentException if seconds is negative
     * @see Command#isTimedOut() isTimedOut()
     */
    protected synchronized final void startExtraTimeout(double seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Seconds must be positive.  Given:" + seconds);
        }
        m_startExtraTime = Timer.getFPGATimestamp();
        m_extraTimeout = seconds;
    }

    /**
     * Returns the time since this command was initialized (in seconds).
     * This function will work even if there is no specified timeout.
     * @return the time since this command was initialized (in seconds).
     */
    public synchronized final double timeSinceExtraInitialized() {
        return m_startExtraTime < 0 ? 0 : Timer.getFPGATimestamp() - m_startExtraTime;
    }

     /**
     * Returns whether or not the {@link Command#timeSinceInitialized() timeSinceInitialized()}
     * method returns a number which is greater than or equal to the timeout for the command.
     * If there is no timeout, this will always return false.
     * @return whether the time has expired
     */
    protected synchronized boolean isExtraTimedOut() {
        return m_extraTimeout != -1 && timeSinceExtraInitialized() >= m_extraTimeout;
    }

    protected void resetTimer() {
        m_extraTimeout = -1;
    }
    
}
