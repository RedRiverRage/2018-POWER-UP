package org.wfrobotics.robot.commands.auto;

import org.wfrobotics.reuse.commands.drive.DriveDistance;
import org.wfrobotics.robot.commands.intake.JawsSet;
import org.wfrobotics.robot.commands.intake.WristSet;
import org.wfrobotics.robot.commands.lift.LiftZero;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSwitchSide extends CommandGroup
{
    public final static double TURN_TOLERANCE = 3.0;

    /**drives from alliance station corner past line to the switch
     * turns based on which side of station it is on (far from exchange/not)
     * Ejects stored crate into switch
     */
    public AutoSwitchSide()
    {
        // make sure the wrist and the jaws are up and clamped
        addSequential(new LiftZero());
        addSequential(new WristSet(true));
        addSequential(new JawsSet(true));

        // drive forward so until we are inline with the switch
        // BDP we could calculate this as distance to switch - half of robot size (center) + half of switch size (center)
        // and this should align the center of the robot with the switch
        // but this comes out to be around 169 inches instead of 144, so test?
        //double travelDistance = Field.kdistanceToSwitchInches
        //                                - Robot.config.RobotLengthWithBumpers() / 2
        //                                + Field.kSwitchWidthFromSideInches / 2;
        //addSequential(new DriveDistance(travelDistance));

        addSequential(new DriveDistance(12.0*12.0));


        // now we need to know if the switch is on the same side as we are
        addSequential(new SwitchOnSameSideAsStartingPosition(
                                        new AutoSwitchSidePlaceSide(), // continue running our auto
                                        new AutoSwitchOppositeSide() // Run the opposite command
                                        // TODO change this to be place on scale if its on our side
                                        ));
    }



    /**
     * Sub class to actually place on the switch depending on which side the switch is on
     */
    public static class AutoSwitchSidePlaceSide extends CommandGroup
    {
        public AutoSwitchSidePlaceSide()
        {

        }
    }

    public static class AutoSwitchOppositeSide extends CommandGroup
    {
        public AutoSwitchOppositeSide()
        {

        }
    }
}
