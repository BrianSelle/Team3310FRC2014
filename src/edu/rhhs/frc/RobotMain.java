/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.rhhs.frc;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.rhhs.frc.commands.CommandBase;
import edu.rhhs.frc.commands.Shoot0BallAutonomous;
import edu.rhhs.frc.commands.Shoot1BallAutonomous;
import edu.rhhs.frc.commands.Shoot1BallAutonomousHot;
import edu.rhhs.frc.commands.Shoot1BallBackAutonomous;
import edu.rhhs.frc.commands.Shoot1BallBackAutonomousHot;
import edu.rhhs.frc.commands.Shoot1BallShortAutonomous;
import edu.rhhs.frc.commands.Shoot2BallBackAutonomous;
import edu.rhhs.frc.commands.Shoot1BallShortAutonomousHot;
import edu.rhhs.frc.commands.Shoot2BallAutonomous;
import edu.rhhs.frc.subsystems.Transmission;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot {

    private Command autonomousCommand;
    private SendableChooser autonomousModeChooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        System.out.println("Robot Init Started");
        
        // Initialize all subsystems
        CommandBase.init();
        
        // instantiate the command used for the autonomous period        
        autonomousModeChooser = new SendableChooser();
        autonomousModeChooser.addDefault("1 Ball Front Short", new Shoot1BallShortAutonomous());
        autonomousModeChooser.addObject("1 Ball Front Short Hot", new Shoot1BallShortAutonomousHot());
        autonomousModeChooser.addObject("1 Ball Front Long", new Shoot1BallAutonomous());
        autonomousModeChooser.addObject("1 Ball Front Long Hot", new Shoot1BallAutonomousHot());
        autonomousModeChooser.addObject("1 Ball Back Long", new Shoot1BallBackAutonomous());
        autonomousModeChooser.addObject("1 Ball Back Long Hot", new Shoot1BallBackAutonomousHot());
        autonomousModeChooser.addObject("2 Ball Front", new Shoot2BallAutonomous());
        autonomousModeChooser.addObject("2 Ball Back", new Shoot2BallBackAutonomous());
        autonomousModeChooser.addObject("Move Forward 150", new Shoot0BallAutonomous(0, 150));
        SmartDashboard.putData("Autonomous Mode", autonomousModeChooser);

        updateStatus();
        System.out.println("Robot Init Completed");
    }

    public void autonomousInit() {
         // instantiate the command used for the autonomous period
        autonomousCommand = (Command)autonomousModeChooser.getSelected();

        // Start CheesyVision server
        CommandBase.getVisionServer().reset();
        CommandBase.getVisionServer().startSamplingCounts();
        
        // schedule the autonomous command (example)
        autonomousCommand.start();
        updateStatus();
        System.out.println("Automomous Init Completed");
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateStatus();
    }

    public void disabledInit() {
        updateStatus();
        System.out.println("Disabled Init Completed");
    }

    public void teleopInit() {
	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        CommandBase.getVisionServer().stop();
        CommandBase.getTransmission().shift(Transmission.HI_GEAR);
        
        updateStatus();
        System.out.println("Teleop Init Completed");
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        updateStatus();
    }
    
    public void disabledPeriodic() {
        updateStatus();
    }

    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        try {
            CommandBase.getChassis().updateStatus();
            CommandBase.getWinch().updateStatus();
            CommandBase.getPivot().updateStatus();
            CommandBase.getIntake().updateStatus();
//            CommandBase.getMotorTest().updateStatus();
//            CommandBase.getPneumaticTest().updateStatus();
        }
        catch (Exception e) {
            // Do nothing... just don't want to crash the robot
        }
    }
}
