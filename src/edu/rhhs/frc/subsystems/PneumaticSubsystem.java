/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author bselle
 */
public abstract class PneumaticSubsystem extends Subsystem {

    public static final int RETRACT = 0;
    public static final int EXTEND = 1;
    public static final int NO_PRESSURE= 2;
    public static final int BOTH_PRESSURE= 2;
    protected static final String NO_PRESSURED = "No Pressure";
    protected static final String BOTH_PRESSURED = "Both Pressure";
    protected static final String RETRACTED = "Retracted";
    protected static final String EXTENDED = "Extended";   
    protected static final String UNKNOWN = "Unknown";   

    protected String m_position = UNKNOWN;
    
    public PneumaticSubsystem(String name) {
        super(name);
    }
        
    public abstract void extend();
    public abstract void retract();
    public abstract void noPressure();
    public abstract void bothPressure();
    
    public void setPosition(int position) {
        if (position == EXTEND) {
            extend();
        }
        else if (position == RETRACT) {
            retract();
        }
        else if (position == NO_PRESSURE) {
            noPressure();
        }
        else if (position == BOTH_PRESSURE) {
            bothPressure();
        }
    }
}
