package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.messaging.Messaging;
import frc.robot.subsystems.swerve.CommandSwerve;

public class RobotContainer {
	private CommandXboxController xbox;
	private CommandJoystick flightSim;
    // private Superstructure structure;
	private CommandSwerve swerve;
	private Messaging messaging;
	private Command autoCommand;
	private SendableChooser<Command> autonChooser;

	private final int DRIVER_PORT = 2;
	private final int OPERATOR_PORT = 1;

	public RobotContainer() {
        DriverStation.silenceJoystickConnectionWarning(true);
        // structure = Superstructure.getInstance();
		swerve = CommandSwerve.getInstance();
		// messaging = Messaging.getInstance();
		// setupAuto();
		setupDriveController();
	}

	public void setupAuto() {
		autonChooser = AutoBuilder.buildAutoChooser();
		Shuffleboard.getTab("Display").add(
			"Auto Route", 
			autonChooser
		);
	}

	public void setupDriveController() {
		xbox = new CommandXboxController(DRIVER_PORT);
		swerve.setDefaultCommand(swerve.robotCentricDrive(xbox));

		Trigger switchDriveModeButton = xbox.x();
		Trigger resetGyroButton = xbox.a();
		Trigger alignToTargetButton = xbox.rightBumper();
		Trigger cancelationButton = xbox.start();
		Trigger moveToAprilTagButton = xbox.leftBumper();

        // moveToAprilTagButton.whileTrue(structure.driveToTag(new Pose2d(1, 0.25, new Rotation2d())));
        // switchDriveModeButton.toggleOnTrue(structure.robotCentricDrive(xbox));
		// resetGyroButton.onTrue(structure.resetGyro());
		// alignToTargetButton.whileTrue(structure.driveToPiece());
		// cancelationButton.onTrue(Commands.runOnce(
		// 	() -> CommandScheduler.getInstance().cancelAll())
		// );
	}

	public void setupOperatorController() {
		flightSim = new CommandJoystick(OPERATOR_PORT);
		Trigger intakeButton = flightSim.button(2);
		Trigger climberClimbButton = flightSim.button(5);
		Trigger climberPullButton = flightSim.button(6);
		Trigger shootButton = flightSim.trigger();
		// intakeButton.onTrue(structure.startIntake());
		// intakeButton.onFalse(structure.finishIntake());
		// climberClimbButton.onTrue(structure.climbUp());
		// climberPullButton.onTrue(structure.pullUp());
		// shootButton.onTrue(structure.shoot());
	}
	
	public Command rumbleCommand(double timeSeconds) {
		return Commands.startEnd(
			() -> xbox.getHID().setRumble(RumbleType.kBothRumble, 0.5),
			() -> xbox.getHID().setRumble(RumbleType.kBothRumble, 0)
		).withTimeout(timeSeconds);
	}

	public void autonomousInit() {
		messaging.setMessagingState(true);
		messaging.addMessage("Auto Started");
		autoCommand = autonChooser.getSelected();
		if (autoCommand != null) {
			autoCommand.schedule();
		} else {
			messaging.addMessage("No Auto Command Selected");
		}
	}

	public void teleopInit() {
		messaging.setMessagingState(true);
		messaging.addMessage("Teleop Started");
		if (autoCommand != null) {
			autoCommand.cancel();
		}
	}
}