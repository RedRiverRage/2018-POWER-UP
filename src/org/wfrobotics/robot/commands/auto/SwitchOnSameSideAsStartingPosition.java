package org.wfrobotics.robot.commands.auto;

import org.wfrobotics.robot.commands.MatchState2018;
import org.wfrobotics.robot.commands.MatchState2018.Side;
import org.wfrobotics.robot.config.Autonomous;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

public class SwitchOnSameSideAsStartingPosition extends ConditionalCommand
{
    public SwitchOnSameSideAsStartingPosition(Command onRight, Command onLeft)
    {
        super(onRight, onLeft);
    }

    protected boolean condition()
    {
        MatchState2018 state =  MatchState2018.getInstance();
        state.update();

        if(Autonomous.autoStartingPosition == Autonomous.StartingPosition.Right)
        {
            if(state.SwitchNear == Side.Right)
            {
                return true;
            }
        }
        else
        {
            if(Autonomous.autoStartingPosition == Autonomous.StartingPosition.Left)
            {
                if(state.SwitchNear == Side.Left)
                {
                    return true;
                }
            }
        }

        return false;
    }
}