//Inspired by http://www.chiefdelphi.com/forums/showthread.php?t=97885&highlight=SPI
package edu.rhhs.frc.utility;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.fpga.tSPI;

/**
 *
 * Represents a device on an SPI bus Before using this class you must
 * SPIDevice.init() to initialize the SPI bus Note that the cRIO only supports
 * one SPI bus
 *
 * @author mwils
 */
public class SPIDevice extends SensorBase {

    //used to synchronize access to the SPI bus
    private static final Object semaphore = new Object();
    //tSPI instance
    private static tSPI spi = null;
    private static boolean createdBusChannels = false;
    private static DigitalOutput clkChannel = null;
    private static DigitalOutput mosiChannel = null;
    private static DigitalInput misoChannel = null;

    /**
     * Initialize SPI bus<br>
     * Only call this method once in the program
     *
     * @param clkChannel	The channel of the digital output for the clock signal.
     * @param mosiChannel	The channel of the digital output for the written data to the slave
     * (master-out slave-in).
     * @param misoChannel	The channel of the digital input for the input data from the slave
     * (master-in slave-out).
     */
    public static void initBus(final int clkChannel, final int mosiChannel, final int misoChannel) {
        initBus(SensorBase.getDefaultDigitalModule(), clkChannel, mosiChannel, misoChannel);
    }

    /**
     * Initialize SPI bus<br>
     * Only call this method once in the program
     *
     * @param slot	The slot of the module the SPIBus channels are in
     * @param clkChannel	The channel of the digital output for the clock signal.
     * @param mosiChannel	The channel of the digital output for the written data to the slave
     * (master-out slave-in).
     * @param misoChannel	The channel of the digital input for the input data from the slave
     * (master-in slave-out).
     */
    public static void initBus(final int slot, final int clkChannel, final int mosiChannel, final int misoChannel) {
        createdBusChannels = true;
        initBus(new DigitalOutput(slot, clkChannel), new DigitalOutput(slot, mosiChannel), new DigitalInput(slot, misoChannel));
    }

    /**
     * Initialize SPI bus<br>
     * Only call this method once in the program
     *
     * @param clk	The digital output for the clock signal.
     * @param mosi	The digital output for the written data to the slave
     * (master-out slave-in).
     * @param miso	The digital input for the input data from the slave
     * (master-in slave-out).
     */
    public static void initBus(final DigitalOutput clk, final DigitalOutput mosi, final DigitalInput miso) {
        if (spi == null) {
            spi = new tSPI();
        }
        else {
            throw new BadSPIConfigException("The SPI bus can only be initialized once");
        }
        
        clkChannel = clk;
        mosiChannel = mosi;
        misoChannel = miso;

        tSPI.writeChannels_SCLK_Module(clk.getModuleForRouting());
        tSPI.writeChannels_SCLK_Channel(clk.getChannelForRouting());

        if (mosi != null) {
            tSPI.writeChannels_MOSI_Module(mosi.getModuleForRouting());
            tSPI.writeChannels_MOSI_Channel(mosi.getChannelForRouting());
        } else {
            tSPI.writeChannels_MOSI_Module(0);
            tSPI.writeChannels_MOSI_Channel(0);
        }

        if (miso != null) {
            tSPI.writeChannels_MISO_Module(miso.getModuleForRouting());
            tSPI.writeChannels_MISO_Channel(miso.getChannelForRouting());
            tSPI.writeConfig_WriteOnly(false);//TODO check these are right
        } else {
            tSPI.writeChannels_MISO_Module(0);
            tSPI.writeChannels_MISO_Channel(0);
            tSPI.writeConfig_WriteOnly(true);
        }

        tSPI.writeChannels_SS_Module(0);
        tSPI.writeChannels_SS_Channel(0);

        tSPI.strobeReset();
        tSPI.strobeClearReceivedData();
    }

    /**
     * Cleanup the SPI Bus
     */
    public static void freeBus() {
        if (spi != null) {
            spi.Release();
            spi = null;
        }
        if(createdBusChannels){
            clkChannel.free();
            mosiChannel.free();
            misoChannel.free();
        }
        createdBusChannels = false;
    }

    /**
     * Perform a SPI transfer with the length of the selected device's current
     * configuration
     *
     * @param value	The value to write to the bus
     */
    private static long transferStatic(long value) {
        synchronized (semaphore) {
            tSPI.strobeClearReceivedData();

            tSPI.writeDataToLoad(value);
            tSPI.strobeLoad();

            while (tSPI.readReceivedElements() == 0) {
                Thread.yield();
            }

            tSPI.strobeReadReceivedData();
            return tSPI.readReceivedData();
        }
    }
    
    
    
    
    
