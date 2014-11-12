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
public class WinchSetPositionLongShotAdjust extends ExtraTimeoutCommandBase {
    
    private static final double WINCH_BRAKE_EXTEND_TIMEOUT = 0.1;   

    private double m_distanceIn;
    private double m_maxSpeed;
    private boolean m_atFirstSetpoint;
    private boolean m_atSecondSetpoint;
    private double m_deltaDistance;
    
    public WinchSetPositionLongShotAdjust(double distanceIn, double maxSpeed) {
        m_distanceIn = distanceIn;
        m_deltaDistance = Winch.WINCH_LONG_ADJUST_DISTANCE;
        m_maxSpeed = maxSpeed;
        requires(getWinch()); 
//        setInterruptible(false);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_atFirstSetpoint = false;
        m_atSecondSetpoint = false;
//        System.out.println("Start winch position = " + m_distanceIn + ", Current = " + getWinch().getDevicePosition());
        getWinch().setDevicePosition(m_distanceIn, m_maxSpeed);
        getWinch().setBrakePosition(PneumaticSubsystem.RETRACT);
        getWinch().setShiftPosition(PneumaticSubsystem.EXTEND);
        getWinch().startPID();
        resetTimer();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//        if (getWinch().isDrawZeroSwitchClosed() == true) {
//            System.out.println("Zero switch closed");
//        }
        if (!m_atFirstSetpoint && (getWinch().isDrawLimitSwitch1Closed() || getWinch().isDrawLimitSwitch2Closed())) {
            getWinch().resetZeroPosition();
            getWinch().setDeviceZeroPositionOffset(0);
            getWinch().setWinchSpeed(Winch.WINCH_RETRACT_SPEED);
            m_atFirstSetpoint = true;
        }
        else if (m_atFirstSetpoint && !m_atSecondSetpoint && (m_deltaDistance - getWinch().getDevicePosition()) > 0) {
            System.out.println("Winch retract brake extended.  Current = " + getWinch().getDevicePosition());
            getWinch().setWinchSpeed(Winch.WINCH_HOLD_SPEED);
            getWinch().setBrakePosition(PneumaticSubsystem.EXTEND);
            startExtraTimeout(WINCH_BRAKE_EXTEND_TIMEOUT); 
            m_atSecondSetpoint = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
//        if (isExtraTimedOut()) {
//            System.out.println("Is timed out");
//        }
        return isExtraTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
//        System.out.println("Winch Set Position End");
        getWinch().setBrakePosition(PneumaticSubsystem.EXTEND);
        getWinch().setShiftPosition(PneumaticSubsystem.RETRACT);
        getWinch().stopWinch();
        getWinch().stopPID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
//        System.out.println("Winch Interrupted");
        end();
    }
}
