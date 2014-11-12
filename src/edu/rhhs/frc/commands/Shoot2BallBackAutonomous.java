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
public class Shoot2BallBackAutonomous extends CommandGroup {
    
    public Shoot2BallBackAutonomous() {  
        addSequential(new TransmissionShift(Transmission.HI_GEAR));
        addParallel(new ChassisMove(Chassis.MOVE_AUTON_BACK_LONG_DISTANCE, Chassis.MOVE_AUTON_SPEED, true));  
        addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_REVERSE_LONG_SHOT));
        addSequential(new WaitForChildren());
        
        addSequential(new WingsSetPosition(PneumaticSubsystem.EXTEND));
        addSequential(new WaitTimer(0.1));
        addSequential(new WinchFire());    

        addParallel(new WinchSetPositionLongShot(Winch.WINCH_DISTANCE_LONG_SHOT, 1.0));
        addParallel(new PivotSetAngle(Pivot.PIVOT_POSITION_FORWARD_INTAKE));
        addSequential(new ChassisMove(60, Chassis.MOVE_AUTON_SPEED, true)); 
        addSequential(new WaitForChildren());
        
        addParallel(new IntakeLoadBall());
        addSequential(new ChassisMove(40, Chassis.MOVE_AUTON_SPEED, true));  
        addSequential(new WaitForChildren());
        
        addParallel(new ChassisMove(-95, Chassis.MOVE_AUTON_SPEED, true));  
        addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_REVERSE_LONG_SHOT));
        addSequential(new WaitForChildren());
        
        addSequential(new IntakeOnTimed(0.3, Intake.INTAKE_ROLLER_SPEED_BALL_PICKUP));
        addSequential(new WingsSetPosition(PneumaticSubsystem.EXTEND));
        addSequential(new WaitTimer(1.0));
        addSequential(new WinchFire());
        
        addParallel(new WingsSetPosition(PneumaticSubsystem.RETRACT));
        addParallel(new PivotSetAngle(Pivot.PIVOT_POSITION_UP));
        addParallel(new TransmissionShift(Transmission.HI_GEAR));
    }
}

