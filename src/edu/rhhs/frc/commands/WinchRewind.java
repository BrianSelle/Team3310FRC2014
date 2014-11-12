/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.PneumaticSubsystem;
import edu.rhhs.frc.subsystems.Winch;

/**
 *
 * @author kenny
 */
public class WinchRewind extends ExtraTwoTimeoutCommandBase {
    
    private static final double REWIND_TIME = 2.0;
    private static final double UNWIND_TIME = 0.1;
    private boolean m_doneRewinding = false;
    
    public WinchRewind() {
        requires(getWinch());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_doneRewinding = false;
//        System.out.println("Start rewind");
        getWinch().setBrakePosition(PneumaticSubsystem.RETRACT);
        getWinch().setShiftPosition(PneumaticSubsystem.EXTEND);
        getWinch().setWinchSpeed(Winch.WINCH_REWIND_SPEED);
        startExtraTimeout1(REWIND_TIME);
        resetTimer2();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (m_doneRewinding == false && isExtraTimedOut1()) {
//            System.out.println("Done with rewinding");
            getWinch().stopWinch();
            startExtraTimeout2(UNWIND_TIME);
            m_doneRewinding = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isExtraTimedOut2();
    }

    // Called once after isFinished returns true
    protected void end() {
//        System.out.println("Rewind Reset Zero");
        getWinch().stopWinch();
        getWinch().resetZeroPosition();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
