package org.wfrobotics.robot.commands.auto.MotionProfiling;

import org.wfrobotics.reuse.commands.drive.TurnToHeading;
import org.wfrobotics.robot.commands.auto.MotionProfiling.DelayCommands.IntakePushDelay;
import org.wfrobotics.robot.commands.lift.LiftToHeight;

import edu.wpi.first.wpilibj.command.CommandGroup;



public class AutoScaleRightRight extends CommandGroup
{
    public AutoScaleRightRight()
    {
        //        addParallel(new DrivePathPosition("Scale_Right_Right"));
        addSequential(new LiftToHeight(48));
        addSequential(new IntakePushDelay(1));
        addSequential(new TurnToHeading(180));
        //        addParallel(new DrivePathPosition("Scale_Right2"));
        addSequential(new LiftToHeight(0));
        addSequential(new LiftToHeight(48));
        addSequential(new IntakePushDelay(1));
    }
}