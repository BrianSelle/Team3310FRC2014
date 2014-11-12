package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Pivot;
import edu.rhhs.frc.subsystems.PneumaticSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @author rhhs
 */
public class IntakePivotLoad extends CommandGroup {
    
    public IntakePivotLoad(double intakePivotPosition, double finalPivotPosition) {
        addSequential(new WingsSetPosition(PneumaticSubsystem.EXTEND));
        addSequential(new IntakeSetArmPosition(PneumaticSubsystem.RETRACT));
        addSequential(new PivotSetAngle(intakePivotPosition));
        addSequential(new IntakeLoadBall());
 //       addSequential(new WingsSetPosition(PneumaticSubsystem.RETRACT));
        addSequential(new PivotSetAngle(finalPivotPosition));
    }
}

