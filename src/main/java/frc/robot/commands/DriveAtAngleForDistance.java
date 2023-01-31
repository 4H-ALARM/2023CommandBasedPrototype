// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.geometry.Rotation2d;

import frc.robot.subsystems.Drivetrain;

public class DriveAtAngleForDistance extends CommandBase {
  private Drivetrain m_dt;
  private double m_startPoint = 0.0;
  private final double m_targetDistance = 5.0; /*made up number for now */
  private Rotation2d m_angle = new Rotation2d(0.785398);  /* 45degrees in radians */


  /** Creates a new DriveAtAngleForDistance. */
  public DriveAtAngleForDistance(Drivetrain dt) {
    this.m_dt = dt;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(dt);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double[] md = m_dt.getMotorDistances();
    m_startPoint = md[0]; /*use FL as our distance start */
    m_dt.resetHeading(); /* take current heading as reference for target angle */
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_dt.polarDrive(0.05, m_angle, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean done = false;

    double[] md = m_dt.getMotorDistances();
    double current = md[0]; /*use FL as our current */
    if ((current - m_startPoint) > m_targetDistance) { done = true;}
    return(done);
  }
}
