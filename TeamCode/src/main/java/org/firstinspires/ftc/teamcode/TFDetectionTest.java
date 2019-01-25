package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "TFDetectionTest", group = "Autonomous")
public class TFDetectionTest extends LinearOpMode {


    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {

        TFDetectionThree tf = new TFDetectionThree(telemetry, this, 1);

        tf.initialize();

        waitForStart();


        int tfResult = tf.detect();

    }

    void waitForSeconds(double seconds) {
        runtime.reset();
        //noinspection StatementWithEmptyBody
        while (runtime.seconds() < seconds && opModeIsActive()) {
        }
    }

}
