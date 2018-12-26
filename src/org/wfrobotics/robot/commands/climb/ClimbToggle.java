package org.wfrobotics.robot.commands.climb;

import org.wfrobotics.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ClimbToggle extends InstantCommand
{
    protected final Climber climb = Climber.getInstance();

    public ClimbToggle(){
        requires(climb);
    }

    protected void initialize()
    {
        Climber.getInstance().setSolenoid(!Climber.getInstance().getSolenoid());
    }

}
