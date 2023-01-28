// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import frc.robot.commands.*;
import frc.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.MecanumControllerCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.subsystems.*;

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
  
  // The robot's subsystems
  private final Drivetrain m_robotDrive = new Drivetrain();
  private final Grabber m_grabberSubsystem = new Grabber();
  private final Arm m_Arm = new Arm();

  // The robot's commands
  private final GrabberOpen m_GrabberOpen = new GrabberOpen(m_grabberSubsystem);
  private final GrabberStop m_GrabberStop = new GrabberStop(m_grabberSubsystem);
  private final GrabberClose m_GrabberClose = new GrabberClose(m_grabberSubsystem);
  private final ArmExtend m_ArmExtend = new ArmExtend(m_Arm);
  private final ArmRetract m_ArmRetract = new ArmRetract(m_Arm);
  private final ArmStop m_ArmStop = new ArmStop(m_Arm);


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
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    new Trigger(m_exampleSubsystem::exampleCondition)
        .onTrue(new ExampleCommand(m_exampleSubsystem));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    m_ArmController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());

    /** Controls option 4 - use scheduler to read joysticks and trigger existing commands classes
     * Pros: makes use of robot built in scheduler, atomic commands can be used in a sequence, no need to pass joystick refrence
     * Cons: new to ALARM, obscure structure, commands need to written
     */
    //m_ArmController.x().onTrue(m_GrabberOpen);
    //m_ArmController.x().onFalse(m_GrabberStop);
    /** same approach but using chained commands */
    m_ArmController.a().onTrue(m_GrabberOpen).onFalse(m_GrabberStop);
    m_ArmController.b().onTrue(m_GrabberClose).onFalse(m_GrabberStop);
    m_ArmController.x().onTrue(m_ArmExtend).onFalse(m_ArmStop);
    m_ArmController.y().onTrue(m_ArmRetract).onFalse(m_ArmStop);

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
