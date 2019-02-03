package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * Main TeleOp OpMode
 */

@TeleOp(name = "OldOmniTeleOp", group = "Linear Opmode")
public class OmniTeleOp extends OpMode {

    // --------------------------------------
    // Latch encoder constants
    public static final int LATCH_RANGE = 23000;
    public static final int LATCH_ALLOWANCE = 25;


    /**
     * Amount of time elapsed
     */
    private ElapsedTime runtime = new ElapsedTime();

    // --------------------------------------
    // Declare motors
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotor horizontalSpool = null;
    private DcMotor verticalSpool = null;
    private DcMotor nomMotor = null;
    private Servo nomServo = null;
    private Servo nomServo2 = null;
    private Servo liftServo = null;
    private DcMotor latch = null;
    public static final double POWER = 0.5;
    public static final double THRESHOLD = 0.25;

    // --------------------------------------
    // Servo Positions
    public static final double LIFT_SERVO_FORWARD = .15;
    public static final double LIFT_SERVO_MID = 0.35;
    public static final double LIFT_SERVO_BACK = 0.75;

    public static final double NOM_SERVO_DUMP = 0.6;
    public static final double NOM_SERVO_IN = 0.75;
    public static final double NOM_SERVO_MID = 0.4;
    public static final double NOM_SERVO_ALMOST_DOWN = 0.25;
    public static final double NOM_SERVO_DOWN = 0.19;


    public static final double NOM_SERVO_2_DUMP = .39;
    public static final double NOM_SERVO_2_IN = 0.29;
    public static final double NOM_SERVO_2_MID = 0.61;
    public static final double NOM_SERVO_2_ALMOST_DOWN = 0.76;
    public static final double NOM_SERVO_2_DOWN = 0.81;

