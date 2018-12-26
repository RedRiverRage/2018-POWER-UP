package org.wfrobotics.robot.commands.auto;

import org.wfrobotics.reuse.commands.drive.DriveDistance;
import org.wfrobotics.robot.commands.MatchState2018;
import org.wfrobotics.robot.commands.MatchState2018.Side;
import org.wfrobotics.robot.commands.auto.MotionProfiling.AutoScaleLeftLeft;
import org.wfrobotics.robot.commands.auto.MotionProfiling.AutoScaleLeftRight;
import org.wfrobotics.robot.config.Autonomous.StartingPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSide extends CommandGroup
{
    private final MatchState2018 state = MatchState2018.getInstance();

    public AutoSide(StartingPosition location)
    {
        //addParallel(new AutoZero());

        if (state.Scale == Side.Unknown)
        {
            addSequential(new DriveDistance(12.0 * 3.0));
        }
        else if (scaleOnThisSide(location))
        {
            addSequential(new AutoScaleLeftLeft());
        }
        else
        {
            addSequential(new AutoScaleLeftRight());
        }
    }

    private boolean scaleOnThisSide(StartingPosition location)
    {
        return (location == StartingPosition.Right && state.Scale == Side.Right) || (location == StartingPosition.Left && state.Scale == Side.Left);
    }
}
