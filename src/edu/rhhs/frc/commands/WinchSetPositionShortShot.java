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
public class WinchSetPositionShortShot extends ExtraThreeTimeoutCommandBase {
    
    private static final double WINCH_ENGAGE_TIMEOUT = 0.1;   
    private static final double WINCH_BRAKE_EXTEND_TIMEOUT = 0.1;   
    private static final double WINCH_FREE_TIMEOUT = 0.1;   
    private boolean m_winchEngaged;
    private boolean m_brakeExtended;
    private boolean m_winchFree;
    private double m_deltaDistance;
    
    public WinchSetPositionShortShot(double deltaDistance) {
        m_deltaDistance = deltaDistance;
        requires(getWinch()); 
        setInterruptible(false);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_winchEngaged = false;
        m_brakeExtended = false;
        m_winchFree = false;
        resetTimer1();
        resetTimer2();
        resetTimer3();
        
        // Check if we are already at the short shot position
//        if (getWinch().getWinchPosition() == Winch.WINCH_AT_SHORT_SHOT) {
//            startExtraTimeout3(0);
//            return;
//        }

        System.out.println("Winch retract started Distance = " + m_deltaDistance);
        getWinch().resetZeroPosition();
        getWinch().setDeviceZeroPositionOffset(0);
        getWinch().setShiftPosition(PneumaticSubsystem.EXTEND);
        getWinch().setWinchSpeed(Winch.WINCH_RELEASE_SPEED);
        startExtraTimeout1(WINCH_ENGAGE_TIMEOUT);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!m_winchEngaged && isExtraTimedOut1()) {
            System.out.println("Winch retract brake retracted");
            getWinch().setWinchSpeed(Winch.WINCH_RETRACT_SPEED);
            getWinch().setBrakePosition(PneumaticSubsystem.RETRACT);
            m_winchEngaged = true;
        }
        else if (!m_brakeExtended && m_winchEngaged && (m_deltaDistance - getWinch().getDevicePosition()) > 0) {
            System.out.println("Winch retract brake extended.  Current = " + getWinch().getDevicePosition());
            getWinch().setWinchSpeed(Winch.WINCH_HOLD_SPEED);
            getWinch().setBrakePosition(PneumaticSubsystem.EXTEND);
            startExtraTimeout2(WINCH_BRAKE_EXTEND_TIMEOUT); 
            m_brakeExtended = true;
        }
        else if (!m_winchFree && isExtraTimedOut2()) {
            getWinch().setBrakePosition(PneumaticSubsystem.EXTEND);
            getWinch().setShiftPosition(PneumaticSubsystem.RETRACT);
            getWinch().setWinchSpeed(-0.2);
            startExtraTimeout3(WINCH_FREE_TIMEOUT); 
            m_winchFree = true;
        }
        System.out.println("Position = " + getWinch().getDevicePosition() + ", Delta distance = " + (m_deltaDistance - getWinch().getDevicePosition()));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (isExtraTimedOut3()) {
            System.out.println("Winch free finished");
        }
        return isExtraTimedOut3();
    }

    // Called once after isFinished returns true
    protected void end() {
        getWinch().setBrakePosition(PneumaticSubsystem.EXTEND);
        getWinch().setShiftPosition(PneumaticSubsystem.RETRACT);
        getWinch().stopWinch();
        getWinch().setWinchPosition(Winch.WINCH_AT_SHORT_SHOT);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println("Winch retract Interrupted");
        end();
    }
}
