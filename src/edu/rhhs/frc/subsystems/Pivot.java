package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author kenny
 */
public class Pivot extends EncoderPIDSubsystem {
    
    public static final double PIVOT_POSITION_FORWARD_INTAKE = 121;
    public static final double PIVOT_POSITION_REVERSE_INTAKE = -124;
    public static final double PIVOT_POSITION_LONG_SHOT = 55;
    public static final double PIVOT_POSITION_REVERSE_LONG_SHOT = -55;
    public static final double PIVOT_POSITION_FORWARD_SHORT_SHOT = 30;
    public static final double PIVOT_POSITION_REVERSE_SHORT_SHOT = -30;
    public static final double PIVOT_POSITION_UP = 0;
    public static final double PIVOT_POSITION_FORWARD_CATCH_HIGH = 30;
    public static final double PIVOT_POSITION_FORWARD_CATCH_LOW = 60;
    public static final double PIVOT_POSITION_REVERSE_CATCH_HIGH = -30;
    public static final double PIVOT_POSITION_REVERSE_CATCH_LOW = -60;
    public static final double PIVOT_POSITION_FORWARD_AUTO_HOLD = 63;
    
    public static final double PIVOT_MAX_SPEED = 1.00;
    public static final double PIVOT_SEEK_LOCK_SPEED_UNLOAD = 0.1;
    public static final double PIVOT_SEEK_LOCK_SPEED_UP = 0.5;  // 0.4
    public static final double PIVOT_SEEK_LOCK_SPEED_DOWN = 0.1;  
    public static final double MAX_ALLOWABLE_ANGLE_ERROR = 10;
    public static final double PIVOT_ZERO_TUBE_WIDTH_ANGLE = -5;

    protected static final double ENCODER_TO_YOKE_GEAR_RATIO = 5.81818;
    
    // Keith p=0.03, i=0, d=0.05, limit = 0.7
    
    protected static final double kP = 0.04; //0.025
    protected static final double kI = 0.001; // 0.001
    protected static final double kD = 0.13; //0.11
    
    private DigitalInput m_pivotLockSwitch;

    protected Solenoid m_lockSolenoidExtend; 
    protected Solenoid m_lockSolenoidRetract; 
    
    protected String m_lockPosition = PneumaticSubsystem.UNKNOWN;
    
    public Pivot() {
        super("Pivot", kP, kI, kD, RobotMap.PIVOT_DSC_PWM_ID, RobotMap.PIVOT_ENCODER_A_DSC_DIO_ID, RobotMap.PIVOT_ENCODER_B_DSC_DIO_ID, false, ENCODER_TO_YOKE_GEAR_RATIO);
        try {
            m_pivotLockSwitch = new DigitalInput(RobotMap.PIVOT_LOCK_SWITCH_DSC_DIO_ID);
            m_lockSolenoidExtend = new Solenoid(RobotMap.PIVOT_LOCK_EXTEND_PNEUMATIC_MODULE_ID, RobotMap.PIVOT_LOCK_EXTEND_PNEUMATIC_RELAY_ID);
            m_lockSolenoidRetract = new Solenoid(RobotMap.PIVOT_LOCK_RETRACT_PNEUMATIC_MODULE_ID, RobotMap.PIVOT_LOCK_RETRACT_PNEUMATIC_RELAY_ID);
            this.setDeviceMaxAllowablePositionError(MAX_ALLOWABLE_ANGLE_ERROR);
            this.setSoftLimits(PIVOT_POSITION_REVERSE_INTAKE, PIVOT_POSITION_FORWARD_INTAKE);
            this.setSoftLimitOn(true);
        } catch (Exception e) {
            System.out.println("Unknown error initializing " + getName() + ".  Message = " + e.getMessage());
        }
    }    
        
    public void initDefaultCommand() {
    }
    
    public boolean isPivotLockSwitchClosed() {
        return !m_pivotLockSwitch.get();
    }
        
    public double getSeekSpeed(double initialAngle, double targetAngle) {
        double speed = PIVOT_SEEK_LOCK_SPEED_DOWN;
       
        // Change speed based on moving with gravity or against gravity (and for small angles)
//        if (Math.abs(targetAngle) < Math.abs(initialAngle) && Math.abs(targetAngle) > 10) {
//            // Calculate speed based on final angle
//            speed = Math.sin(Math.abs(targetAngle) * Math.PI / 180.0) * PIVOT_SEEK_LOCK_SPEED_UP;            
//        }
        
        if (Math.abs(targetAngle) < 10) {
            // Calculate speed based on final angle
            speed = PIVOT_SEEK_LOCK_SPEED_UP;            
        }
        // Moving up
        else if (Math.abs(targetAngle) < Math.abs(initialAngle)) {
             speed = PIVOT_SEEK_LOCK_SPEED_UP;       
        }     
        else {
            speed = PIVOT_SEEK_LOCK_SPEED_DOWN;
        }
        
        // Set direction of speed based on required rotation direction
        if (targetAngle - initialAngle < 0) {
            speed = -speed;
        }
        System.out.println("Seek Speed = " + speed);
        return speed;
    }
        
    public double getWeightOffPinSpeed(double initialAngle) {
        return -Math.sin(initialAngle * Math.PI / 180.0) * PIVOT_SEEK_LOCK_SPEED_UNLOAD;            
    }
        
    public void setLockPosition(int position){
        if (position == PneumaticSubsystem.EXTEND)
            lockExtend();
        else
            lockRetract();
    }
    
    private void lockExtend() {
        m_lockSolenoidExtend.set(true);
        m_lockSolenoidRetract.set(false);
        m_lockPosition = PneumaticSubsystem.EXTENDED;
    }
    
    private void lockRetract() {
        m_lockSolenoidExtend.set(false);
        m_lockSolenoidRetract.set(true);
        m_lockPosition = PneumaticSubsystem.RETRACTED;
    }
    
    public void setPivotSpeed(double speed) {
        moveDevice(speed);
    }
            
    public void stopPivot() {
        stopMotor();
    }

    public void updateStatus() {
        try {
            SmartDashboard.putBoolean("Pivot Lock Switch Closed", isPivotLockSwitchClosed());
            SmartDashboard.putNumber("Pivot Angle", getDevicePosition());
            SmartDashboard.putNumber("Pivot Speed", m_motorController.getSpeed());
            SmartDashboard.putNumber("Pivot Angle Offset", m_deviceZeroPositionOffset);
        } catch (Exception ex) {
            System.out.println("Error in updateStatus " + this.getName() + ".  Message = " + ex.getMessage());
        }
    }
}
