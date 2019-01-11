/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * Basic TeleOp for Hybrid robot.
 * Based on BasicOpMode_Linear.
 * September 24, 2018.
 *
 * Right and left are from the robot's perspective
 */

@Disabled
@TeleOp(name="RaffaHybrid", group="Linear Opmode")
public class RaffaHybrid extends LinearOpMode {

    /**
     * Power threshold for motors
     */
    private static final double DC_MOTOR_THRESHOLD = 0.5;
    private static final double NOM_LOW = 0.8;
    private static final double NOM_MID = 0.4;
    private static final double NOM_HIGH = 0.15;
    private static final double LIFT_LOW = 0.52;
    private static final double LIFT_HIGH = 0.15;

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;
    private DcMotor horizontalSpool = null;
    private DcMotor verticalSpool = null;
    private DcMotor latchMotor = null;
    private DcMotor nomMotor = null;

    private Servo liftServo = null;
    private Servo nomServo = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
        horizontalSpool = hardwareMap.get(DcMotor.class, "hsp");
        verticalSpool = hardwareMap.get(DcMotor.class, "vsp");
        latchMotor = hardwareMap.get(DcMotor.class, "lch");
        nomMotor = hardwareMap.get(DcMotor.class, "nom");
        liftServo = hardwareMap.get(Servo.class, "lsv");
        nomServo = hardwareMap.get(Servo.class, "nsv");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Set motors to brake when power is set to zero.
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontalSpool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        verticalSpool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        nomServo.setPosition(NOM_HIGH);
        liftServo.setPosition(0.15);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;
            double horizontalPower;
            double verticalPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            horizontalPower   = Range.clip(-gamepad2.left_stick_y, -1.0, 1.0) ;
            verticalPower   = Range.clip(-gamepad2.right_stick_y, -1.0, 1.0) ;
//            latchPower   = Range.clip(-gamepad2.right_stick_y, -1.0, 1.0) ;

            // Apply threshold to motor powers so that they don't stall. If power is below threshold, don't run motor at all.
            if (!(leftPower > DC_MOTOR_THRESHOLD || leftPower < -DC_MOTOR_THRESHOLD)) {
                leftPower = 0;
            }
            if (!(rightPower > DC_MOTOR_THRESHOLD || rightPower < -DC_MOTOR_THRESHOLD)) {
                rightPower = 0;
            }
            if (!(horizontalPower > DC_MOTOR_THRESHOLD || horizontalPower < -DC_MOTOR_THRESHOLD)) {
                horizontalPower = 0;
            }
            if (!(verticalPower > DC_MOTOR_THRESHOLD || verticalPower < -DC_MOTOR_THRESHOLD)) {
                verticalPower = 0;
            }

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            frontLeft.setPower(leftPower);
            frontRight.setPower(rightPower);
            backLeft.setPower(leftPower);
            backRight.setPower(rightPower);
            horizontalSpool.setPower(horizontalPower);
            verticalSpool.setPower(verticalPower);
            if (gamepad2.right_bumper) {
                latchMotor.setPower(0.7);
            } else if (gamepad2.left_bumper) {
                latchMotor.setPower(-0.7);
            } else {
                latchMotor.setPower(0);
            }

            if (gamepad2.x) {
                nomServo.setPosition(NOM_LOW);
            } else if (gamepad2.y) {
                nomServo.setPosition(NOM_HIGH);
            } else if (gamepad2.right_trigger > -0.2) {
                nomServo.setPosition(NOM_MID);
            }

            if (gamepad2.dpad_left) {
                liftServo.setPosition(LIFT_LOW);
            } else if (gamepad2.dpad_right) {
                liftServo.setPosition(LIFT_HIGH);
            }

            if (gamepad2.a) {
                nomMotor.setPower(-1);
            } else if (gamepad2.b) {
                nomMotor.setPower(1);
            } else {
                nomMotor.setPower(0);
            }

            // Need to figure out servo positions.
//            if (gamepad2.left_bumper) {
//                liftServo.setPosition(DOWN POSITION???)
//            } else if (gamepad2.right_bumper) {
//                liftServo.setPosition(UP POSITION???)
//            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}
