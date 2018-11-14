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

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotor horizontalSpool = null;
    private DcMotor nomMotor = null;
    private Servo nomServo = null;
    public static final double POWER = 0.5;
    public static final double THRESHOLD = 0.25;

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

        horizontalSpool = hardwareMap.get(DcMotor.class, "HS");
        nomMotor = hardwareMap.get(DcMotor.class, "NM");
        nomServo = hardwareMap.get(Servo.class, "NS");

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
        horizontalSpool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
        nomServo.setPosition(0.5);
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

    @Override
    public void loop() {
        drive();

        horizontalSpool.setPower(applyThreshold(gamepad2.left_stick_x*0.5));
        if (gamepad2.dpad_left) {
            nomMotor.setPower(-1);
        } else if (gamepad2.dpad_right) {
            nomMotor.setPower(1);
        } else {
            nomMotor.setPower(0);
        }

        if (gamepad2.x) {
            nomServo.setPosition(0.1);
        } else if (gamepad2.y) {
            nomServo.setPosition(0.5);
        } else if (gamepad2.b) {
            nomServo.setPosition(0.9);
        }
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
