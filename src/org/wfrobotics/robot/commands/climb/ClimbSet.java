package org.wfrobotics.robot.commands.climb;

import org.wfrobotics.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ClimbSet extends InstantCommand
{
    protected final Climber climb = Climber.getInstance();
    final boolean extend;

    public ClimbSet(boolean extend){
        requires(climb);
        this.extend = extend;
    }

    protected void initialize()
    {
        Climber.getInstance().setSolenoid(extend);
    }

}
