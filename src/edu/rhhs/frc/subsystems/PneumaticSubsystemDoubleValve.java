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
public abstract class PneumaticSubsystemDoubleValve extends PneumaticSubsystem {

    protected Solenoid m_solenoidExtend; 
    protected Solenoid m_solenoidRetract; 
    
    public PneumaticSubsystemDoubleValve(String name, int extendModuleId, int extendRelayId, int retractModuleId, int retractRelayId) {
        super(name);
        try {
            m_solenoidExtend = new Solenoid(extendModuleId, extendRelayId);
            m_solenoidRetract = new Solenoid(retractModuleId, retractRelayId);
        } catch (Exception e) {
            System.out.println("Unknown error initializing " + getName() + ".  Message = " + e.getMessage());
        }
    }
        
    public void initDefaultCommand() {
    }
    
    public void extend() {
        m_solenoidExtend.set(true);
        m_solenoidRetract.set(false);
        m_position = EXTENDED;
    }
    
    public void retract() {
        m_solenoidExtend.set(false);
        m_solenoidRetract.set(true);
        m_position = RETRACTED;
    }
    
    public void noPressure() {
        m_solenoidExtend.set(false);
        m_solenoidRetract.set(false);
        System.out.println("No Pressure");
        m_position = NO_PRESSURED;
    }
    
    public void bothPressure() {
        m_solenoidExtend.set(true);
        m_solenoidRetract.set(true);
        m_position = BOTH_PRESSURED;
    }
}
