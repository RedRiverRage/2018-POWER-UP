package org.wfrobotics.robot.commands.intake;

import org.wfrobotics.robot.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.command.TimedCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeAndClamp extends TimedCommand
{
    protected final IntakeSubsystem intake = IntakeSubsystem.getInstance();

    private final static double kTimeoutSec = 1.5;

    public IntakeAndClamp()
    {
        super(kTimeoutSec);
        requires(intake);
    }

    protected void initialize()
    {
        SmartDashboard.putString("Intake", "AndClamp");
    }

    protected void execute()
    {
        intake.setMotor(-.5);

        if(timeSinceInitialized() > .25)
        {
            intake.setHorizontal(true);
        }
    }

    protected void end()
    {
        //intake.setVert(true);
    }

}
