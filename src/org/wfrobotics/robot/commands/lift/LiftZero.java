package org.wfrobotics.robot.commands.lift;

import org.wfrobotics.robot.subsystems.LiftSubsystem;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class LiftZero extends InstantCommand
{
    public LiftZero()
    {
        requires(LiftSubsystem.getInstance());
    }

    public void initialize()
    {
        LiftSubsystem.getInstance().zeroPosition();
    }
}
