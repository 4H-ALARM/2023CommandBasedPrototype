// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Debug;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Grabber extends SubsystemBase {

  private final WPI_TalonSRX m_clawMotor = new WPI_TalonSRX(CANaddresses.k_claw);
  private final Encoder m_encoder = new Encoder(GrabberParameters.k_encADIO,GrabberParameters.k_encBDIO);
  private final DigitalInput m_openDetector = new DigitalInput(GrabberParameters.k_openDetctorDIO);

  private boolean m_atFullOpen = false;
  private double m_current = 0.0;
  
  /** Creates a new Grabber. */
  public Grabber() {
    m_clawMotor.setInverted(true);
    m_clawMotor.setNeutralMode(NeutralMode.Brake);
    m_clawMotor.configContinuousCurrentLimit(1);
    m_clawMotor.configPeakCurrentLimit(1);
    m_clawMotor.enableCurrentLimit(true);    
  }

  public void open(){ 
    if (m_atFullOpen) {
      stop();
    } else {
      m_clawMotor.set(GrabberParameters.k_openSpeed);
    }    
  }

  public void close() {
    if (m_encoder.get() > GrabberParameters.k_closeCount) {
      stop();
    } else {
      m_clawMotor.set(GrabberParameters.k_closeSpeed);
    }
  }

  public void stop() {
    m_clawMotor.set(0.0);
  }

  @Override
  public void periodic() { 
    checkOpen();
    readCurrent();
    
    updateDashboard();
  }

  private void checkOpen() {
    if (m_openDetector.get()) {
      m_atFullOpen = false; // detector is inverted logic
    } else {
      m_atFullOpen = true;
      m_encoder.reset();
    }
  }

  private void readCurrent() {
    m_current = m_clawMotor.getSupplyCurrent();
  }

  private void updateDashboard() {
    SmartDashboard.putBoolean("Grabber Open", m_atFullOpen);

    if (Debug.ArmON) {
      SmartDashboard.putNumber("Grab Current", m_current);
    }
  }

}
