package org.wfrobotics.robot.commands.intake;

import org.wfrobotics.robot.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class WristToggle extends InstantCommand
{
    // TODO Make this work like JawsToggle, which is super working

    protected void initialize()
    {
        IntakeSubsystem.getInstance().setVert(!IntakeSubsystem.getInstance().getVertical());
    }
}
