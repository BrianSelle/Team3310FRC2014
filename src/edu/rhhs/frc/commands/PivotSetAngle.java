/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Pivot;
import edu.rhhs.frc.subsystems.PneumaticSubsystem;

/**
 * @author rhhs
 */
public class PivotSetAngle extends CommandBase {
    
    private static final double NO_PIVOT_LOCK_THRESHOLD_ANGLE = 80;
    private double m_angleDegrees;
    private double m_seekLockSpeed;
    private boolean m_isAtSetPoint = false;
    private boolean m_isPIDStarted = false;
//    private boolean m_zeroPositionPreviousState = false;
    private boolean m_alreadyAtSetPosition = false;
    
    public PivotSetAngle(double angleDegrees) {
        m_angleDegrees = angleDegrees;
        requires(getPivot()); 
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_alreadyAtSetPosition = false;
        if (Math.abs(m_angleDegrees - getPivot().getDevicePosition()) < 5) {
            m_alreadyAtSetPosition = true;
            return;
        }
        m_isPIDStarted = false;
        m_isAtSetPoint = false;
//        m_zeroPositionPreviousState = false;
//        getPivot().setDeviceMaxAllowablePositionError(3.0);
        m_seekLockSpeed = getPivot().getSeekSpeed(getPivot().getDevicePosition(), m_angleDegrees);
 //       System.out.println("Seek Speed = " + m_seekLockSpeed);
        getPivot().setDevicePosition(m_angleDegrees, Pivot.PIVOT_MAX_SPEED);
        double weightOffPinSpeed = getPivot().getWeightOffPinSpeed(getPivot().getDevicePosition());
//        System.out.println("Weight Off Pin Speed = " + weightOffPinSpeed);
        getPivot().setPivotSpeed(weightOffPinSpeed);
        getPivot().setLockPosition(PneumaticSubsystem.RETRACT);
 //       System.out.println("Pivot Lock Retract");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (m_alreadyAtSetPosition) {
            return;
        }
        
        if(!m_isPIDStarted && !getPivot().isPivotLockSwitchClosed()) {
            getPivot().startPID();
            System.out.println("Start PID");
            m_isPIDStarted = true;
        }
        
//        if (m_zeroPositionPreviousState == false && getPivot().isZeroPositionSwitchClosed()) {
//            getPivot().resetZeroPosition();
//            if (m_seekLockSpeed > 0) {
//                getPivot().setDeviceZeroPositionOffset(Pivot.PIVOT_ZERO_TUBE_WIDTH_ANGLE);
//            }
//            else {
//                getPivot().setDeviceZeroPositionOffset(0);
//            }
//        }
//        m_zeroPositionPreviousState = getPivot().isZeroPositionSwitchClosed();
        
        if (!m_isAtSetPoint && getPivot().atSetpoint()) {
            System.out.println("At setPoint = " + getPivot().getDevicePosition());
            
            // If we are not at the intake positions, extend the lock pin
            if (Math.abs(m_angleDegrees) < NO_PIVOT_LOCK_THRESHOLD_ANGLE) {
                getPivot().setLockPosition(PneumaticSubsystem.EXTEND);
                getPivot().setPivotSpeed(m_seekLockSpeed);
 //               System.out.println("Seeking, Pivot Lock Extend");
            }
            else {
                getPivot().setPivotSpeed(0.1);
            }
            m_isAtSetPoint = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (m_alreadyAtSetPosition) {
            return true;
        }

        boolean atSetPointAndLocked = (m_isAtSetPoint && getPivot().isPivotLockSwitchClosed());
        
        if (atSetPointAndLocked) {
            // If we are the upright position and the pin is extended reset everything
            if (m_angleDegrees == Pivot.PIVOT_POSITION_UP) {
                getPivot().resetZeroPosition();
                getPivot().setDeviceZeroPositionOffset(0);
            }
            
            // If we are at a known position other than upright, reset the offset
            else {
//                System.out.println("Pivot Finished.   Pivot Lock = " + getPivot().isPivotLockSwitchClosed());
                double newOffset = m_angleDegrees - getPivot().getDevicePosition();
//                System.out.println("Pivot New Offset = " + newOffset);
                getPivot().setDeviceZeroPositionOffset(newOffset);
//                System.out.println("New Pivot Angle = " + getPivot().getDevicePosition());
            }
        }
        
        // For the intake positions we don't extend the lock pin
        if (Math.abs(m_angleDegrees) > NO_PIVOT_LOCK_THRESHOLD_ANGLE) {
            atSetPointAndLocked = m_isAtSetPoint;
        } 

        return atSetPointAndLocked ;
    }

    // Called once after isFinished returns true
    protected void end() {
        getPivot().stopPID();
        
        // For the intake positions we don't extend the lock pin
        if (Math.abs(m_angleDegrees) < NO_PIVOT_LOCK_THRESHOLD_ANGLE) {
            getPivot().setLockPosition(PneumaticSubsystem.EXTEND);
        } 
        else {
            getPivot().setPivotSpeed(0.07);
        }
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
