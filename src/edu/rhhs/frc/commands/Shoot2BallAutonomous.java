package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Chassis;
import edu.rhhs.frc.subsystems.Intake;
import edu.rhhs.frc.subsystems.Pivot;
import edu.rhhs.frc.subsystems.PneumaticSubsystem;
import edu.rhhs.frc.subsystems.Transmission;
import edu.rhhs.frc.subsystems.Winch;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitForChildren;

/**
 * @author rhhs
 */
public class Shoot2BallAutonomous extends CommandGroup {
    
    public Shoot2BallAutonomous() {     
        addSequential(new TransmissionShift(Transmission.LO_GEAR));
        addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_FORWARD_AUTO_HOLD));
        addSequential(new ChassisMove(Chassis.MOVE_AUTON_FORWARD_LONG_DISTANCE, Chassis.MOVE_AUTON_SPEED, true));  
        addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_LONG_SHOT));
        
        addSequential(new IntakeSetArmPosition(PneumaticSubsystem.EXTEND));
        addSequential(new WingsSetPosition(PneumaticSubsystem.EXTEND));
        addSequential(new WaitTimer(0.1));
        addSequential(new WinchFire());    
 
        addParallel(new WinchSetPositionLongShot(Winch.WINCH_DISTANCE_LONG_SHOT, 1.0));
        addSequential(new IntakeSetArmPosition(PneumaticSubsystem.RETRACT));
        addSequential(new ChassisMove(-25, Chassis.MOVE_AUTON_SPEED, true));  
        addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_FORWARD_INTAKE));
        addSequential(new WaitForChildren());

        addParallel(new IntakeLoadBall());
        addSequential(new ChassisMove(40, Chassis.MOVE_AUTON_SPEED, true));  
        addSequential(new WaitForChildren());
        
        addParallel(new ChassisMove(-15, Chassis.MOVE_AUTON_SPEED, true));  
        addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_LONG_SHOT));
        addSequential(new WaitForChildren());
        
        addSequential(new IntakeOnTimed(0.2, Intake.INTAKE_ROLLER_SPEED_BALL_PICKUP));
        addSequential(new IntakeSetArmPosition(PneumaticSubsystem.EXTEND));
        addSequential(new WingsSetPosition(PneumaticSubsystem.EXTEND));
        addSequential(new WaitTimer(0.1));
        addSequential(new WinchFire());
        
        addSequential(new IntakeSetArmPosition(PneumaticSubsystem.RETRACT));
        addParallel(new WingsSetPosition(PneumaticSubsystem.RETRACT));
        addParallel(new PivotSetAngle(Pivot.PIVOT_POSITION_UP));
        addParallel(new TransmissionShift(Transmission.HI_GEAR));
    }
}

