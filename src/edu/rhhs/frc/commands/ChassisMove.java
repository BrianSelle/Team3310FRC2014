/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class ChassisMove extends ExtraTimeoutCommandBase {
    
    private static final double PID_AT_TARGET_TIMEOUT = 0.3;
    private double m_distanceIn;
    private double m_maxSpeed;
    private boolean m_reset;
    private boolean m_atSetpoint = false;
    
    public ChassisMove(double distanceIn, double maxSpeed, boolean reset) {
        m_distanceIn = distanceIn;
        m_maxSpeed = maxSpeed;
        m_reset = reset;
        requires(getChassis()); // reserve the chassis subsystem
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_atSetpoint = false;
        resetTimer();
        getChassis().setDistance(m_distanceIn, m_maxSpeed);
        if (m_reset == true) {
            getChassis().startPID();
        }
        else {
            getChassis().startNoResetPID();
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!m_atSetpoint && getChassis().atSetPoint()) {
            startExtraTimeout(PID_AT_TARGET_TIMEOUT);
            m_atSetpoint = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isExtraTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
        getChassis().disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
