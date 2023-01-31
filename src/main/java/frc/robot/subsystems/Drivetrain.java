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

import static frc.robot.Constants.*;

public class Drivetrain extends SubsystemBase {
  private final WPI_TalonFX m_frontLeft = new WPI_TalonFX(CANaddresses.k_FrontLeftMotor);
  private final WPI_TalonFX m_rearLeft = new WPI_TalonFX(CANaddresses.k_RearLeftMotor);
  private final WPI_TalonFX m_frontRight = new WPI_TalonFX(CANaddresses.k_FrontRightMotor);
  private final WPI_TalonFX m_rearRight = new WPI_TalonFX(CANaddresses.k_RearRight);

  private final MecanumDrive m_drive =
      new MecanumDrive(m_frontLeft, m_rearLeft, m_frontRight, m_rearRight);

  private final ADXRS450_Gyro m_gyro = new ADXRS450_Gyro();
  private double m_heading = 0.0;
  private double m_turnRate = 0.0;
  private Rotation2d m_rotation;

  ShuffleboardTab tab = Shuffleboard.getTab("Tab Title");

  /** Creates a new Drivetrain. */
  public Drivetrain() {
    resetHeading();
    m_rotation = m_gyro.getRotation2d();

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
   * @param fieldRelative Whether the provided x and y speeds are relative to the field.
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {

    double x_squared = squareInput(xSpeed);
    double y_squared = squareInput(ySpeed);
    double rot_squared = squareInput(rot);

    if (xSpeed < 0){
      x_squared = -1 * xSpeed * xSpeed;
    }
    else {
      x_squared = xSpeed * xSpeed;
    }

    if (ySpeed < 0){
      y_squared = -1 * ySpeed * ySpeed;
    }
    else {
      y_squared = ySpeed * ySpeed;
    }

    if (rot < 0){
      rot_squared = -1 * rot * rot;
    }
    else {
      rot_squared = rot * rot;
    }

    if (fieldRelative) {
      m_drive.driveCartesian(xSpeed, ySpeed, rot, m_rotation);
    } else {
      m_drive.driveCartesian(x_squared, y_squared, rot_squared);
    }
    
  }

  public void polarDrive(double magnitude, Rotation2d direction, double spin) {
    m_drive.drivePolar(magnitude, direction, spin);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    m_heading = m_gyro.getAngle();
    m_rotation = m_gyro.getRotation2d();
    m_turnRate = m_gyro.getRate();

    updateDashboard();
  }

  public void resetHeading(){
    m_gyro.calibrate();
    m_gyro.reset();
  }

  public void rotate() {
    drive(0.0, 0.0, 0.3, false);
  }

  public void stop() {
    drive(0.0, 0.0, 0.0, false);
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

  private double squareInput(double i) {
    double s = i*i;
    if (i < 0){ s = -1 * s; }
    return(s);
  }

  private void updateDashboard() {
    double modulo = m_heading%360.0;
    if (modulo < 0) {modulo = 360.0 - modulo;}
    SmartDashboard.putNumber("Gyro Heading", modulo);
    SmartDashboard.putNumber("Gyro Rate", m_turnRate);
    SmartDashboard.putNumber("FLMC",encoderToDistance(m_frontLeft.getSelectedSensorPosition()));
    SmartDashboard.putNumber("FRMC",encoderToDistance(m_frontRight.getSelectedSensorPosition()));
    SmartDashboard.putNumber("RLMC",encoderToDistance(m_rearLeft.getSelectedSensorPosition()));
    SmartDashboard.putNumber("RRMC",encoderToDistance(m_rearRight.getSelectedSensorPosition()));

  }
  
}
