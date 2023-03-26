// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Leds extends SubsystemBase {
  private final AddressableLED m_led = new AddressableLED(9);
  private AddressableLEDBuffer m_ledBuffer = new AddressableLEDBuffer(180);
  /** Creates a new Leds. */
  public Leds() {
    m_led.setLength(m_ledBuffer.getLength());
    m_led.setData(m_ledBuffer);
    m_led.start();
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      // Sets the specified LED to the RGB values for red
      m_ledBuffer.setRGB(i, 255, 100, 0);
   }
   
   m_led.setData(m_ledBuffer);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