    /*
     * NS2 positions
     * IN: .29
     * DUMP: .39
     * MID: .61
     * ALOW: .76
     * DLOW: .81
     *
     * */

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize drive motors
        frontLeftDrive = hardwareMap.get(DcMotor.class, "FL");
        frontRightDrive = hardwareMap.get(DcMotor.class, "FR");
        backLeftDrive = hardwareMap.get(DcMotor.class, "BL");
        backRightDrive = hardwareMap.get(DcMotor.class, "BR");

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

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        latch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontalSpool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        verticalSpool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
        nomServo.setPosition(NOM_SERVO_IN);
        nomServo2.setPosition(NOM_SERVO_2_IN);
        liftServo.setPosition(LIFT_SERVO_FORWARD);
    }

    public void drive() {
        float leftY = -gamepad1.left_stick_y;
        float leftX = gamepad1.left_stick_x;
        float rightX = gamepad1.right_stick_x;

        double frontLeft = leftY + leftX + rightX;
        double backLeft = leftY - leftX + rightX;
        double frontRight = -leftY + leftX + rightX;
        double backRight = -leftY - leftX + rightX;

        frontLeft = Range.clip(frontLeft, -1, 1);
        frontRight = Range.clip(frontRight, -1, 1);
        backRight = Range.clip(backRight, -1, 1);
        backLeft = Range.clip(backLeft, -1, 1);


        //frontLeft = applyThreshold(frontLeft * POWER);
        //frontRight = applyThreshold(frontRight * POWER);
        //backLeft = applyThreshold(backLeft * POWER);
        //backRight = applyThreshold(backRight * POWER);
        frontLeft = applyThreshold(frontLeft);
        frontRight = applyThreshold(frontRight);
        backLeft = applyThreshold(backLeft);
        backRight = applyThreshold(backRight);


        if (gamepad1.right_trigger > 0.2) {
            frontLeft = frontLeft / 2;
            frontRight = frontRight / 2;
            backLeft = backLeft / 2;
            backRight = backRight / 2;
        }

        frontLeftDrive.setPower(frontLeft);
        frontRightDrive.setPower(frontRight);
        backRightDrive.setPower(backRight);
        backLeftDrive.setPower(backLeft);
    }

    public boolean dpadDrive() {
        double fl = 0;
        double fr = 0;
        double bl = 0;
        double br = 0;
        if (gamepad1.dpad_up) {
            fl = -1;
            fr = -1;
            bl = 1;
            br = 1;
        } else if (gamepad1.dpad_right) {
            fl = 1;
            fr = -1;
            bl = 1;
            br = -1;
        } else if (gamepad1.dpad_down) {
            fl = 1;
            fr = 1;
            bl = -1;
            br = -1;
        } else if (gamepad1.dpad_left) {
            fl = -1;
            fr = 1;
            bl = -1;
            br = 1;
        } else {
            return false;
        }

        frontLeftDrive.setPower(fl);
        frontRightDrive.setPower(fr);
        backLeftDrive.setPower(bl);
        backRightDrive.setPower(br);

        return true;

    }

    public void motorTest() {
        double pow;
        if (gamepad1.b) {
            pow = -1;
        } else {
            pow = 1;
        }
        if (gamepad1.left_trigger > 0.6) {
            frontLeftDrive.setPower(pow);
        } else {
            frontLeftDrive.setPower(0);
        }

        if (gamepad1.right_trigger > 0.6) {
            frontRightDrive.setPower(pow);
        } else {
            frontRightDrive.setPower(0);
        }

        if (gamepad1.right_bumper) {
            backRightDrive.setPower(pow);
        } else {
            backRightDrive.setPower(0);
        }

        if (gamepad1.left_bumper) {
            backLeftDrive.setPower(pow);
        } else {
            backLeftDrive.setPower(0);
        }
    }

    public void moveLatch() {
//        if (gamepad1.left_bumper && latch.getCurrentPosition() > -LATCH_RANGE + LATCH_ALLOWANCE) {
//            latch.setPower(-1);
//        } else if (gamepad1.right_bumper && latch.getCurrentPosition() < -LATCH_ALLOWANCE) {
//            latch.setPower(1);
//        } else {
//            latch.setPower(0);
//        }
        if (gamepad1.left_bumper) {
            latch.setPower(-1);
        } else if (gamepad1.right_bumper) {
            latch.setPower(1);
        } else {
            latch.setPower(0);
        }
    }

    private boolean almostDown = false;

    @Override
    public void loop() {
        if (!dpadDrive()) drive();

        moveLatch();

        // Move spools
        horizontalSpool.setPower(applyThreshold(gamepad2.left_stick_x * 0.5));
        verticalSpool.setPower(applyThreshold(-gamepad2.right_stick_y));

        // Move intake motor
        if (gamepad2.left_bumper) {
            nomMotor.setPower(-1);
        } else if (gamepad2.right_bumper) {
            nomMotor.setPower(1);
        } else {
            nomMotor.setPower(0);
        }

        // Set nom servo position
        if (gamepad2.a) {
            nomServo.setPosition(NOM_SERVO_ALMOST_DOWN);
            nomServo2.setPosition(NOM_SERVO_2_ALMOST_DOWN);

            almostDown = true;
        } else if (gamepad2.x) {
            nomServo.setPosition(NOM_SERVO_MID);
            nomServo2.setPosition(NOM_SERVO_2_MID);

            almostDown = false;
        } else if (gamepad2.y) {
            nomServo.setPosition(NOM_SERVO_DUMP);
            nomServo2.setPosition(NOM_SERVO_2_DUMP);
            almostDown = false;
        } else if (gamepad2.right_trigger > 0.4) {
            nomServo.setPosition(NOM_SERVO_IN);
            nomServo2.setPosition(NOM_SERVO_2_IN);
            almostDown = false;
        } else if (almostDown) {
            nomServo.setPosition(NOM_SERVO_DOWN);
            nomServo2.setPosition(NOM_SERVO_2_DOWN);
            almostDown = false;
        }

        // Set lift servo position
        if (gamepad2.dpad_left) {
            liftServo.setPosition(LIFT_SERVO_BACK);
        } else if (gamepad2.dpad_right) {
            liftServo.setPosition(LIFT_SERVO_FORWARD);
        }
    }

    public static double applyThreshold(double d) {
        if (!(d >= THRESHOLD || d <= -THRESHOLD)) {
            return 0;
        } else {
            return d;
        }
    }

}

/*
* NS2 positions
* IN: .29
* DUMP: .39
* MID: .61
* ALOW: .76
* DLOW: .81
*
* */