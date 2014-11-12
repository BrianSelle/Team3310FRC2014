/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author bselle
 */
public class PneumaticTest extends Subsystem {

    protected Solenoid[] m_solenoidExtend; 
    protected Solenoid[] m_solenoidRetract; 
    protected String[] m_position;
    
    public PneumaticTest(int numDoubleValves) {
        super("PneumaticTestSubsystem");
        
        try {
            m_solenoidExtend = new Solenoid[numDoubleValves];
            m_solenoidRetract = new Solenoid[numDoubleValves];
            m_position = new String[numDoubleValves];
        
            int relayPort = 1;
            int moduleId = 1;
            for (int valveId = 0; valveId < numDoubleValves; valveId++) {
                if (valveId == 4) {
                    moduleId = 2;
                    relayPort = 1;
                }
                System.out.println("Pneumatic Extend, Valve id = " + valveId +  ", module = " + moduleId +  ", port = " + relayPort);
                m_solenoidExtend[valveId] = new Solenoid(moduleId, relayPort++);
                System.out.println("Pneumatic Retract, Valve id = " + valveId +  ", module = " + moduleId +  ", port = " + relayPort);
                m_solenoidRetract[valveId] = new Solenoid(moduleId, relayPort++);
                m_position[valveId] = PneumaticSubsystem.UNKNOWN;
            }
        } catch (Exception e) {
            System.out.println("Unknown error initializing " + getName() + ".  Message = " + e.getMessage());
        }
    }
        
    public void initDefaultCommand() {
    }
    
    private void extend(int valveId) {
        m_solenoidExtend[valveId].set(true);
        m_solenoidRetract[valveId].set(false);
        m_position[valveId] = PneumaticSubsystem.EXTENDED;
    }
    
    private void retract(int valveId) {
        m_solenoidExtend[valveId].set(false);
        m_solenoidRetract[valveId].set(true);
        m_position[valveId] = PneumaticSubsystem.RETRACTED;
    }
    
    public void setPosition(int valveId, int position) {
       System.out.println("Pneumatic Set Position, Valve id = " + valveId +  ", position = " + position);
       if (position == PneumaticSubsystem.EXTEND) {
            extend(valveId);
        }
        else {
            retract(valveId);
        }
    }
    
    public void updateStatus() {
        try {
            for (int valveId = 0; valveId < m_position.length; valveId++) {
                SmartDashboard.putString("Pneumatic Valve[" + valveId + "]", m_position[valveId]);
            }
        } catch (Exception ex) {
            System.out.println("Error in updateStatus " + this.getName() + ".  Message = " + ex.getMessage());
        }
    }

}
