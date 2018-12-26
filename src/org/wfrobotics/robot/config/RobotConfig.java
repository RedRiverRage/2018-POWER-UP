package org.wfrobotics.robot.config;

import org.wfrobotics.reuse.config.RobotConfigPicker;
import org.wfrobotics.reuse.config.TalonConfig.ClosedLoopConfig;
import org.wfrobotics.reuse.config.TankConfig.TankConfigSupplier;
import org.wfrobotics.robot.config.robotConfigs.RRRConfigCompetition;
import org.wfrobotics.robot.config.robotConfigs.RRRConfigPractice;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;

public abstract class RobotConfig implements TankConfigSupplier
{
    private static RobotConfig instance = null;

    //                      Intake
    // _________________________________________________________________________________
    public double INTAKE_DISTANCE_TO_CUBE; // centmeters
    public double INTAKE_WRIST_TIMEOUT_LENTH; // secounds
    public boolean INTAKE_MOTOR_INVERTED_LEFT;
    public boolean INTAKE_MOTOR_INVERTED_RIGHT;
    public int INTAKE_PDP_CHANNELS[];
    public double INTAKE_CURRENT_LIMIT;
    public double INTAKE_CURRENT_DISABLE_DELAY;
    public double INTAKE_SPEED_MAX_IN_PERCENT;

    //                      Lift
    // _________________________________________________________________________________
    public double LIFT_SPROCKET_DIAMETER_INCHES; // 1.29 16 tooth 25 chain
    public int LIFT_MAX_POSSIBLE_UP; // bottem sensor to top sensor
    public ClosedLoopConfig LIFT_CLOSED_LOOP;

    public double LIFT_SET_SPEED_UP;
    public double LIFT_SET_SPEED_DOWN;

    public boolean LIFT_LIMITS_DISABLE;
    public LimitSwitchNormal[][] LIFT_LIMIT_SWITCH_NORMALLY;

    public boolean LIFT_DEBUG = false;
    public double LIFT_MANUAL_PERCENTAGE_DOWN = .8;

    //                      Tank
    // _________________________________________________________________________________
    public boolean TANK_SWAP_LEFT_RIGHT;
    public boolean TANK_SQUARE_TURN_MAG;
    public boolean TANK_BRAKE_MODE;

    // some robot measurements

    public double INTAKE_HEIGHT_AT_BOTTOM = 8;
    public double LIFT_STAGE_RATIO = 1.9; // TODO is this accurate? in an ideal world, the two stages just double (2) the height

    public double ROBOT_LENGTH = 33;
    public double ROBOT_WIDTH = 28;
    public double ROBOT_HEIGHT = 55;
    public double BUMPER_SIZE = 2.5 + .75;

    public double RobotLengthWithBumpers()
    {
        return ROBOT_LENGTH + BUMPER_SIZE * 2;
    }
    public double RobotWidthWithBumpers()
    {
        return ROBOT_WIDTH + BUMPER_SIZE * 2;
    }

    public static RobotConfig getInstance()
    {
        if (instance == null)
        {
            instance = RobotConfigPicker.get(new RobotConfig[] { new RRRConfigCompetition(), new RRRConfigPractice()});
        }
        return instance;
    }

    public static boolean isCompBot()
    {
        return getInstance() instanceof RRRConfigCompetition;
    }
}

