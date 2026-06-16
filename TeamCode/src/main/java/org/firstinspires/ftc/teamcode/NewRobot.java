package org.firstinspires.ftc.teamcode;

import static com.seattlesolvers.solverslib.hardware.motors.CRServoEx.RunMode.RawPower;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

public class NewRobot extends SubsystemBase {

    private final MecanumDrive drivetrain;
    private MotorEx launchMotor;
    private CRServoEx leftFeeder;
    private CRServoEx rightFeeder;

    public NewRobot(HardwareMap hardwareMap, String flM, String frM, String blM, String brM) {
        MotorEx frontLeft = new MotorEx(hardwareMap, flM, Motor.GoBILDA.RPM_312);
        MotorEx frontRight = new MotorEx(hardwareMap, frM, Motor.GoBILDA.RPM_312);
        MotorEx backLeft = new MotorEx(hardwareMap, blM, Motor.GoBILDA.RPM_312);
        MotorEx backRight = new MotorEx(hardwareMap, brM, Motor.GoBILDA.RPM_312);

        frontLeft.setInverted(false);
        frontRight.setInverted(true);
        backLeft.setInverted(false);
        backRight.setInverted(true);

        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        frontLeft.stopAndResetEncoder();
        frontRight.stopAndResetEncoder();
        backLeft.stopAndResetEncoder();
        backRight.stopAndResetEncoder();

        frontLeft.setRunMode(Motor.RunMode.VelocityControl);
        frontRight.setRunMode(Motor.RunMode.VelocityControl);
        backLeft.setRunMode(Motor.RunMode.VelocityControl);
        backRight.setRunMode(Motor.RunMode.VelocityControl);

        drivetrain = new MecanumDrive(false, frontLeft, frontRight, backLeft, backRight);

        launchMotor = new MotorEx(hardwareMap, "launchMotor", Motor.GoBILDA.BARE);
        launchMotor.setInverted(true);
        launchMotor.setRunMode(Motor.RunMode.RawPower);
        launchMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
        launchMotor.stopAndResetEncoder();

        leftFeeder = new CRServoEx(hardwareMap, "leftFeeder");
        leftFeeder.setRunMode(RawPower);
        rightFeeder = new CRServoEx(hardwareMap, "rightFeeder");
        rightFeeder.setRunMode(RawPower);
    }

    public void driveRobotCentric(GamepadEx gamepad, double limiter) {
        double strafeSpeed = gamepad.getLeftX() * limiter;
        double forwardSpeed = -gamepad.getLeftY() * limiter;
        double turnSpeed = -gamepad.getRightX() * limiter;
        drivetrain.driveRobotCentric(strafeSpeed, forwardSpeed, turnSpeed, true);
    }

    public void setDriveMaxSpeed(double maxSpeed) {
        drivetrain.setMaxSpeed(maxSpeed);
    }

    public void setLauncherSpeed(double launcherSpeed) {
        launchMotor.setVelocity(launcherSpeed);
    }

    public double getLauncherSpeed() {
        return Math.abs(launchMotor.getVelocity());
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
