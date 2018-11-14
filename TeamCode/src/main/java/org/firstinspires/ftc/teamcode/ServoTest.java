package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ServoTest", group="Linear Opmode")
public class ServoTest extends LinearOpMode {

    private Servo liftServo = null;
    private Servo nomServo = null;

    @Override
    public void runOpMode() throws InterruptedException {

//        liftServo = hardwareMap.get(Servo.class, "lsv");
        nomServo = hardwareMap.get(Servo.class, "NS");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        double nomServoPosition = 0.5;
        boolean leftBumper1Pressed = false;
        boolean rightBumper1Pressed = false;

        while (opModeIsActive()) {

            if (gamepad1.left_bumper) {
                if (!leftBumper1Pressed) {
                    leftBumper1Pressed = true;
                    nomServoPosition -= 0.05;
                    nomServo.setPosition(nomServoPosition);
                }
            } else {
                leftBumper1Pressed = false;
            }
            if (gamepad1.right_bumper) {
                if (!rightBumper1Pressed) {
                    rightBumper1Pressed = true;
                    nomServoPosition += 0.05;
                    nomServo.setPosition(nomServoPosition);
                }
            } else {
                rightBumper1Pressed = false;
            }
            telemetry.addData("nom", nomServoPosition);
            telemetry.update();
//            if (gamepad1.left_bumper) {
//                nomServo.setPosition(0.15);
//            } else if (gamepad1.right_bumper) {
//                nomServo.setPosition(0.85);
//            } else if (gamepad1.a) {
//                nomServo.setPosition(0.5);
//            }

//            if (gamepad1.left_bumper) {
//                nomServo.setPosition(0.15);
//            } else if (gamepad1.right_bumper) {
//                nomServo.setPosition(1);
//            } else if (gamepad1.a) {
//                nomServo.setPosition(0.5);
//            } else if (gamepad1.dpad_left) {
//                nomServo.setPosition(0.325);
//            } else if (gamepad1.dpad_right) {
//                nomServo.setPosition(1);
//            }
//
//
//            if (gamepad2.left_bumper) {
//                liftServo.setPosition(0.15);
//            } else if (gamepad2.right_bumper) {
//                liftServo.setPosition(0.85);
//            } else if (gamepad2.a) {
//                liftServo.setPosition(0.5);
//            } else if (gamepad2.dpad_left) {
//                liftServo.setPosition(0.5875);
//            }
        }
    }
}
