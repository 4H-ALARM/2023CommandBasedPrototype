// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import static frc.robot.Constants.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Arm extends SubsystemBase {
  private final WPI_VictorSPX m_Armmotor = new WPI_VictorSPX(CANaddresses.k_Arm);
  
  /** Creates a new Arm. */
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void open(){
    m_Armmotor.set(0.7);
  }

  public void close() {
    m_Armmotor.set(-0.7);
  }

  public void stop() {
    m_Armmotor.set(0.0);
  }


  /** Creates a new Arm. */
  public Arm() {}

}
