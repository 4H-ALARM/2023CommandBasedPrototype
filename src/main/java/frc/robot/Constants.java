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
      public static final int k_Arm = 20;
      public static final int k_Shoulder = 21;
      public static final int k_pigeon2 = 30;
    }

    public static final class USBPorts {
      public static final int k_armPort = 0;
      public static final int k_drivePort = 1;
    }

    public static class DriveParameters {
      public static final double k_minRotInput = 0.05;
      public static final double k_RotationFactor = -0.01875;
      public static final double k_RotationMaxCorrection = 0.25;      
    }

    public static class ArmParameters {
      public static final double k_armRaiseSpeed = 0.7;
      public static final double k_armLowerSpeed = -0.7;

    }

    public static class GrabberParameters {
      public static final int k_GrabberPWM = 3;
      public static final double k_openAngle = 90.0;
      public static final double k_closeAngle = -90.0;
    }

    public static class VisionParameters {

    }
  
    public static enum StartingPosition {
      LEFT, MIDDLE, RIGHT
    } 
    
    public final class Debug {
      //set to false to allow compiler to identify and eliminate
      //unreachable code
      public static final boolean ON = false;
    }
}
  

  