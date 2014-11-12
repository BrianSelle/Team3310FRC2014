/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;

/**
 *
 * @author bselle
 */
public class Transmission extends PneumaticSubsystemDoubleValve {

    public static final int HI_GEAR = 0;
    public static final int LO_GEAR = 1;
    
    public Transmission() {
        super("Transmission", RobotMap.SHIFT_EXTEND_PNEUMATIC_MODULE_ID, RobotMap.SHIFT_EXTEND_PNEUMATIC_RELAY_ID, 
                RobotMap.SHIFT_RETRACT_PNEUMATIC_MODULE_ID, RobotMap.SHIFT_RETRACT_PNEUMATIC_RELAY_ID);
    }
        
    public void shift(int gearPosition) {
        if (gearPosition == HI_GEAR) {
            retract();
        }
        else if (gearPosition == LO_GEAR) {
            extend();
        }
    }
}
