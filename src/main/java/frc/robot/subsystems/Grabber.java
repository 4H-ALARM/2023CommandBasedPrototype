// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Debug;

import static frc.robot.Constants.*;

public class Grabber extends SubsystemBase {

  private final Servo m_clawServo = new Servo(GrabberParameters.k_GrabberPWM);
  private double m_setpoint = 1;
  
  /** Creates a new Grabber. */
  public Grabber() {
  }

  public void open(){ 
   //m_setpoint = m_setpoint - 0.05;
    //if (m_setpoint < 0) {m_setpoint = 0;}
    m_clawServo.set(GrabberParameters.k_openValue);
  }

  public void close() {
    //m_setpoint = m_setpoint + 0.05;
    //if (m_setpoint > 1) {m_setpoint = 1;}
    m_clawServo.set(GrabberParameters.k_closeValueCone);
  }

//  public void stop() {
//    m_clawServo.set(0.0);
//  }

  @Override
  public void periodic() {    
    if (Debug.ArmON) {
      SmartDashboard.putNumber("Grabber", m_clawServo.get());
    }

  }

}
