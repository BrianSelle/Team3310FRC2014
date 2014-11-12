/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.utility.J2MEMath;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.ChassisDriveWithJoystick;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 *
 * @author bselle
 */
public class Chassis extends PIDSubsystem {
    
//    public static final int CONTROLLER_ARCADE = 0;
//    public static final int CONTROLLER_TANK = 1;
//    public static final int CONTROLLER_CHEESY = 2;
//    public static final int CONTROLLER_XBOX = 3;
//    public static final int CONTROLLER_WHEEL = 4;

    public static final double MOVE_AUTON_SPEED = 0.85;
    public static final double MOVE_AUTON_FORWARD_LONG_DISTANCE = 69;
    public static final double MOVE_AUTON_BACK_LONG_DISTANCE = -75;
    
    public static final double STEER_NON_LINEARITY = 0.9;
    public static final double MOVE_NON_LINEARITY = 0.9;
    
    protected static final double ENCODER_TO_WHEEL_GEAR_RATIO = 5.4;  // 3X Encoder x 54:30
    public static final double WHEEL_CIRCUM_IN = 12.564;
    private final static double STEERING_ZERO_OFFSET = -.05;
   
    // PID Constants
    private final static double KP_MOVE_STEERING = -0.2;
    private final static double KP_TURN_STEERING = 0.03;
    private final static double KP = -0.15; //-0.15
    private final static double KI = -0.0002; //-0.0002;
    private final static double KD = -0.3;
    
    private final static double MAX_ALLOWABLE_ERROR_DISTANCE_IN = 2.0;
    private final static int    NUM_CYCLES_REQUIRED_AT_SETPOINT = 1;

    private double m_startTimeMicroSeconds;
    private double m_error = 0;
    private boolean m_enabled = false;
    private double m_maxSpeed;
    private boolean m_isTurnCommand = false;
    private double m_turnAxis = 0;
    
    private int m_atSetPointCycleCount = 0;

    private RobotDrive m_drive;
    private Encoder m_leftEncoder;
    private Encoder m_rightEncoder;
    private Gyro m_yawGyro;
    
    private int m_moveNonLinear = 0;
    private int m_steerNonLinear = 0;
    
    private double m_moveScale = 1.0;
    private double m_steerScale = 1.0;
    
    private double m_moveInput = 0.0;
    private double m_steerInput = 0.0;
    
    private double m_moveOutput = 0.0;
    private double m_steerOutput = 0.0;
    
    private double m_moveTrim = 0.0;
    private double m_steerTrim = 0.0;
    
//    private int m_controllerMode = CONTROLLER_XBOX;

    public Chassis() {
        super(KP, KI, KD);

        try {
            m_drive = new RobotDrive(
                    new Victor(RobotMap.DRIVE_LEFT_TOP_FRONT_DSC_PWM_ID),
                    new Victor(RobotMap.DRIVE_LEFT_REAR_DSC_PWM_ID),
                    new Victor(RobotMap.DRIVE_RIGHT_TOP_FRONT_DSC_PWM_ID),
                    new Victor(RobotMap.DRIVE_RIGHT_REAR_DSC_PWM_ID));
            
            m_drive.setSafetyEnabled(false);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
            m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
     
            m_leftEncoder = new Encoder(
                    1, RobotMap.LEFT_DRIVE_ENCODER_A_DSC_DIO_ID, 
                    1, RobotMap.LEFT_DRIVE_ENCODER_B_DSC_DIO_ID, true, CounterBase.EncodingType.k4X);
            m_rightEncoder = new Encoder(
                    1, RobotMap.RIGHT_DRIVE_ENCODER_A_DSC_DIO_ID, 
                    1, RobotMap.RIGHT_DRIVE_ENCODER_B_DSC_DIO_ID, false, CounterBase.EncodingType.k4X);    
            
            m_leftEncoder.setDistancePerPulse(WHEEL_CIRCUM_IN / 360.0 / ENCODER_TO_WHEEL_GEAR_RATIO);
            m_rightEncoder.setDistancePerPulse(WHEEL_CIRCUM_IN / 360.0 / ENCODER_TO_WHEEL_GEAR_RATIO);

            resetEncoders();

            m_yawGyro = new Gyro(RobotMap.CHASSIS_YAW_RATE_ANALOG_BREAKOUT_PORT);
            m_yawGyro.setSensitivity(0.007);  // 7 mV/deg/sec
            
//            SmartDashboard.putNumber("Move NonLinear ", m_moveNonLinear);
//            SmartDashboard.putNumber("Steer NonLinear ", m_steerNonLinear);
//            SmartDashboard.putNumber("Move Scale ", m_moveScale);
//            SmartDashboard.putNumber("Steer Scale ", m_steerScale);
//            SmartDashboard.putNumber("Move Trim ", -m_moveTrim);
//            SmartDashboard.putNumber("Steer Trim ", m_steerTrim); 
        } catch (Exception e) {
            System.out.println("Unknown error initializing chassis.  Message = " + e.getMessage());
        }
    }
    
