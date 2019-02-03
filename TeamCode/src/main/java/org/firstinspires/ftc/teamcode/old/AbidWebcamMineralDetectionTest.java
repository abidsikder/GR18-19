package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
@Autonomous(name="AbidWebcamMineralDetectionTest", group="Autonomous")
public class AbidWebcamMineralDetectionTest extends LinearOpMode {

    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    public static final double POWER = 0.5;
    public static final double THRESHOLD = 0.25;

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY =
            "ATUUQRD/////AAABmWvkn/HpKkiTkmH+kqcdQa87+E5nnizSTMHex9sTxsSbub3m/AzfdamdYGP7pwr/6Ea3A5aHYC35fc9Nw8wFLofmMHwKHSwnm6wC/kS6oEspjXxlk7p3YKgHpe9iWIuvYVHDI211sVIxCg+wd8DvtdoFulhQ+dLLSajTNryZpsKgOJRHKnq4KREOb3jticHQpvTWDrM3O3yya3F5KEOBUr5ekhLxz06M7VpmIeuCc6FTw3RxRQ6qtqKfXxCzCK0ziyyDyMlBCie0WH1gvI1kKhk3modRIaJfaTcAw54REWyTfIhhV3A4Nyp/99j1FYonm94fu/gvOemiGDI1WWotAOSYqxnLmru7vN7kSzlsKits";

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {

        // Driving Initializer Code
        frontLeftDrive = hardwareMap.get(DcMotor.class, "FL");
        frontRightDrive = hardwareMap.get(DcMotor.class, "FR");
        backLeftDrive = hardwareMap.get(DcMotor.class, "BL");
        backRightDrive = hardwareMap.get(DcMotor.class, "BR");

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();


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

    // functions copy-pasted from the webcam detection tester


}
