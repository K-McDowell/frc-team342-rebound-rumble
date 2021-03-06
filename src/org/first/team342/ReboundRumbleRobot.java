/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.first.team342;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import org.first.team342.autonomous.DefaultAutonomous;
import org.first.team342.autonomous.ShootAndTipCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.first.team342.commands.autonomous.ShootOnly;
import org.first.team342.commands.drive.DriveWithJoystick;
import org.first.team342.subsystems.Elevator;
import org.first.team342.subsystems.Thrower;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * 
 * @author FIRST Team 342
 */
public class ReboundRumbleRobot extends IterativeRobot {

    private Command autonomousCommand;
    private Command joystickCommand;
    private Thrower thrower;
    private Elevator elevator;
    private SendableChooser autonomousChooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        System.out.println("Initializing Robot");
        this.thrower = Thrower.getInstance();
        this.elevator = Elevator.getInstance();
        System.out.println("Before Joystick Command");
        this.joystickCommand = new DriveWithJoystick();
        System.out.println("After Joystick Command");

        System.out.println("Creating Autonomous Mode Chooser");
        this.autonomousChooser = new SendableChooser();
        this.autonomousChooser.addDefault("Default Autonomous", new ShootOnly());
        this.autonomousChooser.addObject("Shoot and Tip", new ShootAndTipCommand());
        SmartDashboard.putData("Autonomous Mode", this.autonomousChooser);
        System.out.println("Autonomous Mode Chooser Sent To Dashboard");
    }

    public void autonomousInit() {
        this.autonomousCommand = (Command) this.autonomousChooser.getSelected();
        this.autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        this.joystickCommand.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();

        this.thrower.updateStatus();
        this.thrower.updatePID();
        this.elevator.updateStatus();

        if (this.isEnabled()) {
            this.elevator.intakeOn();
        } else {
            this.elevator.intakeOff();
        }
    }

    public void disabledInit() {
        SmartDashboard.putBoolean("Conveyor", false);
    }

    //TODO need to override this method.
    public void disabledContinuous() {
        super.disabledContinuous();
    }

    //TODO need to override this method.
    public void disabledPeriodic() {
        super.disabledPeriodic();
    }
}
