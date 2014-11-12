package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 * @author rhhs
 */
public class WinchSafeReleaseRewind extends CommandGroup {
    
    public WinchSafeReleaseRewind() {     
        addSequential(new WinchSafeRelease());
        addSequential(new WaitCommand(2));
        addSequential(new WinchRewind());
    }
}

