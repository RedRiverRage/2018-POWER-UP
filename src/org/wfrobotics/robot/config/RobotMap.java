package org.wfrobotics.robot.config;

public class RobotMap
{
    /*
     * Digital: 0-9 are on-board, 10-25 are on the MXP
     * Analog: 0-3 are on-board, 4-7 are on the MXP
     * PWM: 0-9 are on-board, 10-19 are on the MXP
     */

    public static final int CAN_POWER_DISTRIBUTION_MODULE = 0;
    public static final int CAN_PNEUMATIC_CONTROL_MODULE = 7;

    // Swerve
    public static final int ANG_SWERVE_ANGLE[] = { 0, 1, 2, 3 };
    public static final int CAN_SWERVE_DRIVE_TALONS[] = { 1, 4, 5, 8};
    public static final int CAN_SWERVE_ANGLE_TALONS[] = { 2, 3, 6, 7};
    public static final int PWM_SWERVE_SHIFT_SERVOS[] = { 0, 1, 2, 3 };

    // Tank Drive
    public static final int CAN_TANK_SHIFTER_DEVICE = CAN_PNEUMATIC_CONTROL_MODULE;
    public static final int CAN_TANK_SHIFTER_HIGH = 0;
    public static final int CAN_TANK_SHIFTER_LOW = 4;

    // LEDs
    public static final int CAN_LIGHT = 31;

    // Lift
    public static final int CAN_LIFT_R = 10;
    public static final int CAN_LIFT_L = 11;

    // Intake
    //TODO set the intake values
    public static final int PWM_PRACTICE_INTAKE_L = 0;
    public static final int PWM_PRACTICE_INTAKE_R = 1;
    public static final int CAN_COMPETITION_INTAKE_L = 20;
    public static final int CAN_COMPETITION_INTAKE_R = 21;
    public static final int PNEUMATIC_INTAKE_HORIZONTAL_FORWARD = 1;
    public static final int PNEUMATIC_INTAKE_HORIZONTAL_REVERSE = 5;
    public static final int PNEUMATIC_INTAKE_VERTICAL_FORWARD = 6;
    public static final int PNEUMATIC_INTAKE_VERTICAL_REVERSE = 2;
    public static final int SENSOR_DISTANCE = 0;

    //Climb
    public static final int PWM_PRACTICE_WINCH_MOTOR = 2;
    public static final int CAN_COMPETITION_WINCH_MOTOR = 22;
    public static final int PNEUMATIC_CLIMB_UP = 3;
    public static final int PNEUMATIC_CLIMB_DOWN = 7;

    public static final int PWM_LED_DRIVER = 7;

}
