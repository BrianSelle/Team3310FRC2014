/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

/**
 *
 * @author bselle
 */
public class IntakeOnTimed extends CommandBase {
    
    private double m_motorSpeed;
    private double m_time;
    
    public IntakeOnTimed(double time, double motorSpeed) {
        m_motorSpeed = motorSpeed;
        m_time = time;
        requires(getIntake());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        getIntake().setRollerSpeed(m_motorSpeed);
        setTimeout(m_time);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
        getIntake().stopRoller();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
