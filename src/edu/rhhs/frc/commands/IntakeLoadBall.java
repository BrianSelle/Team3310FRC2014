/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Intake;
import edu.rhhs.frc.subsystems.PneumaticSubsystem;

/**
 *
 * @author bselle
 */
public class IntakeLoadBall extends ExtraTimeoutCommandBase {
    
    private static final double EXTRA_INTAKE_TIME = 0.6;
    private boolean m_ballDetected = false;
    
    public IntakeLoadBall() {
        requires(getIntake());
        requires(getWings());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_ballDetected = false;
        resetTimer();
        getIntake().setRollerSpeed(Intake.INTAKE_ROLLER_SPEED_BALL_PICKUP);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!m_ballDetected && getIntake().isBallDetectSwitchClosed()) {
            startExtraTimeout(EXTRA_INTAKE_TIME);
            getWings().setPosition(PneumaticSubsystem.RETRACT);
            getIntake().setPosition(PneumaticSubsystem.RETRACT);
            m_ballDetected = true;
        };
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isExtraTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
        getIntake().stopRoller();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
