package org.firstinspires.ftc.teamcode.samples;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.util.TelemetryData;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name = "Pedro TeleOp Sample", group = "Samples")
@Disabled
public class PedroTeleOpSample extends CommandOpMode {

    Follower follower;
    TelemetryData telemetryData = new TelemetryData(telemetry);

    @Override
    public void initialize() {
        follower = Constants.createFollower(hardwareMap);
        super.reset();

        follower.startTeleopDrive();
    }

    @Override
    public void run() {
        super.run();

        /* Robot-Centric Drive
        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        */

        // Field-Centric Drive
        follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, false);
        follower.update();

        telemetryData.addData("X", follower.getPose().getX());
        telemetryData.addData("Y", follower.getPose().getY());
        telemetryData.addData("Heading", follower.getPose().getHeading());
        telemetryData.update();
    }

}