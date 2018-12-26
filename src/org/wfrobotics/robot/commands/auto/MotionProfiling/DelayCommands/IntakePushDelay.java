package org.wfrobotics.robot.commands.auto.MotionProfiling.DelayCommands;

import java.util.concurrent.TimeUnit;

import org.wfrobotics.robot.commands.intake.IntakePush;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class IntakePushDelay extends CommandGroup
{
    public IntakePushDelay(int delay)
    {
        try
        {
            TimeUnit.SECONDS.sleep(delay);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        addSequential(new IntakePush());
    }
}
