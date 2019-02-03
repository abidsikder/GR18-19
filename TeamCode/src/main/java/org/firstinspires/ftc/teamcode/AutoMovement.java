package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class AutoMovement {

    private final LinearOpMode opMode;

    public static final double POWER = 0.5;
    public static final double THRESHOLD = 0.25;

    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    private DcMotor latch = null;
    private ElapsedTime runtime = null;

    public AutoMovement(DcMotor frontLeftDrive, DcMotor frontRightDrive, DcMotor backLeftDrive, DcMotor backRightDrive, DcMotor latch, ElapsedTime runtime, LinearOpMode opMode) {
        this.frontLeftDrive = frontLeftDrive;
        this.backLeftDrive = backLeftDrive;
        this.frontRightDrive = frontRightDrive;
        this.backRightDrive = backRightDrive;

        this.latch = latch;

        this.opMode = opMode;
        this.runtime = runtime;
    }

    public void init() {
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void driveForwardForSeconds(double power, double seconds) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(-power);
        backRightDrive.setPower(-power);

        waitForSeconds(seconds);

        frontLeftDrive.setPower(0);
        backLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    public void driveRightForSeconds(double power, double seconds) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(-power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(-power);

        waitForSeconds(seconds);

        frontLeftDrive.setPower(0);
        backLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    public void turnClockwise(double power, double seconds) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(power);

        waitForSeconds(seconds);

        frontLeftDrive.setPower(0);
        backLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    public void waitForSeconds(double seconds) {
        runtime.reset();
        //noinspection StatementWithEmptyBody
        while (runtime.seconds() < seconds && opMode.opModeIsActive()) {
        }
    }

    public void driveTest() {
        driveForwardForSeconds(POWER, 1);
        driveForwardForSeconds(-POWER, 1);
        driveRightForSeconds(POWER, 1);
        driveRightForSeconds(-POWER, 1);
        turnClockwise(POWER, 1);
        turnClockwise(-POWER, 1);
    }

    public void runLatchToPosition(int targetPosition) {
        if (targetPosition > latch.getCurrentPosition()) {
            latch.setPower(1);
            while (opMode.opModeIsActive() && targetPosition > latch.getCurrentPosition()) {
                opMode.telemetry.addData("Going", true);
                opMode.telemetry.addData("Position", latch.getCurrentPosition());
                opMode.telemetry.update();
            }
            latch.setPower(0);
        } else if (targetPosition < latch.getCurrentPosition()) {
            latch.setPower(-1);
            while (opMode.opModeIsActive() && targetPosition < latch.getCurrentPosition()) {
                opMode.telemetry.addData("Going", true);
                opMode.telemetry.addData("Position", latch.getCurrentPosition());
                opMode.telemetry.update();
            }
            latch.setPower(0);
        }
    }
}