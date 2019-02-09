package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Constants.*;

/**
 * Base methods common to all Auto op modes
 */
public class AutoBase {

    private final LinearOpMode opMode;
    private final ElapsedTime runtime;
    private final OmniRobot robot;

    public AutoBase(LinearOpMode opMode, ElapsedTime runtime, OmniRobot robot) {
        this.opMode = opMode;
        this.runtime = runtime;
        this.robot = robot;
    }

    /**
     * Do nothing for a given number of seconds
     *
     * @param seconds the time for which to wait in seconds
     */
    public void waitForSeconds(double seconds) {
        runtime.reset();
        //noinspection StatementWithEmptyBody
        while (runtime.seconds() < seconds && opMode.opModeIsActive()) {
        }
    }

    /**
     * Set the motors to drive for a given number of seconds
     */
    public void driveForSeconds(double seconds, double forwardPower, double rightPower, double clockwisePower) {
        robot.drive(forwardPower, rightPower, clockwisePower);
        waitForSeconds(seconds);
        robot.driveStop();
    }

    /**
     * Run the latch motor to a given encoder position
     * @param targetPosition the encoder position to which to run
     */
    public void runLatchToPosition(int targetPosition) {
        if (targetPosition > robot.latch.getCurrentPosition()) {
            robot.latch.setPower(1);
            while (opMode.opModeIsActive() && targetPosition > robot.latch.getCurrentPosition()) {
                opMode.telemetry.addData("Latch Moving", true);
                opMode.telemetry.addData("Latch Position", robot.latch.getCurrentPosition());
                opMode.telemetry.update();
            }
            robot.latch.setPower(0);
        } else if (targetPosition < robot.latch.getCurrentPosition()) {
            robot.latch.setPower(-1);
            while (opMode.opModeIsActive() && targetPosition < robot.latch.getCurrentPosition()) {
                opMode.telemetry.addData("Latch Moving", true);
                opMode.telemetry.addData("Latch Position", robot.latch.getCurrentPosition());
                opMode.telemetry.update();
            }
            robot.latch.setPower(0);
        }
    }

    /**
     * Extend the latch to come down from the lander
     */
    public void comeDownFromLander() {
        runLatchToPosition(COME_DOWN_ENCVAL);
    }

    /**
     * Detach the robot from the lander, once it has come down
     */
    public void detachFromLander() {
        // Move backwards
        driveForSeconds(DETACH_FROM_LANDER_BACKWARDS_SECONDS, -DETACH_FROM_LANDER_DRIVE_POWER, 0, 0);

        // Move right
        driveForSeconds(DETACH_FROM_LANDER_RIGHT_SECONDS, 0, DETACH_FROM_LANDER_DRIVE_POWER, 0);
    }
}
