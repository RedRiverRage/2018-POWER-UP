package org.wfrobotics.robot.commands.auto.twocube;

import org.wfrobotics.robot.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.command.Command;

/** Set the jaw solenoids open or closed to nom nom nom dat cube */
public class IntakeSet extends Command
{
    private final double speed;

    public IntakeSet(double percentageOutward, double timeout, boolean blockIntake)
    {
        if (blockIntake)  // Don't allow SmartIntake to override this autonomous command
        {
            requires(IntakeSubsystem.getInstance());
        }
        speed = percentageOutward;
        setTimeout(timeout);
    }

    protected void initialize()
    {
        IntakeSubsystem.getInstance().setMotor(speed);
    }

    protected boolean isFinished()
    {
        return isTimedOut();
    }

    protected void end()
    {
        IntakeSubsystem.getInstance().setMotor(0);
    }
}
