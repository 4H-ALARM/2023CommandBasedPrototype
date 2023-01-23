// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Grabber extends SubsystemBase {

  private final WPI_VictorSPX m_clawmotor = new WPI_VictorSPX(4);
  private XboxController m_c;

  /** Creates a new Grabber. */
  public Grabber(XboxController c) {
    m_c= c;
    m_clawmotor.setInverted(false);
  }

  public void open(){
    m_clawmotor.set(0.7);
  }

  public void close() {
    m_clawmotor.set(-0.9);
  }

  public void stop() {
    m_clawmotor.set(0.0);
  }

  @Override
  public void periodic() {
     /** Controls option 2 - read driver inputs here and makes calls into subsystem methods
     * Pros: Simple to understand, compartmentailize operations and subsystems
     * Cons: Likely interactions, need to pass controller reference to subsystems code duplication for autonomous
     */

    // This method will be called once per scheduler run
    boolean open = m_c.getAButton();
    boolean close = m_c.getBButton();

    if (open) {
      open();
    } else if (close) {
       close();
    } else {
      stop();
    }

  }


  public CommandBase openCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          open();
        });
  }

  public CommandBase stopCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          stop();
        });
  }

}