    public void stop() {
        setMoveMotorSpeed(0);
        this.disable();
        m_enabled = false;
    }
    
    public void resetEncoders() {
    	m_leftEncoder.reset();
    	m_rightEncoder.reset();
    	m_leftEncoder.start();
    	m_rightEncoder.start();
        this.getPIDController().reset();
    }
    
    // Methods for PID control to a position
    public void startPID() {
        resetEncoders();
        this.enable();
        m_enabled = true;
        m_startTimeMicroSeconds = Utility.getFPGATime();
    }
    
    // Methods for PID control to a position
    public void startNoResetPID() {
    	m_leftEncoder.start();
    	m_rightEncoder.start();
        this.enable();
        m_enabled = true;
    }
    
    public boolean isEnabled() {
        return m_enabled;
    }
    
    // turn axis = 0 is about the center
    // turn axia = 1 is using just right side of drivetrain
    // turn axia = -1 is using just left side of drivetrain
    public void setTurnDeltaAngle(double angle, double maxSpeed, double turnAxis) {
        m_yawGyro.reset();
        m_isTurnCommand = true;
        m_turnAxis = turnAxis;
        m_maxSpeed = maxSpeed;
        m_atSetPointCycleCount = 0;
        this.setSetpoint(angle);
    }
    
    public void setDistance(double distance, double maxSpeed) {
        m_yawGyro.reset();
        m_isTurnCommand = false;
        m_maxSpeed = maxSpeed;
        m_atSetPointCycleCount = 0;

        this.setSetpoint(distance);
    }
    
    public boolean atSetPoint() {
        m_error = Math.abs(getPIDController().getSetpoint() - returnPIDInput());
        if (Math.abs(m_error) < MAX_ALLOWABLE_ERROR_DISTANCE_IN) {
            m_atSetPointCycleCount++;
        };
        return m_atSetPointCycleCount >= NUM_CYCLES_REQUIRED_AT_SETPOINT;
    }
    
    protected double returnPIDInput() {
        if (m_isTurnCommand == true) {
            return m_yawGyro.getAngle();
        }
        else {
            return getDistance();
        }
    }
    
    private double getDistance() {
        return (m_leftEncoder.getDistance() + m_rightEncoder.getDistance()) / 2;
    }

    protected void usePIDOutput(double percentVbus) {
        if (m_isTurnCommand == true) {
            setTurnMotorSpeed(percentVbus);
        }
        else {
            setMoveMotorSpeed(percentVbus);
        }
    }

    private void setTurnMotorSpeed(double percentVbus) {
        percentVbus = limitSpeed(percentVbus, m_maxSpeed);
        double leftPercentVBus = limitSpeed(percentVbus * (1.0 - 0.5 * m_turnAxis), m_maxSpeed);
        double rightPercentVBus = limitSpeed(percentVbus * (-1.0 - 0.5 * m_turnAxis), m_maxSpeed);
        m_drive.tankDrive(leftPercentVBus, rightPercentVBus);
    }

