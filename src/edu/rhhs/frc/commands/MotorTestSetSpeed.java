/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

/**
 *
 * @author bselle
 */
public class MotorTestSetSpeed extends CommandBase {
    
    private int m_motorId;
    private double m_motorSpeed;
    
    public MotorTestSetSpeed(int motorId, double motorSpeed) {
        System.out.println("Motor Command Construct Motor Id = " + motorId +  ", speed = " + motorSpeed);
        m_motorSpeed = motorSpeed;
        m_motorId = motorId;
        requires(getMotorTest());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("Motor Command init Motor Id = " + m_motorId +  ", speed = " + m_motorSpeed);
        if (m_motorSpeed != 0.0) {
            getMotorTest().setSpeed(m_motorId, m_motorSpeed);
        }
        else {
            getMotorTest().stop(m_motorId);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
