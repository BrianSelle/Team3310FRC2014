package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * @author rhhs
 */
public abstract class ExtraThreeTimeoutCommandBase extends CommandBase {

    protected double m_extraTimeout1 = -1;
    private double m_startExtraTime1;
    protected double m_extraTimeout2 = -1;
    private double m_startExtraTime2;
    protected double m_extraTimeout3 = -1;
    private double m_startExtraTime3;

    public ExtraThreeTimeoutCommandBase() {
    }

    /**
     * Sets the timeout of this command.
     *
     * @param seconds the timeout (in seconds)
     * @throws IllegalArgumentException if seconds is negative
     * @see Command#isTimedOut() isTimedOut()
     */
    protected synchronized final void startExtraTimeout1(double seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Seconds must be positive.  Given:" + seconds);
        }
        m_startExtraTime1 = Timer.getFPGATimestamp();
        m_extraTimeout1 = seconds;
    }

    protected synchronized final void startExtraTimeout2(double seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Seconds must be positive.  Given:" + seconds);
        }
        m_startExtraTime2 = Timer.getFPGATimestamp();
        m_extraTimeout2 = seconds;
    }

    protected synchronized final void startExtraTimeout3(double seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Seconds must be positive.  Given:" + seconds);
        }
        m_startExtraTime3 = Timer.getFPGATimestamp();
        m_extraTimeout3 = seconds;
    }

    /**
     * Returns the time since this command was initialized (in seconds).
     * This function will work even if there is no specified timeout.
     * @return the time since this command was initialized (in seconds).
     */
    public synchronized final double timeSinceExtraInitialized1() {
        return m_startExtraTime1 < 0 ? 0 : Timer.getFPGATimestamp() - m_startExtraTime1;
    }

    public synchronized final double timeSinceExtraInitialized2() {
        return m_startExtraTime2 < 0 ? 0 : Timer.getFPGATimestamp() - m_startExtraTime2;
    }

    public synchronized final double timeSinceExtraInitialized3() {
        return m_startExtraTime3 < 0 ? 0 : Timer.getFPGATimestamp() - m_startExtraTime3;
    }

     /**
     * Returns whether or not the {@link Command#timeSinceInitialized() timeSinceInitialized()}
     * method returns a number which is greater than or equal to the timeout for the command.
     * If there is no timeout, this will always return false.
     * @return whether the time has expired
     */
    protected synchronized boolean isExtraTimedOut1() {
        return m_extraTimeout1 != -1 && timeSinceExtraInitialized1() >= m_extraTimeout1;
    }
    
    protected synchronized boolean isExtraTimedOut2() {
        return m_extraTimeout2 != -1 && timeSinceExtraInitialized2() >= m_extraTimeout2;
    }

    protected synchronized boolean isExtraTimedOut3() {
        return m_extraTimeout3 != -1 && timeSinceExtraInitialized3() >= m_extraTimeout3;
    }

    protected void resetTimer1() {
        m_extraTimeout1 = -1;
    }
    
    protected void resetTimer2() {
        m_extraTimeout2 = -1;
    }
    
    protected void resetTimer3() {
        m_extraTimeout3 = -1;
    }
}
