package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Pivot;
import edu.rhhs.frc.subsystems.PneumaticSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @author rhhs
 */
public class IntakePivotCatch extends CommandGroup {
    
    public static final int CATCH_POSITION_FORWARD = 0;
    public static final int CATCH_POSITION_UP = 1;
    public static final int CATCH_POSITION_REVERSE = 2;
     
    public IntakePivotCatch(int catchPosition) {
        if (catchPosition == CATCH_POSITION_FORWARD) {
            addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_FORWARD_CATCH_HIGH));
        }
        else if (catchPosition == CATCH_POSITION_REVERSE) {
            addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_REVERSE_CATCH_HIGH));
        }
        else if (catchPosition == CATCH_POSITION_UP) {
            addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_UP));
        }
        addSequential(new IntakeSetArmPosition(PneumaticSubsystem.EXTEND));
        addSequential(new WingsSetPosition(PneumaticSubsystem.EXTEND));
        addSequential(new IntakeLoadBall());
//        addSequential(new WingsSetPosition(PneumaticSubsystem.RETRACT));
        addSequential(new IntakeSetArmPosition(PneumaticSubsystem.RETRACT));
        addSequential(new PivotSetAngle(Pivot.PIVOT_POSITION_UP));
    }
}

