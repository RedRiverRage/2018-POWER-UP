package org.wfrobotics.robot.commands.auto;

import org.wfrobotics.reuse.commands.drive.DriveDistance;
import org.wfrobotics.robot.commands.intake.JawsSet;
import org.wfrobotics.robot.commands.intake.WristSet;
import org.wfrobotics.robot.config.Field;
import org.wfrobotics.robot.config.RobotConfig;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoReachLineStraight extends CommandGroup {

    public AutoReachLineStraight() {
        final double robotLengthWithBumpers = RobotConfig.getInstance().RobotLengthWithBumpers();

        // make sure the wrist and the jaws are up and clamped
        addSequential(new WristSet(true));
        addSequential(new JawsSet(true));

        // drive forward (auto line - robot size) + a little extra to be safe
        addSequential(new DriveDistance(Field.kdistanceToAutoLineInches - robotLengthWithBumpers + 18));
    }
}
