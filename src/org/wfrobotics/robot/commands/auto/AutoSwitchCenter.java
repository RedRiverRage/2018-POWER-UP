package org.wfrobotics.robot.commands.auto;

import org.wfrobotics.reuse.commands.drive.DriveCheesy;
import org.wfrobotics.reuse.commands.drive.DriveDistance;
import org.wfrobotics.reuse.commands.drive.TurnToHeading;
import org.wfrobotics.robot.commands.MatchState2018.Side;
import org.wfrobotics.robot.commands.SwitchChoice;
import org.wfrobotics.robot.commands.intake.IntakePush;
import org.wfrobotics.robot.commands.intake.JawsSet;
import org.wfrobotics.robot.commands.intake.WristSet;
import org.wfrobotics.robot.commands.lift.LiftToHeight;
import org.wfrobotics.robot.commands.lift.LiftZero;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSwitchCenter extends CommandGroup
{
    public AutoSwitchCenter()
    {
        // make sure the wrist and the jaws are up and clamped
        addSequential(new LiftZero());
        addSequential(new WristSet(true));
        addSequential(new JawsSet(true));

        // drive forward so we can turn without hitting the wall
        addSequential(new DriveDistance(24));

        // turn to face the switch
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
            addSequential(new SwitchChoice(Side.Right,
                                            new TurnToHeading(40),
                                            new TurnToHeading(-40)));
        }

        // lift the first stage to 12 inches, so intake is over the switch edge
        addParallel(new LiftToHeight(12));

        // drive at the angle towards the switch
        // we should drive a little bit less to the right
        addSequential(new SwitchChoice(Side.Right,
                                        new DriveDistance(6 * 12),
                                        new DriveDistance(6.5 * 12)));

        // turn to face the switch
        addSequential(new TurnToHeading(0));

        // drive forward so we ensure we're over the lip to make the refs happy
        addSequential(new DriveDistance(14));

        // push out the cube
        addSequential(new IntakePush(0.5), 1);

        // turn off the intake
        addSequential(new IntakePush(0), .2);

        // drive backward
        addSequential(new DriveDistance(-12));

        // drop the lift to just above the bottom
        addSequential(new LiftToHeight(2));

        // drop the wrist and open the jaws
        addSequential(new WristSet(false));
        addSequential(new JawsSet(false));

        // make sure to stop driving
        addSequential(new DriveCheesy());
    }
}
