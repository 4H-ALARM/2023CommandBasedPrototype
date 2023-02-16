// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import static frc.robot.Constants.*;

import frc.robot.Constants.Debug;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Arm extends SubsystemBase {
  private final WPI_VictorSPX m_armExtender = new WPI_VictorSPX(CANaddresses.k_Extender);
  private final WPI_TalonFX m_Shoulder = new WPI_TalonFX(CANaddresses.k_Shoulder);

  private final DigitalInput m_lowerLimitDetector = new DigitalInput(ArmParameters.k_lowerLimitDIO);
  private final DigitalInput m_fullRetractDetector = new DigitalInput(ArmParameters.k_fullRetractDIO);
  private SlewRateLimiter m_limiter = new SlewRateLimiter(ArmParameters.k_raiseLimit);

  private final Encoder m_extEnc = new Encoder(ArmParameters.k_extEncADIO, ArmParameters.k_extEncBDIO);

  private boolean m_atFullExtension = false;
  private boolean m_atFullRetraction = false;
  private boolean m_atFullRaise = false;
  private boolean m_atFullLower = false;
  
  /** Creates a new Arm. */
  public Arm () {
    m_Shoulder.setInverted(true);
    m_Shoulder.setNeutralMode(NeutralMode.Brake);
    m_Shoulder.setSensorPhase(false);

    // Note victor controller has brake set by a button on the controller
    m_armExtender.setInverted(false);

    m_extEnc.setDistancePerPulse(1.0);
    m_extEnc.setMinRate(10.0);
    m_extEnc.setReverseDirection(false);
    m_extEnc.setSamplesToAverage(4);
  }

  @Override
  public void periodic() {
        // add these calls after sensors are added and tested
    checkExtensionRetractLimits();
    checkRaiseLowerLimits();

    updateDashboard();
  }

/**
   * Moves the arm, both raising and lowering and extension/retraction at given Speeds range from [-1, 1]
   *
   * @param raiseSpeed Speed of arm raise/lower motion, Positive is lowering
   * @param extendSpeed Speed of arm extend/retract motion, Positive is retracting
   */
    public void move(double raiseSpeed, double extendSpeed) {
    double r = m_limiter.calculate(raiseSpeed);//(squareInput(raiseSpeed))*0.5;
    double e = squareInput(extendSpeed);

    // check to see if we should stop lowering
    if (r > 0) {
      if (m_atFullLower) { r = 0.0; }
    } else {
      // we are raising so check for stop
      if (m_atFullRaise) { r = 0.0; }
    }

    // check to see if we should stop retracting
    if (e > 0) {
      if (m_atFullRetraction) { e = 0.0; }
    } else {
      // we are extending so check for stop
      if (m_atFullExtension) { e = 0.0; }
    }

    m_Shoulder.set(r);
    m_armExtender.set(e);
  }

  public void extend(){
    if (!m_atFullExtension) {
      m_armExtender.set(ArmParameters.k_armExtendSpeed);
    } else {
      stop();
    }
  }

  public void retract() {
    if (!m_atFullRetraction) {
      m_armExtender.set(ArmParameters.k_armRetractSpeed);
    } else {
      stop();
    } 
  }

  public void stop() {
    m_armExtender.set(0.0);
  }

  public void lift(){
    if (!m_atFullRaise) {
      m_Shoulder.set(ArmParameters.k_armRaiseSpeed);
    } else {
      stopShoulder();
    }
  }

  public void lower() {
    if (!m_atFullLower) {
      m_Shoulder.set(ArmParameters.k_armRetractSpeed);
    } else {
      stopShoulder();
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

  private void checkExtensionRetractLimits() {
    // check if we hit limit switch, have to invert reading
    if (!m_fullRetractDetector.get()) {
      m_atFullRetraction = true;
      m_extEnc.reset();
    } else {
      m_atFullRetraction = false;
    }
    
    if (m_extEnc.get() > ArmParameters.k_fullExtendCount) {
      m_atFullExtension = true;
    } else {
      m_atFullExtension = false;
    }
  }

  private void checkRaiseLowerLimits() {
    // check if we hit limit switch, have to invert reading
    if (!m_lowerLimitDetector.get()) {
      m_atFullLower = true;
      m_Shoulder.setSelectedSensorPosition(0.0);
    } else {
      m_atFullLower = false;
    }
    // count in raise direction is negative    
    if (m_Shoulder.getSelectedSensorPosition() < ArmParameters.k_fullRaiseCount) {
      m_atFullRaise = true;
    } else {
      m_atFullRaise = false;
    }
  }

  private void updateDashboard() {
    SmartDashboard.putBoolean("Full Raise", m_atFullRaise);
    SmartDashboard.putBoolean("Full Lower", m_atFullLower);
    SmartDashboard.putBoolean("Full Extend", m_atFullExtension);
    SmartDashboard.putBoolean("Full Retract", m_atFullRetraction);

    if (Debug.ArmON) {
      SmartDashboard.putNumber("ShoulderSp", m_Shoulder.get());
      SmartDashboard.putNumber("ShoulCnt", m_Shoulder.getSelectedSensorPosition());
      SmartDashboard.putNumber("ExtSp", m_armExtender.get());
      SmartDashboard.putNumber("ExtCnt", m_extEnc.get());
    }
  }


}
