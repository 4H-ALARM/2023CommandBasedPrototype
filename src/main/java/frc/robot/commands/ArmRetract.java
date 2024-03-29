// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;



public class ArmRetract extends CommandBase {
  private Arm m_Arm;
  private boolean m_atFullRetract = false;

  /** Creates a new ArmRetract. */
  public ArmRetract(Arm r) {
    this.m_Arm = r;

    // Use addRequirements(g) here to declare subsystem dependencies.
    addRequirements(r);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_atFullRetract =  m_Arm.retract();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Arm.stopExtender();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean done = false;
    if (m_atFullRetract) {
      done = true;
    }
    return (done);
  }
}
