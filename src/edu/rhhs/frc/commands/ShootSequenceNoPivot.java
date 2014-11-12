package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.PneumaticSubsystem;
import edu.rhhs.frc.subsystems.Winch;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @author rhhs
 */
public class ShootSequenceNoPivot extends CommandGroup {
    
    public ShootSequenceNoPivot(double distance) {
        if (distance == Winch.WINCH_DISTANCE_SHORT_SHOT) {
            addSequential(new WinchSetPositionShortShot(Winch.WINCH_LONG_TO_SHORT_DISTANCE));
        }
        else {
            addSequential(new IntakeSetArmPosition(PneumaticSubsystem.EXTEND));
            addSequential(new WingsSetPosition(PneumaticSubsystem.EXTEND));
            addSequential(new WaitTimer(0.1));
        }
        addSequential(new WinchFire());
        addSequential(new WinchSetPositionLongShot(Winch.WINCH_DISTANCE_LONG_SHOT, 1.0));
        addSequential(new WingsSetPosition(PneumaticSubsystem.RETRACT));
        addSequential(new IntakeSetArmPosition(PneumaticSubsystem.RETRACT));
    }
}

