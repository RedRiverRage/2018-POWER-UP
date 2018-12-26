package org.wfrobotics.robot.commands.auto.MotionProfiling;

import org.wfrobotics.reuse.commands.drive.TurnToHeading;
import org.wfrobotics.robot.commands.intake.IntakePush;
import org.wfrobotics.robot.commands.intake.JawsSet;
import org.wfrobotics.robot.commands.intake.SmartIntake;
import org.wfrobotics.robot.commands.intake.WristSet;
import org.wfrobotics.robot.commands.lift.LiftToHeight;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;


public class AutoScaleLeftLeft extends CommandGroup
{
    public AutoScaleLeftLeft()
    {
        //Robot.driveService.zeroGyro();
        addParallel(new SmartIntake());
        addSequential(new JawsSet(true));

        addSequential(new WristSet(true));
        //        addParallel(new DrivePathPosition("Scale_Left_Left"));
        addSequential(new LiftToHeight(28));
        addSequential(new IntakePush(1));
        addParallel(new LiftToHeight(0));
        addSequential(new TurnToHeading(180));
        addSequential(new WaitCommand(1.0));

        addSequential(new WristSet(false));
        addSequential(new JawsSet(false));

        //        addParallel(new DrivePathPosition("Scale_Left2"));

        addSequential(new LiftToHeight(28));
        addSequential(new IntakePush(1));
    }
}