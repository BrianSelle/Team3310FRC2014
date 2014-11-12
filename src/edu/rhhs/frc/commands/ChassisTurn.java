/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class ChassisTurn extends CommandBase {
    
    private double m_angleDeg;
    private double m_maxSpeed;
    private double m_turnAxis;
    
    public ChassisTurn(double angleDeg, double maxSpeed, double turnAxis) {
        m_angleDeg = angleDeg;
        m_maxSpeed = maxSpeed;
        m_turnAxis = turnAxis;
        requires(getChassis()); // reserve the chassis subsystem
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        getChassis().setTurnDeltaAngle(m_angleDeg, m_maxSpeed, m_turnAxis);
        getChassis().startPID();
        System.out.println("Time = " + System.currentTimeMillis() + "Chassis turn start for angle= " + m_angleDeg);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return getChassis().atSetPoint();
    }

    // Called once after isFinished returns true
    protected void end() {
        getChassis().disable();
        System.out.println("Time = " + System.currentTimeMillis() + ", Chassis turn end for angle = " + m_angleDeg);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
