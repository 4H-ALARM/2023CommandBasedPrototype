// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveParameters;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.vision;

public class DriveToOptimalTargetToRobotDistance extends CommandBase {
  private Drivetrain m_dt;
  private vision m_v;
  private double m_targetInfo[] = new double[2];

  /** Creates a new DriveTraverse. */
  public DriveToOptimalTargetToRobotDistance(Drivetrain dt, vision v) {
    this.m_dt = dt;
    this.m_v = v;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(dt, v);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() { }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_targetInfo = m_v.findObject();
    double s = DriveParameters.k_driveSpeed;
    if (m_targetInfo[2] < DriveParameters.k_targetArea ){s = -s;}
    m_dt.drive(s,0,0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_dt.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean inRange = false;
    double a = m_targetInfo[2];
    if ((a>DriveParameters.k_minTargetArea) && (a<DriveParameters.k_maxTargetArea)) {inRange = true;}
    return inRange;
  }
}
