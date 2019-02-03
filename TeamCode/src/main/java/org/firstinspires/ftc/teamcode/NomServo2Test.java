package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="NomServo2Test", group="Servo Test")

public class NomServo2Test extends ServoTest {
    @Override
    public String servoName() {
        return "NS2";
    }
}
