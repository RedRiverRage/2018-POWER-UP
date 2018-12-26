package org.wfrobotics.robot.commands.auto.twocube;

import org.wfrobotics.reuse.subsystems.drive.TankSubsystem;
import org.wfrobotics.robot.RobotState;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveInfaredUndo extends Command
{
    private final RobotState state = RobotState.getInstance();
    private final TankSubsystem drive = TankSubsystem.getInstance();

    private final double tol;
    public static double lastCommanded;

    public DriveInfaredUndo(double tol, double timeout)
    {
        this.tol = tol;
        requires(drive);
        setTimeout(timeout);
    }

    protected void initialize()
    {
        double lastCommanded = -DriveInfared.lastCommanded;
        SmartDashboard.putNumber("Drive Infared Undo", lastCommanded);
        drive.driveDistance(lastCommanded);
    }

    protected boolean isFinished()
    {
        return Math.abs(state.getDistanceDriven()) < tol || isTimedOut();
    }
}
