package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Hybrid Robot Linear TeleOp", group="Linear Opmode")
public class HybridRobotTeleOp extends LinearOpMode {

    /**
     * Total running time of the robot.
     */
    private ElapsedTime runtime = new ElapsedTime();

    // initialize new instance of the robot by binding the motors
    private HybridRobot robot = new HybridRobot(
            hardwareMap.get(DcMotor.class, "topLeft"),
            hardwareMap.get(DcMotor.class, "topRight"),
            hardwareMap.get(DcMotor.class, "bottomLeft"),
            hardwareMap.get(DcMotor.class, "bottomRight")
    );

    private Gamepad driver = gamepad1;
    private Gamepad gunner = gamepad2;

    @Override
    public void runOpMode() throws InterruptedException
    {
        if (robot != null)
        {
            telemetry.addData("Status", "Initialized");
            telemetry.update();
        }
        else
        {
            telemetry.addData("Error", "Robot not initialized");
            telemetry.update();
        }

        while (opModeIsActive())
        {
            // translate gamepad settings to robot movement, naively
            robot.move(driver.left_stick_y);
            // Threshold for turning so accidentally nudging the stick doesn't jerk the robot
            if (Math.abs(driver.right_stick_x) > 0.1)
            {
                robot.turn(driver.right_stick_x);
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
