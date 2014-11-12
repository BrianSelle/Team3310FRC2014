package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Pivot;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @author rhhs
 */
public class ShootSequence extends CommandGroup {
    
    public ShootSequence(double distance) {
        addSequential(new ShootSequenceNoPivot(distance));
        addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_UP));
    }
}

