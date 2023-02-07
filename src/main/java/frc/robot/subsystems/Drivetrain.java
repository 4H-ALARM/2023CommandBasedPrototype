// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.WPI_Pigeon2;
import com.ctre.phoenix.ErrorCode;

import static frc.robot.Constants.*;

public class Drivetrain extends SubsystemBase {
  private final WPI_TalonFX m_frontLeft = new WPI_TalonFX(CANaddresses.k_FrontLeftMotor);
  private final WPI_TalonFX m_rearLeft = new WPI_TalonFX(CANaddresses.k_RearLeftMotor);
  private final WPI_TalonFX m_frontRight = new WPI_TalonFX(CANaddresses.k_FrontRightMotor);
  private final WPI_TalonFX m_rearRight = new WPI_TalonFX(CANaddresses.k_RearRight);

  private final MecanumDrive m_drive =
      new MecanumDrive(m_frontLeft, m_rearLeft, m_frontRight, m_rearRight);

  private double m_y = 0.0;
  private double m_x = 0.0;
  private double m_r = 0.0;

  private final ADXRS450_Gyro m_gyro = new ADXRS450_Gyro();
  private double m_heading = 0.0;
  private double m_driverHeading = 0.0;
  private double m_turnRate = 0.0;
  private Rotation2d m_rotation;
  private boolean m_fieldRelative = false;
  private boolean m_driverSetHeading = false;
  private boolean m_autoMaintainHeading = true;
  private double m_AHCF = DriveParameters.k_RotationFactor;

  private final WPI_Pigeon2 m_pidgeon = new WPI_Pigeon2(30);
  private double m_yaw = 0.0;
  private double[]  m_gravityVector = new double[3];
  private ErrorCode m_gravityError = ErrorCode.OK;

  ShuffleboardTab tab = Shuffleboard.getTab("Tab Title");

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    m_gyro.calibrate();
    m_pidgeon.calibrate();
    resetHeading();
    m_heading = m_pidgeon.getAngle();
    m_driverHeading = m_pidgeon.getAngle();
    m_rotation = m_pidgeon.getRotation2d();

