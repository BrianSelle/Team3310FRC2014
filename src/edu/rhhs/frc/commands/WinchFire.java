/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.PneumaticSubsystem;
import edu.rhhs.frc.subsystems.Winch;

/**
 *
 * @author kenny
 */
public class WinchFire extends ExtraThreeTimeoutCommandBase {
    
    private static final double WAIT_FOR_FIRE_TO_COMPLETE = 0.2;
    private static final double REWIND_TIME = 1.0;
    private static final double UNWIND_TIME = 0.0;
    private boolean m_doneFiring;
    private boolean m_doneRewinding;
    private boolean m_isBallFound;
    
    public WinchFire() {
        requires(getWinch());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_doneFiring = false;
        m_doneRewinding = false;
        m_isBallFound = getIntake().isBallDetectSwitchClosed();
        System.out.println("IsBallFound = " + m_isBallFound);
        
        if (m_isBallFound) {
            System.out.println("FIRE");
            getWinch().setShiftPosition(PneumaticSubsystem.RETRACT);
            getWinch().setBrakePosition(PneumaticSubsystem.RETRACT);
            getWinch().setWinchPosition(Winch.WINCH_AT_ZERO);
            startExtraTimeout1(WAIT_FOR_FIRE_TO_COMPLETE);
            resetTimer2();
            resetTimer3();
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!m_isBallFound) {
            return;
        }
        if (m_doneFiring == false && isExtraTimedOut1()) {
            System.out.println("Done with fire 1");
            getWinch().setShiftPosition(PneumaticSubsystem.EXTEND);
            getWinch().setWinchSpeed(Winch.WINCH_REWIND_SPEED);
            startExtraTimeout2(REWIND_TIME);
            m_doneFiring = true;
        }
        else if (m_doneRewinding == false && isExtraTimedOut2()) {
            System.out.println("Done with rewinding");
            getWinch().stopWinch();
            startExtraTimeout3(UNWIND_TIME);
            m_doneRewinding = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (!m_isBallFound) {
            return true;
        }
        return isExtraTimedOut3();
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("Reset Zero");
        getWinch().stopWinch();
        if (m_isBallFound) {
            getWinch().resetZeroPosition();
        }
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println("Winch fire interupted");
        end();
    }
}
