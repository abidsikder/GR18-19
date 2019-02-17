package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Constants.*;

/**
 * Auto that just comes down from the lander and delatches
 */
@Autonomous(name = "DelatchAuto", group = "Auto")
public class DelatchAuto extends LinearOpMode {

    /**
     * Amount of time elapsed
     */
    private final ElapsedTime runtime = new ElapsedTime();

    private final OmniRobot rb = new OmniRobot();

    private AutoBase base;

    /**
     * Called when the auto runs
     */
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initializing");

        // Initialize the robot
        rb.init(hardwareMap);
        base = new AutoBase(this, runtime, rb);

        telemetry.addData("Status", "Initialized");

        // Wait for the start button to be pressed
        waitForStart();

        telemetry.addData("Status", "Starting");

        rb.nomServo.setPosition(NOM_SERVO_IN);
        rb.nomServo2.setPosition(NOM_SERVO_2_IN);

        base.comeDownFromLander();
        base.detachFromLander();
//        base.driveForSeconds(.45, 0, 0, .25);
    }

}
