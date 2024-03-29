// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Arm;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class LowerToBumper extends CommandBase {
  private Arm m_Arm;
  /** Creates a new RetractToBumper. */
  public LowerToBumper(Arm a) {
    this.m_Arm = a;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(a);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_Arm.fullLower();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Arm.stopShoulder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_Arm.atBumper();
  }
}
