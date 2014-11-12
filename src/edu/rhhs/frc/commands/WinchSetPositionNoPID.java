/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.PneumaticSubsystem;

/**
 * @author rhhs
 */
public class WinchSetPositionNoPID extends ExtraTimeoutCommandBase {
    
    private static final double BRAKE_EXTEND_TIMEOUT = 0.2;   
    private boolean m_atSetpoint;
    
    public WinchSetPositionNoPID() {
        requires(getWinch()); 
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_atSetpoint = false;
//        System.out.println("Start winch long shot, Current = " + getWinch().getDevicePosition());
        getWinch().setBrakePosition(PneumaticSubsystem.RETRACT);
        getWinch().setShiftPosition(PneumaticSubsystem.EXTEND);
        getWinch().setWinchSpeed(0.5);
        resetTimer();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!m_atSetpoint && (getWinch().isDrawLimitSwitch1Closed() || getWinch().isDrawLimitSwitch2Closed())) {
//            if (getWinch().isDrawLimitSwitch1Closed()) {
//                System.out.println("Limit Switch 1 Closed");
//            }
//            if (getWinch().isDrawLimitSwitch2Closed()) {
//                System.out.println("Limit Switch 2 Closed");
//            }
            getWinch().setBrakePosition(PneumaticSubsystem.EXTEND);
            getWinch().setWinchSpeed(0.9);
            startExtraTimeout(BRAKE_EXTEND_TIMEOUT);
            m_atSetpoint = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
//        if (isExtraTimedOut()) {
//            System.out.println("Winch long shot Is timed out");
//        }
        return isExtraTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
//        System.out.println("Winch Set Position long shot End");
        getWinch().setBrakePosition(PneumaticSubsystem.EXTEND);
        getWinch().setShiftPosition(PneumaticSubsystem.RETRACT);
        getWinch().stopWinch();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
//        System.out.println("Winch long shot Interrupted");
        end();
    }
}
