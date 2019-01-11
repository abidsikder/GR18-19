package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="NomServoTest", group="Servo Test")
public class NomServoTest extends ServoTest {
    @Override
    public String servoName() {
        return "NS";
    }
}
