package org.wfrobotics.robot.commands.auto.twocube;

import org.wfrobotics.reuse.commands.wrapper.AutoMode;
import org.wfrobotics.robot.commands.MatchState2018;
import org.wfrobotics.robot.commands.MatchState2018.Side;
import org.wfrobotics.robot.commands.auto.AutoReachLineStraight;
import org.wfrobotics.robot.config.Autonomous.StartingPosition;

public class ModeSide extends AutoMode
{
    private final MatchState2018 state = MatchState2018.getInstance();

    public static enum CubeCount
    {
        None,
        Single,
        Double,
        DoubleButNoneOnFarSide
    }
    public static enum Type
    {
        SameSideOnly,
        SameSideAndCross
    }

    public ModeSide(StartingPosition location, Type type, CubeCount cubes)
    {
        // TODO fall back to placing on the switch if needed

        // if we didn't get match data
        if (state.Scale == Side.Unknown )
        {
            addSequential(new AutoReachLineStraight());
        }

        // if we're on the same side as the scale
        else if (scaleOnThisSide(location))
        {
            addSequential(new ModeScale(location, cubes));
        }

        // if we want to do a cross
        else if (type == Type.SameSideAndCross)
        {
            addSequential(new AutoOppisitScalse(location, cubes));
        }

        // else ensure we at least drive forward
        else
        {
            addSequential(new AutoReachLineStraight());
        }
    }

    private boolean scaleOnThisSide(StartingPosition location)
    {
        return (location == StartingPosition.Right && state.Scale == Side.Right) || (location == StartingPosition.Left && state.Scale == Side.Left);
    }
}