    /**
     * When transferring data the it is sent and received with the most
     * significant bit first
     *
     * @see #setBitOrder(boolean)
     */
    public static final boolean BIT_ORDER_MSB_FIRST = true;
    /**
     * When transferring data the it is sent and received with the least
     * significant bit first
     *
     * @see #setBitOrder(boolean)
     */
    public static final boolean BIT_ORDER_LSB_FIRST = false;
    /**
     * When transferring data the clock will be active high<br> This corresponds
     * to CPOL=0
     *
     * @see #setClockPolarity(boolean)
     */
    public static final boolean CLOCK_POLARITY_ACTIVE_HIGH = false;
    /**
     * When transferring data the clock will be active low<br> This corresponds
     * to CPOL=1
     *
     * @see #setClockPolarity(boolean)
     */
    public static final boolean CLOCK_POLARITY_ACTIVE_LOW = true;
    /**
     * Data is valid on the leading edge of the clock pulse<br> This corresponds
     * to CPHA=0
     *
     * @see #setDataOnFalling(boolean)
     */
    public static final boolean DATA_ON_LEADING_EDGE = false;
    /**
     * Data is valid on the trailing edge of the clock pulse<br> This
     * corresponds to CPHA=1
     *
     * @see #setDataOnFalling(boolean)
     */
    public static final boolean DATA_ON_TRAILING_EDGE = true;
    /**
     * The CS will be brought high when the device is selected
     */
    public static final boolean CS_ACTIVE_HIGH = true;
    /**
     * The CS will be brought low when the device is selected
     */
    public static final boolean CS_ACTIVE_LOW = false;
    /**
     * The maximum rate the clock can transmit at
     */
    public static final double MAX_CLOCK_FREQUENCY = 76628.4;
    /**
     * The minimum rate the clock can transmit at
     */
    public static final double MIN_CLOCK_FREQUENCY = 300;
    
    private boolean createdChannel = false;
    private DigitalOutput cs = null;
    
    private boolean csActiveHigh;
    private boolean bitOrder = BIT_ORDER_MSB_FIRST;
    private boolean clockPolarity = CLOCK_POLARITY_ACTIVE_HIGH;
    private boolean dataOnTrailing = DATA_ON_LEADING_EDGE;
    private int clockHalfPeriodDelay = 0;//fastest clockrate possible
    
    
    /**
     * Create a new device on the SPI bus.<br>The chip select line is active low
     *
     * @param slot The module of the digital output for the device's chip select pin
     * @param csChannel	The channel for the digital output for the device's chip select pin
     */
    public SPIDevice(int slot, int csChannel) {
        this(new DigitalOutput(slot, csChannel), CS_ACTIVE_LOW, true);
    }
    
    /**
     * Create a new device on the SPI bus
     *
     * @param slot The module of the digital output for the device's chip select pin
     * @param csChannel	The channel for the digital output for the device's chip select pin
     * @param csActiveHigh	If the chip select line should be high or low when
     * the device is selected is being selected
     */
    public SPIDevice(int slot, int csChannel, boolean csActiveHigh) {
        this(new DigitalOutput(slot, csChannel), csActiveHigh, true);
    }

    /**
     * Create a new device on the SPI bus.<br>The chip select line is active low
     *
     * @param cs	The digital output for the device's chip select pin
     */
    public SPIDevice(DigitalOutput cs) {
        this(cs, CS_ACTIVE_LOW);
    }

    /**
     * Create a new device on the SPI bus
     *
     * @param cs	The digital output for the device's chip select pin
     * @param csActiveHigh	If the chip select line should be high or low when
     * the device is selected is being selected
     */
    public SPIDevice(DigitalOutput cs, boolean csActiveHigh) {
        this(cs, csActiveHigh, false);
    }

    /**
     * Create a new device on the SPI bus
     *
     * @param cs	The digital output for the device's chip select pin
     * @param csActiveHigh	If the chip select line should be high or low when
     * @param createdChannel	If the free method should free the cs DigitalOutput
     * the device is selected is being selected
     */
    private SPIDevice(DigitalOutput cs, boolean csActiveHigh, boolean createdChannel) {
        if (spi == null) {
            throw new RuntimeException("must call SPI.init first");
        }
        this.createdChannel = createdChannel;
        this.cs = cs;
        this.csActiveHigh = csActiveHigh;
        cs.set(!csActiveHigh);
    }
    
    /**
     * Free the resources used by this object
     */
    public void free(){
        if(createdChannel && cs!=null)
            cs.free();
    }

