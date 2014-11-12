/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

/**
 *
 * @author bselle
 */
public class ChassisSetup extends CommandBase {
    
    public static final int MOVE_SCALE = 0;
    public static final int STEER_SCALE = 1; 
    public static final int MOVE_TRIM = 2;
    public static final int STEER_TRIM = 3; 
    public static final int MOVE_NONLINEAR = 4;
    public static final int STEER_NONLINEAR = 5; 
    public static final int CONTROLLER_INCREMENT_MODE = 6; 
    public static final int CONTROLLER_SET_MODE = 7; 
    
    private double m_increment;
    private int m_parameter;
    
    public ChassisSetup(int parameter, double increment) {
        requires(getChassis());
        m_increment = increment;
        m_parameter = parameter;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        switch(m_parameter) {
            case MOVE_SCALE:
                double scaleFactor = getChassis().getMoveScale() + m_increment;
                getChassis().setMoveScale(scaleFactor);
                break;
            case STEER_SCALE:
                scaleFactor = getChassis().getSteerScale() + m_increment;
                getChassis().setSteerScale(scaleFactor);
                break;
            case MOVE_TRIM:
                double trimFactor = getChassis().getMoveTrim() + m_increment;
                getChassis().setMoveTrim(trimFactor);
                break;
            case STEER_TRIM:
                trimFactor = getChassis().getSteerTrim() + m_increment;
                getChassis().setSteerTrim(trimFactor);
                break;
            case MOVE_NONLINEAR:
                int nonLinearFactor = getChassis().getMoveNonLinear() + (int)m_increment;
                getChassis().setMoveNonLinear(nonLinearFactor);
                break;
            case STEER_NONLINEAR:
                nonLinearFactor = getChassis().getSteerNonLinear() + (int)m_increment;
                getChassis().setSteerNonLinear(nonLinearFactor);
                break;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
