/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.subsystems;

import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author bselle
 */
public abstract class PneumaticSubsystemSingleValve extends PneumaticSubsystem {

    protected Solenoid m_solenoidExtend; 
    
    public PneumaticSubsystemSingleValve(String name, int extendModuleId, int extendRelayId) {
        super(name);
        try {
            m_solenoidExtend = new Solenoid(extendModuleId, extendRelayId);
        } catch (Exception e) {
            System.out.println("Unknown error initializing " + getName() + ".  Message = " + e.getMessage());
        }
    }
        
    public void initDefaultCommand() {
    }
    
    public void extend() {
        m_solenoidExtend.set(true);
        m_position = EXTENDED;
    }
    
    public void retract() {
        m_solenoidExtend.set(false);
        m_position = RETRACTED;
    }
}