    /**
     * Perform a SPI transfer with the length of this device's current
     * configuration. This will select the device, transfer the data and then
     * deselect the device
     *
     * @param writeValue	The value to write to the device
     * @param numBits	The number of bits to write/read
     */
    public long transfer(long writeValue, int numBits) {
        long[] readValue;
        synchronized (semaphore) {
            readValue = transfer(new long[]{writeValue}, new int[]{numBits});
        }
        return readValue[0];
    }

    /**
     * Perform a SPI transfer where an array of bits are written and read. The
     * number of bits to write and read is specified in numBits<br> The whole
     * transfer will occur with the cs line held active throughout
     *
     * @param writeValues	The value to write to the device
     * @param numBits	The number of bits to write/read
     */
    public long[] transfer(long[] writeValues, int[] numBits) {
        if (writeValues.length != numBits.length) {
            throw new BadSPIConfigException("The number of values to write does not match array of data lengths");
        }
        for (int i = 0; i < numBits.length; ++i) {
            if (numBits[i] < 1 || numBits[i] > 32) {
                throw new BadSPIConfigException("All values in the data length must be >0 and <=32");
            }
        }
        long[] readValues = new long[writeValues.length];
        synchronized (semaphore) {
            tSPI.writeConfig_MSBfirst(bitOrder);
            tSPI.writeConfig_ClockHalfPeriodDelay(clockHalfPeriodDelay);
            tSPI.writeConfig_ClockPolarity(clockPolarity);
            tSPI.writeConfig_DataOnFalling(dataOnTrailing);

            
            //cs.set(csActiveHigh);
            tSPI.writeConfig_FramePolarity(!csActiveHigh);
            //Set up FPGA for chip select
            tSPI.writeConfig_LatchLast(false);
            tSPI.writeConfig_LatchFirst(false);
            tSPI.writeChannels_SS_Module(cs.getModuleForRouting());
            tSPI.writeChannels_SS_Channel(cs.getChannelForRouting());

            for (int i = 0; i < writeValues.length; ++i) {
                tSPI.writeConfig_BusBitWidth(numBits[i]);
                readValues[i] = transferStatic(writeValues[i]);
            }

            //cs.set(!csActiveHigh);
            tSPI.writeChannels_SS_Module(0);
            tSPI.writeChannels_SS_Channel(0);
        }
        return readValues;
    }

    //TODO make sure it always sends the lowest bits of the transfer value
    /**
     * Sets the bit order of the transfer sent and received values.<br>The value
     * transfered/received will always be the lowest bits of the value. This
     * method just sets the order in which those bits are transfered
     *
     * @param bitOrder	true=Most significant bit first, false=Least significant
     * bit first
     *
     * @see #BIT_ORDER_MSB_FIRST
     * @see #BIT_ORDER_LSB_FIRST
     */
    public final void setBitOrder(boolean bitOrder) {
        this.bitOrder = bitOrder;
    }

    /**
     * Sets the polarity of the clock when transferring data to the device
     *
     * @param clockPolarity	true=Clock active low, false=Clock active high
     *
     * @see #CLOCK_POLARITY_ACTIVE_HIGH
     * @see #CLOCK_POLARITY_ACTIVE_LOW
     */
    public final void setClockPolarity(boolean clockPolarity) {
        this.clockPolarity = clockPolarity;
    }

    /**
     * If Data is valid at the beginning of the clock pulse or the end of the
     * clock pulse
     *
     * @param dataOnTrailing	true=Process data on the trailing edge of the clock,
     * false=Process data on leading edge of the clock
     *
     * @see #DATA_ON_LEADING_EDGE
     * @see #DATA_ON_TRAILING_EDGE
     */
    public final void setDataOnTrailing(boolean dataOnTrailing) {
        this.dataOnTrailing = dataOnTrailing;
    }

    /**
     * Set the frequence of the clock when sending data
     *
     * @param hz	the frequence of the clock in hz
     *
     * @see #MIN_CLOCK_FREQUENCY
     * @see #MAX_CLOCK_FREQUENCY
     */
    public final void setClockRate(double hz) {
        int delay = 0;
        // TODO: compute the appropriate values based on digital loop timing
        if (hz <= 76628.4) {
            double v = (1.0 / hz) / 1.305e-5;
            int intv = (int) v;
            if (v - intv > 0.5) {
                delay = intv;
            } else {
                delay = intv - 1;
            }
        } else {
            throw new BadSPIConfigException("Clock Rate too high. Hz: " + hz);
        }
        if (delay > 255) {
            throw new BadSPIConfigException("Clock Rate too low. Hz: " + hz);
        }

        clockHalfPeriodDelay = delay;
    }

    public static class BadSPIConfigException extends RuntimeException {

        public BadSPIConfigException(String message) {
            super(message);
        }
    }
    
}
