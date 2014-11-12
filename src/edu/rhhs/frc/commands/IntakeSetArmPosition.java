/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.PneumaticSubsystem;

/**
 *
 * @author bselle
 */
public class IntakeSetArmPosition extends CommandBase {
    
    private int m_position;
    
    public IntakeSetArmPosition(int position) {
        m_position = position;
        requires(getIntake());
        requires(getPivot());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        // Don't want to open the arms if we are doing a back shot
        if (m_position == PneumaticSubsystem.EXTEND && getPivot().getDevicePosition() < -10) {
            return;
        }
        getIntake().setPosition(m_position);
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
