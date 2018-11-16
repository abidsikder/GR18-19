package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="LiftServoTest", group="Servo Test")

public class LiftServoTest extends ServoTest {
    @Override
    public String servoName() {
        return "LS";
    }
}
