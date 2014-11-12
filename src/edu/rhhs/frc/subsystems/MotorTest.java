/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author bselle
 */
public class MotorTest extends Subsystem {

    private Victor[] m_motorController;
    protected String[] m_status;
    
    public MotorTest(int numMotors) {
        super("MotorTestSubsystem");
        
        try {
            m_motorController = new Victor[numMotors];
            
            for (int motorId = 0; motorId < numMotors; motorId++) {
                m_motorController[motorId] = new Victor(motorId+1);
            }
        } catch (Exception e) {
            System.out.println("Unknown error initializing " + getName() + ".  Message = " + e.getMessage());
        }
    }
        
    public void initDefaultCommand() {
    }
    
    public void setSpeed(int motorId, double speed) {
        System.out.println("In Motor Test setSpeed, Motor id = " + motorId +  ", speed = " + speed);
        m_motorController[motorId].set(speed);
    }
    
    public void stop(int motorId) {
        m_motorController[motorId].stopMotor();
    }
    
    public void updateStatus() {
        try {
            for (int motorId = 0; motorId < m_motorController.length; motorId++) {
                SmartDashboard.putNumber("Motor[" + motorId + "]", m_motorController[motorId].getPosition());
            }
        } catch (Exception ex) {
            System.out.println("Error in updateStatus " + this.getName() + ".  Message = " + ex.getMessage());
        }
    }

}
