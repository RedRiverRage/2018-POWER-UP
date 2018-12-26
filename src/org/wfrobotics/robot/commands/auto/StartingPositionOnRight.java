package org.wfrobotics.robot.commands.auto;

import org.wfrobotics.robot.config.Autonomous;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

public class StartingPositionOnRight extends ConditionalCommand
{
    public StartingPositionOnRight(Command onRight, Command onLeft)
    {
        super(onRight, onLeft);
    }

    protected boolean condition()
    {
        return Autonomous.autoStartingPosition == Autonomous.StartingPosition.Right;
    }
}