/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.PneumaticSubsystem;

/**
 * @author rhhs
 */
public class WinchSetPositionNew extends ExtraTwoTimeoutCommandBase {
    
    private static final double PID_TIMEOUT = 2.0;   
    private static final double HOLD_CURRENT_POSITION_TIMEOUT = 1.0;
    private static final double RETRACT_BRAKE_TIMEOUT = 0.3;
    private final double m_distanceIn;
    private final double m_maxSpeed;
    private boolean m_retracting;
    private boolean m_pidSpooledUp;
    private boolean m_brakeFullyRetracted;
    private boolean m_atSetPoint; 

    public WinchSetPositionNew(double distanceIn, double maxSpeed) {
        m_distanceIn = distanceIn;
        m_maxSpeed = maxSpeed;
        requires(getWinch()); 
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        
        // Reset everything
        m_retracting = false;
        m_pidSpooledUp = false;
        m_brakeFullyRetracted = false;
        m_atSetPoint = false; 
        resetTimer1();
        resetTimer2();
        
        // Retracting bow to a lower draw position
        if (m_distanceIn < getWinch().getDevicePosition()) {
            System.out.println("Winch release Set = " + m_distanceIn + ", Current = " + getWinch().getDevicePosition());
            getWinch().setDevicePosition(getWinch().getDevicePosition() + 0.1, 0.1);
            getWinch().setShiftPosition(PneumaticSubsystem.EXTEND);
            getWinch().startPID();
            startExtraTimeout1(HOLD_CURRENT_POSITION_TIMEOUT);
            m_retracting = true;
        }
        
        // Pulling bow to a higher draw position
        else {
            getWinch().setDevicePosition(m_distanceIn, m_maxSpeed);
            getWinch().setBrakePosition(PneumaticSubsystem.RETRACT);
            getWinch().setShiftPosition(PneumaticSubsystem.EXTEND);
            getWinch().startPID();
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

        //  Retracting bow to a lower draw position
        if (m_retracting) {
            
            // Wait until the PID has spooled up and is holding the force of the bow
            if (!m_pidSpooledUp && isExtraTimedOut1()) {
                System.out.println("PID Spooled Up");
               getWinch().setBrakePosition(PneumaticSubsystem.RETRACT);
                startExtraTimeout2(RETRACT_BRAKE_TIMEOUT);
                m_pidSpooledUp = true;
            }
            
            // Wait until the brake lock has been fully retracted before setting the new position
            else if (!m_brakeFullyRetracted && isExtraTimedOut2()) {
                System.out.println("Fully Retracted");
                getWinch().setDevicePosition(m_distanceIn, 1.0);
                m_brakeFullyRetracted = true;
            }
            
            // Check if we are at the setpoint
            else if (m_pidSpooledUp && m_brakeFullyRetracted) {
                if (!m_atSetPoint && (getWinch().isDrawLimitSwitch1Closed() || getWinch().isDrawLimitSwitch2Closed() || getWinch().atSetpoint())) {
                    System.out.println("At Lesser Setpoint");
                    getWinch().setBrakePosition(PneumaticSubsystem.EXTEND);
                    setTimeout(PID_TIMEOUT);
                    m_atSetPoint = true; 
               }
            }            
        }
        
        // Pulling bow to a higher draw position
        else {
            if (!m_atSetPoint && (getWinch().isDrawLimitSwitch1Closed() || getWinch().isDrawLimitSwitch2Closed() || getWinch().atSetpoint())) {
                     System.out.println("At Greater Setpoint");
                getWinch().setBrakePosition(PneumaticSubsystem.EXTEND);
                setTimeout(PID_TIMEOUT);
                m_atSetPoint = true; 
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("End");
        getWinch().stopPID();
        getWinch().setBrakePosition(PneumaticSubsystem.EXTEND);
        getWinch().setShiftPosition(PneumaticSubsystem.RETRACT);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
