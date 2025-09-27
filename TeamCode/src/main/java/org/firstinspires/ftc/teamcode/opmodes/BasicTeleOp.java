package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.Robot;

/**
 * Basic TeleOp for the JV Bot.
 */
@TeleOp(name = "Basic Tele")
public class BasicTeleOp extends LinearOpMode {

    /* Robot encapsulates the hardware for the bot
       whereas the opmode encapsulates the behavior and controls */
    private Robot robot;

    // Controls //
    // Gamepad controllers and settings //

    /* Driver's game controller */
    private GamepadEx driver;
    /* Operator's game controller */
    private GamepadEx operator;
    /* Squaring the controller inputs provide a smoother motion profile */
    private boolean squareInputs = true;

    // Driving //

    /* Speed limit for driving */
    private double maxDriveSpeed = 1.0;

    // Feeder //
    // Two servos that push the artifact into the launcher motor //

    /* Speed to stop the feeder */
    private double feederStopSpeed = 0.0;
    /* Speed to run the feeder */
    private double feederRunSpeed = 1.0;
    /* How long to run the feeder to feed one artifact through */
    private double feederRunDuration = 0.20;

    // Launcher //

    /* What fraction of full speed is the target speed for launching */
    private double launcherTargetSpeedFactor = 0.55;
    /* The ideal speed for the launcher motor; if the speed factor is set, we will use that instead */
    private double launcherTargetSpeed = 1125;
    /* The minimum speed for the launcher motor */
    private double launcherMinSpeed = 1075;
    /* The stop speed - should usually be completely off */
    private double launcherStopSpeed = 0.0;
    /* Toggle button to start/stop the launcher wheel */
    private ToggleButtonReader runLauncherToggle;

    private boolean displayDebugTelemetry = true;

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize the robot
        robot = new Robot(hardwareMap);
        robot.setDriveMaxSpeed(maxDriveSpeed);

        // Gamepads
        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);

        if (launcherTargetSpeedFactor > 0.0) {
            launcherTargetSpeed = robot.getLaunchMotorMaxSpeed() * launcherTargetSpeedFactor;
        }

        runLauncherToggle = new ToggleButtonReader(driver, GamepadKeys.Button.Y);

        telemetry.addData("Status", "Waiting for Start");
        telemetry.update();
        this.waitForStart();

        while (this.opModeIsActive()) {
            // Check for command and gamepad updates
            CommandScheduler.getInstance().run();
            driver.readButtons();
            operator.readButtons();
            runLauncherToggle.readValue();

            // Drive
            this.drive();

            // Launcher actions
            if (runLauncherToggle.getState()) {
                robot.setLauncherSpeed(launcherTargetSpeed);
            } else if (driver.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
                robot.setLauncherSpeed(launcherTargetSpeed);
            } else {
                robot.setLauncherSpeed(launcherStopSpeed);
            }
            runLauncherToggle.readValue();

            if (driver.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
                robot.setFeederSpeed(feederRunSpeed);
            } else {
                robot.setFeederSpeed(feederStopSpeed);
            }

            this.sendTelemetry();
        }

        robot.setLauncherSpeed(0.0);
        robot.setFeederSpeed(0.0);
    }

    private void drive() {
        robot.arcadeDrive(-driver.getLeftY(), -driver.getRightX(), squareInputs);
    }

    private void sendTelemetry() {
        telemetry.addData("Status", "Running");
        if (displayDebugTelemetry) {
            telemetry.addLine("LAUNCHER");
            telemetry.addData("Launch Toggle", runLauncherToggle.getState());
            telemetry.addData("Launch Speed", robot.getLauncherSpeed());
            telemetry.addData("Launch Target", launcherTargetSpeedFactor + " (" + launcherTargetSpeed + ")");
            telemetry.addLine("FEEDER");
            telemetry.addData("Feed Speed", robot.getFeederSpeed());
            telemetry.addLine("DRIVE");
            telemetry.addData("Drive Max", maxDriveSpeed);
        }
        telemetry.update();
    }

}
