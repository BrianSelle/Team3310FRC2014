
package edu.rhhs.frc;

import edu.rhhs.frc.buttons.XBoxDPadTriggerButton;
import edu.rhhs.frc.buttons.XBoxTriggerButton;
import edu.rhhs.frc.commands.ChassisMove;
import edu.rhhs.frc.commands.ChassisReset;
import edu.rhhs.frc.commands.IntakeLoadBall;
import edu.rhhs.frc.commands.IntakeOnTimed;
import edu.rhhs.frc.commands.IntakePivotCatch;
import edu.rhhs.frc.commands.IntakePivotLoad;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.rhhs.frc.commands.IntakeSetArmPosition;
import edu.rhhs.frc.commands.IntakeSetRollerSpeed;
import edu.rhhs.frc.commands.PivotResetAngle;
import edu.rhhs.frc.commands.PivotSetAngle;
import edu.rhhs.frc.commands.PivotSetLockPosition;
import edu.rhhs.frc.commands.PivotSetSpeed;
import edu.rhhs.frc.commands.ShootSequence;
import edu.rhhs.frc.commands.WinchFire;
import edu.rhhs.frc.commands.TransmissionShift;
import edu.rhhs.frc.commands.WheelSetPosition;
import edu.rhhs.frc.commands.WinchResetPosition;
import edu.rhhs.frc.commands.WinchRewind;
import edu.rhhs.frc.commands.WinchSafeReleaseRewind;
import edu.rhhs.frc.commands.WinchSetBrakePosition;
import edu.rhhs.frc.commands.WinchSetPositionLongShot;
import edu.rhhs.frc.commands.WinchSetPositionLongShotAdjust;
import edu.rhhs.frc.commands.WinchSetPositionShortShot;
import edu.rhhs.frc.commands.WinchSetShiftPosition;
import edu.rhhs.frc.commands.WinchSetSpeed;
import edu.rhhs.frc.commands.WingsSetPosition;
import edu.rhhs.frc.controller.XboxController;
import edu.rhhs.frc.subsystems.Chassis;
import edu.rhhs.frc.subsystems.Intake;
import edu.rhhs.frc.subsystems.Pivot;
import edu.rhhs.frc.subsystems.PneumaticSubsystem;
import edu.rhhs.frc.subsystems.Transmission;
import edu.rhhs.frc.subsystems.Winch;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    // Driver stick

    private static OI instance = null;
    private Joystick m_joystick1;
    private Joystick m_joystick2;
    private XboxController m_xboxController;

    private OI() {
        // Driver stick
        m_joystick1 = new Joystick(RobotMap.JOYSTICK_1_USB_ID);
        m_joystick2 = new Joystick(RobotMap.JOYSTICK_2_USB_ID);
        m_xboxController = new XboxController(RobotMap.XBOX_USB_ID);
        
        JoystickButton joystickShiftTrigger = new JoystickButton(m_joystick1, RobotMap.JOYSTICK_1_SHIFT_BUTTON);
        joystickShiftTrigger.whenPressed(new TransmissionShift(Transmission.LO_GEAR));
        joystickShiftTrigger.whenReleased(new TransmissionShift(Transmission.HI_GEAR));
        
        JoystickButton joystickWheelTrigger = new JoystickButton(m_joystick2, RobotMap.JOYSTICK_2_WHEEL_BUTTON);
        joystickWheelTrigger.whenPressed(new WheelSetPosition(PneumaticSubsystem.EXTEND));
        joystickWheelTrigger.whenReleased(new WheelSetPosition(PneumaticSubsystem.RETRACT));
        
        JoystickButton extendPivotLockTrigger = new JoystickButton(m_joystick1, RobotMap.JOYSTICK_1_PIVOT_LOCK_EXTEND_BUTTON);
        extendPivotLockTrigger.whenReleased(new PivotSetLockPosition(PneumaticSubsystem.EXTEND));

        JoystickButton retractPivotLockTrigger = new JoystickButton(m_joystick1, RobotMap.JOYSTICK_1_PIVOT_LOCK_RETRACT_BUTTON);
        retractPivotLockTrigger.whenReleased(new PivotSetLockPosition(PneumaticSubsystem.RETRACT));
                  
        JoystickButton pivotForwardTrigger = new JoystickButton(m_joystick1, RobotMap.JOYSTICK_1_PIVOT_FORWARD_BUTTON);
        pivotForwardTrigger.whenPressed(new PivotSetSpeed(0.6));
        pivotForwardTrigger.whenReleased(new PivotSetSpeed(0));

        JoystickButton pivotReverseTrigger = new JoystickButton(m_joystick1, RobotMap.JOYSTICK_1_PIVOT_REVERSE_BUTTON);
        pivotReverseTrigger.whenPressed(new PivotSetSpeed(-0.6));
        pivotReverseTrigger.whenReleased(new PivotSetSpeed(0));
                  
        // A, B, X, Y Buttons
        JoystickButton intakeLoadBallForward = new JoystickButton(m_xboxController.getJoyStick(), XboxController.X_BUTTON);
        intakeLoadBallForward.whenPressed(new IntakePivotLoad(Pivot.PIVOT_POSITION_FORWARD_INTAKE, Pivot.PIVOT_POSITION_FORWARD_INTAKE));

        JoystickButton intakeCatchBallUp = new JoystickButton(m_xboxController.getJoyStick(), XboxController.Y_BUTTON);
        intakeCatchBallUp.whenPressed(new IntakePivotCatch(IntakePivotCatch.CATCH_POSITION_UP));

        JoystickButton pivotShoot = new JoystickButton(m_xboxController.getJoyStick(), XboxController.A_BUTTON);
        pivotShoot.whenPressed(new PivotSetAngle(Pivot.PIVOT_POSITION_LONG_SHOT));

        JoystickButton pivotBackShoot = new JoystickButton(m_xboxController.getJoyStick(), XboxController.B_BUTTON);
        pivotBackShoot.whenPressed(new PivotSetAngle(Pivot.PIVOT_POSITION_REVERSE_LONG_SHOT));

        // Back, Start Buttons
        JoystickButton pivotReset = new JoystickButton(m_xboxController.getJoyStick(), XboxController.START_BUTTON);
        pivotReset.whenPressed(new PivotResetAngle());
        
        JoystickButton winchReset = new JoystickButton(m_xboxController.getJoyStick(), XboxController.BACK_BUTTON);
        winchReset.whenPressed(new WinchResetPosition());
        
        // Trigger Buttons
        XBoxTriggerButton pivotIntakeForward = new XBoxTriggerButton(m_xboxController, XBoxTriggerButton.LEFT_AXIS_LEFT_TRIGGER);
        pivotIntakeForward.whenPressed(new PivotSetAngle(Pivot.PIVOT_POSITION_LONG_SHOT));
        
        XBoxTriggerButton pivotShortShot = new XBoxTriggerButton(m_xboxController, XBoxTriggerButton.LEFT_AXIS_UP_TRIGGER);
        pivotShortShot.whenPressed(new PivotSetAngle(Pivot.PIVOT_POSITION_FORWARD_SHORT_SHOT));
        
        XBoxTriggerButton pivotLongShot = new XBoxTriggerButton(m_xboxController, XBoxTriggerButton.LEFT_AXIS_DOWN_TRIGGER);
        pivotLongShot.whenPressed(new PivotSetAngle(Pivot.PIVOT_POSITION_REVERSE_SHORT_SHOT));
        
        XBoxTriggerButton pivotLongShotReverse = new XBoxTriggerButton(m_xboxController, XBoxTriggerButton.LEFT_AXIS_RIGHT_TRIGGER);
        pivotLongShotReverse.whenPressed(new PivotSetAngle(Pivot.PIVOT_POSITION_REVERSE_LONG_SHOT));
        
        JoystickButton pivotUp = new JoystickButton(m_xboxController.getJoyStick(), XboxController.LEFT_JOYSTICK_BUTTON);
        pivotUp.whenPressed(new PivotSetAngle(Pivot.PIVOT_POSITION_UP));
        
        // Right Joystick Buttons
        XBoxTriggerButton wingsClose = new XBoxTriggerButton(m_xboxController, XBoxTriggerButton.RIGHT_AXIS_LEFT_TRIGGER);
        wingsClose.whenPressed(new WingsSetPosition(PneumaticSubsystem.RETRACT));
        
        XBoxTriggerButton wingsOpen = new XBoxTriggerButton(m_xboxController, XBoxTriggerButton.RIGHT_AXIS_RIGHT_TRIGGER);
        wingsOpen.whenPressed(new WingsSetPosition(PneumaticSubsystem.EXTEND));
        
        XBoxTriggerButton armsOpen = new XBoxTriggerButton(m_xboxController, XBoxTriggerButton.RIGHT_AXIS_UP_TRIGGER);
        armsOpen.whenPressed(new IntakeSetArmPosition(PneumaticSubsystem.EXTEND));
        
        XBoxTriggerButton armsClose = new XBoxTriggerButton(m_xboxController, XBoxTriggerButton.RIGHT_AXIS_DOWN_TRIGGER);
        armsClose.whenPressed(new IntakeSetArmPosition(PneumaticSubsystem.RETRACT));
        
        JoystickButton safeWinchRelease = new JoystickButton(m_xboxController.getJoyStick(), XboxController.RIGHT_JOYSTICK_BUTTON);
        safeWinchRelease.whenPressed(new WinchSafeReleaseRewind());
        
        // D-Pad Buttons
        XBoxDPadTriggerButton winchShortShot = new XBoxDPadTriggerButton(m_xboxController, XBoxDPadTriggerButton.LEFT_TRIGGER);
        winchShortShot.whenPressed(new WinchRewind());
        
        XBoxDPadTriggerButton winchLongShot = new XBoxDPadTriggerButton(m_xboxController, XBoxDPadTriggerButton.RIGHT_TRIGGER);
        winchLongShot.whenPressed(new WinchSetPositionLongShot(Winch.WINCH_DISTANCE_LONG_SHOT, 1.0));
        
        // Bumper Buttons
        JoystickButton intakeEject = new JoystickButton(m_xboxController.getJoyStick(), XboxController.LEFT_BUMPER_BUTTON);
        intakeEject.whileHeld(new IntakeSetRollerSpeed(Intake.INTAKE_ROLLER_SPEED_BALL_EJECT));
        intakeEject.whenReleased(new IntakeSetRollerSpeed(0));
        
        JoystickButton intakeOn = new JoystickButton(m_xboxController.getJoyStick(), XboxController.RIGHT_BUMPER_BUTTON);
        intakeOn.whenPressed(new IntakeLoadBall());
        
        // Trigger Buttons
        XBoxTriggerButton shootShort = new XBoxTriggerButton(m_xboxController, XBoxTriggerButton.LEFT_TRIGGER);
        shootShort.whenPressed(new ShootSequence(Winch.WINCH_DISTANCE_SHORT_SHOT));
       
        XBoxTriggerButton shootLong = new XBoxTriggerButton(m_xboxController, XBoxTriggerButton.RIGHT_TRIGGER);
        shootLong.whenPressed(new ShootSequence(Winch.WINCH_DISTANCE_LONG_SHOT));
        
        // SmartDashboard buttons
        InternalButton chassisMove0 = new InternalButton();
        chassisMove0.whenReleased(new ChassisMove(24, Chassis.MOVE_AUTON_SPEED, true));
        SmartDashboard.putData("Chassis Move 24", chassisMove0); 

        InternalButton chassisMove1 = new InternalButton();
        chassisMove1.whenReleased(new ChassisMove(48, Chassis.MOVE_AUTON_SPEED, true));
        SmartDashboard.putData("Chassis Move 48", chassisMove1); 

        InternalButton chassisMove2 = new InternalButton();
        chassisMove2.whenReleased(new ChassisMove(96, Chassis.MOVE_AUTON_SPEED, true));
        SmartDashboard.putData("Chassis Move 96", chassisMove2); 

        InternalButton chassisReset = new InternalButton();
        chassisReset.whenReleased(new ChassisReset());
        SmartDashboard.putData("Chassis Reset", chassisReset); 

        InternalButton openWings = new InternalButton();
        openWings.whenReleased(new WingsSetPosition(PneumaticSubsystem.EXTEND));
        SmartDashboard.putData("Open Wings", openWings); 

        InternalButton closeWings = new InternalButton();
        closeWings.whenReleased(new WingsSetPosition(PneumaticSubsystem.RETRACT));
        SmartDashboard.putData("Close Wings", closeWings);

        InternalButton extendWinchBrake = new InternalButton();
        extendWinchBrake.whenReleased(new WinchSetBrakePosition(PneumaticSubsystem.EXTEND));
        SmartDashboard.putData("Extend Winch Brake", extendWinchBrake); 

        InternalButton retractWinchBrake = new InternalButton();
        retractWinchBrake.whenReleased(new WinchSetBrakePosition(PneumaticSubsystem.RETRACT));
        SmartDashboard.putData("Retract Winch Brake", retractWinchBrake); 

        InternalButton extendWinchShift = new InternalButton();
        extendWinchShift.whenReleased(new WinchSetShiftPosition(PneumaticSubsystem.EXTEND));
        SmartDashboard.putData("Extend Winch Shift", extendWinchShift); 

        InternalButton retractWinchShift = new InternalButton();
        retractWinchShift.whenReleased(new WinchSetShiftPosition(PneumaticSubsystem.RETRACT));
        SmartDashboard.putData("Retract Winch Shift", retractWinchShift); 

        InternalButton setPivotStop = new InternalButton();
        setPivotStop.whenReleased(new PivotSetSpeed(0));
        SmartDashboard.putData("Pivot Stop", setPivotStop); 

        InternalButton setPivotMoveForwardAuton = new InternalButton();
        setPivotMoveForwardAuton.whenReleased(new PivotSetAngle(Pivot.PIVOT_POSITION_FORWARD_AUTO_HOLD));
        SmartDashboard.putData("Pivot Forward Auton", setPivotMoveForwardAuton); 

        InternalButton setPivotMoveForwardIntake = new InternalButton();
        setPivotMoveForwardIntake.whenReleased(new PivotSetAngle(Pivot.PIVOT_POSITION_FORWARD_INTAKE));
        SmartDashboard.putData("Pivot Forward Intake", setPivotMoveForwardIntake); 

        InternalButton setPivotMoveShoot = new InternalButton();
        setPivotMoveShoot.whenReleased(new PivotSetAngle(Pivot.PIVOT_POSITION_LONG_SHOT));
        SmartDashboard.putData("Pivot Shoot", setPivotMoveShoot); 

        InternalButton setPivotMoveShootBack = new InternalButton();
        setPivotMoveShootBack.whenReleased(new PivotSetAngle(Pivot.PIVOT_POSITION_REVERSE_LONG_SHOT));
        SmartDashboard.putData("Pivot Shoot Back", setPivotMoveShootBack); 

        InternalButton setPivotMoveShootShortBack = new InternalButton();
        setPivotMoveShootShortBack.whenReleased(new PivotSetAngle(Pivot.PIVOT_POSITION_REVERSE_SHORT_SHOT));
        SmartDashboard.putData("Pivot Shoot Short Back", setPivotMoveShootShortBack); 

        InternalButton setPivotMoveShootShort = new InternalButton();
        setPivotMoveShootShort.whenReleased(new PivotSetAngle(Pivot.PIVOT_POSITION_FORWARD_SHORT_SHOT));
        SmartDashboard.putData("Pivot Shoot Short Forward", setPivotMoveShootShort); 

        InternalButton setPivotMoveUp = new InternalButton();
        setPivotMoveUp.whenReleased(new PivotSetAngle(Pivot.PIVOT_POSITION_UP));
        SmartDashboard.putData("Pivot Upright", setPivotMoveUp); 

        InternalButton setPivotAngleReset = new InternalButton();
        setPivotAngleReset.whenReleased(new PivotResetAngle());
        SmartDashboard.putData("Pivot Reset", setPivotAngleReset); 

        InternalButton setPivotForward = new InternalButton();
        setPivotForward.whileHeld(new PivotSetSpeed(0.3));
        setPivotForward.whenReleased(new PivotSetSpeed(0));
        SmartDashboard.putData("Pivot Forward", setPivotForward); 

        InternalButton setPivotReverse = new InternalButton();
        setPivotReverse.whileHeld(new PivotSetSpeed(-0.3));
        setPivotReverse.whenReleased(new PivotSetSpeed(0));
        SmartDashboard.putData("Pivot Reverse", setPivotReverse); 

        InternalButton setWinchStop = new InternalButton();
        setWinchStop.whenReleased(new WinchSetSpeed(0));
        SmartDashboard.putData("Winch Stop", setWinchStop); 

        InternalButton setIntakeForward = new InternalButton();
        setIntakeForward.whenReleased(new IntakeSetRollerSpeed(Intake.INTAKE_ROLLER_SPEED_BALL_PICKUP));
        SmartDashboard.putData("Intake Forward", setIntakeForward); 

        InternalButton setIntakeReverse = new InternalButton();
        setIntakeReverse.whenReleased(new IntakeSetRollerSpeed(Intake.INTAKE_ROLLER_SPEED_BALL_EJECT));
        SmartDashboard.putData("Intake Reverse", setIntakeReverse); 

        InternalButton setIntakeStop = new InternalButton();
        setIntakeStop.whenReleased(new IntakeSetRollerSpeed(0));
        SmartDashboard.putData("Intake Stop", setIntakeStop); 

        InternalButton setIntakeTimed = new InternalButton();
        setIntakeTimed.whenReleased(new IntakeOnTimed(0.5, Intake.INTAKE_ROLLER_SPEED_BALL_PICKUP));
        SmartDashboard.putData("Intake Timed", setIntakeTimed); 

        InternalButton setIntakeBall = new InternalButton();
        setIntakeBall.whenReleased(new IntakeLoadBall());
        SmartDashboard.putData("Intake Ball", setIntakeBall); 

        InternalButton setWinchMoveLong = new InternalButton();
        setWinchMoveLong.whenReleased(new WinchSetPositionLongShot(Winch.WINCH_DISTANCE_LONG_SHOT, 1.0));
        SmartDashboard.putData("Winch Move Long", setWinchMoveLong); 

        InternalButton setWinchMoveLongAdjust = new InternalButton();
        setWinchMoveLongAdjust.whenReleased(new WinchSetPositionLongShotAdjust(Winch.WINCH_DISTANCE_LONG_SHOT, 1.0));
        SmartDashboard.putData("Winch Move Long Adjust", setWinchMoveLongAdjust); 

        InternalButton setWinchMoveShort = new InternalButton();
        setWinchMoveShort.whenReleased(new WinchSetPositionLongShot(Winch.WINCH_DISTANCE_SHORT_SHOT, 1.0));
        SmartDashboard.putData("Winch Move Short", setWinchMoveShort); 

        InternalButton setWinchMove6 = new InternalButton();
        setWinchMove6.whenReleased(new WinchSetPositionLongShot(6, 1.0));
        SmartDashboard.putData("Winch Move 6", setWinchMove6); 

        InternalButton setWinchMove4 = new InternalButton();
        setWinchMove4.whenReleased(new WinchSetPositionLongShot(4, 1.0));
        SmartDashboard.putData("Winch Move 4", setWinchMove4); 

        InternalButton setWinchMove2 = new InternalButton();
        setWinchMove2.whenReleased(new WinchSetPositionLongShot(2, 1.0));
        SmartDashboard.putData("Winch Move 2", setWinchMove2); 

        InternalButton setWinchMove0 = new InternalButton();
        setWinchMove0.whenReleased(new WinchSetPositionLongShot(0, 1.0));
        SmartDashboard.putData("Winch Move 0", setWinchMove0); 

        InternalButton setWinchRelease = new InternalButton();
        setWinchRelease.whenReleased(new WinchSafeReleaseRewind());
        SmartDashboard.putData("Winch Safe Release", setWinchRelease); 

        InternalButton setWinchRewind = new InternalButton();
        setWinchRewind.whenReleased(new WinchRewind());
        SmartDashboard.putData("Winch Rewind", setWinchRewind); 

        InternalButton setWinchRetract = new InternalButton();
        setWinchRetract.whenReleased(new WinchSetPositionShortShot(Winch.WINCH_LONG_TO_SHORT_DISTANCE));
        SmartDashboard.putData("Winch Retract", setWinchRetract); 

        InternalButton setWinchForward = new InternalButton();
        setWinchForward.whenReleased(new WinchSetSpeed(0.2));
        SmartDashboard.putData("Winch Forward", setWinchForward); 

        InternalButton setWinchReverse = new InternalButton();
        setWinchReverse.whenReleased(new WinchSetSpeed(-0.2));
        SmartDashboard.putData("Winch Reverse", setWinchReverse); 

        InternalButton setWinchMoveReset = new InternalButton();
        setWinchMoveReset.whenReleased(new WinchResetPosition());
        SmartDashboard.putData("Winch Reset", setWinchMoveReset); 

        InternalButton shooterFire = new InternalButton();
        shooterFire.whenReleased(new WinchFire());
        SmartDashboard.putData("FIRE", shooterFire); 

        InternalButton extendPivotLock = new InternalButton();
        extendPivotLock.whenReleased(new PivotSetLockPosition(PneumaticSubsystem.EXTEND));
        SmartDashboard.putData("Extend Pivot Lock", extendPivotLock); 

        InternalButton retractPivotLock = new InternalButton();
        retractPivotLock.whenReleased(new PivotSetLockPosition(PneumaticSubsystem.RETRACT));
        SmartDashboard.putData("Retract Pivot Lock", retractPivotLock); 

        InternalButton extendIntakeArms = new InternalButton();
        extendIntakeArms.whenReleased(new IntakeSetArmPosition(Intake.EXTEND));
        SmartDashboard.putData("Extend Intake Arms", extendIntakeArms); 

        InternalButton retractIntakeArms = new InternalButton();
        retractIntakeArms.whenReleased(new IntakeSetArmPosition(Intake.RETRACT));
        SmartDashboard.putData("Retract Intake Arms", retractIntakeArms); 
        
        InternalButton setPivotIntakeLoadForward = new InternalButton();
        setPivotIntakeLoadForward.whenReleased(new IntakePivotLoad(Pivot.PIVOT_POSITION_FORWARD_INTAKE, Pivot.PIVOT_POSITION_FORWARD_INTAKE));
        SmartDashboard.putData("Intake Pivot Load Forward", setPivotIntakeLoadForward); 

        InternalButton setPivotIntakeLoadReverse = new InternalButton();
        setPivotIntakeLoadReverse.whenReleased(new IntakePivotLoad(Pivot.PIVOT_POSITION_REVERSE_INTAKE, Pivot.PIVOT_POSITION_REVERSE_INTAKE));
        SmartDashboard.putData("Intake Pivot Load Reverse", setPivotIntakeLoadReverse); 
        
        InternalButton setPivotIntakeCatchForward = new InternalButton();
        setPivotIntakeCatchForward.whenReleased(new IntakePivotCatch(IntakePivotCatch.CATCH_POSITION_FORWARD));
        SmartDashboard.putData("Intake Pivot Catch Forward", setPivotIntakeCatchForward); 

        InternalButton setPivotIntakeCatchReverse = new InternalButton();
        setPivotIntakeCatchReverse.whenReleased(new IntakePivotCatch(IntakePivotCatch.CATCH_POSITION_REVERSE));
        SmartDashboard.putData("Intake Pivot Catch Reverse", setPivotIntakeCatchReverse); 

        InternalButton setPivotIntakeCatchUp = new InternalButton();
        setPivotIntakeCatchUp.whenReleased(new IntakePivotCatch(IntakePivotCatch.CATCH_POSITION_UP));
        SmartDashboard.putData("Intake Pivot Catch Up", setPivotIntakeCatchUp); 
          
//        InternalButton moveNonLinearPos = new InternalButton();
//        moveNonLinearPos.whenReleased(new ChassisSetup(ChassisSetup.MOVE_NONLINEAR, 1));
//        SmartDashboard.putData("+ MNL", moveNonLinearPos);
//
//        InternalButton moveNonLinearNeg = new InternalButton();
//        moveNonLinearNeg.whenReleased(new ChassisSetup(ChassisSetup.MOVE_NONLINEAR, -1));
//        SmartDashboard.putData("- MNL", moveNonLinearNeg);
//
//        InternalButton steeringNonLinearPos = new InternalButton();
//        steeringNonLinearPos.whenReleased(new ChassisSetup(ChassisSetup.STEER_NONLINEAR, 1));
//        SmartDashboard.putData("+ SNL", steeringNonLinearPos);
//
//        InternalButton steeringNonLinearNeg = new InternalButton();
//        steeringNonLinearNeg.whenReleased(new ChassisSetup(ChassisSetup.STEER_NONLINEAR, -1));
//        SmartDashboard.putData("- SNL", steeringNonLinearNeg);
//
//        InternalButton moveTrimPos = new InternalButton();
//        moveTrimPos.whenReleased(new ChassisSetup(ChassisSetup.MOVE_TRIM, 0.05));
//        SmartDashboard.putData("+ MT", moveTrimPos);
//
//        InternalButton moveTrimNeg = new InternalButton();
//        moveTrimNeg.whenReleased(new ChassisSetup(ChassisSetup.MOVE_TRIM, -0.05));
//        SmartDashboard.putData("- MT", moveTrimNeg);
//
//        InternalButton steeringTrimPos = new InternalButton();
//        steeringTrimPos.whenReleased(new ChassisSetup(ChassisSetup.STEER_TRIM, 0.05));
//        SmartDashboard.putData("+ ST", steeringTrimPos);
//
//        InternalButton steeringTrimNeg = new InternalButton();
//        steeringTrimNeg.whenReleased(new ChassisSetup(ChassisSetup.STEER_TRIM, -0.05));
//        SmartDashboard.putData("- ST", steeringTrimNeg);
//        
//        InternalButton moveScalePos = new InternalButton();
//        moveScalePos.whenReleased(new ChassisSetup(ChassisSetup.MOVE_SCALE, 0.05));
//        SmartDashboard.putData("+ MS", moveScalePos);
//
//        InternalButton moveScaleNeg = new InternalButton();
//        moveScaleNeg.whenReleased(new ChassisSetup(ChassisSetup.MOVE_SCALE, -0.05));
//        SmartDashboard.putData("- MS", moveScaleNeg);
//
//        InternalButton steeringScalePos = new InternalButton();
//        steeringScalePos.whenReleased(new ChassisSetup(ChassisSetup.STEER_SCALE, 0.05));
//        SmartDashboard.putData("+ SS", steeringScalePos);
//
//        InternalButton steeringScaleNeg = new InternalButton();
//        steeringScaleNeg.whenReleased(new ChassisSetup(ChassisSetup.STEER_SCALE, -0.05));
//        SmartDashboard.putData("- SS", steeringScaleNeg); 
        
//        // Test Motors
//        for (int motorId = 0; motorId < 10; motorId++) {
//            System.out.println("Button Motor id = " + motorId);
//            InternalButton fwdButton = new InternalButton();
//            fwdButton.whenReleased(new MotorTestSetSpeed(motorId, 1.0));
//            SmartDashboard.putData("Forward Motor[" + motorId + "]", fwdButton); 
//            InternalButton revButton = new InternalButton();
//            revButton.whenReleased(new MotorTestSetSpeed(motorId, -1.0));
//            SmartDashboard.putData("Reverse Motor[" + motorId + "]", revButton); 
//            InternalButton offButton = new InternalButton();
//            offButton.whenReleased(new MotorTestSetSpeed(motorId, 0.0));
//            SmartDashboard.putData("Off Motor[" + motorId + "]", offButton); 
//        }
//
//        // Test Pneumatics
//        for (int valveId = 0; valveId < 6; valveId++) {
//            InternalButton extendButton = new InternalButton();
//            extendButton.whenReleased(new PneumaticTestSetPosition(valveId, PneumaticSubsystem.EXTEND));
//            SmartDashboard.putData("Extend Valve[" + valveId + "]", extendButton); 
//            InternalButton retractButton = new InternalButton();
//            retractButton.whenReleased(new PneumaticTestSetPosition(valveId, PneumaticSubsystem.RETRACT));
//            SmartDashboard.putData("Retract Valve[" + valveId + "]", retractButton); 
//        }
    }  

    public Joystick getJoystick1() {
        return m_joystick1;
    }
    
    public Joystick getJoystick2() {
        return m_joystick2;
    }
    
    public XboxController getXBoxController() {
        return m_xboxController;
    }
    
    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }
}

