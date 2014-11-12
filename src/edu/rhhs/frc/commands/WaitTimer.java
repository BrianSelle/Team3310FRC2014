package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class WaitTimer extends CommandBase {
    
    private double m_timeout;

    public WaitTimer(double timeoutSeconds) {
        m_timeout = timeoutSeconds;
    }

    protected void initialize() {
        setTimeout(m_timeout);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
