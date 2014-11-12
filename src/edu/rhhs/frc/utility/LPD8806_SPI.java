/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.rhhs.frc.utility;

import edu.rhhs.frc.RobotMap;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.SensorBase;

/**
 *
 * @author bselle 
 * FRC Team 3310
 */
public class LPD8806_SPI extends SensorBase {            
    private SPIDevice spi;
    
    private static final int numLEDs = 64;
    private static final int latchLen = (numLEDs + 63) / 64;
//    private static final int numLEDs = 32;
//    private static final int latchLen = (numLEDs + 31) / 32;
    private long[] colors = new long[numLEDs];
    private int[] colorBits = new int[numLEDs];

    /**
     * Constructor.
     */
    public LPD8806_SPI() { 
        // Don't really need a chip select, but for now the SPIDevice class requires one
  //      spi = new SPIDevice(new DigitalOutput(RobotMap.LED_LIGHTS_CS_NOT_NEEDED_DSC_SLOT_ID, RobotMap.LED_LIGHTS_CS_NOT_NEEDED_DSC_DIO_ID), SPIDevice.CS_ACTIVE_HIGH);
        
        // Based on the Adafruit Arduino C++ code for the LPD8806 this is correct
        spi.setBitOrder(SPIDevice.BIT_ORDER_MSB_FIRST);  
        
        // Based on the Adafruit Arduino C++ code for the LPD8806 they use SPI_MODE0 which is:
        //  Clock Polarity CPOL = 0, Clock Phase CPHA = 0
        spi.setClockPolarity(SPIDevice.CLOCK_POLARITY_ACTIVE_HIGH);
        spi.setDataOnTrailing(SPIDevice.DATA_ON_LEADING_EDGE);
        
        // Based on the Adafruit Arduino C++ code for the LPD8806 they use setClockDivider(SPI_CLOCK_DIV8) which 
        // sets the SPI clock divider relative to the system clock. On AVR based Arduino boards, 
        // the dividers available are 2, 4, 8, 16, 32, 64 or 128. The default setting is SPI_CLOCK_DIV4, 
        // which sets the SPI clock to one-quarter the frequency of the system clock (4 Mhz for the boards at 16 MHz).
        // So SPI_CLOCK_DIV8 will set it at 2Mhz.  The notes in the LPD8806 code say, "Although the LPD8806 should, 
        // in theory, work up to 20MHz, the unshielded wiring from the Arduino is more susceptible to interference.  
        // Experiment and see what you get.
        //
        // The max clock rate from SPIDevice is 76628 hz which is 0.076628Mhz... 26x slower.   Not sure if this is OK or if we
        // are comparing apples to apples. Update: 76628 hz seems to work fine
        spi.setClockRate(76628.4);

        // Set up the latch the first time to "prime" the lights
        sendLatchReset();
        
        // Set up the number of bit for each LED color
        for (int i = 0; i < numLEDs; i++) {
            colorBits[i] = 24;
        }
    }
    
    public void free(){
        spi.free();
    }
    
    public short numLEDS() {
        return numLEDs;
    }

    public void setPixelColor(short ledIndex, int r, int g, int b) {
        colors[ledIndex] = getColor(r, g, b); 
    }   
    
    public void setAllPixelColor(int r, int g, int b) {
        int color = getColor(r, g, b); 
        for (int i = 0; i < numLEDs; i++) {
            colors[i] = color;
        }        
    }   
    
    public void setAllPixelColor2(int r1, int g1, int b1, int r2, int g2, int b2) {
        int color1 = getColor(r1, g1, b1); 
        int color2 = getColor(r2, g2, b2); 
        for (int i = 0; i < numLEDs; i+=2) {
            colors[i] = color1;
        }        
        for (int i = 1; i < numLEDs; i+=2) {
            colors[i] = color2;
        }        
    }   
    
    private int getColor(int r, int g, int b) {
       return ((g | 0x80) << 16)
                | ((r | 0x80) << 8)
                | b | 0x80;
    }

    private void sendLatchReset() {
        for(int i = 0; i < latchLen; i++) {
            spi.transfer(0, 8);
        }        
    }

    public void sendColors() {
        spi.transfer(colors, colorBits);
        sendLatchReset();
        sendLatchReset();
    }
}