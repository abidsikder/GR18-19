package org.firstinspires.ftc.teamcode;

/**
 * Constants for omni robot
 */
public final class Constants {

    // NOM SERVO POSITIONS
    public static final double NOM_SERVO_DUMP = 0.6;
    public static final double NOM_SERVO_IN = 0.75;
    public static final double NOM_SERVO_MID = 0.4;
    public static final double NOM_SERVO_ALMOST_DOWN = 0.25;
    public static final double NOM_SERVO_DOWN = 0.19;

    // NOM SERVO 2 POSITIONS
    public static final double NOM_SERVO_2_DUMP = .39;
    public static final double NOM_SERVO_2_IN = 0.29;
    public static final double NOM_SERVO_2_MID = 0.61;
    public static final double NOM_SERVO_2_ALMOST_DOWN = 0.76;
    public static final double NOM_SERVO_2_DOWN = 0.81;

    // LIFT SERVO POSITIONS
    public static final double LIFT_SERVO_FORWARD = .15;
    public static final double LIFT_SERVO_MID = 0.35;
    public static final double LIFT_SERVO_BACK = 0.75;

    // DRIVING CONSTANTS
    public static final float DRIVE_LEFT_STICK_THRESHOLD_SQUARED = .25f;
    public static final float DRIVE_POWER = 1;
    public static final float DRIVE_POWER_SLOW = .4f;
    public static final float DRIVE_STICK_THRESHOLD = .6f;
    public static final float TRIGGER_THRESHOLD = .65f;

    public static final int LATCH_ALLOWANCE = 30;

    // ----- AUTO CONSTANTS
    /**
     * Encoder value for the latch motor to come down from the lander
     */
    public static final int COME_DOWN_ENCVAL = 22000;

    // DETACH FROM LANDER CONSTANTS
    public static final double DETACH_FROM_LANDER_DRIVE_POWER = .75;
    public static final double DETACH_FROM_LANDER_BACKWARDS_SECONDS = .15;
    public static final double DETACH_FROM_LANDER_RIGHT_SECONDS = .2;

    // VUFORIA DETECTION CONSTANTS
    public static final int MAX_DETECTION_ATTEMPTS = 8;
    public static final double INITIAL_DETECTION_DELAY_SECONDS = 3;
    public static final double DETECTION_DELAY_SECONDS = .75;

    private Constants() {
    }

    ;
}
