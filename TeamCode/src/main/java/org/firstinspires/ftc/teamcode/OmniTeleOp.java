package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * Test opmode for omni chassis
 */

@TeleOp(name = "OmniTeleOp", group = "Linear Opmode")
public class OmniTeleOp extends OpMode {

    public static final int LATCH_RANGE = 44000;
    public static final int LATCH_ALLOWANCE = 250;

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;
//    private DcMotor horizontalSpool = null;
//    private DcMotor verticalSpool = null;
//    private DcMotor nomMotor = null;
//    private Servo nomServo = null;
//    private Servo liftServo = null;
    private DcMotor latch = null;
    public static final double POWER = 0.5;
    public static final double THRESHOLD = 0.25;

    public static final double LIFT_SERVO_FORWARD = 0.2;
    public static final double LIFT_SERVO_MID = 0.38;
    public static final double LIFT_SERVO_BACK = 0.55;

    public static final double NOM_SERVO_DOWN_LOW = 0.1;
    public static final double NOM_SERVO_DOWN_HIGH = 0.15;
    public static final double NOM_SERVO_MID = 0.4;
    public static final double NOM_SERVO_UP = 0.9;
    // 0.1   0.3   0.9

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftDrive = hardwareMap.get(DcMotor.class, "FL");
        frontRightDrive = hardwareMap.get(DcMotor.class, "FR");
        backLeftDrive = hardwareMap.get(DcMotor.class, "BL");
        backRightDrive = hardwareMap.get(DcMotor.class, "BR");

//        horizontalSpool = hardwareMap.get(DcMotor.class, "HS");
//        verticalSpool = hardwareMap.get(DcMotor.class, "VS");
//        nomMotor = hardwareMap.get(DcMotor.class, "NM");
//        nomServo = hardwareMap.get(Servo.class, "NS");
//        liftServo = hardwareMap.get(Servo.class, "LS");
        latch = hardwareMap.get(DcMotor.class, "LATCH");
        latch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        latch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        horizontalSpool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
//        nomServo.setPosition(NOM_SERVO_UP);
//        liftServo.setPosition(LIFT_SERVO_FORWARD);
    }

    public void drive() {
        float leftY = -gamepad1.left_stick_y;
        float leftX = gamepad1.left_stick_x;
        float rightX = gamepad1.right_stick_x;

        double frontLeft = -leftY - leftX - rightX;
        double frontRight = leftY - leftX - rightX;
        double backRight = leftY + leftX - rightX;
        double backLeft = -leftY + leftX - rightX;

        frontLeft = Range.clip(frontLeft, -1, 1);
        frontRight = Range.clip(frontRight, -1, 1);
        backRight = Range.clip(backRight, -1, 1);
        backLeft = Range.clip(backLeft, -1, 1);


        frontLeft = applyThreshold(frontLeft * POWER);
        frontRight = applyThreshold(frontRight * POWER);
        backLeft = applyThreshold(backLeft * POWER);
        backRight = applyThreshold(backRight * POWER);

        frontLeftDrive.setPower(frontLeft);
        frontRightDrive.setPower(frontRight);
        backRightDrive.setPower(backRight);
        backLeftDrive.setPower(backLeft);
    }

    public void moveLatch() {
        if (gamepad1.left_bumper && latch.getCurrentPosition() > LATCH_ALLOWANCE) {
            latch.setPower(-1);
        } else if (gamepad1.right_bumper && latch.getCurrentPosition() < LATCH_RANGE - LATCH_ALLOWANCE) {
            latch.setPower(1);
        } else {
            latch.setPower(0);
        }
    }

    @Override
    public void loop() {
        drive();

//        horizontalSpool.setPower(applyThreshold(gamepad2.left_stick_x*0.5));
//        verticalSpool.setPower(applyThreshold(gamepad2.right_stick_x));
//        if (gamepad2.left_bumper) {
//            nomMotor.setPower(-1);
//        } else if (gamepad2.right_bumper) {
//            nomMotor.setPower(1);
//        } else {
//            nomMotor.setPower(0);
//        }
//
//        if (gamepad2.y) {
//            nomServo.setPosition(NOM_SERVO_UP);
//        } else if (gamepad2.x) {
//            nomServo.setPosition(NOM_SERVO_MID);
//        } else if (gamepad2.a) {
//            nomServo.setPosition(NOM_SERVO_DOWN_LOW);
//        } else if (gamepad2.right_trigger > 0.3) {
//            nomServo.setPosition(NOM_SERVO_DOWN_HIGH);
//        }
//
//        if (gamepad2.dpad_down) {
//            liftServo.setPosition(LIFT_SERVO_BACK);
//        } else if (gamepad2.dpad_up) {
//            liftServo.setPosition(LIFT_SERVO_FORWARD);
//        } else if (gamepad2.dpad_left) {
//            liftServo.setPosition(LIFT_SERVO_MID);
//        }
    }

    public static double applyThreshold(double d) {
        if (!(d >= THRESHOLD || d <= -THRESHOLD)) {
            return 0;
        } else {
            return d;
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