    public void setMoveMotorSpeed(double percentVbus) {
        double deltaTimeMicroSeconds = Utility.getFPGATime() - m_startTimeMicroSeconds;
        double deltaTimeSeconds = deltaTimeMicroSeconds / 1000000;

        percentVbus = limitAccel(percentVbus, m_maxSpeed, deltaTimeSeconds);
        double steeringError = m_rightEncoder.getDistance() - m_leftEncoder.getDistance();
//        double steeringError = m_yawGyro.getAngle();
        double steerOffset = STEERING_ZERO_OFFSET;
//        if (percentVbus > 0) {  // neg = forward
//            steerOffset = -steerOffset;
//        }
        m_drive.arcadeDrive(percentVbus, steerOffset + steeringError * KP_MOVE_STEERING);
//        System.out.println("Time = " + System.currentTimeMillis() + ", PercentVbus = " + percentVbus + ", Distance = " + returnPIDInput() + ", Steer error = " + steeringError + ", Left Distance = " + m_leftEncoder.getDistance() + ", Right Distance = " + m_rightEncoder.getDistance());
    }
    
    private double limitSpeed(double percentVbus, double maxSpeed) {
        if (Math.abs(percentVbus) > maxSpeed) {
            if (percentVbus > 0) {
                percentVbus = maxSpeed;
            }
            else {
                percentVbus = -maxSpeed;
            }
        }
        
        return percentVbus;
    }

    private double limitAccel(double percentVbus, double maxSpeed, double deltaTimeSeconds) {
//        double limitedSpeed = 0.1 + (maxSpeed - 0.1) * deltaTimeSeconds;
        double limitedSpeed = percentVbus;
//        System.out.println("Percentvbus = " + percentVbus + ", limitSpeed = " + limitedSpeed);
        if (Math.abs(limitedSpeed) > maxSpeed) {
            if (percentVbus > 0) {
                limitedSpeed = maxSpeed;
            }
            else {
                limitedSpeed = -maxSpeed;
            }
        }
        
        return limitedSpeed;
    }

    public int getMoveNonLinear() {
        return m_moveNonLinear;
    }
    
    public void setMoveNonLinear(int nonLinearFactor) {
        m_moveNonLinear = nonLinearFactor;
        SmartDashboard.putNumber("Move NonLinear ", m_moveNonLinear);
    }
    
    public int getSteerNonLinear() {
        return m_steerNonLinear;
    }
    
    public void setSteerNonLinear(int nonLinearFactor) {
        m_steerNonLinear = nonLinearFactor;
        SmartDashboard.putNumber("Steer NonLinear ", m_steerNonLinear);
    }

    public double getMoveScale() {
        return m_moveScale;
    }
    
    public void setMoveScale(double scale) {
        m_moveScale = scale;
        SmartDashboard.putNumber("Move Scale ", m_moveScale);
    }
    
    public double getSteerScale() {
        return m_steerScale;
    }
    
    public void setSteerScale(double scale) {
        m_steerScale = scale;
        SmartDashboard.putNumber("Steer Scale ", m_steerScale);
    }
   
    public double getMoveTrim() {
        return -m_moveTrim;
    }
    
    public void setMoveTrim(double trim) {
        m_moveTrim = -trim;
        SmartDashboard.putNumber("Move Trim ", -m_moveTrim);
    }
    
    public double getSteerTrim() {
        return m_steerTrim;
    }
    
