package org.wfrobotics.robot.config.robotConfigs;

import org.wfrobotics.reuse.config.TalonConfig.ClosedLoopConfig;
import org.wfrobotics.reuse.config.TalonConfig.FollowerConfig;
import org.wfrobotics.reuse.config.TalonConfig.Gains;
import org.wfrobotics.reuse.config.TalonConfig.MasterConfig;
import org.wfrobotics.robot.config.RobotConfig;
import org.wfrobotics.reuse.config.TankConfig;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;

public class RRRConfigPractice extends RobotConfig
{
    public RRRConfigPractice()
    {
        INTAKE_DISTANCE_TO_CUBE = 15; // centimeters
        INTAKE_WRIST_TIMEOUT_LENTH = 0.5; //secounds
        INTAKE_MOTOR_INVERTED_LEFT = false;
        INTAKE_MOTOR_INVERTED_RIGHT = true;
        INTAKE_PDP_CHANNELS = new int[] {4, 11};
        INTAKE_CURRENT_LIMIT = 12;
        INTAKE_CURRENT_DISABLE_DELAY = .5;
        INTAKE_SPEED_MAX_IN_PERCENT = 1.0;

        LIFT_MAX_POSSIBLE_UP = 3200; // bottem sensor to top sensor

        LIFT_CLOSED_LOOP = new ClosedLoopConfig("Lift", new MasterConfig[] {
            new MasterConfig(11, false, true),
            new MasterConfig(10, false, false),
        }, new Gains[] {
            new Gains("Magic Motion", 0, 4.0, 0.001, 0.0, 1023.0 / LIFT_MAX_POSSIBLE_UP, 20, LIFT_MAX_POSSIBLE_UP, (int) (LIFT_MAX_POSSIBLE_UP * 4.5)),
        });

        LIFT_SET_SPEED_UP = .65;
        LIFT_SET_SPEED_DOWN = .5;

        LIFT_LIMIT_SWITCH_NORMALLY = new LimitSwitchNormal[][] {
            { LimitSwitchNormal.NormallyOpen,  // Left Fwd
                LimitSwitchNormal.NormallyOpen},
            { LimitSwitchNormal.NormallyOpen,  // Right Fwd
                    LimitSwitchNormal.NormallyOpen}
        };
        LIFT_LIMITS_DISABLE = true;

        TANK_SWAP_LEFT_RIGHT = false;
        TANK_BRAKE_MODE = false;
        TANK_SQUARE_TURN_MAG = true;
    }

    public TankConfig getTankConfig()
    {
        TankConfig config = new TankConfig();

        config.VELOCITY_MAX = 10500.0;  // 12000 works way better than say 10500 at 9.9 ft/s DRL 3-16-18
        config.VELOCITY_PATH = (int) (config.VELOCITY_MAX * .80);
        config.ACCELERATION = (int) (config.VELOCITY_PATH * .955);
        config.STEERING_DRIVE_DISTANCE_P = 0.000022;
        config.STEERING_DRIVE_DISTANCE_I = 0.000005;
        config.OPEN_LOOP_RAMP = 0.15;

        config.CLOSED_LOOP = new ClosedLoopConfig("Tank", new MasterConfig[] {
            new MasterConfig(15, false, true, new FollowerConfig(17, true)),
            new MasterConfig(16, true, true, new FollowerConfig(14, true)),
        }, new Gains[] {
            new Gains("Motion Magic", 0, 2.25, 0.006, 3.77, 1023.0 / config.VELOCITY_MAX, 35, config.VELOCITY_PATH, config.ACCELERATION),
            new Gains("Path", 1, 0.33, 0.0, 0.0, 0, 35),
            new Gains("Velocity", 2, 1.0, 0.0017, 5.0, 0, 0),
            new Gains("Gyro", 3, 2.0, 0.0, 0.0, 0.0, 0),
        });

        config.GEAR_RATIO_HIGH = (36.0 / 15.0) * (24.0 / 40.0);
        config.GEAR_RATIO_LOW = (36.0 / 15.0) * (40.0 / 24.0);
        config.SCRUB = 0.96;
        config.WHEEL_DIAMETER = 6.25;
        config.WIDTH = 24.0;

        return config;
    }
}
