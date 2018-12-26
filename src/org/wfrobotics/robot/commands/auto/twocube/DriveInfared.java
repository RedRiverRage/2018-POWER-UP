package org.wfrobotics.robot.commands.auto.twocube;

import org.wfrobotics.reuse.subsystems.drive.TankSubsystem;
import org.wfrobotics.robot.RobotState;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveInfared extends Command
{
    private final RobotState state = RobotState.getInstance();
    private final TankSubsystem drive = TankSubsystem.getInstance();

    private final double target;
    private final double tol;
    private final double max;
    public static double lastCommanded;

    public DriveInfared(double targetDistance, double maxDistance, double absoluteTol, double timeout)
    {
        requires(drive);
        target = targetDistance;
        tol = absoluteTol;
        max = maxDistance;
        setTimeout(timeout);
    }

    protected void initialize()
    {
        double lastCommanded = remaining();
        SmartDashboard.putNumber("Drive Infared", lastCommanded);
        drive.driveDistance(lastCommanded);
    }

    protected void execute()
    {
        //drive.driveDistance(remaining());
    }

    protected boolean isFinished()
    {
        return Math.abs(remaining()) < tol || isTimedOut();
    }

    private double remaining()
    {
        return Math.min(max, state.intakeDistance / 2.54 - target);
    }
}
