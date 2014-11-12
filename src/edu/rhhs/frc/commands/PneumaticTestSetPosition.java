/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

/**
 *
 * @author bselle
 */
public class PneumaticTestSetPosition extends CommandBase {
    
    private int m_valveId;
    private int m_position;
    
    public PneumaticTestSetPosition(int valveId, int position) {
        m_valveId = valveId;
        m_position = position;
        requires(getPneumaticTest());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        getPneumaticTest().setPosition(m_valveId, m_position);
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
