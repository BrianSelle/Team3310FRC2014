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
public class WinchSetPositionLongShot extends ExtraTimeoutCommandBase {
    
    private static final double PID_TIMEOUT = 0.2;   
    private double m_distanceIn;
    private double m_maxSpeed;
    private boolean m_atSetpoint;
    
    public WinchSetPositionLongShot(double distanceIn, double maxSpeed) {
        m_distanceIn = distanceIn;
        m_maxSpeed = maxSpeed;
        requires(getWinch()); 
//        setInterruptible(false);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_atSetpoint = false;
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
        if (!m_atSetpoint && (getWinch().isDrawLimitSwitch1Closed() || getWinch().isDrawLimitSwitch2Closed() || getWinch().atSetpoint())) {
//            if (getWinch().isDrawLimitSwitch1Closed()) {
//                System.out.println("Limit Switch 1 Closed");
//            }
//            if (getWinch().isDrawLimitSwitch2Closed()) {
//                System.out.println("Limit Switch 2 Closed");
//            }
//            if (getWinch().atSetpoint()) {
//                System.out.println("At Setpoint");
//            }
            getWinch().setBrakePosition(PneumaticSubsystem.EXTEND);
            getWinch().setWinchPosition(Winch.WINCH_AT_LONG_SHOT);
            startExtraTimeout(PID_TIMEOUT);
            m_atSetpoint = true;
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
        getWinch().stopPID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
//        System.out.println("Winch Interrupted");
        end();
    }
}
