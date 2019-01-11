package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

@Disabled
public class HybridRobot
{
    private DcMotor topLeft, topRight, bottomLeft, bottomRight;
    private double motorLowerBound = -1.0;
    private double motorUpperBound = 1.0;

    /**
    * Constructor that takes in the hardware map reference of the motors, servos, and any sensors we add in the future.
    * @param topLeft the top left motor.
    * @param topRight the top right motor.
    * @param bottomLeft the bottom left motor.
    * @param bottomRight the bottom right motor.
    */
    HybridRobot(DcMotor topLeft, DcMotor topRight, DcMotor bottomLeft, DcMotor bottomRight)
    {
        this.topLeft        = topLeft;
        this.topRight       = topRight;
        this.bottomLeft     = bottomLeft;
        this.bottomRight    = bottomRight;
    }

    /**
     * Moves the robot in a straight direction, forward if positive power or backward if power is negative.
     * @param power an double from -1 to 1.
     */
    public void move(double power)
    {
        double clipped = Range.clip(power, motorLowerBound, motorUpperBound);
        topLeft.setPower(clipped);
        topRight.setPower(clipped);
        bottomLeft.setPower(clipped);
        bottomRight.setPower(clipped);
    }

    /**
     * Turns the robot right if power is positive, left is power is negative.
     * @param intensity the intensity with which to turn.
     */
    public void turn(double intensity)
    {
        double clipped = Range.clip(intensity, motorLowerBound, motorUpperBound);
        boolean turnRight = clipped > 0;

        if (turnRight)
        {
            topLeft.setPower(clipped);
            bottomLeft.setPower(clipped);
            topRight.setPower(-clipped);
            bottomRight.setPower(-clipped);
        }
        else
        {
            topLeft.setPower(-clipped);
            bottomLeft.setPower(-clipped);
            topRight.setPower(clipped);
            bottomRight.setPower(clipped);

        }
    }
}