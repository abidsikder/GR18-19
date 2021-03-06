package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@Disabled
@Autonomous(name = "OmniWebcamAuto", group = "Autonomous")
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
    public int goldMineralPosition = -1;

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

        // latch
//        latch = hardwareMap.get(DcMotor.class, "LATCH");
//
//        latch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//        latch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        latch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Nom Servo
        nomServo = hardwareMap.get(Servo.class, "NS");

        // team marker
        // INTENTIONALLY LEFT EMPTY FOR NOW

        // movement around field
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

        nomServo.setPosition(NOM_SERVO_UP);

        while (opModeIsActive()) {
//            // de-latch and come down
//            /*
//             * TODO
//             */
//
//            // getUpdatedRecognitions() will return null if no new information is available since
//            // the last time that call was made.
//            List<Recognition> updatedRecognitions;
//
//            if (tfod != null) {
//
//                tfod.activate();
//
//                // wait for 2 seconds once on the ground to let the recognition program settle
//                waitForSeconds(2);
//
//                // defined earlier in a larger scope
//                updatedRecognitions = tfod.getUpdatedRecognitions();
//
//                telemetry.addData("# Object Detected", updatedRecognitions.size());
//
//                if (updatedRecognitions != null) {
//                    int goldMineralX = -1;
//                    int silverMineral1X = -1;
//                    int silverMineral2X = -1;
//
//                    if (updatedRecognitions.size() == 3) {
//                        for (Recognition recognition : updatedRecognitions) {
//                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
//                                goldMineralX = (int) recognition.getLeft();
//                            } else if (silverMineral1X == -1) {
//                                silverMineral1X = (int) recognition.getLeft();
//                            } else {
//                                silverMineral2X = (int) recognition.getLeft();
//                            }
//                        }
//                        if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
//                            if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
//                                goldMineralPosition = 0;
//                                telemetry.addData("Gold Mineral Position", "Left");
//                            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
//                                goldMineralPosition = 2;
//                                telemetry.addData("Gold Mineral Position", "Right");
//                            } else {
//                                goldMineralPosition = 1;
//                                telemetry.addData("Gold Mineral Position", "Center");
//                            }
//                        } // end if
//                    } // end if
//
//
//                    // handle the case where only two objects are detected
//                    // the way the webcam is setup, the webcam will most commonly see the rightmost 2 minerals
//                    else if (updatedRecognitions.size() == 2) {
//                        /*
//                         * I bet this could be more efficient without having to loop through the list every time,
//                         * but for simplicity and a first time case, I'm just going to go with the most easy-to-understand
//                         * code. - Abid
//                         */
//
//                        // if both the minerals detected are silver, then the gold is on the left
//                        // because that's the mineral the webcam can't see
//                        if (updatedRecognitions.get(0).getLabel().equals(LABEL_SILVER_MINERAL)
//                                && updatedRecognitions.get(1).getLabel().equals(LABEL_SILVER_MINERAL)) {
//                            goldMineralPosition = 0;
//                            telemetry.addData("Gold Mineral Position", "Left");
//                        }
//
//                        // otherwise, if it is a mixed case, then handle according to the relative position of the gold mineral
//                        else {
//                            for (Recognition recognition : updatedRecognitions) {
//                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
//                                    goldMineralX = (int) recognition.getLeft();
//                                } else {
//                                    silverMineral1X = (int) recognition.getLeft();
//                                }
//                            }
//                            // if the gold mineral is to the left of the silver, then it is in the center
//                            if (goldMineralX < silverMineral1X) {
//                                goldMineralPosition = 1;
//                                telemetry.addData("Gold Mineral Position", "Center");
//                            }
//
//                            // if the gold is not on the left or the center, then it is on the right position
//                            else {
//                                goldMineralPosition = 2;
//                                telemetry.addData("Gold Mineral Position", "Right");
//                            }
//                        }
//
//                    }
//
//                    // if less than two minerals were detected, record it as a bad sample, but just guess and try for the one on the right
//                    else {
//                        telemetry.addData("Less than 2 Minerals Detected, Guessing on the Right", true);
//                        goldMineralPosition = 2;
//                    }
//                } // end if
//
//            } // end if statement with the tfod != null
//
//            double xMin = 0;
//            double xMax = 40000;
//            double tolerance = xMax / 150;
//            // adjusted because of the angle at which the webcam is mounted
//            // filled in with 2E4 as a placeholder value for now
//            double targetX = 20000;
//
//            // the proportional constant for turning
//            double turnConstant = 0.3;
//
//            // move and knock over the gold mineral on the left
//            if (goldMineralPosition == 0) {
//                // turn to the left until the mineral is within sight
//                boolean goldVisible = false;
//                while (!goldVisible) {
//                    turnCounterClockwise(0.5, 0.5);
//
//                    updatedRecognitions = tfod.getUpdatedRecognitions();
//
//                    for (Recognition recognition : updatedRecognitions) {
//                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
//                            goldVisible = true;
//                        }
//                    }
//                }
//
//                // turn proportionally until the mineral is at the center within the bounds of tolerance
//                boolean goldWithinBounds = false;
//                while (!goldWithinBounds) {
//                    updatedRecognitions = tfod.getUpdatedRecognitions();
//
//                    // find the gold cube's recognition object
//                    // need to instantiate so that java compiler does not throw errors
//                    Recognition goldRecognition = updatedRecognitions.get(0);
//                    for (Recognition recognition : updatedRecognitions) {
//                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
//                            goldRecognition = recognition;
//                        }
//                    }
//
//                    if (
//                            (goldRecognition.getLeft() < (targetX - tolerance)) ||
//                                    (goldRecognition.getRight() < (targetX - tolerance))
//                            ) {
//                        goldWithinBounds = true;
//                    } else {
//                        // turns proportional to how far the left side is from the maximum allowed limits of the tolerance for the cube
//                        turnCounterClockwise(0.5, turnConstant * (goldRecognition.getLeft() - (targetX - tolerance)));
//                    }
//                }
//
//                // once lined up, move forward and knock the ball over
//                driveForwardForSeconds(0.7, 3);
//            } else if (goldMineralPosition == 1) {
//                // try and knock it over on the center
//                driveForwardForSeconds(0.7, 3);
//            } else if (goldMineralPosition == 2) {
//                // turn proportionally until the mineral is at the center within the bounds of tolerance
//                boolean goldWithinBounds = false;
//                while (!goldWithinBounds) {
//                    updatedRecognitions = tfod.getUpdatedRecognitions();
//
//                    // find the gold cube's recognition object
//                    // need to instantiate so that java compiler does not throw errors
//                    Recognition goldRecognition = updatedRecognitions.get(0);
//                    for (Recognition recognition : updatedRecognitions) {
//                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
//                            goldRecognition = recognition;
//                        }
//                    }
//
//                    if (
//                            (goldRecognition.getLeft() < (targetX - tolerance)) ||
//                                    (goldRecognition.getRight() < (targetX - tolerance))
//                            ) {
//                        goldWithinBounds = true;
//                    } else {
//                        // turns proportional to how far the left side is from the maximum allowed limits of the tolerance for the cube
//                        turnClockwise(0.5, turnConstant * (goldRecognition.getLeft() - (targetX - tolerance)));
//                    }
//                }
//
//                // try and knock it over on the center
//                driveForwardForSeconds(0.7, 3);
//            } else {
//                telemetry.addData("Gold Mineral Position Variable was Malformed, something went wrong", true);
//            }
//
//            telemetry.update();
//
//            break;
        } // end opModeIsActive() while loop

        if (tfod != null) {
            tfod.shutdown();
        }
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
        power = Math.abs(power);

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

    void turnCounterClockwise(double power, double seconds) {
        power = Math.abs(power);

        frontLeftDrive.setPower(-power);
        backLeftDrive.setPower(-power);
        frontRightDrive.setPower(-power);
        backRightDrive.setPower(-power);

        waitForSeconds(seconds);

        frontLeftDrive.setPower(0);
        backLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    void waitForSeconds(double seconds) {
        runtime.reset();
        //noinspection StatementWithEmptyBody
        while (runtime.seconds() < seconds && opModeIsActive()) {
        }
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
        // because you can only see the rightmost two, turn to the left until a gold mineral is detected
        double goldMineralX = 0;
        boolean goldInCenter = false;

        do {

        } while (!goldInCenter);

        driveForwardForSeconds(0.5, 0.3);
    }

    /**
     * Move to the center mineral
     */
    void sampleCenter() {
        // check whether the minerl

        driveForwardForSeconds(0.5, 0.3);
    }

    /**
     * Move to the right position mineral
     */
    void sampleRight() {


        driveForwardForSeconds(0.5, 0.3);
    }

    void turnUntilGoldInCenter(double power, double seconds, boolean clockwiseTurn) {
        // clockwiseTurn = true = turn clockwise
        // clockwiseTurn = false = turn counter-clockwise

        // set manually after measuring
        double xMin = 0;
        double xMax = 1000;

        double targetX = 400;
        double toleranceX = 20;

        double yMin = 0;
        double yMax = 0;

        power = Math.abs(power);

        boolean goldInTheCenter = false;
        double goldMineralX = 0;

        if (clockwiseTurn) {
            // turn proportionally until the block is within the tolerance limit
            turnClockwise(power, seconds);


            // check whether the gold mineral is cloes enough to the center
        } else {
            // turn

            // check whether the gold mineral is close enough to the center
        }

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
