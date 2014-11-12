package edu.rhhs.frc.utility;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;

/**
 * @author rhhs
 */
public class PositionEncoder {
    private final static double DEFAULT_PULSES_PER_ROTATION = 250;
    private Encoder m_encoder;
    
    public PositionEncoder(int aSlot, int aChannel, int bSlot, int bChannel, boolean reverseDirection, double wheelDiameter, double gearRatioWheelToEncoder) {
        this(aSlot, aChannel, bSlot, bChannel, reverseDirection, DEFAULT_PULSES_PER_ROTATION, wheelDiameter, gearRatioWheelToEncoder);
    }
    
    public PositionEncoder(int aSlot, int aChannel, int bSlot, int bChannel, boolean reverseDirection, double pulsesPerRotation, double wheelDiameter, double gearRatioWheelToEncoder) {
            m_encoder = new Encoder(aSlot, aChannel, bSlot, bChannel, reverseDirection, CounterBase.EncodingType.k4X);
            double distancePerPulse = Math.PI * wheelDiameter * wheelDiameter * gearRatioWheelToEncoder / 4.0 / pulsesPerRotation;
            m_encoder.setDistancePerPulse(distancePerPulse);
            resetDistance();
    }
    
    public void resetDistance() {
        m_encoder.reset();
        m_encoder.start();
    }
    
    public double getDistance() {
        return m_encoder.getDistance();
    }
            
    public double getRaw() {
        return m_encoder.getRaw();
    }
}
