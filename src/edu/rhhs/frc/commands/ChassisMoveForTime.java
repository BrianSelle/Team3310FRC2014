/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class ChassisMoveForTime extends ExtraTimeoutCommandBase {
    
    private double m_time;
    private double m_speed;

    public ChassisMoveForTime(double time, double speed) {
        m_time = time;
        m_speed = speed;
        requires(getChassis()); // reserve the chassis subsystem
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        resetTimer();
        getChassis().setMoveMotorSpeed(m_speed);
        startExtraTimeout(m_time);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isExtraTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
        getChassis().setMoveMotorSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
