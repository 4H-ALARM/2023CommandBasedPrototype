// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Leds;

public class changeLedColor extends CommandBase {
  private Leds m_Leds;
  Integer m_counter = 0;

  /** Creates a new changeLedcolor. */
  public changeLedColor(Leds l) {
    this.m_Leds = l;

    // Use addRequirements(v) here to declare subsystem dependencies.
    addRequirements(l);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    m_counter++;

    switch(m_counter) {
      case 0:
        m_Leds.setRGB(0,0,0);
        break;
      case 1:
        m_Leds.setRGB(255,100,0);
        break;
      case 2:
        m_Leds.setRGB(255,0,255);
        m_counter = -1;
        break;
      case 3:
        m_counter = -1;
        break;
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
