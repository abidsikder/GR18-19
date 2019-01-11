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

@TeleOp(name = "LatchMover", group = "Linear Opmode")
public class LatchMover extends OpMode {

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
    // 0.1   0.3   0.9

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        latch = hardwareMap.get(DcMotor.class, "LATCH");
        latch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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
    }

    public void moveLatch() {
        if (gamepad1.left_bumper) {
            latch.setPower(-1);
        } else if (gamepad1.right_bumper) {
            latch.setPower(1);
        } else {
            latch.setPower(0);
        }
    }

    @Override
    public void loop() {
        moveLatch();

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
