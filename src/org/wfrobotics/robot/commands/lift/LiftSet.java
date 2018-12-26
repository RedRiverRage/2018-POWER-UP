package org.wfrobotics.robot.commands.lift;

import org.wfrobotics.robot.subsystems.LiftSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftSet extends Command
{
    double setpoint = 0;

    public LiftSet(double setpoint)
    {
        requires(LiftSubsystem.getInstance());
        this.setpoint = setpoint;
    }

    protected void initialize()
    {
        SmartDashboard.putString("Lift", getClass().getSimpleName());
    }

    protected void execute()
    {
        //        if(Math.abs(setpoint) > Robot.config.LIFT_SET_SPEED_MAX)
        //        {
        //            setpoint = Math.signum(setpoint) * Robot.config.LIFT_SET_SPEED_MAX;
        //        }

        LiftSubsystem.getInstance().goToSpeedInit(setpoint);
    }

    protected boolean isFinished()
    {
        return false;
    }
}
