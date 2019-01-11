package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="OmniAutoDepot", group="Autonomous")
public class OmniAutoDepot extends LinearOpMode {

    public static final int CONVERSION_FACTOR = 44000;

//    private Servo liftServo = null;
    private DcMotor latch = null;

    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    private Servo nomServo = null;

    public static final double POWER = 0.5;
    public static final double THRESHOLD = 0.25;

    public static final double NOM_SERVO_UP = 0.1;
    public static final double NOM_SERVO_DOWN = 0.8;

    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {

        TFDetectionTwo tf = new TFDetectionTwo(telemetry, this);

        latch = hardwareMap.get(DcMotor.class, "LATCH");

        latch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        latch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        latch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeftDrive = hardwareMap.get(DcMotor.class, "FL");
        frontRightDrive = hardwareMap.get(DcMotor.class, "FR");
        backLeftDrive = hardwareMap.get(DcMotor.class, "BL");
        backRightDrive = hardwareMap.get(DcMotor.class, "BR");
        nomServo = hardwareMap.get(Servo.class, "NS");

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//        tf.initialize();

        waitForStart();

        nomServo.setPosition(NOM_SERVO_UP);

        runLatchToPosition(22700);
        driveRightForSeconds(0.4, 0.4);
        driveForwardForSeconds(0.5, 0.5);
//        runLatchToPosition(0);
        driveRightForSeconds(0.4, 1);
        driveForwardForSeconds(0.4, 1);
        turnClockwise(-0.6, 0.2);
        driveForwardForSeconds(0.4, 1.5);
        nomServo.setPosition(NOM_SERVO_DOWN);
        waitForSeconds(0.5);
        driveForwardForSeconds(-0.4, 1.5);
        nomServo.setPosition(NOM_SERVO_UP);
        driveRightForSeconds(-0.4, 0.2);
        turnClockwise(-0.6, 0.75);
        driveForwardForSeconds(0.4, 3);
        nomServo.setPosition(0.5);


//        int tfResult = tf.detect();
//        if (tfResult == 0) {
//            sampleLeft();
//        } else if (tfResult == 1) {
//            sampleCenter();
//        } else {
//            sampleRight();
//        }
//        driveTest();

    }

    public void runLatchToPosition(int targetPosition) {
        if (targetPosition > latch.getCurrentPosition()) {
            latch.setPower(1);
            while (opModeIsActive() && targetPosition > latch.getCurrentPosition()) {
                telemetry.addData("Going", true);
                telemetry.addData("Position", latch.getCurrentPosition());
                telemetry.update();
            }
            latch.setPower(0);
        } else if (targetPosition < latch.getCurrentPosition()) {
            latch.setPower(-1);
            while (opModeIsActive() && targetPosition < latch.getCurrentPosition()) {
                telemetry.addData("Going", true);
                telemetry.addData("Position", latch.getCurrentPosition());
                telemetry.update();
            }
            latch.setPower(0);
        }
    }

    void driveForwardForSeconds(double power, double seconds) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(-power);
        backRightDrive.setPower(-power);

        waitForSeconds(seconds);

        frontLeftDrive.setPower(0);
        backLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    void driveRightForSeconds(double power, double seconds) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(-power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(-power);

        waitForSeconds(seconds);

        frontLeftDrive.setPower(0);
        backLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    void turnClockwise(double power, double seconds) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(power);

        waitForSeconds(seconds);

        frontLeftDrive.setPower(0);
        backLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    void waitForSeconds(double seconds) {
        runtime.reset();
        //noinspection StatementWithEmptyBody
        while (runtime.seconds() < seconds && opModeIsActive()) {}
    }

    void driveTest() {
        driveForwardForSeconds(POWER, 1);
        driveForwardForSeconds(-POWER, 1);
        driveRightForSeconds(POWER, 1);
        driveRightForSeconds(-POWER, 1);
        turnClockwise(POWER, 1);
        turnClockwise(-POWER, 1);
    }
    
    void sampleLeft() {

    }

    void sampleCenter() {

    }

    void sampleRight() {

    }
}
