package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import static org.firstinspires.ftc.teamcode.OmniTeleOp.*;


/**
 * Test opmode for omni chassis
 */

@TeleOp(name = "UpDownAuto", group = "Autonomous")
public class UpDownAuto extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotor horizontalSpool = null;
    private DcMotor verticalSpool = null;
    private DcMotor nomMotor = null;
    private Servo nomServo = null;
    private Servo liftServo = null;
    private DcMotor latch = null;
    // 0.1   0.3   0.9

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftDrive = hardwareMap.get(DcMotor.class, "FL");
        frontRightDrive = hardwareMap.get(DcMotor.class, "FR");
        backLeftDrive = hardwareMap.get(DcMotor.class, "BL");
        backRightDrive = hardwareMap.get(DcMotor.class, "BR");

        horizontalSpool = hardwareMap.get(DcMotor.class, "HS");
        verticalSpool = hardwareMap.get(DcMotor.class, "VS");
        nomMotor = hardwareMap.get(DcMotor.class, "NM");
        nomServo = hardwareMap.get(Servo.class, "NS");
        liftServo = hardwareMap.get(Servo.class, "LS");

        latch = hardwareMap.get(DcMotor.class, "LATCH");

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

        waitForStart();

        runtime.reset();
        nomServo.setPosition(NOM_SERVO_UP);
        liftServo.setPosition(LIFT_SERVO_FORWARD);

        latch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        latch.setTargetPosition(1);
        latch.setPower(1);
    }

    public static double applyThreshold(double d) {
        if (!(d >= THRESHOLD || d <= -THRESHOLD)) {
            return 0;
        } else {
            return d;
        }
    }

}
