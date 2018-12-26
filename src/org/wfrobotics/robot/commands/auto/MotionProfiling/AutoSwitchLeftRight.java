package org.wfrobotics.robot.commands.auto.MotionProfiling;

import org.wfrobotics.reuse.commands.drive.TurnToHeading;
import org.wfrobotics.robot.commands.intake.IntakePush;
import org.wfrobotics.robot.commands.lift.LiftToHeight;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSwitchLeftRight extends CommandGroup
{
    public AutoSwitchLeftRight()
    {
        //        addParallel(new DrivePathPosition("Switch_Left_Right"));
        addSequential(new LiftToHeight(48));
        addSequential(new IntakePush());
        addSequential(new TurnToHeading(180));
        //        addParallel(new DrivePathPosition("Switch_Right2"));
        addSequential(new LiftToHeight(0));
        addSequential(new LiftToHeight(48));
        addSequential(new IntakePush());
    }
}
