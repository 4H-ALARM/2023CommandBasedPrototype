// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import static frc.robot.Constants.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class Arm extends SubsystemBase {
  private final WPI_TalonFX m_armExtender = new WPI_TalonFX(CANaddresses.k_Arm);
  private final WPI_TalonSRX m_Shoulder = new WPI_TalonSRX(CANaddresses.k_Shoulder);

  private boolean m_atFullExtension = false;
  private boolean m_atFullRetraction = false;
  private boolean m_atFullRaise = false;
  private boolean m_atFullLower = false;
  
  /** Creates a new Arm. */
  public Arm () {
    m_Shoulder.setInverted(false);
    m_Shoulder.setNeutralMode(NeutralMode.Brake);
    m_armExtender.setInverted(false);
    m_armExtender.setNeutralMode(NeutralMode.Brake);
    
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    checkExtension();
    checkRaise();
  }

  public void move(double raiseSpeed, double extendSpeed) {
    double r = squareInput(raiseSpeed);
    double e = squareInput(extendSpeed);

    // check to see if we should stop retracting
    if (e > 0) {
      if (m_atFullRetraction) { e = 0.0; }
    } else {
      // we are extending so check for stop
      if (m_atFullExtension) { e = 0.0; }
    }

    // check to see if we should stop lowering
    if (r > 0) {
      if (m_atFullLower) { r = 0.0; }
    } else {
      // we are raising so check for stop
      if (m_atFullRaise) { r = 0.0; }
    }

    m_Shoulder.set(r);
    m_armExtender.set(e);
  }

  public void extend(){
    if (!m_atFullExtension) {
      m_armExtender.set(ArmParameters.k_armExtendSpeed);
    }    
  }

  public void retract() {
    if (!m_atFullRetraction) {
      m_armExtender.set(ArmParameters.k_armRetractSpeed);
    }    
  }

  public void stop() {
    m_armExtender.set(0.0);
  }

  public void lift(){
    if (!m_atFullRaise) {
      m_Shoulder.set(ArmParameters.k_armRaiseSpeed);
    }    
  }

  public void lower() {
    if (!m_atFullLower) {
      m_Shoulder.set(ArmParameters.k_armRetractSpeed);
    }
  }

  public void stopShoulder() {
    m_Shoulder.set(0.0);
  }

  private double squareInput(double i) {
    double s = i*i;
    if (i < 0){ s = -s; }
    return(s);
  }

  private void checkExtension() {
    // assume we a way to see if fully extended or rettracted
  }

  private void checkRaise() {
    // assume we a way to see if fully extended or rettracted
  }


}
