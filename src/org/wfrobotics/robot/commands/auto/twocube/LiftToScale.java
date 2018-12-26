package org.wfrobotics.robot.commands.auto.twocube;

import org.wfrobotics.robot.commands.intake.WristSet;
import org.wfrobotics.robot.commands.lift.LiftToHeight;
import org.wfrobotics.robot.config.LiftHeight;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LiftToScale extends CommandGroup
{
    public LiftToScale()
    {
        this.addSequential(new WristSet(true));  // Keep cube level to prevent slippage
        this.addSequential(new LiftToHeight(LiftHeight.Scale.get()));
    }
}
