package org.wfrobotics.robot.commands.auto.MotionProfiling;

import org.wfrobotics.reuse.commands.drive.TurnToHeading;
import org.wfrobotics.robot.commands.intake.IntakePush;
import org.wfrobotics.robot.commands.lift.LiftToHeight;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoScaleRightLeft extends CommandGroup
{
    public AutoScaleRightLeft()
    {
        //        addParallel(new DrivePathPosition("Scale_Right_Left"));
        addSequential(new LiftToHeight(48));
        addSequential(new IntakePush());
        addSequential(new TurnToHeading(180));
        //        addParallel(new DrivePathPosition("Scale_Left2"));
        addSequential(new LiftToHeight(0));
        addSequential(new LiftToHeight(48));
        addSequential(new IntakePush());
    }
}
