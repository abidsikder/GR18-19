package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.Iterator;
import java.util.List;

import static org.firstinspires.ftc.teamcode.Constants.*;
import static org.firstinspires.ftc.teamcode.VuforiaInterop.*;

/**
 * Auto that just comes down from the lander and delatches
 */
public abstract class AbstractSampleAuto extends LinearOpMode {

    /**
     * Amount of time elapsed
     */
    private final ElapsedTime runtime = new ElapsedTime();

    private final OmniRobot rb = new OmniRobot();

    private AutoBase base;

    private VuforiaInterop vf;

    /**
     * Called when the auto runs
     */
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initializing");
        telemetry.update();

        // Initialize the robot
        rb.init(hardwareMap);
        base = new AutoBase(this, runtime, rb);

        // Initialize vuforia
        vf = new VuforiaInterop(telemetry, hardwareMap);
        vf.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the start button to be pressed
        waitForStart();

        telemetry.addData("Status", "Starting");
        telemetry.update();

        rb.nomServo.setPosition(NOM_SERVO_IN);
        rb.nomServo2.setPosition(NOM_SERVO_2_IN);

        base.comeDownFromLander();
        base.detachFromLander();
//        base.driveForSeconds(.45, 0, 0, .25);
//        base.driveForSeconds(.1,-.2,0,0);
        base.driveForSeconds(.075, 0, 0, .25);

        // Wait for a few seconds to allow detection to settle
        base.waitForSeconds(INITIAL_DETECTION_DELAY_SECONDS);

        MineralLocation mineralLocation = null;

        // The number of times detection has been attempted
        int attempts = 0;
        telemetry.addData("Detection attempts", attempts);

        telemetry.addData("Detection status", "Detecting");
        telemetry.update();

