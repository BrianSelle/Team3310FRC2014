package edu.rhhs.frc.commands;

import edu.rhhs.frc.OI;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Command;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.subsystems.Chassis;
import edu.rhhs.frc.subsystems.CheesyVisionServer;
import edu.rhhs.frc.subsystems.Intake;
import edu.rhhs.frc.subsystems.MotorTest;
import edu.rhhs.frc.subsystems.Pivot;
import edu.rhhs.frc.subsystems.PneumaticTest;
import edu.rhhs.frc.subsystems.Transmission;
import edu.rhhs.frc.subsystems.Wheel;
import edu.rhhs.frc.subsystems.Winch;
import edu.rhhs.frc.subsystems.Wings;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    // Get an instance of the OI 
    private static OI oi;
    
    // Create a single static instance of all of your subsystems
    private static Chassis m_chassis;
    private static Intake m_intake;
    private static Pivot m_pivot;
    private static Wings m_wings;
    private static Winch m_winch;
    private static Wheel m_wheel;
    private static Transmission m_transmission;
    
    private static MotorTest m_motorTest;
    private static PneumaticTest m_pneumaticTest;
   
    public static final int visionServerListenPort = 1180;

    public static Compressor m_compressor = new Compressor(
            RobotMap.COMPRESSOR_SWITCH_DSC_DIO_ID, 
            RobotMap.COMPRESSOR_DSC_RELAY_ID);

    public static void init() {
        oi = OI.getInstance();
        m_compressor.start();
        
        // Initialize CheesyVision server
        getVisionServer().setPort(visionServerListenPort);
        getVisionServer().start();

        // No commands create this subsystem 
        getChassis();
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
    
    public static Chassis getChassis() {
        if (m_chassis == null) {
            m_chassis = new Chassis();
        }
        return m_chassis;
    }
    
    public static Intake getIntake() {
        if (m_intake == null) {
            m_intake = new Intake();
        }
        return m_intake;
    }
    
    public static Wings getWings(){
        if (m_wings == null) {
            m_wings = new Wings();
        }
        return m_wings;
    }
    
    public static Wheel getWheel(){
        if (m_wheel == null) {
            m_wheel = new Wheel();
        }
        return m_wheel;
    }
    
    public static Pivot getPivot(){
        if (m_pivot == null) {
            m_pivot = new Pivot();
        }
        return m_pivot;
    }
    
    public static Winch getWinch(){
        if (m_winch == null) {
            m_winch = new Winch();
        }
        return m_winch;
    }
    
    public static Transmission getTransmission() {
        if (m_transmission == null) {
            m_transmission = new Transmission();
        }
        return m_transmission;
    }
    
     
    public static CheesyVisionServer getVisionServer() {
        return CheesyVisionServer.getInstance();
    }

    public static MotorTest getMotorTest() {
        if (m_motorTest == null) {
            m_motorTest = new MotorTest(10);
        }
        return null;
    }
    
    public static PneumaticTest getPneumaticTest() {
        if (m_pneumaticTest == null) {
            m_pneumaticTest = new PneumaticTest(6);
        }
        return null;
    }
}
