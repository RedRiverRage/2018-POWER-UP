package org.wfrobotics.robot.commands.auto.twocube;

import org.wfrobotics.reuse.commands.drive.DriveDistance;
import org.wfrobotics.reuse.commands.drive.TurnToHeading;
import org.wfrobotics.reuse.commands.wrapper.AutoMode;
import org.wfrobotics.robot.commands.MatchState2018.Side;
import org.wfrobotics.robot.commands.SwitchChoice;
import org.wfrobotics.robot.commands.intake.SmartIntake;
import org.wfrobotics.robot.commands.intake.WristSet;
import org.wfrobotics.robot.commands.lift.LiftToHeight;
import org.wfrobotics.robot.config.LiftHeight;

import edu.wpi.first.wpilibj.command.WaitCommand;

public class ModeCenter extends AutoMode
{
    public static enum Type
    {
        SingleCube,
        SingleCubeAndBackup,
        TwoCube
    }

    public ModeCenter(Type type)
    {
        addParallel(new WristSet(true));
        addParallel(new LiftToHeight(LiftHeight.Switch.get()));
        //        addSequential(new SwitchChoice(Side.Right, new DrivePath("CenterRight"), new DrivePathPosition("CenterLeft")));
        addSequential(new WristSet(false));
        addSequential(new IntakeSet(.75, 0.5, true));  // Yes, 1.0 outtake is good here

        if(type == Type.SingleCubeAndBackup)
        {
            //oneCube();
            twoCube();
        }
        else if(type == Type.TwoCube)
        {
            //oneCube();
            twoCube();
        }
    }

    public void oneCube()
    {
        // Get away from wall
        addSequential(new DriveDistance(-12.0 * 1.0));
        addParallel(new WristSet(false));
        addParallel(new LiftToHeight(LiftHeight.Intake.get()));
        addSequential(new SwitchChoice(Side.Right, new TurnToHeading(-90.0), new TurnToHeading(90.0)));
        //        addSequential(new IntakePush(0.0, 15));  // Don't open and close jaws
    }

    public void twoCube()
    {
        // Get away from wall
        addParallel(new LiftToHeight(LiftHeight.Intake.get()));
        addSequential(new DriveDistance(-12 * 1));
        addParallel(new JawsSet(false, .1, false));
        addParallel(new WristSet(false));
        addSequential(new SwitchChoice(Side.Right, new TurnToHeading(-82.5), new TurnToHeading(82.5)));
        addSequential(new WaitCommand(0.5));
        //Grab the second cube
        addParallel(new IntakeSet(0.5, 1.0, false));
        addSequential(new DriveDistance(12.0 * 4));
        addParallel(new SmartIntake());
        addSequential(new WaitCommand(1));
        addParallel(new WristSet(true));

        //Back up to the switch
        addSequential(new DriveDistance(12.0 * -3));
        addSequential(new TurnToHeading(0));
        addSequential(new WaitCommand(0.5));
        addSequential(new LiftToHeight(LiftHeight.Switch.get()+5));
        addSequential(new DriveDistance(12 * 1));
        //Score the second cube
        addSequential(new WristSet(false));
        addSequential(new IntakeSet(.75, 0.5, true));

        //Get ready grab a cube in teleop
        addSequential(new DriveDistance(-12 * 2));
        addSequential(new LiftToHeight(0));
        addSequential(new DriveDistance(12 * 1));

        addSequential(new SwitchChoice(Side.Right, new TurnToHeading(-90.0), new TurnToHeading(90.0)));
        //addSequential(new DriveDistance())
    }
}
