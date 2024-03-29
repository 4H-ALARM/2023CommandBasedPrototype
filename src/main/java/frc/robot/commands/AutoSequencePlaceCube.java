// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.vision;
import frc.robot.subsystems.Grabber;
import frc.robot.Constants.DriveParameters;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoSequencePlaceCube extends SequentialCommandGroup {
  /** Creates a new AutoSequenceFromCenter. */
  public AutoSequencePlaceCube(Drivetrain dt, Arm a, vision v, Grabber g, boolean goLeft) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new GrabberClose(g).withTimeout(1.5),
                new GrabberStop(g),
                new FullRetract(a),
                new FullLower(a),
                new DriveTraverseToTarget(dt, v, goLeft).withTimeout(DriveParameters.k_autonmousDriveTimeOut),
                new ShoulderDeploy(a),
                new ExtenderDeploy(a),
                new GrabberOpen(g).withTimeout(3),
                new GrabberStop(g),
                new DriveTraverse(dt, goLeft).withTimeout(1),
                new DriveAtAngleForDistance(dt, 0.5, 0.0, 8.0).withTimeout(DriveParameters.k_autonmousDriveTimeOut),
                new DriveStop(dt));
  }
}
