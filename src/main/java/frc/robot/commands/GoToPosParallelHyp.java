// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.Arm;
import frc.robot.Constants.ArmParameters;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class GoToPosParallelHyp extends ParallelCommandGroup {
  /** Creates a new GoToPosParallel. */
  public GoToPosParallelHyp(Arm a,double sp,double ep) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    double shoulderCount = Math.abs(a.getShoulderCount());
    double extendCount = Math.abs(a.getExtenderCount());
    boolean up = (shoulderCount < sp);
    double maxH = a.calculateHypotenus(shoulderCount) - ArmParameters.k_hypSafety;
    double currentH = ArmParameters.k_shortestArmLength + extendCount;
    if (up){
      if (shoulderCount > Math.abs(ArmParameters.k_clearBumperCount)){
        if (currentH >= maxH){
          addCommands(new ArmPositionToCount(a,sp));
        } else {
          addCommands(new ArmPositionToCount(a,sp), new ArmExtendToCount(a, ep));
        }
      } else {
        addCommands(new ArmPositionToCount(a,sp));
      }
    } else {
      if (shoulderCount > Math.abs(ArmParameters.k_clearBumperCount)){
        if (currentH >= maxH){
          addCommands(new ArmExtendToCount(a, ep));
        } else {
          addCommands(new ArmPositionToCount(a,sp), new ArmExtendToCount(a, ep));
        }
      } else {
        if (a.isArmRetracted()) {
          addCommands(new ArmPositionToCount(a,sp));
        } else {
          addCommands(new ArmExtendToCount(a, ep));
        }
      }
    }
  }
}
