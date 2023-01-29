// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import static frc.robot.Constants.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Arm extends SubsystemBase {
  private final WPI_VictorSPX m_Armmotor = new WPI_VictorSPX(CANaddresses.k_Arm);
  private final WPI_VictorSPX m_Shoulder = new WPI_VictorSPX(CANaddresses.k_Shoulder);
  
  /** Creates a new Arm. */
  public Arm () {
    
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void extend(){
    m_Armmotor.set(0.7);
  }

  public void retract() {
    m_Armmotor.set(-0.7);
  }

  public void stop() {
    m_Armmotor.set(0.0);
  }



  public void lift(){
    m_Shoulder.set(0.7);
  }

  public void lower() {
    m_Shoulder.set(-0.7);
  }

  public void stopShoulder() {
    m_Shoulder.set(0.0);
  }


}
