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
  private double m_error = 0;

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
    double targetInfo[] = new double[3];
    targetInfo = this.m_v.findObject();
    double s = DriveParameters.k_driveSpeed;
    s = s * m_error * DriveParameters.k_optimalDistanceErrorGain;
    if (s > 0.5) {s = 0.5;}
    if (targetInfo[2] < DriveParameters.k_targetArea ){s = -s;}
    this.m_dt.drive(s,0,0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.m_dt.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double pipe = m_v.getPipeline();
    double targetInfo[] = new double[3];
    targetInfo = this.m_v.findObject();
    boolean stop = false;
    double area = DriveParameters.k_targetArea;
    double minArea = DriveParameters.k_minTargetArea;
    double maxArea = DriveParameters.k_maxTargetArea;
    if (pipe != 0) {
      area = DriveParameters.k_targetArea;
      minArea = DriveParameters.k_minTargetArea;
      maxArea = DriveParameters.k_maxTargetArea;
    }
    if (this.m_v.targetFound()) {
      double a = targetInfo[2];
      m_error = Math.abs(a - area);
      if ((a>minArea) && (a<maxArea)) {stop = true;}
  } else {
      stop = true;
    }
    return stop;
  }
}
