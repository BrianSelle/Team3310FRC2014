package edu.rhhs.frc;

import edu.rhhs.frc.controller.XboxController;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    
    // USB Port IDs
    public static final int JOYSTICK_1_USB_ID = 1;
    public static final int JOYSTICK_2_USB_ID = 2;
    public static final int XBOX_USB_ID = 3;
    public static final int STEERING_WHEEL_USB_ID = 4;

    // Joystick 1 Buttons
    public static final int JOYSTICK_1_SHIFT_BUTTON = 1;
    public static final int JOYSTICK_1_PIVOT_LOCK_EXTEND_BUTTON = 5;
    public static final int JOYSTICK_1_PIVOT_LOCK_RETRACT_BUTTON = 4;
    public static final int JOYSTICK_1_PIVOT_FORWARD_BUTTON = 3;
    public static final int JOYSTICK_1_PIVOT_REVERSE_BUTTON = 2;
    
    // Joystick 2 Buttons
    public static final int JOYSTICK_2_WHEEL_BUTTON = 1;
  
    // XBox Controller Buttons
    public static final int XBOX_SHOOTER_PITCH_ANGLE_LOAD_BUTTON = XboxController.B_BUTTON;
    public static final int XBOX_SHOOTER_PITCH_ANGLE_HALF_COURT_BUTTON = XboxController.Y_BUTTON;
    public static final int XBOX_SHOOTER_PITCH_ANGLE_PYRAMID_BACK_BUTTON = XboxController.A_BUTTON;
    public static final int XBOX_SHOOTER_INTAKE_LOAD_FOUR_BUTTON = XboxController.RIGHT_BUMPER_BUTTON;
    public static final int XBOX_SHOOTER_INTAKE_EJECT_BUTTON = XboxController.LEFT_BUMPER_BUTTON;
    public static final int XBOX_LIGHTS_GOLD_BUTTON = XboxController.Y_BUTTON;
    public static final int XBOX_LIGHTS_OFF_BUTTON = XboxController.X_BUTTON;

    // DSC PWM Port IDs 
    public static final int DRIVE_LEFT_TOP_FRONT_DSC_PWM_ID = 2;    //split PWM
    public static final int DRIVE_LEFT_REAR_DSC_PWM_ID = 3;
    public static final int DRIVE_RIGHT_TOP_FRONT_DSC_PWM_ID = 5;   //split PWM
    public static final int DRIVE_RIGHT_REAR_DSC_PWM_ID = 6;
    public static final int INTAKE_TOP_DSC_PWM_ID = 7;
    public static final int INTAKE_BOTTOM_DSC_PWM_ID = 8;
    public static final int WINCH_DSC_PWM_ID = 9;                   //split PWM   // Port 1 practive, port 9 comp
    public static final int PIVOT_DSC_PWM_ID = 10;                  //split PWM
    
    // DSC Digial IO Port IDs 
    public static final int RIGHT_DRIVE_ENCODER_A_DSC_DIO_ID = 1;
    public static final int RIGHT_DRIVE_ENCODER_B_DSC_DIO_ID = 2;
    public static final int LEFT_DRIVE_ENCODER_A_DSC_DIO_ID = 3;
    public static final int LEFT_DRIVE_ENCODER_B_DSC_DIO_ID = 4;
    public static final int WINCH_ENCODER_A_DSC_DIO_ID = 5;
    public static final int WINCH_ENCODER_B_DSC_DIO_ID = 6;
    public static final int PIVOT_ENCODER_A_DSC_DIO_ID = 7;
    public static final int PIVOT_ENCODER_B_DSC_DIO_ID = 8; 
    public static final int BALL_INTAKE_1_DSC_DIO_ID = 9;
    public static final int DRAW_ZERO_SWITCH_DSC_DIO_ID = 10;
    public static final int WINCH_SWITCH_1_DSC_DIO_ID = 11;
    public static final int WINCH_SWITCH_2_DSC_DIO_ID = 12;
    public static final int PIVOT_LOCK_SWITCH_DSC_DIO_ID = 13;
    public static final int COMPRESSOR_SWITCH_DSC_DIO_ID = 14;  

    // Solenoid Breakout Relay Port IDs
    public static final int SHIFT_EXTEND_PNEUMATIC_MODULE_ID = 1;
    public static final int SHIFT_EXTEND_PNEUMATIC_RELAY_ID = 1;
    
    public static final int SHIFT_RETRACT_PNEUMATIC_MODULE_ID = 1;
    public static final int SHIFT_RETRACT_PNEUMATIC_RELAY_ID = 2;
    
    public static final int BRAKE_EXTEND_PNEUMATIC_MODULE_ID = 1;
    public static final int BRAKE_EXTEND_PNEUMATIC_RELAY_ID = 3;
    
    public static final int BRAKE_RETRACT_PNEUMATIC_MODULE_ID = 1;
    public static final int BRAKE_RETRACT_PNEUMATIC_RELAY_ID = 4;
    
    public static final int WINCH_EXTEND_PNEUMATIC_MODULE_ID = 1;
    public static final int WINCH_EXTEND_PNEUMATIC_RELAY_ID = 5;
    
    public static final int WINCH_RETRACT_PNEUMATIC_MODULE_ID = 1;
    public static final int WINCH_RETRACT_PNEUMATIC_RELAY_ID = 6;
    
    public static final int PIVOT_LOCK_EXTEND_PNEUMATIC_MODULE_ID = 1;
    public static final int PIVOT_LOCK_EXTEND_PNEUMATIC_RELAY_ID = 7;
    
    public static final int PIVOT_LOCK_RETRACT_PNEUMATIC_MODULE_ID = 1;
    public static final int PIVOT_LOCK_RETRACT_PNEUMATIC_RELAY_ID = 8;
    
    public static final int INTAKE_EXTEND_PNEUMATIC_MODULE_ID = 2;
    public static final int INTAKE_EXTEND_PNEUMATIC_RELAY_ID = 1;
    
    public static final int INTAKE_RETRACT_PNEUMATIC_MODULE_ID = 2;
    public static final int INTAKE_RETRACT_PNEUMATIC_RELAY_ID = 2;
    
    public static final int WINGS_EXTEND_PNEUMATIC_MODULE_ID = 2;
    public static final int WINGS_EXTEND_PNEUMATIC_RELAY_ID = 3;
    
    public static final int WINGS_RETRACT_PNEUMATIC_MODULE_ID = 2;
    public static final int WINGS_RETRACT_PNEUMATIC_RELAY_ID = 4;
    
    public static final int WHEEL_EXTEND_PNEUMATIC_MODULE_ID = 2;
    public static final int WHEEL_EXTEND_PNEUMATIC_RELAY_ID = 5;
    
    public static final int WHEEL_RETRACT_PNEUMATIC_MODULE_ID = 2;
    public static final int WHEEL_RETRACT_PNEUMATIC_RELAY_ID = 6;
    
    // Analog Breakout Port IDs
    public static final int CHASSIS_YAW_RATE_ANALOG_BREAKOUT_PORT = 1; 

    // DSC Relay Port IDs 
    public static final int COMPRESSOR_DSC_RELAY_ID = 1;
}