    public void setSteerTrim(double trim) {
        m_steerTrim = trim;
        SmartDashboard.putNumber("Steer Trim ", m_steerTrim); 
    }

//    public int getControllerMode() {
//        return m_controllerMode;
//    }
//    
//    public void setControllerMode(int controllerMode) {
//        m_controllerMode = controllerMode;
//        System.out.println("Controller Mode = " + m_controllerMode);
//    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new ChassisDriveWithJoystick()); // set default command   
    }
    
    public void driveWithJoystick() {        
        
        if (m_drive != null) {
//            switch(m_controllerMode) {
//                case CONTROLLER_ARCADE: 
//                    m_moveInput = OI.getInstance().getJoystick1().getY();
//                    m_steerInput = OI.getInstance().getJoystick1().getX();
//                    m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
//                    m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
//                    m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
//                    break;
//                case CONTROLLER_TANK:
//                    m_moveInput = OI.getInstance().getJoystick1().getY();
//                    m_steerInput = OI.getInstance().getJoystick2().getY();
//                    m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
//                    m_steerOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_steerInput, m_moveNonLinear, MOVE_NON_LINEARITY);
//                    m_drive.tankDrive(m_moveOutput, m_steerOutput);
//                    break;
//                case CONTROLLER_CHEESY:
                    m_moveInput = OI.getInstance().getJoystick1().getY();
                    m_steerInput = OI.getInstance().getJoystick2().getX();
                    m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
                    m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
                    m_drive.arcadeDrive(m_moveOutput, -m_steerOutput);
//                    break;
//                case CONTROLLER_XBOX:
//                    m_moveInput = OI.getInstance().getXBoxController().getLeftYAxis();
//                    m_steerInput = OI.getInstance().getXBoxController().getRightXAxis();
//                    m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
//                    m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
//                    m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
//                    break;
//                case CONTROLLER_WHEEL:
//                    m_moveInput = OI.getInstance().getJoystick1().getY();
//                    m_steerInput = OI.getInstance().getSteeringWheel().getX();
//                    m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim, m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
//                    m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim, m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
//                    m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
//                    break;
//            }
       }
    }
    
    private double adjustForSensitivity(double scale, double trim, double steer, int nonLinearFactor, double wheelNonLinearity) {
        steer += trim;
        steer *= scale;
        steer = limitValue(steer);
        
        int iterations = Math.abs(nonLinearFactor);    
        for (int i = 0; i < iterations; i++) {
            if (nonLinearFactor > 0) {
                steer = nonlinearStickCalcPositive(steer, wheelNonLinearity);
            }
            else {
                steer = nonlinearStickCalcNegative(steer, wheelNonLinearity);
            }
        }
        return steer;	
    }
    
    private double limitValue(double value) {
        if(value > 1.0) {
            value = 1.0;
        }
        else if(value < -1.0) {
            value = -1.0;
        }
        return value;
    }

    private double nonlinearStickCalcPositive(double steer, double steerNonLinearity) {
            return Math.sin(Math.PI / 2.0 * steerNonLinearity * steer) / Math.sin(Math.PI / 2.0 * steerNonLinearity);	
    }

    private double nonlinearStickCalcNegative(double steer, double steerNonLinearity) {
            return J2MEMath.asin(steerNonLinearity * steer) / J2MEMath.asin(steerNonLinearity);	
    }

    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        try {
//            SmartDashboard.putNumber("Move Input ", -m_moveInput);
//            SmartDashboard.putNumber("Steer Input ", m_steerInput);
//            SmartDashboard.putNumber("Move Output ", -m_moveOutput);
//            SmartDashboard.putNumber("Steer Output ", m_steerOutput);
//            SmartDashboard.putNumber("Move NonLinear ", m_moveNonLinear);
//            SmartDashboard.putNumber("Steering NonLinear ", m_steerNonLinear);
//            SmartDashboard.putNumber("Move Scale ", m_moveScale);
//            SmartDashboard.putNumber("Steering Scale ", m_steerScale);
//            SmartDashboard.putNumber("Move Trim ", -m_moveTrim);
//            SmartDashboard.putNumber("Steering Trim ", m_steerTrim); 
            SmartDashboard.putNumber("Left Raw ", m_leftEncoder.getRaw());
            SmartDashboard.putNumber("Left Distance ", m_leftEncoder.getDistance());
            SmartDashboard.putNumber("Right Raw ", m_rightEncoder.getRaw());
            SmartDashboard.putNumber("Right Distance ", m_rightEncoder.getDistance());
            SmartDashboard.putNumber("Distance ", getDistance());
//            SmartDashboard.putNumber("Distance ", returnPIDInput());
//            SmartDashboard.putNumber("Yaw Angle ", m_yawGyro.getAngle());
        } catch (Exception ex) {
            System.out.println("Error in updateStatus Chassis.  Message = " + ex.getMessage());
        }
    }
} 
