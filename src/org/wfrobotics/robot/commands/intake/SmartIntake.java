package org.wfrobotics.robot.commands.intake;

import org.wfrobotics.reuse.math.Util;
import org.wfrobotics.robot.RobotState;
import org.wfrobotics.robot.subsystems.IntakeSubsystem;
import org.wfrobotics.robot.subsystems.LiftSubsystem;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Pull in the cube purely based on sensors */
public class SmartIntake extends CommandGroup
{
    private final double kCubeIn = 8.0;
    private final double kCubeInDeadband = 1.25;

    protected final RobotState state = RobotState.getInstance();
    protected final IntakeSubsystem intake = IntakeSubsystem.getInstance();


    /**
     * This command gets the distance from the subsystem and based on that distance
     * drive the motors at different speeds
     */
    public SmartIntake()
    {
        requires(intake);
    }

    protected void initialize()
    {
        SmartDashboard.putString("Intake", "Smart");
    }

    protected void execute()
    {
        if(IntakeSubsystem.getInstance().SmartIntakeEnabled && LiftSubsystem.getInstance().getHeightAverage() < 1 && !IntakeSubsystem.getInstance().getVertical())
        {
            double distanceToCube = state.intakeDistance;

            autoIntake(distanceToCube);
            autoJaws(distanceToCube);
        }
        else  // Cancel intaking if transition to lifting
        {
            intake.setMotor(0.0);
        }
    }

    protected boolean isFinished()
    {
        return false;
    }

    private void autoIntake(double distanceToCube)
    {
        SmartDashboard.putBoolean("In the if", true);
        double speed = 0.0;

        if (distanceToCube < kCubeInDeadband)
        {
            intake.setMotor(0);
        }

        if (distanceToCube < kCubeIn && distanceToCube > kCubeInDeadband)
        {
            speed = -Util.scaleToRange(distanceToCube, kCubeInDeadband, kCubeIn, .25, 0.7);
        }
        else if (distanceToCube > kCubeIn && distanceToCube < 50)  // TODO Need to move sensor, otherwise we stall motors
        {
            speed = -0.7;
        }

        // TODO After it's in for a little bit, it's SUPER effective to pulse the cube out a sec then back in to orient it
        //      This would also help us not stall if we don't drive wheels after that pulse. Or we move the distance sensor back enough to always be in a valid range.

        intake.setMotor(speed);
    }

    private void autoJaws(double distanceToCube)
    {
        if (distanceToCube < 30)
        {
            intake.setHorizontal(true);  // Can't always set, otherwise we chatter?
        }
        else if (distanceToCube > 30 && distanceToCube < 75)  // TODO find ideal range to be auto-opened, put in RobotMap or use robot state
        {
            intake.setHorizontal(false);
        }
    }
}
