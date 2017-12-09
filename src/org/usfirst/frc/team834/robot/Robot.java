package org.usfirst.frc.team834.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	/*
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	SendableChooser<String> chooser = new SendableChooser<>();
	*/
	RobotDrive robot;
	
	Victor driveFLeft, driveBLeft, driveFRight, driveBRight, liftMotor;
	Joystick rightStick, leftStick, xbox;
	
	Compressor comp;
	Solenoid shift, shiftRetract, leftArm, leftArmRetract, rightArm, rightArmRetract;

	Encoder lEncoder;
	DigitalInput liftSensor;
	
	boolean rightOn;
	boolean rightPressed;
	boolean leftOn;
	boolean leftPressed;

	boolean isInFirstGear;

	public Robot() {
		//robot.setExpiration(0.1);
	}

	@Override
	public void robotInit() {
		/*
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto modes", chooser);
		*/
		driveFLeft = new Victor(0);
    	driveBLeft = new Victor(1);
    	driveFRight = new Victor(2);
    	driveBRight = new Victor(3);
    	liftMotor = new Victor(5);
    	
    	rightStick = new Joystick(0);
    	leftStick = new Joystick(1);
    	xbox = new Joystick(2);
    	
    	comp = new Compressor(1);
    	
    	leftArm = new Solenoid(1,0);
    	leftArmRetract = new Solenoid(1,1);
    	rightArm = new Solenoid(1,2);
    	rightArmRetract = new Solenoid(1,3);
    	shift = new Solenoid(1,4);
    	shiftRetract = new Solenoid(1,5);
    	
    	liftSensor = new DigitalInput(0);
    	lEncoder = new Encoder(1, 2);
    	
    	rightOn = false;
    	rightPressed = false;

    	leftOn = false;
    	leftPressed = false;

    	isInFirstGear = false;
    	
    	robot = new RobotDrive(driveFLeft, driveBLeft, driveFRight, driveBRight);
    	
    	robot.setExpiration(0.1);
    	robot.setSafetyEnabled(false);
	}

	@Override
	public void operatorControl() {
		robot.setSafetyEnabled(false);
		
		while (isOperatorControl() && isEnabled()) {
			robot.setSafetyEnabled(false);
			//comp.start();
			
			robot.tankDrive(leftStick, rightStick);
			//robot.arcadeDrive(rightStick); // drive with arcade style (use right stick)
				    	
	    	// Lift Up(Xbox Y) and Down(Xbox A)
	    	if(xbox.getRawButton(4)){
	    		liftMotor.set(1.0);
	    	}
	    	else if(xbox.getRawButton(1) && liftSensor.get()){
	    		liftMotor.set(-1.0);
	    	}
	    	else{
	    		liftMotor.set(0.0);
	    	}

	    	//When the B button is pressed the right arm changes position
	    	if(xbox.getRawButton(6)) {
	    		if(!rightPressed) {
	    			rightOn = !rightOn;
	    			rightArm.set(rightOn);
	    			rightArmRetract.set(!rightOn);	
	    		}
	    		rightPressed = true;
	    	}
	    	else {
	    		rightPressed = false;
	    	}

	    	//When the X button is pressed the left arm changes position
	    	if(xbox.getRawButton(5)) {
	    		if(!leftPressed) {
	    			leftOn = !leftOn;
	    			leftArm.set(leftOn);
	    			leftArmRetract.set(!leftOn);
	    		}
	    		leftPressed = true;
	    	}
	    	else {
	    		leftPressed = false;
	    	}
	    	
	    	if(xbox.getRawButton(8)){
	    		comp.start();
	    	}
	    	
	    	if(xbox.getRawButton(7)){
	    		comp.stop();
	    	}
	    	
	    	/*
	    	//MORE SHIFTING
	    	if(rightStick.getRawButton(2)){
	    		//FIRST GEAR
	    		if(!isInFirstGear){
	    			shift.set(true);
	    			shiftRetract.set(false);
	    			isInFirstGear = true;
	    		}
	    	}
	    	//MORE SHIFTING
	    	if(rightStick.getRawButton(3)){
	    		//SECOND GEAR
	    		if(isInFirstGear){
	    			shift.set(false);
	    			shiftRetract.set(true);
	    			isInFirstGear = false;
	    		}
	    	}
	    	*/
	    	
	    	Timer.delay(0.005); // wait for a motor update time
		}
		
		while (isOperatorControl() && isDisabled()) {
			comp.stop();
		}
	}
}
