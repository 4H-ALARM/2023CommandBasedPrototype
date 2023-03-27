// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Debug;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Grabber extends SubsystemBase {

  private final WPI_TalonSRX m_clawMotor = new WPI_TalonSRX(CANaddresses.k_claw);
  
  private double m_current = 0.0;
  
  /** Creates a new Grabber. */
  public Grabber() {
    m_clawMotor.setInverted(true);
    m_clawMotor.setNeutralMode(NeutralMode.Brake);
    m_clawMotor.configContinuousCurrentLimit(10);
    m_clawMotor.configPeakCurrentLimit(20); 
       
  }

  public void open(){ 
    m_clawMotor.set(GrabberParameters.k_openSpeed);   
  }

  public void close() {
    m_clawMotor.set(GrabberParameters.k_closeSpeed);
  }

  public void stop() {
    m_clawMotor.set(0.0);
  }

  @Override
  public void periodic() { 
    readCurrent();
    
    updateDashboard();
  }

  private void readCurrent() {
    m_current = m_clawMotor.getSupplyCurrent();
  }

  private void updateDashboard() {
    if (Debug.ArmON) {
      SmartDashboard.putNumber("Grab Current", m_current);
      SmartDashboard.putNumber("GBV", m_clawMotor.getBusVoltage());
      SmartDashboard.putNumber("GMOV", m_clawMotor.getMotorOutputVoltage());
      SmartDashboard.putNumber("GSC", m_clawMotor.getSupplyCurrent());
    }
  }

}
