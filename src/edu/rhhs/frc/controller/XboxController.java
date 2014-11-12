/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.controller;

import edu.wpi.first.wpilibj.Joystick;

//TODO Code support for Start/Back/XBox(?) buttons.
/**
 * This is a wrapper for the WPILib Joystick class that represents an XBox 
 * controller.
 * @author frc1675
 */
public class XboxController{
    public static final int LEFT_X_AXIS = 1;
    public static final int LEFT_Y_AXIS = 2;
    public static final int TRIGGER_AXIS = 3;
    public static final int RIGHT_X_AXIS = 4;
    public static final int RIGHT_Y_AXIS = 5;
    public static final int LEFT_RIGHT_DPAD_AXIS = 6;
//    public static final int TOP_BOTTOM_DPAD_AXIS = 7;    // Can't use this axis... the Driver Station only transmits 6 axes.
    
    public static final int A_BUTTON = 1;
    public static final int B_BUTTON = 2;
    public static final int X_BUTTON = 3;
    public static final int Y_BUTTON = 4;
    public static final int LEFT_BUMPER_BUTTON = 5;
    public static final int RIGHT_BUMPER_BUTTON = 6;
    public static final int BACK_BUTTON = 7;
    public static final int START_BUTTON = 8;
    
    public static final int LEFT_JOYSTICK_BUTTON = 9;
    public static final int RIGHT_JOYSTICK_BUTTON = 10;
    
    private static final double LEFT_DPAD_TOLERANCE = -0.9;
    private static final double RIGHT_DPAD_TOLERANCE = 0.9;
//    private static final double BOTTOM_DPAD_TOLERANCE = -0.9;
//    private static final double TOP_DPAD_TOLERANCE = 0.9;
    
    private static final double LEFT_TRIGGER_TOLERANCE = 0.9;
    private static final double RIGHT_TRIGGER_TOLERANCE =-0.9;
    
    private static final double RIGHT_AXIS_UP_TOLERANCE = -0.9;
    private static final double RIGHT_AXIS_DOWN_TOLERANCE = 0.9;
    private static final double RIGHT_AXIS_RIGHT_TOLERANCE = 0.9;
    private static final double RIGHT_AXIS_LEFT_TOLERANCE = -0.9;
   
    private static final double LEFT_AXIS_UP_TOLERANCE = -0.9;
    private static final double LEFT_AXIS_DOWN_TOLERANCE = 0.9;
    private static final double LEFT_AXIS_RIGHT_TOLERANCE = 0.9;
    private static final double LEFT_AXIS_LEFT_TOLERANCE = -0.9;
   
    private static final double DEADZONE = 0.1;
    
    private Joystick stick;
    
    public XboxController(int portNumber){
        stick = new Joystick(portNumber);
    }
    
    public Joystick getJoyStick() {
        return stick;
    }
    
    private boolean inDeadZone(double input){
        boolean inDeadZone; 
        if(Math.abs(input) < DEADZONE){
            inDeadZone = true;
        }else{
            inDeadZone = false;
        }
        return inDeadZone;
    }
    
    private double getAxisWithDeadZoneCheck(double input){
       if(inDeadZone(input)){
            input = 0.0;       
        }
        return input; 
    }
    
    public boolean getAButton(){
        return stick.getRawButton(A_BUTTON);
    }
    
    public boolean getXButton(){
        return stick.getRawButton(X_BUTTON);    
    }
    
    public boolean getBButton(){
         return stick.getRawButton(B_BUTTON);
    }
    
    public boolean getYButton(){
        return stick.getRawButton(Y_BUTTON);
    } 
    
    public boolean getBackButton(){
         return stick.getRawButton(BACK_BUTTON);
    }
    
    public boolean getStartButton(){
        return stick.getRawButton(START_BUTTON);
    } 
    
    public boolean getLeftBumperButton(){
        return stick.getRawButton(LEFT_BUMPER_BUTTON);
    }
    
    public boolean getRightBumperButton(){
        return stick.getRawButton(RIGHT_BUMPER_BUTTON);
    }
    
    public boolean getLeftJoystickButton(){
        return stick.getRawButton(LEFT_JOYSTICK_BUTTON);
    }
    
    public boolean getRightJoystickButton(){
        return stick.getRawButton(RIGHT_JOYSTICK_BUTTON);
    }
    
    public double getLeftXAxis(){         
        return getAxisWithDeadZoneCheck(stick.getRawAxis(LEFT_X_AXIS)); 
    }
    
    public double getLeftYAxis(){
        return getAxisWithDeadZoneCheck(stick.getRawAxis(LEFT_Y_AXIS));
    }
    
    public double getRightXAxis(){
       return getAxisWithDeadZoneCheck(stick.getRawAxis(RIGHT_X_AXIS));
    }
    
    public double getRightYAxis(){
        return getAxisWithDeadZoneCheck(stick.getRawAxis(RIGHT_Y_AXIS)); 
    }
    
    private double getTriggerAxis(){
        return getAxisWithDeadZoneCheck(stick.getRawAxis(TRIGGER_AXIS)); 
    }
    
    public boolean getDPadLeft(){
        return (stick.getRawAxis(LEFT_RIGHT_DPAD_AXIS) < LEFT_DPAD_TOLERANCE);
    }
            
    public boolean getDPadRight(){
        return (stick.getRawAxis(LEFT_RIGHT_DPAD_AXIS) > RIGHT_DPAD_TOLERANCE);
    }
    
//    public boolean getDPadTop(){
//        return (stick.getRawAxis(TOP_BOTTOM_DPAD_AXIS) < TOP_DPAD_TOLERANCE);
//    }
//            
//    public boolean getDPadBottom(){
//        return (stick.getRawAxis(TOP_BOTTOM_DPAD_AXIS) > BOTTOM_DPAD_TOLERANCE);
//    }
//    
    public boolean getLeftTrigger(){
        return (getTriggerAxis() > LEFT_TRIGGER_TOLERANCE);
    }
    
    public boolean getRightTrigger(){
        return (getTriggerAxis() > RIGHT_TRIGGER_TOLERANCE);
    }   
    
    public boolean getRightAxisUpTrigger(){
        return (getRightYAxis() < RIGHT_AXIS_UP_TOLERANCE);
    }   
    
    public boolean getRightAxisDownTrigger(){
        return (getRightYAxis() > RIGHT_AXIS_DOWN_TOLERANCE);
    }   
    
    public boolean getRightAxisLeftTrigger(){
        return (getRightXAxis() > RIGHT_AXIS_LEFT_TOLERANCE);
    }   
    
    public boolean getRightAxisRightTrigger(){
        return (getRightXAxis() > RIGHT_AXIS_RIGHT_TOLERANCE);
    }   
    
    public boolean getLeftAxisUpTrigger(){
        return (getLeftYAxis() < LEFT_AXIS_UP_TOLERANCE);
    }   
    
    public boolean getLeftAxisDownTrigger(){
        return (getLeftYAxis() > LEFT_AXIS_DOWN_TOLERANCE);
    }   
    
    public boolean getLeftAxisLeftTrigger(){
        return (getLeftXAxis() > LEFT_AXIS_LEFT_TOLERANCE);
    }   
    
    public boolean getLeftAxisRightTrigger(){
        return (getLeftXAxis() > LEFT_AXIS_RIGHT_TOLERANCE);
    }   
    
}