// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class Grabber extends SubsystemBase {

  private final Servo m_clawServo = new Servo(GrabberParameters.k_GrabberPWM);
  
  /** Creates a new Grabber. */
  public Grabber() {
  }

  public void open(){
    m_clawServo.setAngle(GrabberParameters.k_openAngle);
  }

  public void close() {
    m_clawServo.setAngle(GrabberParameters.k_closeAngle);
  }

//  public void stop() {
//    m_clawServo.set(0.0);
//  }

  @Override
  public void periodic() {     

  }

}
