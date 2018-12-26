package org.wfrobotics.robot.config;

import java.util.function.Supplier;

import org.wfrobotics.reuse.commands.drive.DriveOff;
import org.wfrobotics.robot.RobotState;
import org.wfrobotics.robot.commands.auto.AutoReachLineStraight;
import org.wfrobotics.robot.commands.auto.twocube.ModeCenter;
import org.wfrobotics.robot.commands.auto.twocube.ModeSide;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// TODO Recursively log mode/profile of composing commands with newlines, could ask each to print their toString?

/** Organizes autonomous modes supported by Robot **/
public class Autonomous
{
    private static SendableChooser<AutoMode> autoCommands;
    private static SendableChooser<StartingPosition> autoStartingPositions;
    public static StartingPosition autoStartingPosition = StartingPosition.Left;

    public static enum StartingPosition
    {
        Left,
        Center,
        Right
    }

    /**
     * FIRST Power Up - Top level autonomous modes
     */
    public static AutoMode[] makeModes()
    {
        /*
        return new AutoMode[] {
            new AutoMode("Auto None", () -> new DriveOff(), 0.0, false),
            new AutoMode("Auto Reach Line Drive Straight", () -> new AutoReachLineStraight(), 0.0, false),
            new AutoMode("Auto Switch Score Center", () -> new AutoSwitchCenter(), 0.0, true),
            new AutoMode("Auto Switch Score Side", () -> new AutoSwitchSide(), 0.0, false),
            new AutoMode("Auto Scale Score Side", () -> new AutoScaleSide(), 0.0, false)
        };
         */

        return new AutoMode[] {
            AutoMode.Default("Auto Reach Line", () -> new AutoReachLineStraight(), 0.0),
            AutoMode.Option("Switch Center", () -> new ModeCenter(ModeCenter.Type.SingleCube), 0.0),
            AutoMode.Option("Scale Same Side One Cube", () -> new ModeSide(autoStartingPosition, ModeSide.Type.SameSideOnly, ModeSide.CubeCount.Single), 0.0),
            AutoMode.Option("Scale Either Side Two Cube", () -> new ModeSide(autoStartingPosition, ModeSide.Type.SameSideAndCross, ModeSide.CubeCount.Double), 0.0),
            AutoMode.Option("Scale Either Side One Cube", () -> new ModeSide(autoStartingPosition, ModeSide.Type.SameSideAndCross, ModeSide.CubeCount.Single), 0.0),
            AutoMode.Option("Scale Same Side Two Cube", () -> new ModeSide(autoStartingPosition, ModeSide.Type.SameSideOnly, ModeSide.CubeCount.Double), 0.0),
            AutoMode.Option("Scale 2 on Near, None on Far", () -> new ModeSide(autoStartingPosition, ModeSide.Type.SameSideAndCross, ModeSide.CubeCount.DoubleButNoneOnFarSide), 0.0),

            AutoMode.Default("Auto Reach Line", () -> new AutoReachLineStraight(), 0.0),

            //AutoMode.Option("Auto Switch Score Side", () -> new AutoSide(autoStartingPosition), 0.0, false),
            AutoMode.Option("Switch Center And Backup", () -> new ModeCenter(ModeCenter.Type.SingleCubeAndBackup), 0.0),
            //AutoMode.Option("Switch Center Two Cube", () -> new ModeCenter(ModeCenter.Type.TwoCube), 0.0, false),

            //AutoMode.Option("Scale Same Side One Cube", () -> new ModeSide(autoStartingPosition, ModeSide.Type.SameSideOnly, ModeSide.CubeCount.Single), 0.0),
            //AutoMode.Option("Scale Either Side One Cube", () -> new ModeSide(autoStartingPosition, ModeSide.Type.SameSideAndCross, ModeSide.CubeCount.Single), 0.0),
            //AutoMode.Option("Scale Either Side Two Cube", () -> new ModeSide(autoStartingPosition, ModeSide.Type.SameSideAndCross, ModeSide.CubeCount.Double), 0.0),
            //AutoMode.Option("Scale Same Side Two Cube", () -> new ModeSide(autoStartingPosition, ModeSide.Type.SameSideOnly, ModeSide.CubeCount.Double), 0.0),
            AutoMode.Option("Auto None", () -> new DriveOff(), 0.0),

        };
    }

    /**
     * Defines everything needed for a singular autonomous mode the robot may run
     */
    protected static class AutoMode
    {
        public final String text;
        public final Supplier<Command> maker;
        public final double gyroOffset;
        public final boolean defaultCmd;


        public AutoMode(String chooserText, Supplier<Command> mode, double gyro, boolean defaultCmd)
        {
            text = chooserText;
            maker = mode;
            gyroOffset = gyro;
            this.defaultCmd = defaultCmd;
        }

        public static AutoMode Option(String chooserText, Supplier<Command> mode, double gyro)
        {
            return new AutoMode(chooserText, mode, gyro, false);
        }


        public static AutoMode Default(String chooserText, Supplier<Command> mode, double gyro)
        {
            return new AutoMode(chooserText, mode, gyro, true);
        }
    }

    /**
     * Grabs the selected 'Auto Mode' from SmartDashboard, sets up gyro,
     * returns the command to run in autonomous
     */
    protected static Command setupSelectedMode()
    {
        if(autoCommands == null)
        {
            setupSendableChooser();
        }
        else if(autoCommands.getSelected() == null)
        {

            DriverStation.reportError("Null autoCommands.getSelected", false);
        }

        // get the selected string
        String selected = autoCommands.getSelected().text;
        AutoMode[] modes = makeModes();
        int choice = 0;

        // find the choice that matches what was selected
        for (int index = 0; index < modes.length; index++)
        {
            if (modes[index].text.equals(selected))
            {
                choice = index;
                break;
            }
        }

        // zero the gyro
        //        Gyro.getInstance().zeroYaw(modes[choice].gyroOffset);
        RobotState.getInstance().updateRobotHeading(modes[choice].gyroOffset);

        // get the starting position
        autoStartingPosition = autoStartingPositions.getSelected();

        // actually create the auto command and return it
        return modes[choice].maker.get();
    }

    /**
     * Setup the sendable choosers to select which auto you're using
     */
    public static void setupSendableChooser()
    {
        // first build the auto mode selector
        autoCommands = new SendableChooser<AutoMode>();
        for(AutoMode auto: makeModes()) {
            if(auto.defaultCmd)
                autoCommands.addDefault(auto.text, auto);
            else
                autoCommands.addObject(auto.text, auto);
        }
        //autoCommands.addDefault("Auto Line", new AutoMode("Auto Line", () -> new DriveOff(), 0.0));
        SmartDashboard.putData("Auto Mode", autoCommands);

        // now build the starting position selector
        autoStartingPositions = new SendableChooser<StartingPosition>();
        autoStartingPositions.addDefault("Left", StartingPosition.Left);
        autoStartingPositions.addObject("Center", StartingPosition.Center);
        autoStartingPositions.addObject("Right", StartingPosition.Right);
        SmartDashboard.putData("Starting Position", autoStartingPositions);
    }

    /**
     * Grabs the selected 'Auto Mode' from SmartDashboard,
     * sets up gyro, returns the command to run in autonomous
     */
    public static Command setupAndReturnSelectedMode()
    {
        return setupSelectedMode();  // Static getter seemed cleaner in Robot
    }

    public static void updateDash()
    {
        SmartDashboard.putString("Selected Auto", autoCommands.getSelected().text);
        SmartDashboard.putString("Selected Position", autoStartingPositions.getSelected().toString());
    }
}
