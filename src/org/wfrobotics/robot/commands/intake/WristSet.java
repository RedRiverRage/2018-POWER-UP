package org.wfrobotics.robot.commands.intake;

import org.wfrobotics.robot.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class WristSet extends InstantCommand
{
    boolean wantOpen;

    public WristSet(boolean open)
    {
        wantOpen = open;
    }

    protected void initialize()
    {
        IntakeSubsystem.getInstance().setVert(wantOpen);
    }
}
