package org.wfrobotics.robot.config.robotConfigs;

import org.wfrobotics.reuse.config.TalonConfig.ClosedLoopConfig;
import org.wfrobotics.reuse.config.TalonConfig.Gains;
import org.wfrobotics.reuse.config.TalonConfig.MasterConfig;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;

public class RRRConfigCompetition extends RRRConfigPractice
{
    public RRRConfigCompetition()
    {
        INTAKE_DISTANCE_TO_CUBE = 10;

        LIFT_MAX_POSSIBLE_UP = 3600; // bottem sensor to top sensor
        LIFT_CLOSED_LOOP = new ClosedLoopConfig("Lift", new MasterConfig[] {
            new MasterConfig(11, false, true),
            new MasterConfig(10, true, false),
        }, new Gains[] {
            new Gains("Magic Motion", 0, 0.8, 0.001, 0.0, 1023.0 / LIFT_MAX_POSSIBLE_UP, 20, (int) (LIFT_MAX_POSSIBLE_UP * .95), (int) (LIFT_MAX_POSSIBLE_UP * 4.5)),
        });

        LIFT_SET_SPEED_UP = .5;
        LIFT_SET_SPEED_DOWN = .3;
        LIFT_LIMITS_DISABLE = false;

        LIFT_LIMIT_SWITCH_NORMALLY = new LimitSwitchNormal[][] {
            { LimitSwitchNormal.NormallyOpen,  // Left Fwd
                LimitSwitchNormal.NormallyOpen},
            { LimitSwitchNormal.NormallyOpen,  // Right Fwd
                    LimitSwitchNormal.NormallyOpen}
        };
    }
}
