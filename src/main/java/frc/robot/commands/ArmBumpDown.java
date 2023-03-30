// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import frc.robot.subsystems.Arm;
import frc.robot.Constants.ArmParameters;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ArmBumpDown extends CommandBase {
  private Arm m_a;
  private double m_targetCount = ArmParameters.k_bumperCount;


  /** Creates a new ArmBumpDown. */
  public ArmBumpDown(Arm a) {
    this.m_a = a;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(a);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double startCount = this.m_a.getShoulderCount();
    // positive is down
    m_targetCount = startCount + ArmParameters.k_bumpDownCount;
    // don't allow a target lower than the bumper
    if (m_targetCount > ArmParameters.k_bumperCount)  {
      m_targetCount = ArmParameters.k_bumperCount;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    this.m_a.lower();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    this.m_a.stopShoulder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean done = false;
    double currentCount = this.m_a.getShoulderCount();
    if (currentCount >= m_targetCount) {
      done = true;
    }
    return (done);
  }
}
