// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.ArmParameters;
import frc.robot.subsystems.Arm;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ShoulderPosition extends CommandBase {
  private Arm m_a;
  private double m_targetCount = 0;
  private double m_targetCountUL = 0;
  private double m_targetCountLL = 0;

  /** Creates a new ArmPositionToCount. */
  public ShoulderPosition(Arm a, double c) {
    this.m_a = a;
    // as arm counts are always negative take absolute value to make compares easier
    this.m_targetCount = Math.abs(c);
    this.m_targetCountUL = Math.abs(c) + ArmParameters.k_targetCountRange;
    if (this.m_targetCountUL > Math.abs(ArmParameters.k_fullRaiseCount)) {
      this.m_targetCountUL = Math.abs(ArmParameters.k_fullRaiseCount);
    }
    this.m_targetCountLL = Math.abs(c) - ArmParameters.k_targetCountRange;
    if (this.m_targetCountLL <0 ) {
      this.m_targetCountLL= 0;
    }

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // decide it arm goes up or down
    // take abssolute value as counts are always negative to
    // make comparisons easier
    double currentCount = Math.abs(m_a.getShoulderCount());
    if (currentCount < m_targetCount) {
      m_a.lift();
    } else {
      m_a.lower();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_a.stopShoulder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean inRange = false;
    double currentCount = Math.abs(m_a.getShoulderCount());
    if ((currentCount > m_targetCountLL) && (currentCount < m_targetCountUL)) {
      inRange = true;
    }

    return (inRange);
  }
}
