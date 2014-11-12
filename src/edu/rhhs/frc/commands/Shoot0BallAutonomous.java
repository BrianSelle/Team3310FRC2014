package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Chassis;
import edu.rhhs.frc.subsystems.Transmission;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 * @author rhhs
 */
public class Shoot0BallAutonomous extends CommandGroup {
    
    public Shoot0BallAutonomous(double waitTime, double distance) {     
        addSequential(new TransmissionShift(Transmission.LO_GEAR));
        addSequential(new WaitCommand(waitTime));
        addSequential(new ChassisMove(distance, Chassis.MOVE_AUTON_SPEED, true));  
    }
}

