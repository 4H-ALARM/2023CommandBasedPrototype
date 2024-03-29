// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.vision;

public class DriveTraverseToTargetOnRight extends CommandBase {
  private Drivetrain m_dt;
  private vision m_v;

  private boolean m_goLeft = false;

  /** Creates a new DriveTraverse. */
  public DriveTraverseToTargetOnRight(Drivetrain dt, vision v, boolean goLeft) {
    this.m_dt = dt;
    this.m_v = v;
    this.m_goLeft = goLeft;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(dt, v);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() { }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_dt.traverse(m_goLeft);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_dt.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean found = m_v.targetFoundRight();
    return found;
  }
}
