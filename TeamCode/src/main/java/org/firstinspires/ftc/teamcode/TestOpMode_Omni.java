package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * Test opmode for omni chassis
 */

@TeleOp(name = "Test: Omni OpMode", group = "Omni Opmode")
public class TestOpMode_Omni extends OpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;
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
    }

//    /*
//     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
//     */
//    @Override
//    public void loop() {
//        if (gamepad1.dpad_up) {
//            telemetry.addData("direction", "up");
//            frontLeftDrive.setPower(POWER);
//            frontRightDrive.setPower(-POWER);
//            backLeftDrive.setPower(POWER);
//            backRightDrive.setPower(-POWER);
//        } else if (gamepad1.dpad_down) {
//            telemetry.addData("direction", "down");
//            frontLeftDrive.setPower(-POWER);
//            frontRightDrive.setPower(POWER);
//            backLeftDrive.setPower(-POWER);
//            backRightDrive.setPower(POWER);
//        } else if (gamepad1.dpad_left) {
//            telemetry.addData("direction", "left");
//            frontLeftDrive.setPower(-POWER);
//            frontRightDrive.setPower(-POWER);
//            backLeftDrive.setPower(POWER);
//            backRightDrive.setPower(POWER);
//        } else if (gamepad1.dpad_right) {
//            telemetry.addData("direction", "right");
//            frontLeftDrive.setPower(POWER);
//            frontRightDrive.setPower(POWER);
//            backLeftDrive.setPower(-POWER);
//            backRightDrive.setPower(-POWER);
//        } else if (gamepad1.left_bumper) {
//            telemetry.addData("direction", "turn left");
//            frontLeftDrive.setPower(-POWER);
//            frontRightDrive.setPower(-POWER);
//            backLeftDrive.setPower(-POWER);
//            backRightDrive.setPower(-POWER);
//        } else if (gamepad1.right_bumper) {
//            telemetry.addData("direction", "turn right");
//            frontLeftDrive.setPower(POWER);
//            frontRightDrive.setPower(POWER);
//            backLeftDrive.setPower(POWER);
//            backRightDrive.setPower(POWER);
//        } else {
//            telemetry.addData("direction", "none");
//            frontLeftDrive.setPower(0);
//            frontRightDrive.setPower(0);
//            backLeftDrive.setPower(0);
//            backRightDrive.setPower(0);
//        }
//        telemetry.update();
//    }
    @Override
    public void loop() {

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
