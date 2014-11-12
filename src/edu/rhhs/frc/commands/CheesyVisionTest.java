package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @author rhhs
 */
public class CheesyVisionTest extends CommandGroup {
    
    public CheesyVisionTest() {
        addSequential(new WaitOnCheesyVision(5));
        addSequential(new IntakeSetRollerSpeed(0.5));
    }
}

