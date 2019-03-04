package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Constants.*;

/**
 * TeleOp mode identitical to General Tele Op, except everything is handled by just one controller.
 *
 * Complete Controller Map:
 * Left
 * ======
 * joystick = move and strafe around
 * D-pad
 *   * Up = elevator up
 *   * down = elevator down
 *   * left = nom arm in
 *   * right = nom arm out
 * trigger
 *    * ON --> SLOW MODE, LATCH DIRECTION DOWN
 *    * OFF --> REGULAR POWER, LATCH DIRECTION UP
 * bumper
 *    * If left trigger on, move latch down
 *    * else move latch up
 *
 * Right
 * ======
 * joystick = rotate around
 * X = lift
 *    * right trigger on = lift basket goes into lander
 *    * right trigger off = lift basket goes to stationary upright
 * Y = nom to flipped over position
 * A = nom to medium height
 * B = nom to touching the ground
 * Y & B = nom to original touching the elevator position
 * trigger
 *     * ON --> NOM OUT, LIFT BOX FLIPPED OVER
 *     * OFF --> NOM IN, LIFT BOX STATIONARY
 * bumper
 *     * if right trigger on, nom out
 *     * else nom in
 */
@TeleOp(name = "AllForOneTeleOp", group = "TeleOp")
public class AllForOneTeleOp extends OpMode {
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

        // in order of most important to least important

        /* Robot Movement */
        driveChassis();

        /* Nom Arm/Intake */
        // arm
        moveArmSpool();

        // basket
        moveNomServos();

        // ellipse
        moveNom();

        /* Elevator and Placement */
        moveElevatorSpool();

        moveLiftServo();

        /* Latch */
        moveLatchWithEncoders();
    }

    /**
     * Drive the chassis according to left joystick
     */
    private void driveChassis() {
        float leftY = -gamepad1.left_stick_y;
        float leftX = gamepad1.left_stick_x;
        float rightX = gamepad1.right_stick_x;

        float leftMagSquared = leftX * leftX + leftY * leftY;

        double forwardPower;
        double rightPower;

        double pow;
        if (gamepad1.left_trigger >= TRIGGER_THRESHOLD) {
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
     * Move the nom servos according to the controller input
     */
    private void moveNomServos() {
        if (gamepad1.b) {
            rb.nomServo.setPosition(NOM_SERVO_ALMOST_DOWN);
            rb.nomServo2.setPosition(NOM_SERVO_2_ALMOST_DOWN);
            nomServoAlmostDown = true;
        } else if (gamepad1.a) {
            rb.nomServo.setPosition(NOM_SERVO_MID);
            rb.nomServo2.setPosition(NOM_SERVO_2_MID);
            nomServoAlmostDown = false;
        } else if (gamepad1.y) {
            rb.nomServo.setPosition(NOM_SERVO_DUMP);
            rb.nomServo2.setPosition(NOM_SERVO_2_DUMP);
            nomServoAlmostDown = false;
        } else if (gamepad1.y && gamepad1.b) {
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
        boolean lift_on = gamepad1.x;
        boolean right_trigger = (gamepad1.right_trigger > TRIGGER_THRESHOLD);

        if (lift_on) {
            // if the trigger is on, then the lift basket will go into the lander
            if (right_trigger) {
                rb.liftServo.setPosition(LIFT_SERVO_BACK);
            }
            // otherwise move back to the stationary waiting position
            else {
                rb.liftServo.setPosition(LIFT_SERVO_FORWARD);
            }
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
        boolean left_trigger = (gamepad1.left_trigger > TRIGGER_THRESHOLD);
        boolean left_bumper = gamepad1.left_bumper;

        double latchPosition = rb.latch.getCurrentPosition();


        // move upwards if the right trigger is off and the left bumper is being pressed
        if ((!left_trigger && left_bumper) && (latchPosition >= -COME_DOWN_ENCVAL + LATCH_ALLOWANCE)) {
            rb.latch.setPower(-1);
        }
        // move downwards if the right trigger is on and the left bumper is being pressed
        else if ((left_trigger && left_bumper) && latchPosition <= -LATCH_ALLOWANCE) {
            rb.latch.setPower(1);
        }
        // otherwise don't move
        else {
            rb.latch.setPower(0);
        }
    }

    public void moveArmSpool() {
        boolean dpad_left = gamepad1.dpad_left;
        boolean dpad_right = gamepad1.dpad_right;

        double default_power = 0.5;

        if (dpad_left) {
            rb.horizontalSpool.setPower(default_power);
        }
        if (dpad_right) {
            rb.horizontalSpool.setPower(default_power);
        }

        // since only one can ever be on at the same time, check whether both are false
        if (!(dpad_left && dpad_right)) {
            rb.horizontalSpool.setPower(0);
        }
    }

    public void moveElevatorSpool() {
        boolean dpad_up = gamepad1.dpad_up;
        boolean dpad_down = gamepad1.dpad_down;

        double default_power = 0.75;

        if (dpad_up) {
            // move it upwards
            rb.verticalSpool.setPower(default_power);
        }
        if (dpad_down) {
            // move it downwards
            rb.verticalSpool.setPower(-default_power);
        }

        // since only one can ever be on at the same time, check whether both are false
        if (!(dpad_up && dpad_down)) {
            rb.verticalSpool.setPower(0);
        }

    }

    /**
     * Move the intake motor according to controller input
     */
    public void moveNom() {
        boolean nom_bumper = gamepad1.right_bumper;
        boolean right_trigger = (gamepad1.right_trigger > TRIGGER_THRESHOLD);

        if (nom_bumper) {
            if (right_trigger) {
                // nom outwards
                rb.nomMotor.setPower(-1);
            }
            else {
                // nom inwards to take in stuff
                rb.nomMotor.setPower(1);
            }
        }
        else {
            rb.nomMotor.setPower(0);
        }
    }
}
