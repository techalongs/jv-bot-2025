package org.firstinspires.ftc.teamcode;

import static com.seattlesolvers.solverslib.hardware.motors.CRServoEx.RunMode.RawPower;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.drivebase.DifferentialDrive;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

/**
 * Robot represents the hardware and physical configuration of the bot.
 */
public class Robot {

    private MotorEx backRight;
    private MotorEx backLeft;
    private MotorEx launchMotor;
    private CRServoEx leftFeeder;
    private CRServoEx rightFeeder;
    private DifferentialDrive drivetrain;

    public Robot(HardwareMap hardwareMap) {
        backLeft = new MotorEx(hardwareMap, "backLeft", Motor.GoBILDA.RPM_312);
        backLeft.setRunMode(Motor.RunMode.VelocityControl);
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backLeft.stopAndResetEncoder();

        backRight = new MotorEx(hardwareMap, "backRight", Motor.GoBILDA.RPM_312);
        backRight.setRunMode(Motor.RunMode.VelocityControl);
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backRight.stopAndResetEncoder();

        drivetrain = new DifferentialDrive(backLeft, backRight);

        launchMotor = new MotorEx(hardwareMap, "launchMotor", Motor.GoBILDA.RPM_312);
        launchMotor.setRunMode(Motor.RunMode.RawPower);
        launchMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        launchMotor.stopAndResetEncoder();

        leftFeeder = new CRServoEx(hardwareMap, "leftFeeder");
        leftFeeder.setRunMode(RawPower);
        rightFeeder = new CRServoEx(hardwareMap, "rightFeeder");
        rightFeeder.setInverted(true);
        rightFeeder.setRunMode(RawPower);
    }

    public void arcadeDrive(double forwardSpeed, double turnSpeed, boolean squareInputs) {
        drivetrain.arcadeDrive(forwardSpeed, turnSpeed, squareInputs);
    }

    public void setDriveMaxSpeed(double maxSpeed) {
        drivetrain.setMaxSpeed(maxSpeed);
    }

    public void setLauncherSpeed(double launcherSpeed) {
        launchMotor.setVelocity(launcherSpeed);
    }

    public double getLauncherSpeed() {
        return launchMotor.getVelocity();
    }

    public double getLaunchMotorMaxSpeed() {
        return launchMotor.ACHIEVABLE_MAX_TICKS_PER_SECOND;
    }

    public void setFeederSpeed(double feederSpeed) {
        leftFeeder.set(feederSpeed);
        rightFeeder.set(feederSpeed);
    }

    public double getFeederSpeed() {
        return leftFeeder.get();
    }

}
