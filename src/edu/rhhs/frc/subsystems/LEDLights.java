/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.utility.LPD8806_SPI;
import edu.rhhs.frc.utility.SPIDevice;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author bselle Team 3310
 */
public class LEDLights extends Subsystem {
    private static short RIGHT_START_INDEX = 20;
    private static short BACK_START_INDEX = 41;
    private static short LEFT_START_INDEX = 43;
    private static short NUM_LIGHTS_SIDE = 5;
    private static short NUM_LIGHTS_BACK = 5;
    private int m_shootRed = 255;
    private int m_shootGreen = 0;
    private int m_shootBlue = 0;
    private LPD8806_SPI m_led;
    
    public LEDLights() {
        try {
            // Right now the LED lights are the only SPI device we are using but this intiBus call 
            // should be placed higher up in the code so other devices could use it
//            SPIDevice.initBus(
 //                   new DigitalOutput(RobotMap.LED_LIGHTS_CLK_DSC_SLOT_ID, RobotMap.LED_LIGHTS_CLK_DSC_DIO_ID), 
//                    new DigitalOutput(RobotMap.LED_LIGHTS_DATA_DSC_SLOT_ID, RobotMap.LED_LIGHTS_DATA_DSC_DIO_ID), 
//                    new DigitalInput(RobotMap.LED_LIGHTS_NOT_NEEDED_DSC_SLOT_ID, RobotMap.LED_LIGHTS_NOT_NEEDED_DSC_DIO_ID));

            // Create the LDP8806 device
            m_led = new LPD8806_SPI();
        } catch (Exception e) {
            System.out.println("Unknown error initializing LED Lights.  Message = " + e.getMessage());
        }
    }
    
    public void initDefaultCommand() {
    }
    
    public void setAlliance(int alliance) {
        if (alliance == 1) {
            m_shootRed = 0;
            m_shootGreen = 0;
            m_shootBlue = 255;
        }
        else {
            m_shootRed = 255;
            m_shootGreen = 0;
            m_shootBlue = 0;
        }
    }
    
    public void setColor(int r, int g, int b) {
        m_led.setAllPixelColor(r, g, b);
        m_led.sendColors();
    }
    
    public void setColorIndex(short ledIndex, int r, int g, int b) {
        m_led.setPixelColor(ledIndex, r, g, b);
    }
    
    public void setColorRipple() {
        // Turn off all the lights
        m_led.setAllPixelColor(0, 0, 0);
        m_led.sendColors();
        for (int i = 0; i < 4 * NUM_LIGHTS_SIDE; i++) {
            int rightIndex = RIGHT_START_INDEX - i;
            int backIndex = BACK_START_INDEX - i;
            int leftIndex = LEFT_START_INDEX + i;
            m_led.setPixelColor((short)rightIndex, m_shootRed, m_shootGreen, m_shootBlue);
            m_led.setPixelColor((short)backIndex, m_shootRed, m_shootGreen, m_shootBlue);
            m_led.setPixelColor((short)leftIndex, m_shootRed, m_shootGreen, m_shootBlue);
            m_led.sendColors();
       }
    }
    
    public void setColor2(int r1, int g1, int b1, int r2, int g2, int b2) {
        m_led.setAllPixelColor2(r1, g1, b1, r2, g2, b2);
        m_led.sendColors();
    }
    
    public void setIncrementShootColor(int increment) {
        if (increment == 0 || increment == 4) {
            m_led.setAllPixelColor(0, 255, 0);            
        }
        else {
            m_led.setAllPixelColor(0, 0, 0);
            for (short i = RIGHT_START_INDEX; i > (RIGHT_START_INDEX - increment * NUM_LIGHTS_SIDE); i--) {
                m_led.setPixelColor(i, m_shootRed, m_shootGreen, m_shootBlue);
            }
            for (short i = BACK_START_INDEX; i > (BACK_START_INDEX - increment * NUM_LIGHTS_BACK); i--) {
                m_led.setPixelColor(i, m_shootRed, m_shootGreen, m_shootBlue);
            }
            for (short i = LEFT_START_INDEX; i < (LEFT_START_INDEX + increment * NUM_LIGHTS_SIDE); i++) {
                m_led.setPixelColor(i, m_shootRed, m_shootGreen, m_shootBlue);
            }
        }
        m_led.sendColors();
    }
    
    public void sendColors() {
        m_led.sendColors();
    }
}
