package org.wfrobotics.robot.commands.intake;

import org.wfrobotics.robot.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.command.InstantCommand;

/** Set the jaw solenoids to the opposite state, repeated-buttonsmashing-safe */
public class JawsToggle extends InstantCommand
{
    protected void initialize()
    {
        IntakeSubsystem.getInstance().setHorizontal(!IntakeSubsystem.getInstance().getHorizontal());
    }
}
