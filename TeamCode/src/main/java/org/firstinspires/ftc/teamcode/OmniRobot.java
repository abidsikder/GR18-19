package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Contains the Omni robot's hardware
 */
public class OmniRobot {

    // --------------------------------------
    // Declare motors

    DcMotor flMotor = null;
    DcMotor frMotor = null;
    DcMotor blMotor = null;
    DcMotor brMotor = null;

    DcMotor horizontalSpool = null;
    DcMotor verticalSpool = null;

    DcMotor nomMotor = null;

    Servo nomServo = null;
    Servo nomServo2 = null;
    Servo liftServo = null;

    DcMotor latch = null;

    /**
     * Initialize all motors and servos
     */
    void init(HardwareMap hardwareMap) {
        // Initialize drive motors
        flMotor = hardwareMap.get(DcMotor.class, "FL");
        frMotor = hardwareMap.get(DcMotor.class, "FR");
        blMotor = hardwareMap.get(DcMotor.class, "BL");
        brMotor = hardwareMap.get(DcMotor.class, "BR");

        // Initialize servos
        nomServo = hardwareMap.get(Servo.class, "NS");
        nomServo2 = hardwareMap.get(Servo.class, "NS2");
        liftServo = hardwareMap.get(Servo.class, "LS");

        // Initialize spool motors
        verticalSpool = hardwareMap.get(DcMotor.class, "VS");
        horizontalSpool = hardwareMap.get(DcMotor.class, "HS");

        // Initialize intake motor
        nomMotor = hardwareMap.get(DcMotor.class, "NOM");

        // Initialize latch motor
        latch = hardwareMap.get(DcMotor.class, "LATCH");
        latch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set motor directions
        flMotor.setDirection(DcMotor.Direction.FORWARD);
        frMotor.setDirection(DcMotor.Direction.REVERSE);
        blMotor.setDirection(DcMotor.Direction.FORWARD);
        brMotor.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to brake when power is zero
        flMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        blMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        brMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        latch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontalSpool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        verticalSpool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Set the motors to drive
     */
    void drive(double forwardPower, double rightPower, double clockwisePower) {
        forwardPower = Range.clip(forwardPower, -1, 1);
        rightPower = Range.clip(rightPower, -1, 1);
        clockwisePower = Range.clip(clockwisePower, -1, 1);

        flMotor.setPower(forwardPower + rightPower + clockwisePower);
        frMotor.setPower(-forwardPower + rightPower + clockwisePower);
        blMotor.setPower(forwardPower - rightPower + clockwisePower);
        brMotor.setPower(-forwardPower - rightPower + clockwisePower);
    }

    /**
     * Stop the drive motors
     */
    void driveStop() {
        flMotor.setPower(0);
        frMotor.setPower(0);
        blMotor.setPower(0);
        brMotor.setPower(0);

    }
}
