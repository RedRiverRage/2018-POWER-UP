package org.wfrobotics.robot.commands.auto.MotionProfiling.DelayCommands;

import java.util.concurrent.TimeUnit;

import org.wfrobotics.robot.commands.lift.LiftToHeight;

import edu.wpi.first.wpilibj.command.CommandGroup;

//Delayed CommandGroups for two cube autos
public class LiftDelay extends CommandGroup
{
    public LiftDelay(int delay, int height)
    {
        try
        {
            TimeUnit.SECONDS.sleep(delay);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        addSequential(new LiftToHeight(height));
    }
}
