// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Arm;

public class ShoulderDouble extends CommandBase {
  Arm m_Arm;
  public ShoulderDouble(Arm a) {
    this.m_Arm = a;
    addRequirements(a);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_Arm.shoulderDouble();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Arm.stopShoulder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_Arm.isArmRaised();
  }
}
