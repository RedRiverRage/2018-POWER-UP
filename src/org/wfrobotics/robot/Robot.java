package org.wfrobotics.robot;

import org.wfrobotics.reuse.EnhancedRobot;
import org.wfrobotics.reuse.hardware.Blinkin;
import org.wfrobotics.reuse.hardware.LEDs;
import org.wfrobotics.reuse.hardware.lowleveldriver.BlinkinPatterns.PatternName;
import org.wfrobotics.robot.commands.MatchState2018;
import org.wfrobotics.robot.config.IO;
import org.wfrobotics.robot.config.RobotConfig;
import org.wfrobotics.robot.subsystems.Climber;
import org.wfrobotics.robot.subsystems.IntakeSubsystem;
import org.wfrobotics.robot.subsystems.LiftSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/** @author Team 7048 Red River Rage<p>STEM Alliance of Fargo Moorhead */
public class Robot extends EnhancedRobot
{
    public static LEDs leds = new Blinkin(9, PatternName.Breath_Red);
    public static IO controls;
    Command autonomousCommand;

    protected void registerRobotSpecific()
    {
        RobotConfig config = RobotConfig.getInstance();

        subsystems.register(new IntakeSubsystem(config));
        subsystems.register(LiftSubsystem.getInstance());
        subsystems.register(new Climber());
    }

    @Override
    public void disabledPeriodic()
    {
        MatchState2018.getInstance().update();  // Need game-specific data
        super.disabledPeriodic();
    }
}
