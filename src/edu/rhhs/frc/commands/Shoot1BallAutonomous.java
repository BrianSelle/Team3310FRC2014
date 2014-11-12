package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Chassis;
import edu.rhhs.frc.subsystems.Pivot;
import edu.rhhs.frc.subsystems.Transmission;
import edu.rhhs.frc.subsystems.Winch;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.command.WaitForChildren;

/**
 * @author rhhs
 */
public class Shoot1BallAutonomous extends CommandGroup {
    
    public Shoot1BallAutonomous() {     
        addSequential(new TransmissionShift(Transmission.LO_GEAR));
        addParallel(new ChassisMove(Chassis.MOVE_AUTON_FORWARD_LONG_DISTANCE, Chassis.MOVE_AUTON_SPEED, true));  
        addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_LONG_SHOT));
        addSequential(new WaitForChildren());
        
        addSequential(new WaitCommand(1));

        addSequential(new ShootSequence(Winch.WINCH_DISTANCE_LONG_SHOT));
    }
}

