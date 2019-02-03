package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Constants.*;

@Disabled
@Autonomous(name = "QuirkyAuto", group = "Autonomous")
public class QuirkyAuto extends LinearOpMode {

    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    private Servo nomServo = null;
    private Servo nomServo2 = null;

    private DcMotor latch = null;

    private ElapsedTime runtime = new ElapsedTime();

    AutoMovement am = null;

    @Override
    public void runOpMode() throws InterruptedException {

        frontLeftDrive = hardwareMap.get(DcMotor.class, "FL");
        frontRightDrive = hardwareMap.get(DcMotor.class, "FR");
        backLeftDrive = hardwareMap.get(DcMotor.class, "BL");
        backRightDrive = hardwareMap.get(DcMotor.class, "BR");
        nomServo = hardwareMap.get(Servo.class, "NS");
        nomServo2 = hardwareMap.get(Servo.class, "NS2");
        latch = hardwareMap.get(DcMotor.class, "LATCH");

        am = new AutoMovement(frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive, latch, runtime, this);

        am.init();

//        tf.initialize();

        waitForStart();

        nomServo.setPosition(NOM_SERVO_IN);
        nomServo2.setPosition(NOM_SERVO_2_IN);
    }

    public void unlatch() {
//        am.runLatchToPosition();
    }
}
