// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */

 public final class Constants {
    public static final class CANaddresses {
      public static final int k_FrontLeftMotor = 1;
      public static final int k_RearLeftMotor = 2;
      public static final int k_FrontRightMotor = 3;
      public static final int k_RearRight = 4;
      public static final int k_Extender = 20;
      public static final int k_Shoulder = 21;
      public static final int k_claw = 22;
      public static final int k_pigeon2 = 30;
    }

    public static final class USBPorts {
      public static final int k_armPort = 0;
      public static final int k_drivePort = 1;
    }

    public static class DriveParameters {
      public static final double k_minRotInput = 0.05;
      public static final double k_RotationFactor = -0.01875;
      public static final double k_RotationMaxCorrection = 0.10;
      public static final double k_balancePoint = 0.9;  //TODO adjust the max amount out of balanace
      public static final double k_balanceCorrectionFactor = 0.002; //TODO measure and correct this
      public static final double k_maxBalanceCorrection = 0.1;  //TODO adjust the max amount of correction 
      public static final double k_traverseSpeed = 0.3; //TODO measure and correct this
    }

    public static class ArmParameters {
      public static final double k_armRaiseSpeed = -0.3;
      public static final double k_armLowerSpeed = 0.3;
      public static final double k_armExtendSpeed = -0.3;
      public static final double k_armRetractSpeed = 0.3;
      public static final int k_lowerLimitDIO = 0;
      public static final int k_fullRetractDIO = 1;
      public static final double k_fullExtendCount = -179000.0;
      public static final double k_fullRaiseCount = -160000.0;
      public static final double k_startStowCount = -50000.0;
      public static final double k_safeExtenderStowCount = -100.0; 
      public static final double k_llExtendGoodGrab = 20;
      public static final double k_ulExtendGoodGrab = 25;
      public static final double k_llSoulderGoodGrab = -250;
      public static final double k_ulShoulderGoodGrab = -200;
      public static final double k_armRadianPerCount = Math.toRadians(90.0)/145000.0; // ~145K in 90 degrees
      public static final double k_armHeight = 201000;  // arm height in extender counts
      public static final double k_shortestArmLength = 230000; // length of the fixed portion of the arm in extender counts
      public static final double k_safeReach = 100000; // max extension count allowed for a grab
    }

    public static class GrabberParameters {
      public static final double k_openSpeed = 0.8;
      public static final double k_closeSpeed = -0.8;
    }

    public static class VisionParameters {
      public static final int k_lightOff = 1;
      public static final int k_lightOn = 3;
      public static final int k_maxPipeline = 2;
      public static final int k_aprilTagPipeline = 0;
      public static final int k_conePipeline = 1;
      public static final int k_cubePipeline = 2;
    }
  
    public static enum AutonomousOptions {
      DRIVE, PLACE, NONE
    } 
    
    public final class Debug {
      //set to false to allow compiler to identify and eliminate
      //unreachable code
      public static final boolean DriveON = false;
      public static final boolean ArmON = false;
      public static final boolean VisionON = false;
    }
}
  

  