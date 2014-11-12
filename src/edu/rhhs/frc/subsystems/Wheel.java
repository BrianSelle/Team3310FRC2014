/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;

/**
 *  
 * @author kenny
 */
public class Wheel extends PneumaticSubsystemDoubleValve {
    
    public Wheel() {
        super("Wheel", RobotMap.WHEEL_EXTEND_PNEUMATIC_MODULE_ID, RobotMap.WHEEL_EXTEND_PNEUMATIC_RELAY_ID, RobotMap.WHEEL_RETRACT_PNEUMATIC_MODULE_ID, RobotMap.WHEEL_RETRACT_PNEUMATIC_RELAY_ID);
    }

    public void initDefaultCommand() {
    }
}
