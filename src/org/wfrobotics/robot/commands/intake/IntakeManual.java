package org.wfrobotics.robot.commands.intake;

import org.wfrobotics.robot.Robot;
import org.wfrobotics.robot.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeManual extends Command
{
    protected final IntakeSubsystem intake = IntakeSubsystem.getInstance();

    public IntakeManual()
    {
        requires(intake);
    }

    protected void initialize()
    {
        SmartDashboard.putString("Intake", "Manual");
    }

    protected void execute()
    {
        intake.setMotor(Robot.controls.getIntakeManual());
    }

    protected boolean isFinished()
    {
        return false;
    }

}
