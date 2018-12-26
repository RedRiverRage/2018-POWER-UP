package org.wfrobotics.robot.commands.intake;

import org.wfrobotics.robot.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartOutput extends Command
{
    protected final IntakeSubsystem intake = IntakeSubsystem.getInstance();
    private double motorSpeed = 0.3;

    public SmartOutput()
    {
        requires(intake);
        setTimeout(1);
    }

    public SmartOutput(double motorSpeed)
    {
        this.motorSpeed = motorSpeed;
        requires(intake);
        setTimeout(1);
    }

    protected void initialize()
    {
        SmartDashboard.putString("Intake", "Smart Out");
    }

    protected void execute()
    {
        intake.setMotor(motorSpeed);
        if (timeSinceInitialized() > .2)
        {
            intake.setHorizontal(false);
        }
    }

    protected boolean isFinished()
    {
        return isTimedOut();
    }
}
