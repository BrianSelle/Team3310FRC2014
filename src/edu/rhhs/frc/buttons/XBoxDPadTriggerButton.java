/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.buttons;

import edu.rhhs.frc.controller.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 *
 * @author bselle
 */
public class XBoxDPadTriggerButton extends Button {

    public static final int RIGHT_TRIGGER = 0;
    public static final int LEFT_TRIGGER = 1;
//    public static final int TOP_TRIGGER = 3;
//    public static final int BOTTOM_TRIGGER = 4;
    
    private XboxController m_controller;
    private int m_trigger;
    
    public XBoxDPadTriggerButton(XboxController controller, int trigger) {
        m_controller = controller;
        m_trigger = trigger;
    }
    
    public boolean get() {
        if (m_trigger == RIGHT_TRIGGER) {
            return m_controller.getDPadRight();
        }
        else if (m_trigger == LEFT_TRIGGER) {
            return m_controller.getDPadLeft();
        }
//        else if (m_trigger == TOP_TRIGGER) {
//            return m_controller.getDPadTop();
//        }
//        else if (m_trigger == BOTTOM_TRIGGER) {
//            return m_controller.getDPadBottom();
//        }
        return false;
    }
    
}
