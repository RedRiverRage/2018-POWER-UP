package org.wfrobotics.robot.commands.auto;

import org.wfrobotics.reuse.commands.drive.DriveCheesy;
import org.wfrobotics.reuse.commands.drive.DriveDistance;
import org.wfrobotics.reuse.commands.drive.TurnToHeading;
import org.wfrobotics.robot.commands.MatchState2018.Side;
import org.wfrobotics.robot.commands.ScaleChoice;
import org.wfrobotics.robot.commands.auto.AutoSwitchSide.AutoSwitchSidePlaceSide;
import org.wfrobotics.robot.commands.intake.IntakePush;
import org.wfrobotics.robot.commands.intake.JawsSet;
import org.wfrobotics.robot.commands.intake.WristSet;
import org.wfrobotics.robot.commands.lift.LiftToHeight;
import org.wfrobotics.robot.commands.lift.LiftZero;
import org.wfrobotics.robot.config.Field;
import org.wfrobotics.robot.config.RobotConfig;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoScaleSide extends CommandGroup
{
    public AutoScaleSide()
    {
        final double robotLengthWithBumpers = RobotConfig.getInstance().RobotLengthWithBumpers();

        // make sure the wrist and the jaws are up and clamped
        addSequential(new LiftZero());
        addSequential(new WristSet(true));
        addSequential(new JawsSet(true));

        // drive forward so until we are inline with the switch
        // BDP we could calculate this as distance to switch - half of robot size (center) + half of switch size (center)
        // and this should align the center of the robot with the scale
        double travelDistance = Field.kDistanceToScaleInches
                                        - robotLengthWithBumpers / 2
                                        + Field.kSwitchWidthFromSideInches / 2;

        addSequential(new ScaleOnSameSideAsStartingPosition(
                                        new DriveDistance(travelDistance - (4 *12)), // continue running our auto
                                        new DriveDistance(12.0*12.0) // otherwise stop moving
                                        // TODO change this to be place on scale if its on our side
                                        ));

        // now we need to know if the switch is on the same side as we are
        addSequential(new ScaleOnSameSideAsStartingPosition(
                                        new AutoScaleSidePlaceSide(), // continue running our auto
                                        new SwitchOnSameSideAsStartingPosition(new AutoSwitchSidePlaceSide(),
                                                                        new DriveCheesy())
                                        ));
    }



    /**
     * Sub class to actually place on the scale depending on which side the scale is on
     */
    public class AutoScaleSidePlaceSide extends CommandGroup
    {
        public AutoScaleSidePlaceSide()
        {
            // turn to face the scale
            {
                // boolean switchOnRight = MatchState2018.getInstance().SwitchNear == Side.Right;
                // if(switchOnRight)
                // {
                //     addSequential(new TurnToHeading(35, TURN_TOLERANCE));
                // }
                //
                // else
                // {
                //     addSequential(new TurnToHeading(-35, TURN_TOLERANCE));
                // }

                // try this to turn
                // if this doesn't work, uncomment the lines above
                addSequential(new ScaleChoice(Side.Right,
                                                new TurnToHeading(-90),
                                                new TurnToHeading(90)));
            }

            // lifting to get high enough
            addParallel(new LiftToHeight(36));

            // drive toward the switch
            addSequential(new DriveDistance(-12));

            addSequential(new DriveDistance(12));

            // shoot the cube out
            addSequential(new IntakePush(1), 1);

            // turn off the intake
            addSequential(new IntakePush(0), .2);

            // drive backward
            addSequential(new DriveDistance(-12*2));

            // drop the lift to just above the bottom
            addSequential(new LiftToHeight(2));

            // drop the wrist and open the jaws
            addSequential(new WristSet(false));
            addSequential(new JawsSet(false));

            // turn to face away from the drivers
            addSequential(new TurnToHeading(0));

            // make sure to stop driving
            addSequential(new DriveCheesy());
        }
    }
}
