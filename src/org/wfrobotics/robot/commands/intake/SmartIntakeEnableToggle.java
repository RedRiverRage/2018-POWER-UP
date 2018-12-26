package org.wfrobotics.robot.commands.intake;

import org.wfrobotics.robot.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class SmartIntakeEnableToggle extends InstantCommand
{
    protected void initialize()
    {
        IntakeSubsystem.getInstance().SmartIntakeEnabled = !IntakeSubsystem.getInstance().SmartIntakeEnabled;
    }
}
