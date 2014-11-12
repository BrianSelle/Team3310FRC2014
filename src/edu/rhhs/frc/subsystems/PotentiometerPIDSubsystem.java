package edu.rhhs.frc.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 * @author rhhs
 */
public abstract class PotentiometerPIDSubsystem extends PIDSubsystem {
    
    // Tolerance, calibration info
    protected double MIN_DEVICE_ANGLE_SOFT_LIMIT = 0.0; 
    protected double MAX_DEVICE_ANGLE_SOFT_LIMIT = 90.0;
    protected double POTENTIOMETER_TO_DEVICE_GEAR_RATIO = 1.0;

    protected double DEFAULT_POTENTIOMETER_ZERO_POSITION_VOLTS = 0.1;
    protected double MAX_ALLOWABLE_ERROR_VOLTS = 0.05;
   
    protected double NUM_POTENTIOMETER_TURNS = 1;
    protected double VOLTS_PER_DEG;
        
    protected Victor m_motorController = null;
    protected AnalogChannel m_potentiometer = null;
    
    protected double m_setDeviceAngleDeg = 0;
    protected double m_deviceZeroPositionOffsetDeg = 0;
    protected double m_potentiometer_volts_zero_position_volts;
    
    protected boolean m_softLimitOn = false;
        
    public PotentiometerPIDSubsystem(double kP, double kI, double kD) {
        super(kP, kI, kD);
    }
    
    // Methods for manual control
    public void rotateDevice(double speed) {
        // When a button is HELD down this is called repeatedly.  The disable() function sets the motor to 0
        // which causes the motor to switch between commanded speed and 0.  We only want to call disable once
        if (getPIDController().isEnable()) {
            this.disable();   
        }
        
        double deviceAngle = getDeviceAngle();
        if ((m_softLimitOn == true) && 
            ((deviceAngle < MIN_DEVICE_ANGLE_SOFT_LIMIT && speed > 0) ||
             (deviceAngle > MAX_DEVICE_ANGLE_SOFT_LIMIT && speed < 0))) {
            m_motorController.stopMotor();
//            System.out.println("stop motor controller, angle = " + getDeviceAngle() + ", min = " + MIN_DEVICE_ANGLE_SOFT_LIMIT + ", Max = " + MAX_DEVICE_ANGLE_SOFT_LIMIT);
        }
        else {
//            System.out.println("Got to motor controller = " + speed);
            m_motorController.set(speed);  
        }
    } 
    
    protected void initPotentiometerPID() {
        updatePotentiometerZeroPosition();
        VOLTS_PER_DEG = 5.0/(NUM_POTENTIOMETER_TURNS * 360.0);        
    }
    
    protected void setSoftLimitOn(boolean status) {
        m_softLimitOn = status;
    }
    
    protected void updatePotentiometerZeroPosition() {
        double motorAngleDeg = m_deviceZeroPositionOffsetDeg * POTENTIOMETER_TO_DEVICE_GEAR_RATIO;
        m_potentiometer_volts_zero_position_volts = DEFAULT_POTENTIOMETER_ZERO_POSITION_VOLTS + motorAngleDeg * VOLTS_PER_DEG;
    }
    
    public void setDeviceZeroPositionOffsetDeg(double deviceZeroAngleDeg) {
        m_deviceZeroPositionOffsetDeg = deviceZeroAngleDeg;
        updatePotentiometerZeroPosition();
        updateSetDeviceAngle();
    }
    
    public double getDeviceZeroPositionOffsetDeg() {
        return m_deviceZeroPositionOffsetDeg;
    }

    public void stop() {
        m_motorController.stopMotor();
    }
    
    // Methods for PID control to a position
    public void startPID() {
        this.enable();
    }
    
    public void setDeviceAngle(double deviceAngleDeg) {
        m_setDeviceAngleDeg = deviceAngleDeg;
        updateSetDeviceAngle();
    }
    
    protected void updateSetDeviceAngle() {
        double motorAngleDeg = m_setDeviceAngleDeg * POTENTIOMETER_TO_DEVICE_GEAR_RATIO;
        double potentiometerVolts = m_potentiometer_volts_zero_position_volts + motorAngleDeg * VOLTS_PER_DEG;
        this.setSetpoint(potentiometerVolts);
    }

    public double getDeviceAngle() {
        double motorAngleDeg = (m_potentiometer.getVoltage() - m_potentiometer_volts_zero_position_volts) / VOLTS_PER_DEG;
        double deviceAngleDeg = motorAngleDeg / POTENTIOMETER_TO_DEVICE_GEAR_RATIO;
        return deviceAngleDeg;
    }
    
    public boolean atSetpoint() {
        return Math.abs(getPIDController().getSetpoint() - returnPIDInput()) < MAX_ALLOWABLE_ERROR_VOLTS;
    }
    
    protected double returnPIDInput() {
        return m_potentiometer.getVoltage();
    }

    protected void usePIDOutput(double output) {
        m_motorController.set(output);
    }

    public void initDefaultCommand() {
    }
}