        if (vf.tfod != null) {
            vf.tfod.activate();

            // Try to detect the gold mineral until a conclusion is reached
            while (opModeIsActive() && mineralLocation == null && attempts <= MAX_DETECTION_ATTEMPTS) {
                mineralLocation = sample();
                attempts++;
                telemetry.addData("Detection attempts:", attempts);
                telemetry.update();

                // Wait for some time to allow a result to be reached
                base.waitForSeconds(DETECTION_DELAY_SECONDS);
            }

            // Drive in different directions depending on the location of the gold mineral
            if (mineralLocation == MineralLocation.LEFT) {
                telemetry.addData("Detection status", "Success: Left");
                telemetry.update();
                pushLeft();
            } else if (mineralLocation == MineralLocation.CENTER) {
                telemetry.addData("Detection status", "Success: Center");
                telemetry.update();
                pushCenter();
            } else if (mineralLocation == MineralLocation.RIGHT) {
                telemetry.addData("Detection status", "Success: Right");
                telemetry.update();
                pushRight();
            } else {
                // If nothing was detected, default to going left
                telemetry.addData("Detection status", "Failure (default Left)");
                telemetry.update();
                pushLeft();
            }
        } else {
            // If tfod fails to activate, default to going right
            telemetry.addData("Detection status", "Failure");
            telemetry.update();
            pushRight();
        }
//        base.waitForSeconds(10);
    }

    /**
     * Use TFOD to detect the location of the gold mineral
     *
     * @return The location of the gold mineral, or null if could not detect
     */
    private MineralLocation sample() {
        // Get a list of recognized objects
        List<Recognition> updatedRecognitions = vf.tfod.getUpdatedRecognitions();

        if (updatedRecognitions != null) {

            // Get rid of all detections before the confidence threshold
            {
                Iterator<Recognition> iter = updatedRecognitions.iterator();
                while (iter.hasNext()) {
                    Recognition r = iter.next();
                    if (r.getConfidence() < Constants.CONFIDENCE_THRESHOLD && r.getLabel().equals(LABEL_GOLD_MINERAL)) {
                        iter.remove();
                    }
                }
            }

            // Get the number of recognized objects
            final int numRecognitions = updatedRecognitions.size();
            telemetry.addData("# Objects Detected", numRecognitions);
            telemetry.update();

            float goldMineralX = 0;
            float silverMineral1X = 0;
            float silverMineral2X = 0;

            boolean goldFound = false;
            boolean silver1Found = false;
            boolean silver2Found = false;

            // Check how many minerals were recognized
            if (numRecognitions == 3) {
                // If three minerals were detected, find the x and type (gold or silver) of each
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals(LABEL_GOLD_MINERAL) && !goldFound) {
                        goldMineralX = recognition.getLeft();
                        goldFound = true;
                    } else if (recognition.getLabel().equals(LABEL_SILVER_MINERAL) && !silver1Found) {
                        silverMineral1X = recognition.getLeft();
                        silver1Found = true;
                    } else if (recognition.getLabel().equals(LABEL_SILVER_MINERAL) && !silver2Found) {
                        silverMineral2X = recognition.getLeft();
                        silver2Found = true;
                    }
                }

                // Check if 1 gold and two silver minerals were found; if not, return null
                if (goldFound && silver1Found && silver2Found) {
                    // If gold is to the left of both silvers, return LEFT.
                    // If gold is to the right of both silvers, return RIGHT.
                    // Otherwise, return CENTER.
                    if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                        return MineralLocation.LEFT;
                    } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                        return MineralLocation.RIGHT;
                    } else {
                        return MineralLocation.CENTER;
                    }
                } else {
                    return null;
                }
            } else if (numRecognitions == 2) {
                // If two minerals were recognized, assume that the two minerals seen are the left two
                // EDIT: right two
                // Get each recognition.
                Recognition recognition1 = updatedRecognitions.get(0);
                Recognition recognition2 = updatedRecognitions.get(1);

                // Mineral types: false: silver, true: gold
                boolean isGold1;
                boolean isGold2;

                // Figure out whether mineral 1 is gold or silver
                if (recognition1.getLabel().equals(LABEL_GOLD_MINERAL)) {
                    isGold1 = true;
                } else {
                    isGold1 = false;
                }

                // Figure out whether mineral 2 is gold or silver
                if (recognition2.getLabel().equals(LABEL_GOLD_MINERAL)) {
                    isGold2 = true;
                } else {
                    isGold2 = false;
                }

                // If neither mineral is gold, then return RIGHT, since the other mineral is the gold one
                // If both are gold, return null (this represents failure)
                if (!isGold1 && !isGold2) {
//                    return MineralLocation.RIGHT;
                    return MineralLocation.LEFT;
                } else if (isGold1 && isGold2) {
                    return null;
                } else {
                    // If one is gold and the other is silver, figure out if gold is to the right or left of the silver
                    if (isGold1) {
                        if (recognition1.getLeft() < recognition2.getLeft()) {
//                            return MineralLocation.LEFT;
                            return MineralLocation.CENTER;
                        } else {
//                            return MineralLocation.CENTER;
                            return MineralLocation.RIGHT;
                        }
                    } else {
                        if (recognition2.getLeft() < recognition1.getLeft()) {
//                            return MineralLocation.LEFT;
                            return MineralLocation.CENTER;
                        } else {
//                            return MineralLocation.CENTER;
                            return MineralLocation.RIGHT;
                        }
                    }
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Drive to and displace the left mineral
     */
    private void pushLeft() {
        base.driveForSeconds(.25, 1, 0, 0);
        base.driveForSeconds(.4, 0, .5, 0);
        base.driveForSeconds(.25, .8, 0, 0);
//        base.driveForSeconds(.6, 0, .5, 0);
        base.driveForSeconds(.8, 0, .5, 0);
        // TODO
    }

    /**
     * Drive to and displace the left mineral
     */
    private void pushCenter() {
//        base.driveForSeconds(.3, 1, 0, 0);
        base.driveForSeconds(.25, .7, 0, 0); // TODO: less?
        base.driveForSeconds(1, 0, .5, 0);
        // TODO
    }

    /**
     * Drive to and displace the left mineral
     */
    private void pushRight() {
        base.driveForSeconds(.2, -1, 0, 0);
        base.driveForSeconds(1, 0, .5, 0);
        // TODO
    }

    public abstract boolean crater();
}

/**
 * Represents the location of the gold mineral
 */
enum MineralLocation {
    LEFT, CENTER, RIGHT
}