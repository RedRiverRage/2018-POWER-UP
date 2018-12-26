package org.wfrobotics.robot.commands;

import org.wfrobotics.reuse.subsystems.drive.TankSubsystem;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class TogglingBrake extends InstantCommand
{
    private boolean request;
    private final boolean toggle;
    private boolean toggleVal = false;

    public TogglingBrake()
    {
        toggle = true;
    }
    public TogglingBrake(boolean enable)
    {
        toggle = false;
        request = enable;
    }

    protected void initialize()
    {
        if(toggle)
        {
            toggleVal = !toggleVal;
            TankSubsystem.getInstance().setBrake(toggleVal);
        }
        else
        {
            TankSubsystem.getInstance().setBrake(request);
        }
    }
}
