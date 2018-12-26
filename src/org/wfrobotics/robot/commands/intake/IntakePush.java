package org.wfrobotics.robot.commands.intake;

import org.wfrobotics.robot.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class IntakePush extends Command
{
    protected final IntakeSubsystem intake = IntakeSubsystem.getInstance();
    private double speed = 1;

    public IntakePush()
    {
        requires(intake);
    }

    public IntakePush(double speed, int timeout)
    {
        requires(intake);
        setTimeout(timeout);
        this.speed = speed;
    }

    public IntakePush(double speed)
    {
        requires(intake);
        this.speed = speed;
    }

    protected void execute()
    {
        IntakeSubsystem.getInstance().setMotor(speed); // TODO Pass a boolean, pick either in or out speed off that, make instantcommand, rename to IntakeSet?
    }

    protected void end()
    {
        IntakeSubsystem.getInstance().setMotor(0);
    }

    protected boolean isFinished()
    {
        return isTimedOut();
    }
}
