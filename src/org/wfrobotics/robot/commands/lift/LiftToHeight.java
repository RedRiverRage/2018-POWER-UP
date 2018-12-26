package org.wfrobotics.robot.commands.lift;

import org.wfrobotics.robot.subsystems.LiftSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftToHeight extends Command
{
    private final double TOLERANCE_INCHES = 1.0;
    private final double desired;

    public LiftToHeight(double desired)
    {
        requires(LiftSubsystem.getInstance());
        this.desired = desired;
    }

    protected void initialize()
    {
        SmartDashboard.putString("Lift", getClass().getSimpleName());
        setTimeout(2.5);

        LiftSubsystem.getInstance().goToHeightInit(desired);
    }

    protected boolean isFinished()
    {
        return LiftSubsystem.getInstance().isAtDesiredHeight(TOLERANCE_INCHES) || isTimedOut();
    }
}
