/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.PneumaticSubsystem;
import edu.rhhs.frc.subsystems.Winch;

/**
 * @author rhhs
 */
public class WinchSafeRelease extends ExtraTwoTimeoutCommandBase {
    
    private static final double WINCH_ENGAGE_TIMEOUT = 0.5;   
    private static final double WINCH_RELEASE_TIMEOUT = 1.0;   
    private boolean m_winchEngaged; 
    
    public WinchSafeRelease() {
        requires(getWinch()); 
        setInterruptible(false);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("Winch safe release start");
        m_winchEngaged = false;
        getWinch().setShiftPosition(PneumaticSubsystem.EXTEND);
        getWinch().setWinchSpeed(Winch.WINCH_RELEASE_SPEED);
        startExtraTimeout1(WINCH_ENGAGE_TIMEOUT);
        resetTimer2();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!m_winchEngaged && isExtraTimedOut1()) {
            getWinch().setBrakePosition(PneumaticSubsystem.RETRACT);
            startExtraTimeout2(WINCH_RELEASE_TIMEOUT);
            m_winchEngaged = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (isExtraTimedOut2()) {
            System.out.println("Winch safe release finished");
        }
        return isExtraTimedOut2();
    }

    // Called once after isFinished returns true
    protected void end() {
        getWinch().stopWinch();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println("Winch safe release Interrupted");
        end();
    }
}
