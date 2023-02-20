// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.hal.PWMConfigDataResult;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Debug;

import static frc.robot.Constants.*;

public class Grabber extends SubsystemBase {

  private final Servo m_clawServo = new Servo(GrabberParameters.k_GrabberPWM);
  
  /** Creates a new Grabber. */
  public Grabber() {
    
  }

  public void open(){ 
    m_clawServo.set(GrabberParameters.k_openValue);
  }

  public void close() {
    m_clawServo.set(GrabberParameters.k_closeValueCone);
  }

  @Override
  public void periodic() {  
    PWMConfigDataResult i = m_clawServo.getRawBounds();  
    if (Debug.ArmON) {
      SmartDashboard.putNumber("Grabber", m_clawServo.get());
    }

  }

}
