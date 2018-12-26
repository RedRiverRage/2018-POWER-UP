package org.wfrobotics.robot.commands.auto.twocube;

import org.wfrobotics.robot.subsystems.LiftSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftEnsureBottom extends Command
{
    private final double speed;

    public LiftEnsureBottom(double speed, double timeout)
    {
        requires(LiftSubsystem.getInstance());
        this.speed = speed;
        setTimeout(timeout);
    }

    protected void initialize()
    {
        SmartDashboard.putString("Lift", getClass().getSimpleName());
    }

    protected void execute()
    {
        LiftSubsystem.getInstance().goToSpeedInit(speed);  // Must be in execute in case interrupted
    }

    protected boolean isFinished()
    {
        return LiftSubsystem.getInstance().isAtBottom() || isTimedOut();
    }

    protected void end()
    {
        LiftSubsystem.getInstance().goToSpeedInit(0);
    }
}
