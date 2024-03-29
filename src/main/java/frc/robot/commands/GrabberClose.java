// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Grabber;

public class GrabberClose extends CommandBase {
  private Grabber m_Grabber;

  /** Creates a new GrabberOpen. */
  public GrabberClose(Grabber g) {
    this.m_Grabber = g;

    // Use addRequirements(g) here to declare subsystem dependencies.
    addRequirements(g);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // clear hold mode in case we are in hold from a prior call to this command
    m_Grabber.releaseHold();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_Grabber.close();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Grabber.setHold();
  }

  /**
   * only ends when button is released, MUST be
   * mapped to a "whileTrue" trigger
   * 
   * @return always returns false
   */
  @Override
  public boolean isFinished() {    
    return false;
  }
}
