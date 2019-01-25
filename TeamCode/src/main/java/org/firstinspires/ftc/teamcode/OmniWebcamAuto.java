package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name="OmniWebcamAuto", group="Autonomous")
public class OmniWebcamAuto extends LinearOpMode {

    /* BEGIN variables needed for mineral detection autonomous */
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    // Abid's Vuforia Key
    private static final String VUFORIA_KEY =
            "ATUUQRD/////AAABmWvkn/HpKkiTkmH+kqcdQa87+E5nnizSTMHex9sTxsSbub3m/AzfdamdYGP7pwr/6Ea3A5aHYC35fc9Nw8wFLofmMHwKHSwnm6wC/kS6oEspjXxlk7p3YKgHpe9iWIuvYVHDI211sVIxCg+wd8DvtdoFulhQ+dLLSajTNryZpsKgOJRHKnq4KREOb3jticHQpvTWDrM3O3yya3F5KEOBUr5ekhLxz06M7VpmIeuCc6FTw3RxRQ6qtqKfXxCzCK0ziyyDyMlBCie0WH1gvI1kKhk3modRIaJfaTcAw54REWyTfIhhV3A4Nyp/99j1FYonm94fu/gvOemiGDI1WWotAOSYqxnLmru7vN7kSzlsKits";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    // 0 = left
    // 1 = center
    // 2 = right
    public int goldParticlePosition = -1;

    /* END variables needed for mineral detection */

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
        // Procedures Needed to Set Up Autonomous
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        // Procedures needed to set up mechanical stuff

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

        waitForStart();

        nomServo.setPosition(NOM_SERVO_UP);

        while (opModeIsActive()) {
            // find the position of the gold mineral
            if (tfod != null) {
                tfod.activate();
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;

                    if (updatedRecognitions.size() == 3) {
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }
                        if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                            if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                telemetry.addData("Gold Mineral Position", "Left");
                            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                telemetry.addData("Gold Mineral Position", "Right");
                            } else {
                                telemetry.addData("Gold Mineral Position", "Center");
                            }
                        } // end if
                    } // end if

                    // handle the case where only two objects are detected
                    else if (updatedRecognitions.size() == 2) {
                        ;
                    }

                } // end if
            } // end if


            telemetry.update();
        } // end opModeIsActive() while loop


        // move to the gold mineral


//        runLatchToPosition(23000);
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

    /**
     * Move to the left mineral
     */
    void sampleLeft() {

    }

    /**
     * Move to the center mineral
     */
    void sampleCenter() {

    }

    /**
     * Move to the right position mineral
     */
    void sampleRight() {

    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}
