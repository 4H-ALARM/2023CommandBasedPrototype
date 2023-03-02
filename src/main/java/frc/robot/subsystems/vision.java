// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import static frc.robot.Constants.*;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Debug;


public class vision extends SubsystemBase {
  NetworkTable m_table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry m_tx = m_table.getEntry("tx");
  NetworkTableEntry m_ty = m_table.getEntry("ty");
  NetworkTableEntry m_ta = m_table.getEntry("ta");
  NetworkTableEntry m_tid = m_table.getEntry("tid");
  NetworkTableEntry m_ledMode = m_table.getEntry("ledMode");
  NetworkTableEntry m_pipeLine = m_table.getEntry("pipeline");
  NetworkTableEntry m_llpython = m_table.getEntry("llpython");
  
  private double m_llpythonReturn[];

  private double m_currentPipeline = 1;

  /** Creates a new vision. */
  public vision() {
    
    off();
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    double x = m_tx.getDouble(0.0);
    double y = m_ty.getDouble(0.0);
    double id = m_tid.getDouble(0.0);
    double area = m_ta.getDouble(0.0);
    
  
    if (Debug.VisionON) {
      SmartDashboard.putNumber("LimelightX", x);
      SmartDashboard.putNumber("LimelightY", y);
      SmartDashboard.putNumber("LimelightArea", area);
      SmartDashboard.putNumber("AprilTag ID;", id);
    }
    

    double ledState = m_ledMode.getDouble(-1);
    if (Debug.VisionON) {
      SmartDashboard.putNumber("ledMode", ledState);
    }
    

    m_pipeLine.setNumber(m_currentPipeline);
    if (Debug.VisionON) {
      SmartDashboard.putNumber("Pipe", m_currentPipeline);
    }
    

  }

  public void off() {
    m_ledMode.setNumber(VisionParameters.k_lightOff);
  }

  public void on() {
    m_ledMode.setNumber(VisionParameters.k_lightOn);
  }

  public void swapPipeline() {
    m_currentPipeline++;
    if (m_currentPipeline > VisionParameters.k_maxPipeline) {
      m_currentPipeline = 0.0;
    } 
    m_pipeLine.setNumber(m_currentPipeline);
  }


  public boolean targetFound () {
    boolean found = false;
    double id = m_tid.getDouble(0.0);
    if (id != -1) { found = true;}
    return(found);
  }

  /**
   * Gets the vision returns from the pipeline selected
   *
   * @param pipe - the pipeline to read from
   * @return - array of vision data, tx, ty and ta
   */
  public double[] findObject () {
    double targetInfo[] = new double[3];

    targetInfo[0] = m_tx.getDouble(0.0);
    targetInfo[1] = m_ty.getDouble(0.0);
    targetInfo[2] = m_ta.getDouble(0.0);
    
    return(targetInfo);
  }

  public boolean foundObject() {
    boolean found = false;
    if (m_currentPipeline == VisionParameters.k_aprilTagPipeline){
      found = targetFound();
    } else {
    if (m_llpythonReturn[0] == 74) { found = true;}
    }
    return(found);
  }

  /**
   * determine if the target relecting tape is to the left 
   */
  //TODO flesh this out
  public boolean markerToLeft() {
    return true;
  }

  /**
   * determine if the target relecting tape is in front of robot 
   */
  //TODO flesh this out
  public boolean markerFound() {
    return true;
  }

}
