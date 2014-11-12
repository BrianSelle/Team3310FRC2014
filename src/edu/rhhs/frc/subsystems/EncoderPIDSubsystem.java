package edu.rhhs.frc.subsystems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 * @author rhhs
 */
public abstract class EncoderPIDSubsystem extends PIDSubsystem {
            
    protected static final double DEFAULT_MAX_ALLOWABLE_POSITION_ERROR = 2.0;
    protected final static double DEFAULT_PULSES_PER_ROTATION = 360;
    
    protected Victor m_motorController;
    protected Encoder m_encoder;
    
    protected boolean m_softLimitOn = false;

    protected double m_deviceZeroPositionOffset = 0;
    protected double m_deviceMinPosition;
    protected double m_deviceMaxPosition;
    protected double m_deviceMaxSpeed = 1.0;
    protected double m_deviceMaxAllowablePositionError = DEFAULT_MAX_ALLOWABLE_POSITION_ERROR;

    protected double m_feedForwardInput = 0;
       
    public EncoderPIDSubsystem(String name, double kP, double kI, double kD, int motorChannel, int encoderAChannel, int encoderBChannel, boolean reverseEncoder, double gearRatioEncoderToOutput) {
        super(name, kP, kI, kD);
        try {
            m_motorController = new Victor(motorChannel);
            m_encoder = new Encoder(1, encoderAChannel, 1, encoderBChannel, reverseEncoder, CounterBase.EncodingType.k4X);
            double degPerPulse = 360.0 / gearRatioEncoderToOutput / DEFAULT_PULSES_PER_ROTATION;
            m_encoder.setDistancePerPulse(degPerPulse);
            resetZeroPosition();
        } catch (Exception e) {
            System.out.println("Unknown error initializing " + getName() + "-EncoderPIDSubsystem.  Message = " + e.getMessage());
        }
    }
    
    public EncoderPIDSubsystem(String name, double kP, double kI, double kD, int motorChannel, int encoderAChannel, int encoderBChannel, boolean reverseEncoder, double wheelDiameter, double gearRatioEncoderToWheel) {
        super(name, kP, kI, kD);
        try {
            m_motorController = new Victor(motorChannel);
            m_encoder = new Encoder(1, encoderAChannel, 1, encoderBChannel, reverseEncoder, CounterBase.EncodingType.k4X);
            double distancePerPulse = Math.PI * wheelDiameter / gearRatioEncoderToWheel / DEFAULT_PULSES_PER_ROTATION;
            m_encoder.setDistancePerPulse(distancePerPulse);
            resetZeroPosition();
        } catch (Exception e) {
            System.out.println("Unknown error initializing " + getName() + "-EncoderPIDSubsystem.  Message = " + e.getMessage());
        }
    }
    
    protected void setSoftLimitOn(boolean status) {
        m_softLimitOn = status;
    }
    
    protected void setSoftLimits(double minPositionLimit, double maxPositionLimit) {
        m_deviceMinPosition = minPositionLimit;
        m_deviceMaxPosition = maxPositionLimit;
    }
    
    public void setDeviceMaxAllowablePositionError(double deviceMaxAllowablePositionError) {
        m_deviceMaxAllowablePositionError = deviceMaxAllowablePositionError;
    }
    
    public void setDeviceZeroPositionOffset(double deviceZeroPositionOffset) {
        m_deviceZeroPositionOffset = deviceZeroPositionOffset;
    }
    
    // Methods for manual control
    public void moveDevice(double speed) {
        // When a button is HELD down this is called repeatedly.  The disable() function sets the motor to 0
        // which causes the motor to switch between commanded speed and 0.  We only want to call disable once
        if (getPIDController().isEnable()) {
            this.disable();   
        }
        
        setMotorSpeed(speed);
    } 
    
    public void stopMotor() {
        m_motorController.stopMotor();
    }
    
    // Methods for PID control to a position
    public void startPID() {
        this.enable();
    }
    
    public void stopPID() {
        this.disable();
    }

    public void resetZeroPosition() {
        m_encoder.reset();
        m_encoder.start();
        getPIDController().reset();
    }
    
    public void setDevicePosition(double devicePosition, double maxSpeed) {
        m_deviceMaxSpeed = maxSpeed;
        this.setSetpoint(devicePosition);
    }
    
    public void setFeedForwardInput(double feedForwardInput) {
        m_feedForwardInput = feedForwardInput;
    }
    
    public double getDevicePosition() {
        return m_encoder.getDistance() + m_deviceZeroPositionOffset;
    }
    
    protected void updateDeviceSetPosition() {
    }

    public boolean atSetpoint() {
        return Math.abs(getPIDController().getSetpoint() - returnPIDInput()) < m_deviceMaxAllowablePositionError;
    }
    
    protected double returnPIDInput() {
        return getDevicePosition();
    }

    protected void usePIDOutput(double output) {
        output += m_feedForwardInput;
        setMotorSpeed(output);
    }

    protected void setMotorSpeed(double requestedMotorSpeed) {
        if (Math.abs(requestedMotorSpeed) > m_deviceMaxSpeed) {
            if (requestedMotorSpeed < 0) {
                requestedMotorSpeed = -m_deviceMaxSpeed;
            }
            else {
                requestedMotorSpeed = m_deviceMaxSpeed;
            }
        }
        
        System.out.println("Requested speed = " + requestedMotorSpeed + ", Position = " + getDevicePosition());

        double devicePosition = getDevicePosition();
        if (m_softLimitOn == true) {
            if ((devicePosition < m_deviceMaxPosition && requestedMotorSpeed > 0) ||
                (devicePosition > m_deviceMinPosition && requestedMotorSpeed < 0)) {
               m_motorController.set(requestedMotorSpeed);            
            }
            else {
                m_motorController.stopMotor();
            }
        }
        else {
            m_motorController.set(requestedMotorSpeed);  
        }
    }
    
    public void initDefaultCommand() {
    }
}
