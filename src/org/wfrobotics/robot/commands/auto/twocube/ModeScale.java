package org.wfrobotics.robot.commands.auto.twocube;

import org.wfrobotics.reuse.commands.drive.DriveDistance;
import org.wfrobotics.reuse.commands.drive.TurnToHeading;
import org.wfrobotics.reuse.commands.wrapper.AutoMode;
import org.wfrobotics.reuse.commands.wrapper.SynchronizedCommand;
import org.wfrobotics.robot.commands.auto.twocube.ModeSide.CubeCount;
import org.wfrobotics.robot.commands.intake.SmartIntake;
import org.wfrobotics.robot.commands.intake.WaitUntilCube;
import org.wfrobotics.robot.commands.intake.WristSet;
import org.wfrobotics.robot.commands.lift.LiftToHeight;
import org.wfrobotics.robot.config.Autonomous.StartingPosition;
import org.wfrobotics.robot.config.LiftHeight;

import edu.wpi.first.wpilibj.command.WaitCommand;

public class ModeScale extends AutoMode
{
    private final double angleToScale = -43; //-50
    private final double angleToSecondCube = -148.75; //-145.06
    private final double inchesToSecondCube = 66; //78
    private final double speedOuttake = 0.4;
    private final double timeOuttake = 0.55;

    public ModeScale(StartingPosition location, ModeSide.CubeCount cubes)
    {
        // Setup
        //addSequential(new GyroZero());  // For teleop testing
        //addSequential(new WaitCommand(waitForGyroToFullyZero));

        // Travel to first scale
        //addSequential(new DriveDistance(12.0 * 12.5 - 29.1));
        //addSequential(new SynchronizedCommand(new DriveDistance(12.0 * 12.0), new LiftToScale()));
        // OR
        //addSequential(new WristSet(false));
        addParallel(new WristSet(false));  // Keep cube level to prevent slippage
        addParallel(new WristSet(true));  // Keep cube level to prevent slippage
        addSequential(new SynchronizedCommand(new DriveDistance(12.0 * (12.0 + 12.5) - 38.1 - 8), new LiftToScale()));


        // -------------- Common --------------

        // Score first scale
        addSequential(new TurnToHeading((location == StartingPosition.Right) ? angleToScale : -angleToScale));
        addParallel(new WristSet(true));
        addSequential(new IntakeSet(speedOuttake, timeOuttake, true));

        // Reset
        addParallel( new LiftToHeight(LiftHeight.Scale.get() - 11.0));
        addSequential(new TurnToHeading((location == StartingPosition.Right) ? angleToSecondCube + 30.0 : -angleToSecondCube - 30.0)); // to find distance: x= 51 y= 73
        addSequential(new WaitCommand(0.05));
        addParallel(new WristSet(false));
        addSequential(new LiftToHeight(LiftHeight.Intake.get()));
        addSequential(new TurnToHeading((location == StartingPosition.Right) ? angleToSecondCube : -angleToSecondCube)); // to find distance: x= 51 y= 73
        addSequential(new WaitCommand(0.5));

        // Acquire second cube
        addParallel(new LiftEnsureBottom(-0.2, 0.5));  // Ensure at smart intake height
        addParallel(new JawsSet(false, 0.1, false));  // Prime smart intake
        addParallel(new SmartIntake());

        if(cubes == CubeCount.Single)
        {
            addSequential(new DriveInfared(4.0, inchesToSecondCube + 21.0, 0.05, 2.0));  // Champs Friday 10am, added 6 inches
            addSequential(new WaitUntilCube());
            addSequential(new WaitCommand(.05));

            addParallel(new WristSet(true));
            addSequential(new WaitCommand(.05));
        }
        else if (cubes == CubeCount.Double || cubes == CubeCount.DoubleButNoneOnFarSide)
        {
            addSequential(new DriveInfared(4.0, inchesToSecondCube + 21.0, 0.05, 2.0));  // Champs Friday 10am, added 6 inches
            addSequential(new WaitUntilCube());
            addSequential(new WaitCommand(.05));

            addParallel(new WristSet(true));
            addSequential(new WaitCommand(.05));
            addSequential(new DriveDistance(-5.0));
            addSequential(new TurnToHeading((location == StartingPosition.Right) ? angleToSecondCube : -angleToSecondCube), .2); // to find distance: x= 51 y= 73
            addSequential(new WaitCommand(.05));

            // Travel to second scale
            addSequential(new DriveDistance(-inchesToSecondCube + 5.0));

            // Score second cube
            addSequential(new SynchronizedCommand(new TurnToHeading((location == StartingPosition.Right) ? angleToScale - 7.5 : -angleToScale + 7.5), new LiftToHeight(LiftHeight.Scale.get() - 10.0)));
            //            addSequential(new WaitCommand(.1));
            addSequential(new IntakeSet(speedOuttake * 1.3, timeOuttake, true));

            // Reset
            // addSequential(new TurnToHeading((location == StartingPosition.Right) ? 20 : -20, 1.0));
            // addSequential(new LiftToHeight(LiftHeight.Intake.get()));
        }
    }
}