package org.wfrobotics.robot.commands.lift;

import org.wfrobotics.reuse.math.Util;
import org.wfrobotics.robot.Robot;
import org.wfrobotics.robot.subsystems.LiftSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftPercentVoltage extends Command
{
    private final double deadbandPercent = .2;
    private final double maxHeight = 38;
    private final double slowRange = 12.0;
    private final double slowSpeedUp = .35;
    private final double slowSpeedDown = .2;

    public LiftPercentVoltage()
    {
        requires(LiftSubsystem.getInstance());
    }

    protected void initialize()
    {
        SmartDashboard.putString("Lift", getClass().getSimpleName());
    }

    protected void execute()
    {
        double setpoint = Robot.controls.getLiftStick();

        if (Math.abs(setpoint) < deadbandPercent)
        {
            LiftSubsystem.getInstance().goToSpeedInit(0);
        }
        else
        {
            double currentHeight = LiftSubsystem.getInstance().getHeightAverage();
            if(setpoint < 0.0 && currentHeight < slowRange)
            {
                double heightScale = Util.scaleToRange(currentHeight , 0, slowRange, slowSpeedDown, 1.0);
                setpoint *= heightScale;
            }
            else if(setpoint > 0.0 && currentHeight > (maxHeight - slowRange))
            {
                double heightScale = Util.scaleToRange(maxHeight-currentHeight, 0, slowRange, slowSpeedUp, 1.0);
                setpoint *= heightScale;
            }
            LiftSubsystem.getInstance().goToSpeedInit(setpoint);
        }
    }

    protected boolean isFinished()
    {
        return false;
    }
}
