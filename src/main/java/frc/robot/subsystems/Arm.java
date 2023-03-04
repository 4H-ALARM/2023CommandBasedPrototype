// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.*;
import frc.robot.Constants.Debug;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class Arm extends SubsystemBase {
  private final WPI_TalonFX m_armExtender = new WPI_TalonFX(CANaddresses.k_Extender);
  private final WPI_TalonFX m_Shoulder = new WPI_TalonFX(CANaddresses.k_Shoulder);

  private final DigitalInput m_lowerLimitDetector = new DigitalInput(ArmParameters.k_lowerLimitDIO);
  private final DigitalInput m_fullRetractDetector = new DigitalInput(ArmParameters.k_fullRetractDIO);
  //TODO remove private SlewRateLimiter m_limiter = new SlewRateLimiter(ArmParameters.k_raiseLimit);

  private boolean m_atFullExtension = false;
  private boolean m_atFullRetraction = false;
  private boolean m_atFullRaise = false;
  private boolean m_atFullLower = false;
  private boolean m_safeToLower = true;  //TODO change to false when method to check for safe lower is debugged
  private boolean m_goodGrabPos = false;
  private boolean m_armRaiseZeroed = false;  //TODO implement blocking motion based on this
  private boolean m_armExtendZeroed = false; //TODO implement blocking motion based on this
  private boolean m_overrideShoulder = false;
  private boolean m_overrideExtender = false;
  
  /** Creates a new Arm. */
  public Arm () {
    m_Shoulder.setInverted(true);
    m_Shoulder.setNeutralMode(NeutralMode.Brake);
    m_Shoulder.setSensorPhase(false);

    // Note victor controller has brake set by a button on the controller
    m_armExtender.setInverted(true);
    m_armExtender.setNeutralMode(NeutralMode.Brake);
    m_armExtender.setSensorPhase(false);
  }

  @Override
  public void periodic() {
        // add these calls after sensors are added and tested
    checkExtensionRetractLimits();
    checkRaiseLowerLimits();
    //TODO add back in when debugged checkSafeToLower();
    //TODO add back in when debugged checkGrabPos();

    updateDashboard();
  }

/**
   * Moves the arm, both raising and lowering and extension/retraction at given Speeds range from [-1, 1]
   *
   * @param raiseSpeed Speed of arm raise/lower motion, Positive is lowering
   * @param extendSpeed Speed of arm extend/retract motion, Positive is retracting
   */
    public void move(double raiseSpeed, double extendSpeed) {
    double r = raiseSpeed; //m_limiter.calculate(raiseSpeed);
    double e = extendSpeed; //squareInput(extendSpeed);

    // check if shoulder raise override is active
    if (m_overrideShoulder) {
      
    } else {
      // check to see if we should stop lowering
      if (r > 0) {
        if ((m_atFullLower) || (!m_safeToLower)) { r = 0.0; }
      } else {
        // we are raising so check for stop
        if ((m_atFullRaise) || (!m_armRaiseZeroed)) { r = 0.0; }
      }
    }
    
    //check to see if arm extend override is active
    if (m_overrideExtender){

    } else {
      // check to see if we should stop retracting
      if (e > 0) {
        if (m_atFullRetraction) { e = 0.0; }
      } else {
        // we are extending so check for stop
        if ((m_atFullExtension) || (!m_armExtendZeroed)) { e = 0.0; }
    }
    }

    m_Shoulder.set(r);
    m_armExtender.set(e);
  }

  public void extend(){
    if ((!m_atFullExtension) && (m_armExtendZeroed)) {
      m_armExtender.set(ArmParameters.k_armExtendSpeed);
    } else {
      stop();
    }
  }

  public boolean retract() {
    if (!m_atFullRetraction) {
      m_armExtender.set(ArmParameters.k_armRetractSpeed);
    } else {
      stop();
    } 
    return (m_atFullRetraction);
  }

  public void fullRetract() {
    
    if (!m_atFullRetraction) {
      m_armExtender.set(ArmParameters.k_armRetractSpeed);
    }
    
    
  }

  public void fullLower() {
    if (!m_atFullLower) {
      m_Shoulder.set(ArmParameters.k_armLowerSpeed);
    }
  }

  public void extenderDeploy() {
    
    if ((!m_atFullExtension) && (m_armExtendZeroed)) {
      m_armExtender.set(ArmParameters.k_armExtendSpeed);
    }
    
    
  }

  public void shoulderDeploy() {
    if ((!m_atFullRaise) && (m_armRaiseZeroed)) {
      m_Shoulder.set(ArmParameters.k_armRaiseSpeed);
    }
  }

  public void stop() {
    m_armExtender.set(0.0);
  }

  public void lift(){
    if ((!m_atFullRaise) && (m_armRaiseZeroed)) {
      m_Shoulder.set(ArmParameters.k_armRaiseSpeed);
    } else {
      stopShoulder();
    }
  }

  public void lower() {
    if ((!m_atFullLower) && (m_safeToLower)) {
      m_Shoulder.set(ArmParameters.k_armLowerSpeed);
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

  public boolean isArmRetracted() {
    return  m_atFullRetraction;
  }

  public boolean isArmLowered() {
    return m_atFullLower;
  }

  public boolean isArmExtended() {
    return  m_atFullExtension;
  }

  public boolean isArmRaised() {
    return m_atFullRaise;
  }

  public void shoulderOverride() {
    if (m_overrideShoulder) {
      m_overrideShoulder = false;
    } else {
      m_overrideShoulder = true;
    }
  }

  public void extenderOverride() {
    if (m_overrideExtender) {
      m_overrideExtender = false;
    } else {
      m_overrideExtender = true;
    }
  }

  private void checkExtensionRetractLimits() {
    // check if we hit limit switch, have to invert reading
    if (!m_fullRetractDetector.get()) {
      m_atFullRetraction = true;
      m_armExtendZeroed = true;
      m_armExtender.setSelectedSensorPosition(0.0);
    } else {
      m_atFullRetraction = false;
    }
    
    if (m_armExtender.getSelectedSensorPosition() < ArmParameters.k_fullExtendCount) {
      m_atFullExtension = true;
    } else {
      m_atFullExtension = false;
    }
  }

  private void checkRaiseLowerLimits() {
    // check if we hit limit switch, have to invert reading
    if (!m_lowerLimitDetector.get()) {
      m_armRaiseZeroed = true;
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

  //TODO correct the limits for these checks
  private void checkSafeToLower() {
    if (m_Shoulder.getSelectedSensorPosition() < ArmParameters.k_startStowCount) {
      m_safeToLower = true;  //lowering arm is away from the chassis
    } else  {
      if (m_armExtender.getSelectedSensorPosition() > ArmParameters.k_safeExtenderStowCount) {
        m_safeToLower = true; // retracted enough to stow
      } else {
        m_safeToLower = true;  //TODO change to false after testing need to retract before allowing stow
      }
    }
    //TODO possible alternate approach using length calculation
    double h = calculateHypotenus(m_Shoulder.getSelectedSensorPosition());

  }

  private void checkGrabPos() {
    double ec = Math.abs(m_armExtender.getSelectedSensorPosition());
    double sc = m_Shoulder.getSelectedSensorPosition();

        double te = getExtensionNeededForGrab(sc);
        if ((te == 0.0) ||  
            (ec > (te + ArmParameters.k_ulExtendGoodGrab)) ||
            (ec < (te - ArmParameters.k_llExtendGoodGrab))){
              m_goodGrabPos = true;
          } else {
            m_goodGrabPos = true; //TODO change to false when debugged
          }

        if (Debug.ArmON) {
          SmartDashboard.putNumber("TarExt", te);
        }
  }

/**
   * Returns the nominal extension count needed for a succesful grab
   *
   * @param shoulderCount current shoulder position
   * @return extendCount nominal extension count, always positive an should have error margin applied, 0 if none
   */
  private double getExtensionNeededForGrab(double shoulderCount) {
    double ec = calculateHypotenus(shoulderCount);
      // remove the fixed length of the arm from the target extension
    if (ec != 0.0 ) {
      ec = ec - ArmParameters.k_shortestArmLength;
    }    
    // if reach is too far return 0
    if (ec > ArmParameters.k_safeReach) { ec = 0.0; }

    return (ec);
  }

  private double calculateHypotenus(double shoulderCount) {
    double h = 0.0;
    double angleInRadians = Math.abs(shoulderCount) * ArmParameters.k_armRadianPerCount;
    double cosAngle = Math.cos(angleInRadians);

    if (cosAngle != 0) {
      h = ArmParameters.k_armHeight/cosAngle;
    }
    if (Debug.ArmON) {
      SmartDashboard.putNumber("Hyp", h);
      SmartDashboard.putNumber("Angle", Math.toDegrees(angleInRadians));
      SmartDashboard.putNumber("Cos", cosAngle);
    }
    return(h);
  }

  private void updateDashboard() {
    SmartDashboard.putBoolean("Full Raise", m_atFullRaise);
    SmartDashboard.putBoolean("Full Lower", m_atFullLower);
    SmartDashboard.putBoolean("Full Extend", m_atFullExtension);
    SmartDashboard.putBoolean("Full Retract", m_atFullRetraction);
    SmartDashboard.putBoolean("Shoulder Override", m_overrideShoulder);
    SmartDashboard.putBoolean("Extender Override", m_overrideExtender);

    if (Debug.ArmON) {
      SmartDashboard.putBoolean("Safe-Lower", m_safeToLower);
      SmartDashboard.putBoolean("Good Grab", m_goodGrabPos);
      SmartDashboard.putNumber("ShoulderSp", m_Shoulder.get());
      SmartDashboard.putNumber("ShoulCnt", m_Shoulder.getSelectedSensorPosition());
      SmartDashboard.putNumber("ExtSp", m_armExtender.get());
      SmartDashboard.putNumber("ExtCnt", m_armExtender.getSelectedSensorPosition());
    }
  }


}
