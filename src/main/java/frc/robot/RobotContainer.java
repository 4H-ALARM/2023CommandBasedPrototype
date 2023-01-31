// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.RunCommand;

import frc.robot.subsystems.*;
import frc.robot.commands.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  // Replace with CommandPS4Controller or CommandJoystick if needed
  public final CommandXboxController m_ArmController =
    new CommandXboxController(0);

  public final CommandXboxController m_DriveJoystick = 
    new CommandXboxController(1);

  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  
  // Robot's subsystems *************************************************
  private final Drivetrain m_robotDrive = new Drivetrain();
  private final Grabber m_grabberSubsystem = new Grabber();
  private final Arm m_Arm = new Arm();
  private final vision m_vision = new vision();
  // End subsystems *****************************************************

  // Robot's commands  **************************************************

  // Grabber commands
  private final GrabberOpen m_GrabberOpen = new GrabberOpen(m_grabberSubsystem);
  private final GrabberStop m_GrabberStop = new GrabberStop(m_grabberSubsystem);
  private final GrabberClose m_GrabberClose = new GrabberClose(m_grabberSubsystem);

  // Arm Commands
  private final ArmExtend m_ArmExtend = new ArmExtend(m_Arm);
  private final ArmRetract m_ArmRetract = new ArmRetract(m_Arm);
  private final ArmStop m_ArmStop = new ArmStop(m_Arm);
  private final liftShoulder m_liftShoulder = new liftShoulder(m_Arm);
  private final lowerShoulder m_lowerShoulder = new lowerShoulder(m_Arm);
  private final stopShoulder m_stopShoulder = new stopShoulder(m_Arm);

  // Drive Commands note these are in addition to the default 
  // joystick controlled driving in teleop
  private final ResetGyro m_ResetGyro = new ResetGyro(m_robotDrive);
  private final RotateForTargetSeq m_RTS = new RotateForTargetSeq(m_vision, m_robotDrive);
  private final DriveAtAngleForDistance m_Drive45Angle = 
    new DriveAtAngleForDistance(m_robotDrive,0.5,0.785398,5.0);

  // Vision Commands

  private final limeLightOff m_limeLightOff = new limeLightOff(m_vision); 
  private final limeLightOn m_limeLightOn = new limeLightOn(m_vision);

  // End robot's commands  **************************************************


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    // Configure default commands
    // Set the default drive command to split-stick arcade drive
    m_robotDrive.setDefaultCommand(
      new RunCommand(
        () ->
            m_robotDrive.drive(
              m_DriveJoystick.getLeftY(),
              m_DriveJoystick.getLeftX(),
              m_DriveJoystick.getRightX(),
              false
              ),
        m_robotDrive)
        );

  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    
    /** Controls option 4 - use scheduler to read joysticks and trigger existing commands classes
     * Pros: makes use of robot built in scheduler, atomic commands can be used in a sequence, no need to pass joystick refrence
     * Cons: new to ALARM, obscure structure, commands need to written
     */
    
     // Trigger to Grabber command mappings
    m_ArmController.a().onTrue(m_GrabberOpen).onFalse(m_GrabberStop);
    m_ArmController.b().onTrue(m_GrabberClose).onFalse(m_GrabberStop);

    // Trigger to Arm command mappings
    m_ArmController.x().onTrue(m_ArmExtend).onFalse(m_ArmStop);
    m_ArmController.y().onTrue(m_ArmRetract).onFalse(m_ArmStop);
    m_ArmController.povUp().onTrue(m_liftShoulder).onFalse(m_stopShoulder);
    m_ArmController.povDown().onTrue(m_lowerShoulder).onFalse(m_stopShoulder);

    // Trigger to Drive command mappings  
    m_DriveJoystick.a().onTrue(m_ResetGyro);
    m_DriveJoystick.x().onTrue(m_RTS);
    m_DriveJoystick.y().onTrue(m_Drive45Angle);

    // Trigger to Vision command mappings 

    m_DriveJoystick.button(7).onTrue(m_limeLightOn);
    m_DriveJoystick.button(8).onTrue(m_limeLightOff);

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }

}
