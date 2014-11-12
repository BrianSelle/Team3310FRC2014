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
public class PivotSetAngleTune extends CommandBase {
    
    private double m_angleDegrees;
    private double m_seekLockSpeed;
    private boolean m_isAtSetPoint = false;
    private boolean m_isPIDStarted = false;
    private boolean m_zeroPositionPreviousState = false;
    
    public PivotSetAngleTune(double angleDegrees) {
        m_angleDegrees = angleDegrees;
        requires(getPivot()); 
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_isPIDStarted = false;
        m_isAtSetPoint = false;
        m_zeroPositionPreviousState = false;
        m_seekLockSpeed = getPivot().getSeekSpeed(getPivot().getDevicePosition(), m_angleDegrees);
        getPivot().setDevicePosition(m_angleDegrees, Pivot.PIVOT_MAX_SPEED);
        getPivot().setPivotSpeed(-m_seekLockSpeed);
        getPivot().setLockPosition(PneumaticSubsystem.RETRACT);
        System.out.println("Pivot Lock Retract");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(!m_isPIDStarted && !getPivot().isPivotLockSwitchClosed()) {
            getPivot().startPID();
            System.out.println("Start PID");
            m_isPIDStarted = true;
        }
        
//        if (m_zeroPositionPreviousState == false && getPivot().isZeroPositionSwitchClosed()) {
//            getPivot().resetZeroPosition();
//            if (m_seekLockSpeed > 0) {
//                getPivot().setDeviceZeroPositionOffset(Pivot.PIVOT_ZERO_TUBE_WIDTH_ANGLE);
//                System.out.println("Reset Encoder.  Angle = " + Pivot.PIVOT_ZERO_TUBE_WIDTH_ANGLE);
//            }
//            else {
//                getPivot().setDeviceZeroPositionOffset(0);
//                System.out.println("Reset Encoder.  Angle = 0");
//            }
//        }
//        m_zeroPositionPreviousState = getPivot().isZeroPositionSwitchClosed();
        
        if (!m_isAtSetPoint && getPivot().atSetpoint()) {
            System.out.println("At setPoint = " + getPivot().getDevicePosition());
            if (Math.abs(m_angleDegrees) < 100) {
//                getPivot().setLockPosition(PneumaticSubsystem.EXTEND);
//                getPivot().setPivotSpeed(m_seekLockSpeed);
                System.out.println("Pivot Lock Extend");
                setTimeout(3);
            }
            m_isAtSetPoint = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
//        boolean atSetPointAndLocked = (m_isAtSetPoint && getPivot().isPivotLockSwitchClosed());
//        if (atSetPointAndLocked) {
//           System.out.println("Pivot Finished.   Pivot Lock = " + getPivot().isPivotLockSwitchClosed());
//           getPivot().setDeviceZeroPositionOffset(m_angleDegrees - getPivot().getDevicePosition());
//        }
//        return atSetPointAndLocked ;
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
        getPivot().stopPID();
        getPivot().setLockPosition(PneumaticSubsystem.EXTEND);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
