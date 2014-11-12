package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author kenny
 */
public class Winch extends EncoderPIDSubsystem {
    
    public static final double WINCH_REWIND_SPEED = 0.3;
    public static final double WINCH_RETRACT_SPEED = 0.05;
    public static final double WINCH_RELEASE_SPEED = 0.6;  
    public static final double WINCH_HOLD_SPEED = 0.6;  
    public static final double WINCH_DISTANCE_LONG_SHOT = 16;
    public static final double WINCH_DISTANCE_SHORT_SHOT = 7;
    public static final double WINCH_LONG_TO_SHORT_DISTANCE = -3.25;
    public static final double WINCH_LONG_ADJUST_DISTANCE = -1.5;
    
    public static final int WINCH_AT_ZERO = 0;
    public static final int WINCH_AT_SHORT_SHOT = 1;
    public static final int WINCH_AT_LONG_SHOT = 2;

    protected static final double WINCH_DRUM_DIAMETER = 1.0;
    protected static final double WINCH_ENCODER_TO_DRUM_GEAR_RATIO = 3.0;
    protected static final double kP = 0.4;
    protected static final double kI = 0.0;
    protected static final double kD = 0.0;
    protected static final double kF = 0.0;
    
    private DigitalInput m_drawLimitSwitch1;
    private DigitalInput m_drawLimitSwitch2;
    private DigitalInput m_drawZeroSwitch;

    protected Solenoid m_brakeSolenoidExtend; 
    protected Solenoid m_brakeSolenoidRetract; 
    protected Solenoid m_shiftSolenoidRetract; 
    protected Solenoid m_shiftSolenoidExtend; 
    
    protected int m_winchPosition = WINCH_AT_ZERO;

    protected String m_brakePosition = PneumaticSubsystem.UNKNOWN;
    protected String m_shiftPosition = PneumaticSubsystem.UNKNOWN;
    
    public Winch() {
        super("Winch", kP, kI, kD, RobotMap.WINCH_DSC_PWM_ID, RobotMap.WINCH_ENCODER_A_DSC_DIO_ID, RobotMap.WINCH_ENCODER_B_DSC_DIO_ID, false, WINCH_DRUM_DIAMETER, WINCH_ENCODER_TO_DRUM_GEAR_RATIO);
        try {
            setFeedForwardInput(kF);
            m_drawLimitSwitch1 = new DigitalInput(RobotMap.WINCH_SWITCH_1_DSC_DIO_ID);
            m_drawLimitSwitch2 = new DigitalInput(RobotMap.WINCH_SWITCH_2_DSC_DIO_ID);
            m_drawZeroSwitch = new DigitalInput(RobotMap.DRAW_ZERO_SWITCH_DSC_DIO_ID);
            m_brakeSolenoidExtend = new Solenoid(RobotMap.BRAKE_EXTEND_PNEUMATIC_MODULE_ID, RobotMap.BRAKE_EXTEND_PNEUMATIC_RELAY_ID);
            m_brakeSolenoidRetract = new Solenoid(RobotMap.BRAKE_RETRACT_PNEUMATIC_MODULE_ID, RobotMap.BRAKE_RETRACT_PNEUMATIC_RELAY_ID);
            m_shiftSolenoidExtend = new Solenoid(RobotMap.WINCH_EXTEND_PNEUMATIC_MODULE_ID, RobotMap.WINCH_EXTEND_PNEUMATIC_RELAY_ID);
            m_shiftSolenoidRetract = new Solenoid(RobotMap.WINCH_RETRACT_PNEUMATIC_MODULE_ID, RobotMap.WINCH_RETRACT_PNEUMATIC_RELAY_ID);
        } catch (Exception e) {
            System.out.println("Unknown error initializing " + getName() + ".  Message = " + e.getMessage());
        }
    }    
        
    public void initDefaultCommand() {
    }
    
    public boolean isDrawLimitSwitch1Closed() {
        return !m_drawLimitSwitch1.get();
    }
        
    public boolean isDrawLimitSwitch2Closed() {
        return !m_drawLimitSwitch2.get();
    }
        
    public boolean isDrawZeroSwitchClosed() {
        return m_drawZeroSwitch.get();
    }
    
    public void setWinchPosition(int position) {
        m_winchPosition = position;
    }
    
    public int getWinchPosition() {
        return m_winchPosition;
    }
         
    public void setBrakePosition(int position){
        if (position == PneumaticSubsystem.EXTEND)
            brakeExtend();
        else
            brakeRetract();
    }
    
    public void setShiftPosition(int position){
        if (position == PneumaticSubsystem.RETRACT)
            shiftExtend();
        else
            shiftRetract();
    }
    
    private void brakeExtend() {
        m_brakeSolenoidExtend.set(true);
        m_brakeSolenoidRetract.set(false);
        m_brakePosition = PneumaticSubsystem.EXTENDED;
    }
    
    private void brakeRetract() {
        m_brakeSolenoidExtend.set(false);
        m_brakeSolenoidRetract.set(true);
        m_brakePosition = PneumaticSubsystem.RETRACTED;
    }
    
    private void shiftExtend() {
        m_shiftSolenoidExtend.set(true);
        m_shiftSolenoidRetract.set(false);
        m_shiftPosition = PneumaticSubsystem.EXTENDED;
    }
    
    public void shiftRetract() {
        m_shiftSolenoidExtend.set(false);
        m_shiftSolenoidRetract.set(true);
        m_shiftPosition = PneumaticSubsystem.RETRACTED;
    }
    
    public void setWinchSpeed(double speed) {
        moveDevice(speed);
    }
            
    public void stopWinch() {
        stopMotor();
    }

    public void updateStatus() {
        try {
//            SmartDashboard.putNumber("Winch Speed", m_motorController.getPosition());
//            SmartDashboard.putNumber("Winch Speed Plot", m_motorController.getPosition());
            SmartDashboard.putNumber("Winch Encoder", m_encoder.getDistance());
            SmartDashboard.putBoolean("Winch Draw Limit Switch 1 Closed", isDrawLimitSwitch1Closed());
            SmartDashboard.putBoolean("Winch Draw Limit Switch 2 Closed", isDrawLimitSwitch2Closed());
            SmartDashboard.putBoolean("Winch Draw Zero Switch Closed", isDrawZeroSwitchClosed());
//            SmartDashboard.putNumber("Winch Distance", m_encoder.getDistance());
        } catch (Exception ex) {
            System.out.println("Error in updateStatus " + this.getName() + ".  Message = " + ex.getMessage());
        }
    }

}
