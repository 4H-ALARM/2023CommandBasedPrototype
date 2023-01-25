// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Pulley extends SubsystemBase {
  /** Creates a new Pulley. */

  private final WPI_VictorSPX m_pulleymotor = new WPI_VictorSPX(CANaddresses.k_Grabber);
  
  /** Creates a new Grabber. */
  public Pulley() {
    m_pulleymotor.setInverted(false);
  }

  public void open(){
    m_pulleymotor.set(0.8);
  }

  public void close() {
    m_pulleymotor.set(-0.9);
  }

  public void stop() {
    m_pulleymotor.set(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public CommandBase openCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          open();
        });
  }

  public CommandBase stopCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          stop();
        });
  }
}
