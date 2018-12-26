package org.wfrobotics.robot.commands.auto.MotionProfiling;

import org.wfrobotics.reuse.commands.config.GyroZero;
import org.wfrobotics.reuse.commands.drive.DriveDistance;
import org.wfrobotics.reuse.commands.drive.TurnToHeading;
import org.wfrobotics.robot.commands.intake.SmartOutput;
import org.wfrobotics.robot.commands.lift.LiftToHeight;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutoSwitchLeftLeft extends CommandGroup
{
    public AutoSwitchLeftLeft()
    {
        addSequential(new GyroZero());

        //        addParallel(new DrivePathPosition("Switch_Left_Left"));
        addSequential(new LiftToHeight(6));
        addSequential(new SmartOutput());

        addSequential(new TurnToHeading(-40));

        addSequential(new WaitCommand(1.0));

        //        addParallel(new DrivePathPosition("Switch_Left2"));
        addSequential(new LiftToHeight(0));
        addSequential(new LiftToHeight(6));
        addSequential(new DriveDistance(1));
        addSequential(new SmartOutput());
    }
}
