// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.RunCommand;

import frc.robot.Constants.StartingPosition;
import frc.robot.Constants.USBPorts;

import frc.robot.commands.*;
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
    new CommandXboxController(USBPorts.k_armPort);

  public final CommandXboxController m_DriveJoystick = 
    new CommandXboxController(USBPorts.k_drivePort);

  // The robot's subsystems and commands are defined here...
  // private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  
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
  //TODO remove private final ArmExtend m_ArmExtend = new ArmExtend(m_Arm);
  //TODO remove private final ArmRetract m_ArmRetract = new ArmRetract(m_Arm);
  private final ArmStop m_ArmStop = new ArmStop(m_Arm);
  //TODO removeprivate final liftShoulder m_liftShoulder = new liftShoulder(m_Arm);
  //TODO removeprivate final lowerShoulder m_lowerShoulder = new lowerShoulder(m_Arm);
  //TODO remove private final stopShoulder m_stopShoulder = new stopShoulder(m_Arm);
  private final OverrideShoulder m_overrideShoulder = new OverrideShoulder(m_Arm);
  private final OverrideExtender m_overrideExtender = new OverrideExtender(m_Arm);
  //TODO remove private final FullLower m_fullLower = new FullLower(m_Arm);
  //TODO remove private final FullRetract m_FullRetract = new FullRetract(m_Arm);
  private final StowArm m_StowArm = new StowArm(m_Arm);
  private final DeployArm m_DeployArm = new DeployArm(m_Arm);
  

  // Drive Commands note these are in addition to the default 
  // joystick controlled driving in teleop
  private final ResetGyro m_ResetGyro = new ResetGyro(m_robotDrive);
  private final RotateForTargetSeq m_RTS = new RotateForTargetSeq(m_vision, m_robotDrive);
  //TODO remove private final DriveStop m_Stop = new DriveStop(m_robotDrive);
  //TODO remove private final DriveAtAngleForDistance m_Drive45Angle = 
  //TODO remove  new DriveAtAngleForDistance(m_robotDrive, 0.3, 0.785398163, 15.0); 
  //TODO remove private final DriveAtAngleForDistance m_DriveStraight = 
  //TODO remove new DriveAtAngleForDistance(m_robotDrive, 0.2, 0.0, 10.0); 
  private final SwitchDrivePerspective m_switchPerspective = 
    new SwitchDrivePerspective(m_robotDrive);
  private final ToggleMaintainHeading m_toggleMaintainHeading = 
    new ToggleMaintainHeading(m_robotDrive);
  //TODO remove private final AutoDriveStraight m_autoStraight = 
  //TODO remove  new AutoDriveStraight(m_robotDrive);

  // Vision Commands
  private final limeLightOff m_limeLightOff = new limeLightOff(m_vision); 
  private final limeLightOn m_limeLightOn = new limeLightOn(m_vision);
  private final swapPipeline m_swapPipeline = new swapPipeline(m_vision);

  // End robot's commands  **************************************************


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    // Configure default commands
    // Set the default drive command to split-stick mecanum drive
    m_robotDrive.setDefaultCommand(
      new RunCommand(
        () ->
            m_robotDrive.drive(
              m_DriveJoystick.getLeftY(),
              -m_DriveJoystick.getLeftX(),
              -m_DriveJoystick.getRightX()
            ),
      m_robotDrive)
    );

    // Set the default arm command to split-stick arcade drive
    m_Arm.setDefaultCommand(
      new RunCommand(
        () ->
            m_Arm.move
              (m_ArmController.getLeftY(), 
              m_ArmController.getRightY()
            ),
        m_Arm)
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
    //TODO remove m_ArmController.x().onTrue(m_ArmExtend).onFalse(m_ArmStop);
    //TODO remove m_ArmController.y().onTrue(m_ArmRetract).onFalse(m_ArmStop);
    //TODO remove m_ArmController.povUp().onTrue(m_liftShoulder).onFalse(m_stopShoulder);
    //TODO remove m_ArmController.povDown().onTrue(m_lowerShoulder).onFalse(m_stopShoulder);
    m_ArmController.leftBumper().onTrue(m_overrideShoulder);
    m_ArmController.rightBumper().onTrue(m_overrideExtender);
    m_ArmController.leftTrigger().onTrue(m_StowArm).onFalse(m_ArmStop); 
    m_ArmController.rightTrigger().onTrue(m_DeployArm).onFalse(m_ArmStop);

    // Trigger to Drive command mappings  
    m_DriveJoystick.a().onTrue(m_ResetGyro);
    m_DriveJoystick.x().onTrue(m_RTS);  //TODO change rotate for target sequence and test this slowly and carefully
    //TODO remove m_DriveJoystick.y().onTrue((m_Drive45Angle).andThen(m_Stop));
    m_DriveJoystick.y().onTrue((m_switchPerspective));
    m_DriveJoystick.leftTrigger().onTrue(m_toggleMaintainHeading);
    //TODO remove m_DriveJoystick.b().onTrue((m_DriveStraight).andThen(m_Stop));


    // Trigger to Vision command mappings 
    m_DriveJoystick.button(7).onTrue(m_limeLightOn);
    m_DriveJoystick.button(8).onTrue(m_limeLightOff);
    m_DriveJoystick.rightTrigger().onTrue(m_swapPipeline);

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public CommandBase getAutonomousCommand(StartingPosition sp) {
    // An example command will be run in autonomous

    CommandBase autCommand = new AutoSequenceOne(m_robotDrive);  //TODO add autonoumos

      switch(sp) {
        case LEFT:
          break;
        case MIDDLE:
          break;
        case RIGHT:
          break;
    }
    return (autCommand);
  }

}
