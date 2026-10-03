package org.firstinspires.ftc.teamcode.opmodes;

import androidx.core.math.MathUtils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.RobotLog;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.NewRobot;
import org.firstinspires.ftc.teamcode.util.TeamVersion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Basic TeleOp for the JV Bot.
 */
@TeleOp(name = "JV Bot")
public class NewTeleOp extends LinearOpMode {

    /* Robot encapsulates the hardware for the bot
       whereas the opmode encapsulates the behavior and controls */
    private NewRobot robot;

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
    private double maxDriveSpeed = 0.5;

    // Feeder //
    // Two servos that push the artifact into the launcher motor //

    /* Speed to stop the feeder */
    private double feederStopSpeed = 0.0;
    /* Speed to run the feeder */
    private double feederRunSpeed = 1.0;
    /* How long to run the feeder to feed one artifact through */
    private long feederSleep = 1000;

    // Launcher //

    /* The ideal speed for the launcher motor; if the speed factor is set, we will use that instead */
    private double launcherTargetSpeed = 1125;
    /* The minimum speed for the launcher motor */
    private double launcherMinSpeed = 1075;
    /* The stop speed - should usually be completely off */
    private double launcherStopSpeed = 0.0;

    // sdcard/FIRST/settings/botsettings.properties
    private String settingsFilename = "botsettings.properties";
    private boolean displayDebugTelemetry = true;

    @Override
    public void runOpMode() throws InterruptedException {

        this.loadConfig();

        // Initialize the robot
        robot = new NewRobot(hardwareMap, "frontLeft", "frontRight", "backLeft", "backRight");
        robot.setDriveMaxSpeed(maxDriveSpeed);

        // Gamepads
        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);

        driver.getGamepadButton(GamepadKeys.Button.A).whenPressed(() -> {
            robot.setFeederSpeed(feederRunSpeed);
            sleep(feederSleep);
            robot.setFeederSpeed(feederStopSpeed);
        });

        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(() -> addLauncherTargetSpeed(100));
        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(() -> addLauncherTargetSpeed(-100));
        driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).and(driver.getGamepadButton(GamepadKeys.Button.OPTIONS)).whenActive((() -> addFeederSleep(-50)));
        driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).and(driver.getGamepadButton(GamepadKeys.Button.OPTIONS)).whenActive((() -> addFeederSleep(50)));

        driver.getGamepadButton(GamepadKeys.Button.Y).toggleWhenPressed(
                () -> robot.setLauncherSpeed(launcherTargetSpeed),
                () -> robot.setLauncherSpeed(launcherStopSpeed)
        );

        telemetry.addData("Status", "Waiting for Start");
        telemetry.update();
        this.waitForStart();

        while (this.opModeIsActive()) {
            // Check for command and gamepad updates
            driver.readButtons();
            operator.readButtons();
            CommandScheduler.getInstance().run();

            // Drive
            this.drive();

            this.sendTelemetry();
        }

        robot.setLauncherSpeed(0.0);
        robot.setFeederSpeed(0.0);
    }

    private void drive() {
        robot.driveRobotCentric(driver, 1);
    }

    private void setLauncherTargetSpeed(double target) {
        launcherTargetSpeed = MathUtils.clamp(target, 0, robot.getLaunchMotorMaxSpeed());
    }

    private void addLauncherTargetSpeed(double increment) {
        this.setLauncherTargetSpeed(launcherTargetSpeed + increment);
        this.saveConfig();
    }

    private void addFeederSleep(long increment) {
        feederSleep = MathUtils.clamp(feederSleep + increment, 500, 1000);
        this.saveConfig();
    }

    private void sendTelemetry() {
        telemetry.addData("Status", "Running");
        if (displayDebugTelemetry) {
            telemetry.addLine("LAUNCHER");
            telemetry.addData("Launch Speed", robot.getLauncherSpeed());
            telemetry.addData("Launch Target", launcherTargetSpeed + "(" + (int)((launcherTargetSpeed / robot.getLaunchMotorMaxSpeed()) * 100) + "%)");
            telemetry.addLine("FEEDER");
            telemetry.addData("• Feed Speed", robot.getFeederSpeed());
            telemetry.addData("• Feed Sleep", feederSleep);
            telemetry.addLine("LIMITS");
            telemetry.addData("• Drive Max", maxDriveSpeed);
            telemetry.addData("• Launch Max", robot.getLaunchMotorMaxSpeed());
            telemetry.addLine("ABOUT");
            telemetry.addData("• Version", TeamVersion.VERSION.getFormattedVersion());
            telemetry.addData("• Build", TeamVersion.VERSION.getFormattedBuild());
        }
        telemetry.update();
    }

    public void saveConfig() {
        Properties props = new Properties();
        props.setProperty("launcherTargetSpeed", String.valueOf(launcherTargetSpeed));
        props.setProperty("maxDriveSpeed", String.valueOf(maxDriveSpeed));
        props.setProperty("feederSleep", String.valueOf(feederSleep));

        try (FileOutputStream out = new FileOutputStream(AppUtil.getInstance().getSettingsFile(settingsFilename))) {
            props.store(out, "JV Bot Config");
        } catch (Throwable t) {
            RobotLog.ee("BotSettings", t, "Failed to save %s", settingsFilename);
        }
    }

    private void loadConfig() {
        File file = AppUtil.getInstance().getSettingsFile(settingsFilename);
        if (file.exists()) {
            Properties props = new Properties();
            try (FileInputStream in = new FileInputStream(file)) {
                props.load(in);
                this.launcherTargetSpeed = Double.parseDouble(
                        props.getProperty("launcherTargetSpeed", String.valueOf(launcherTargetSpeed)));
                // Do not load yet. Implement later, if needed.
//                this.maxDriveSpeed = Double.parseDouble(
//                        props.getProperty("maxDriveSpeed", String.valueOf(maxDriveSpeed)));
                this.feederSleep = Long.parseLong(
                        props.getProperty("feederSleep", String.valueOf(feederSleep)));
            } catch (Exception e) {
                RobotLog.ee("BotSettings", e, "Failed to load %s", settingsFilename);
            }
        }
    }

}