    initMotor(m_frontLeft, true);
    initMotor(m_rearLeft, true);
    initMotor(m_frontRight, false);
    initMotor(m_rearRight, false);
  }

  /**
   * Drives the robot at given x, y and theta speeds. Speeds range from [-1, 1] and the linear
   * speeds have no effect on the angular speed.
   *
   * @param xSpeed Speed of the robot in the x direction (forward/backwards).
   * @param ySpeed Speed of the robot in the y direction (sideways).
   * @param rot Angular rate of the robot.
   */
  public void drive(double xSpeed, double ySpeed, double rot) {
    double r = 0.0;
    double x_squared = squareInput(xSpeed);
    double y_squared = squareInput(ySpeed);
    double rot_squared = squareInput(rot);

    if ((rot < -DriveParameters.k_minRotInput) || 
        (rot > DriveParameters.k_minRotInput) ||
        (!m_autoMaintainHeading)) {
      // If driver is providing rotation input - use it
      m_driverSetHeading = true;
      m_driverHeading = m_pidgeon.getAngle();
      r = rot_squared;
    } else {
      // apply correction to maintain last heading
      if (m_driverSetHeading) {
        // if last pass driver set rotation, get the latest heading
        m_driverHeading = m_pidgeon.getAngle();
      }
      m_driverSetHeading = false;
      r = rotationCorrection();
    }

    m_y = y_squared;
    m_x = x_squared;
    m_r = r;

    if (m_fieldRelative) {
      m_drive.driveCartesian(x_squared, y_squared, r, m_rotation);
    } else {
      m_drive.driveCartesian(x_squared, y_squared, r);
    }
    
  }

  public void polarDrive(double magnitude, Rotation2d direction, double spin) {
    m_drive.drivePolar(magnitude, direction, spin);
  }

  /** getMotorDistances
   * @return array of motor distances, FL, FR, RL, RR
   */
  public double[] getMotorDistances () {
    double[] ms = new double[4];

    ms[0] = encoderToDistance(m_frontLeft.getSelectedSensorPosition());
    ms[1] = encoderToDistance(m_frontRight.getSelectedSensorPosition());
    ms[2] = encoderToDistance(m_rearLeft.getSelectedSensorPosition());
    ms[3] = encoderToDistance(m_rearRight.getSelectedSensorPosition());

    return(ms);
  }

  public void switchPerspective (){
    m_fieldRelative = !m_fieldRelative;
  }

  public void switchHeadingAutoMaintain () {
    m_autoMaintainHeading = !m_autoMaintainHeading;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    m_heading = m_pidgeon.getAngle();
    m_rotation = m_pidgeon.getRotation2d();
    m_turnRate = m_pidgeon.getRate();
    m_yaw = m_pidgeon.getYaw();
    m_gravityError = m_pidgeon.getGravityVector(m_gravityVector);

    updateDashboard();
    m_AHCF = SmartDashboard.getNumber("AHCF", DriveParameters.k_RotationFactor);
  }

  public void resetHeading(){
    m_gyro.reset();
    m_pidgeon.reset();
  }

  public void rotate() {
    drive(0.0, 0.0, 0.3);
  }

  public void stop() {
    drive(0.0, 0.0, 0.0);
  }

  public double getHeading() {
    return(m_heading);
  }

  private void initMotor(WPI_TalonFX m, boolean invert) {
      m.setInverted(invert);
      m.setNeutralMode(NeutralMode.Brake);
      m.setSelectedSensorPosition(0);
  }

  private double encoderToDistance(double encoderCount ) {
    double d = java.lang.Math.round(encoderCount /2048 *0.06 * Math.PI);
    return (d);
  }

  private double rotationCorrection() {
    double c = (m_heading - m_driverHeading)*m_AHCF;

    if ((c > -DriveParameters.k_minRotInput) && (c < DriveParameters.k_minRotInput)) {
      // correction is too small, stop hunting by setting to 0
      c = 0.0;
    }

    // Don't allow corrections too large
    if (c < -DriveParameters.k_RotationMaxCorrection) {
      c = -DriveParameters.k_RotationMaxCorrection;
    } 
    if (c > DriveParameters.k_minRotInput) {
      c = DriveParameters.k_RotationMaxCorrection;
    }    

    return (c);
  }

  private double squareInput(double i) {
    double s = i*i;
    if (i < 0){ s = -s; }
    return(s);
  }

  private void updateDashboard() {

    double modulo = m_heading%360.0;
    if (modulo < 0) {modulo = 360.0 - java.lang.Math.abs(modulo);}
    SmartDashboard.putNumber("Gyro Heading", modulo);
    SmartDashboard.putNumber("Gyro Rate", m_turnRate);
    SmartDashboard.putNumber("Yaw", m_yaw);
    SmartDashboard.putNumber("GravityX",m_gravityVector[0]);
    SmartDashboard.putNumber("GravityY",m_gravityVector[1]);
    SmartDashboard.putNumber("GravityZ",m_gravityVector[2]);
    SmartDashboard.putNumber("FLMC",encoderToDistance(m_frontLeft.getSelectedSensorPosition()));
    SmartDashboard.putNumber("FRMC",encoderToDistance(m_frontRight.getSelectedSensorPosition()));
    SmartDashboard.putNumber("RLMC",encoderToDistance(m_rearLeft.getSelectedSensorPosition()));
    SmartDashboard.putNumber("RRMC",encoderToDistance(m_rearRight.getSelectedSensorPosition()));
    SmartDashboard.putNumber("FLMCv",encoderToDistance(m_frontLeft.getSelectedSensorVelocity()));
    SmartDashboard.putNumber("FRMCv",encoderToDistance(m_frontRight.getSelectedSensorVelocity()));
    SmartDashboard.putNumber("RLMCv",encoderToDistance(m_rearLeft.getSelectedSensorVelocity()));
    SmartDashboard.putNumber("RRMCv",encoderToDistance(m_rearRight.getSelectedSensorVelocity()));
    SmartDashboard.putBoolean("Perspective",m_fieldRelative);
    SmartDashboard.putBoolean("Maintain Heading",m_autoMaintainHeading);
    SmartDashboard.putNumber("Y In", m_y);
    SmartDashboard.putNumber("X In", m_x);
    SmartDashboard.putNumber("R In", m_r);
    SmartDashboard.putNumber("FLMCs",m_frontLeft.get());
    SmartDashboard.putNumber("FRMCs",m_frontRight.get());
    SmartDashboard.putNumber("RLMCs",m_rearLeft.get());
    SmartDashboard.putNumber("RRMCs",m_rearRight.get());
  }
  
}
