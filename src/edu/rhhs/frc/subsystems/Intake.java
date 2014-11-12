package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author kenny
 */
public class Intake extends PneumaticSubsystemDoubleValve {
    public static final double INTAKE_ROLLER_SPEED_BALL_PICKUP = -1.0;
    public static final double INTAKE_ROLLER_SPEED_BALL_EJECT = 1.0;
    private Victor m_motorControllerTop;
    private Victor m_motorControllerBottom;
    private DigitalInput m_ballDetectSwitch1;

    public Intake() {
        super("Intake", RobotMap.INTAKE_EXTEND_PNEUMATIC_MODULE_ID, RobotMap.INTAKE_EXTEND_PNEUMATIC_RELAY_ID,
              RobotMap.INTAKE_RETRACT_PNEUMATIC_MODULE_ID, RobotMap.INTAKE_RETRACT_PNEUMATIC_RELAY_ID);
        try {
            m_ballDetectSwitch1 = new DigitalInput(RobotMap.BALL_INTAKE_1_DSC_DIO_ID);
            m_motorControllerTop = new Victor(RobotMap.INTAKE_TOP_DSC_PWM_ID);
            m_motorControllerBottom = new Victor(RobotMap.INTAKE_BOTTOM_DSC_PWM_ID);             
        } catch (Exception e) {
            System.out.println("Unknown error initializing " + getName() + ".  Message = " + e.getMessage());
        }
    }

    public void initDefaultCommand() {
    }
    
    public boolean isBallDetectSwitchClosed() {
        return m_ballDetectSwitch1.get();
    }
        
    public void setRollerSpeed(double speed) {
        m_motorControllerTop.set(speed);
        m_motorControllerBottom.set(speed);
    }
            
    public void stopRoller() {
        m_motorControllerTop.stopMotor();
        m_motorControllerBottom.stopMotor();
    }
    
    public void updateStatus() {
        try {
            SmartDashboard.putBoolean("Ball Detect Switch Closed", isBallDetectSwitchClosed());
        } catch (Exception ex) {
            System.out.println("Error in updateStatus " + this.getName() + ".  Message = " + ex.getMessage());
        }
    }
}
