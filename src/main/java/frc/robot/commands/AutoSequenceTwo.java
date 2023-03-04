// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Grabber;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoSequenceTwo extends SequentialCommandGroup {
  /** Creates a new AutoSequenceTwo. */
  public AutoSequenceTwo(Drivetrain dt, Arm a, Grabber g) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new GrabberClose(g).withTimeout(1),
                new FullRetract(a),
                new FullLower(a),
                new DriveAtAngleForDistance(dt, 0.2, 0.0, 5.0),
                new DriveStop(dt),
                new ShoulderDeploy(a),
                new ExtenderDeploy(a),
                new DriveAtAngleForDistance(dt, 0.2, 0.0, 1.0),  
                new DriveStop(dt),
                new GrabberOpen(g).withTimeout(2)
                );
  }
}
