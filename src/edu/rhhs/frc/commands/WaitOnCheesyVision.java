/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

/**
 *
 * @author kenny
 */
public class WaitOnCheesyVision extends CommandBase {
    
    private boolean m_hotDetected;
    private double m_timeout;
    
    public WaitOnCheesyVision(double timeout) {
        m_timeout = timeout;
        requires(getVisionServer());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_hotDetected = false;
        this.setTimeout(m_timeout);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (getVisionServer().getLeftStatus() || getVisionServer().getRightStatus()) {
            m_hotDetected = true;
            System.out.println("Hot goal detected");
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (m_hotDetected) {
            System.out.println("Hot goal finished");
        }
        else if (isTimedOut()) {
            System.out.println("Vision Timeout");
        }

        return m_hotDetected || isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
