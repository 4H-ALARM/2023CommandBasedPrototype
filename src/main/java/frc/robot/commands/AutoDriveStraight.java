// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.geometry.Rotation2d;

import frc.robot.subsystems.Drivetrain;

public class AutoDriveStraight extends CommandBase {
  private Drivetrain m_dt;
  private double m_speed = 0.3;
  private double m_startPoint = 0.0;
  private double m_UL = 0.0;
  private double m_LL = 0.0;
  private double m_targetDistance = 15.0;
  private Rotation2d m_angle;


  /** Creates a new DriveAtAngleForDistance. 
   * @param drivetrain susbsystem
   * @param speed range 0 - 1
   * @param a angle in radians
   * @param td target distance (always positive arbitrary units)
  */
  public AutoDriveStraight(Drivetrain dt) {
    this.m_dt = dt;

    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(dt);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double[] md = m_dt.getMotorDistances();
    this.m_startPoint = md[0]; /*use FL as our distance start */
    this.m_UL = java.lang.Math.abs(this.m_startPoint) + this.m_targetDistance;
    this.m_LL = java.lang.Math.abs(this.m_startPoint) - this.m_targetDistance;
    this.m_dt.resetHeading(); /* take current heading as reference for target angle */
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    this.m_dt.polarDrive(this.m_speed, this.m_angle, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean done = true;
    double[] md = m_dt.getMotorDistances();
    double current = java.lang.Math.abs(md[0]); /*use FL as our current */
    if ((current > this.m_UL) || (current < this.m_LL)) { done = true;}
    return(done);
  }
}