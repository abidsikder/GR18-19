package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="EncoderTest", group="Encoder Test")
public class EncoderTest extends LinearOpMode {

    public static final int CONVERSION_FACTOR = 44000;

//    private Servo liftServo = null;
    private DcMotor motor = null;

    public String NAME = "LATCH";

    @Override
    public void runOpMode() throws InterruptedException {

        motor = hardwareMap.get(DcMotor.class, NAME);

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Motor busy", motor.isBusy());
        telemetry.update();

//        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        boolean leftBumperWasPressed = false;
        boolean rightBumperWasPressed = false;
        boolean bWasPressed = false;

        int targetPosition = 0;

        while (opModeIsActive()) {
            if (gamepad1.left_bumper) {
                if (!leftBumperWasPressed) {
                    leftBumperWasPressed = true;
                    targetPosition -= 1000;
                }
            } else {
                leftBumperWasPressed = false;
            }

            if (gamepad1.right_bumper) {
                if (!rightBumperWasPressed) {
                    rightBumperWasPressed = true;
                    targetPosition += 1000;
                }
            } else {
                rightBumperWasPressed = false;
            }

            if (gamepad1.b) {
                if (!bWasPressed) {
                    bWasPressed = true;
                    telemetry.addData("Target position", targetPosition);
                    if (targetPosition > motor.getCurrentPosition()) {
                        motor.setPower(1);
                        while (opModeIsActive() && targetPosition > motor.getCurrentPosition()) {
                            telemetry.addData("Going", true);
                            telemetry.addData("Position", motor.getCurrentPosition());
                            telemetry.update();
                        }
                        motor.setPower(0);
                    } else if (targetPosition < motor.getCurrentPosition()) {
                        motor.setPower(-1);
                        while (opModeIsActive() && targetPosition < motor.getCurrentPosition()) {
                            telemetry.addData("Going", true);
                            telemetry.addData("Position", motor.getCurrentPosition());
                            telemetry.update();
                        }
                        motor.setPower(0);
                    }
                }
            } else {
                bWasPressed = false;
            }

            telemetry.addData("Target position", targetPosition);
            telemetry.addData("Going", false);
            telemetry.addData("Position", motor.getCurrentPosition());
            telemetry.update();

        }
    }
}
