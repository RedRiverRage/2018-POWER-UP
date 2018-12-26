package org.wfrobotics.robot.commands.auto.twocube;

import org.wfrobotics.robot.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/** Set the jaw solenoids open or closed to nom nom nom dat cube */
public class JawsSet extends Command
{
    private final boolean wantOpen;

    public JawsSet(boolean open, double timeout, boolean blockIntake)
    {
        if (blockIntake)  // Don't allow SmartIntake to override this autonomous command
        {
            requires(IntakeSubsystem.getInstance());
        }
        wantOpen = open;
        setTimeout(timeout);
    }

    protected void initialize()
    {
        IntakeSubsystem.getInstance().setHorizontal(wantOpen);
    }

    protected boolean isFinished()
    {
        return isTimedOut();
    }
}
