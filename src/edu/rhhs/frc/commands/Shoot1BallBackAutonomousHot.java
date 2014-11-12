package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Chassis;
import edu.rhhs.frc.subsystems.Pivot;
import edu.rhhs.frc.subsystems.PneumaticSubsystem;
import edu.rhhs.frc.subsystems.Transmission;
import edu.rhhs.frc.subsystems.Winch;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitForChildren;

/**
 * @author rhhs
 */
public class Shoot1BallBackAutonomousHot extends CommandGroup {
    
    public Shoot1BallBackAutonomousHot() {     
        addSequential(new TransmissionShift(Transmission.LO_GEAR));
        addParallel(new ChassisMove(Chassis.MOVE_AUTON_BACK_LONG_DISTANCE, Chassis.MOVE_AUTON_SPEED, true));  
        addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_REVERSE_LONG_SHOT));
        addSequential(new WaitForChildren());
        
        addSequential(new WaitOnCheesyVision(5));

        addSequential(new WingsSetPosition(PneumaticSubsystem.EXTEND));
        addSequential(new WaitTimer(0.1));
        addSequential(new WinchFire());    

        addParallel(new WingsSetPosition(PneumaticSubsystem.RETRACT));
        addParallel(new WinchSetPositionLongShot(Winch.WINCH_DISTANCE_LONG_SHOT, 1.0));
        addParallel(new PivotSetAngle(Pivot.PIVOT_POSITION_UP));
    }
}

