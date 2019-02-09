package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Constants.*;

@TeleOp(name = "GeneralTeleOp", group = "TeleOp")
public class GeneralTeleOp extends OpMode {

    /**
     * Amount of time elapsed
     */
    private ElapsedTime runtime = new ElapsedTime();

    private OmniRobot rb = new OmniRobot();

    private boolean nomServoAlmostDown = false;

    /**
     * This method will be called once when the INIT button is pressed.
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        rb.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
    }

    /**
     * This method will be called once when the PLAY button is first pressed.
     */
    @Override
    public void start() {
        // Reset elapsed time
        runtime.reset();

        // Set initial servo positions
        rb.nomServo.setPosition(NOM_SERVO_IN);
        rb.nomServo2.setPosition(NOM_SERVO_2_IN);
        rb.liftServo.setPosition(LIFT_SERVO_FORWARD);
    }

    /**
     * This method will be called repeatedly in a loop while this op mode is running
     */
    @Override
    public void loop() {
        telemetry.addData("Status", "Looping");

        // Only use the sticks to drive if the dpad is not being pressed
        if (!dpadDrive()) driveChassis();

        moveNomServos();
        moveLiftServo();

        moveSpools();
        moveNom();

        moveLatchWithEncoders();
    }

    /**
     * Drive the chassis according to joysticks
     */
    private void driveChassis() {
        float leftY = -gamepad1.left_stick_y;
        float leftX = gamepad1.left_stick_x;
        float rightX = gamepad1.right_stick_x;

        float leftMagSquared = leftX * leftX + leftY * leftY;

        double forwardPower;
        double rightPower;

        double pow;
        if (gamepad1.right_trigger >= TRIGGER_THRESHOLD) {
            pow = DRIVE_POWER_SLOW;
        } else {
            pow = DRIVE_POWER;
        }

        // Calculate strafe from left stick
        if (leftMagSquared >= DRIVE_LEFT_STICK_THRESHOLD_SQUARED) {
            if (leftY > leftX) {
                if (leftY > -leftX) {
                    // Go forward
                    forwardPower = 1;
                    rightPower = 0;
                } else {
                    // Go left
                    forwardPower = 0;
                    rightPower = -1;
                }
            } else {
                if (leftY > -leftX) {
                    // Go right
                    forwardPower = 0;
                    rightPower = 1;
                } else {
                    // Go backwards
                    forwardPower = -1;
                    rightPower = 0;
                }
            }
        } else {
            forwardPower = 0;
            rightPower = 0;
        }

        double clockwisePower;

        if (Math.abs(rightX) > DRIVE_STICK_THRESHOLD) {
            clockwisePower = rightX;
        } else {
            clockwisePower = 0;
        }

        forwardPower *= pow;
        rightPower *= pow;
        clockwisePower *= pow;

        rb.drive(forwardPower, rightPower, clockwisePower);
    }

    /**
     * Use the dpad to drive with a rotated reference frame so that the latch is forward
     */
    private boolean dpadDrive() {

        double pow;
        if (gamepad1.right_trigger >= TRIGGER_THRESHOLD) {
            pow = DRIVE_POWER_SLOW;
        } else {
            pow = DRIVE_POWER;
        }

        if (gamepad1.dpad_up) {
            rb.drive(0, -pow, 0);
        } else if (gamepad1.dpad_right) {
            rb.drive(pow, 0, 0);
        } else if (gamepad1.dpad_down) {
            rb.drive(0, pow, 0);
        } else if (gamepad1.dpad_left) {
            rb.drive(-pow, 0, 0);
        } else {
            return false;
        }
        return true;
    }

    /**
     * Move the nom servos according to the controller input
     */
    private void moveNomServos() {
        if (gamepad2.a) {
            rb.nomServo.setPosition(NOM_SERVO_ALMOST_DOWN);
            rb.nomServo2.setPosition(NOM_SERVO_2_ALMOST_DOWN);
            nomServoAlmostDown = true;
        } else if (gamepad2.x) {
            rb.nomServo.setPosition(NOM_SERVO_MID);
            rb.nomServo2.setPosition(NOM_SERVO_2_MID);
            nomServoAlmostDown = false;
        } else if (gamepad2.y) {
            rb.nomServo.setPosition(NOM_SERVO_DUMP);
            rb.nomServo2.setPosition(NOM_SERVO_2_DUMP);
            nomServoAlmostDown = false;
        } else if (gamepad2.right_trigger > 0.4) {
            rb.nomServo.setPosition(NOM_SERVO_IN);
            rb.nomServo2.setPosition(NOM_SERVO_2_IN);
            nomServoAlmostDown = false;
        } else if (nomServoAlmostDown) {
            rb.nomServo.setPosition(NOM_SERVO_DOWN);
            rb.nomServo2.setPosition(NOM_SERVO_2_DOWN);
            nomServoAlmostDown = false;
        }
    }

    /**
     * Move the lift servo according to the controller input
     */
    private void moveLiftServo() {
        if (gamepad2.dpad_left) {
            rb.liftServo.setPosition(LIFT_SERVO_BACK);
        } else if (gamepad2.dpad_right) {
            rb.liftServo.setPosition(LIFT_SERVO_FORWARD);
        }
    }

    /**
     * Move the latch according to the controller input
     */
    public void moveLatch() {
        if (gamepad1.left_bumper) {
            rb.latch.setPower(-1);
        } else if (gamepad1.right_bumper) {
            rb.latch.setPower(1);
        } else {
            rb.latch.setPower(0);
        }
    }

    /**
     * Move the latch according to the controller input, limiting the range using encoders
     */
    public void moveLatchWithEncoders() {
        double latchPosition = rb.latch.getCurrentPosition();
        if (gamepad1.left_bumper && latchPosition >= -COME_DOWN_ENCVAL + LATCH_ALLOWANCE) {
            rb.latch.setPower(-1);
        } else if (gamepad1.right_bumper && latchPosition <= -LATCH_ALLOWANCE) {
            rb.latch.setPower(1);
        } else {
            rb.latch.setPower(0);
        }
    }

    /**
     * Move the horizontal and vertical spools according to controller input
     */
    public void moveSpools() {
        rb.horizontalSpool.setPower(OmniRobot.applyThreshold(gamepad2.left_stick_x, .75) * 0.5);
        rb.verticalSpool.setPower(OmniRobot.applyThreshold(-gamepad2.right_stick_y, .75));
    }

    /**
     * Move the intake motor according to controller input
     */
    public void moveNom() {
        if (gamepad2.left_bumper) {
            rb.nomMotor.setPower(-1);
        } else if (gamepad2.right_bumper) {
            rb.nomMotor.setPower(1);
        } else {
            rb.nomMotor.setPower(0);
        }
    }
}
