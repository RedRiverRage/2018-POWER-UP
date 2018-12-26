package org.wfrobotics.robot.commands.climb;

import org.wfrobotics.robot.Robot;
import org.wfrobotics.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.command.Command;

public class Winch extends Command
{
    protected final Climber climb = Climber.getInstance();
    double Speed;
    boolean useRawInput = false;

    public Winch()
    {
        useRawInput = true;
    }

    public Winch(double speed){
        requires(climb);
        Speed = speed;
    }

    protected void execute()
    {
        if(useRawInput)
        {
            Climber.getInstance().setWinchSpeed(Robot.controls.getWinchSpeed());
        }
        else
        {
            Climber.getInstance().setWinchSpeed(Speed);;
        }
    }

    protected boolean isFinished()
    {
        return false;
    }

}