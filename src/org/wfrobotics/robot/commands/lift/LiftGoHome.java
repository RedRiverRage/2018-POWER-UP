package org.wfrobotics.robot.commands.lift;

import org.wfrobotics.robot.subsystems.LiftSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftGoHome extends Command
{
    private static boolean hasZeroed = false;

    public static boolean everZeroed()
    {
        return hasZeroed;
    }

    public LiftGoHome()
    {
        requires(LiftSubsystem.getInstance());
    }

    protected void initialize()
    {
        SmartDashboard.putString("Lift", getClass().getSimpleName());
    }

    protected void execute()
    {
        LiftSubsystem.getInstance().goToSpeedInit(getSpeed());
    }

    protected boolean isFinished()
    {
        boolean result = LiftSubsystem.getInstance().isAtBottom();

        if (result)
        {
            hasZeroed = true;
        }
        return result;
    }

    private double getSpeed()
    {
        return -.2;
    }
}
