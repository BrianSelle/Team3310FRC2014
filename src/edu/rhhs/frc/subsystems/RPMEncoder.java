package edu.rhhs.frc.subsystems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Utility;

/**
 * @author rhhs
 */
public class RPMEncoder {
    private final static double DEFAULT_PULSES_PER_ROTATION = 250;
    private final static int DEFAULT_NUM_AVERAGED_CYCLES = 5;
    private final static double MAX_RPM = 6000;

    private double m_pulsesPerRotation;
    private int m_numAveragedCycles;

    private Encoder m_encoder;
    
    private int m_rpmIndex;
    private double[] m_motorRPM;
    private double m_averagedMotorRPM;
    private long m_lastTimeMicroSeconds;
    
    public RPMEncoder(int aSlot, int aChannel, int bSlot, int bChannel, boolean reverseDirection) {
        this(aSlot, aChannel, bSlot, bChannel, reverseDirection, DEFAULT_PULSES_PER_ROTATION, DEFAULT_NUM_AVERAGED_CYCLES);
    }
    
    public RPMEncoder(int aSlot, int aChannel, int bSlot, int bChannel, boolean reverseDirection, double pulsesPerRotation, int numAveragedCycles) {
            m_pulsesPerRotation = pulsesPerRotation;
            m_numAveragedCycles = numAveragedCycles;
        
            m_encoder = new Encoder(aSlot, aChannel, bSlot, bChannel, reverseDirection, CounterBase.EncodingType.k4X);

            m_motorRPM = new double[numAveragedCycles];
            m_encoder.reset();
            resetMotorRPM();
    }
    
    public void resetMotorRPM() {
        for (int i = 0; i < m_numAveragedCycles; i++) {
        	m_motorRPM[i] = 0.0;
        }
        m_lastTimeMicroSeconds = Utility.getFPGATime();
        m_averagedMotorRPM = 0;
        m_encoder.start();
    }
    
    public void setDistancePerPulse(double distancePerPulse) {
        m_encoder.setDistancePerPulse(distancePerPulse);
    }
    
    public void resetDistance() {
        m_encoder.reset();
    }
    
    public double getDistance() {
        return m_encoder.getDistance();
    }
        
    // Update the current speed calculation
    public void updateMotorRPM() { 
    	long currentTimeMicroSeconds = Utility.getFPGATime();
    	long deltaTimeMicroSeconds = currentTimeMicroSeconds - m_lastTimeMicroSeconds;
    	m_lastTimeMicroSeconds = currentTimeMicroSeconds;
    	
    	// RPM = counts/microsec * 1000000 microsec/sec * 60 sec/min * 1/counts_per_rev
    	double replacedMotorRPM = m_motorRPM[m_rpmIndex];
    	double currentRPM = (m_encoder.get() / (double)deltaTimeMicroSeconds) * 60000000.0 / m_pulsesPerRotation;
        if (currentRPM > MAX_RPM) {
            currentRPM = MAX_RPM;
        }
        m_motorRPM[m_rpmIndex] = currentRPM;
        m_encoder.reset();
 
    	m_averagedMotorRPM += (m_motorRPM[m_rpmIndex] - replacedMotorRPM) / m_numAveragedCycles;
    	
        m_rpmIndex++;
    	if (m_rpmIndex == m_numAveragedCycles) {
            m_rpmIndex = 0;
    	}
    }
    
    public double getAveragedRPM() {
        return m_averagedMotorRPM;
    }
    
    public double getInstantRPM() {
        int index = m_rpmIndex == 0 ? m_numAveragedCycles - 1 : m_rpmIndex - 1;
        return m_motorRPM[index];
    }
    
    public double getRaw() {
        return m_encoder.getRaw();
    }
}
