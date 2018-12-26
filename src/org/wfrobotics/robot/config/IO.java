package org.wfrobotics.robot.config;

import org.wfrobotics.reuse.commands.drive.DriveDistance;
import org.wfrobotics.reuse.config.ButtonFactory;
import org.wfrobotics.reuse.config.ButtonFactory.TRIGGER;
import org.wfrobotics.reuse.config.HerdJoystick;
import org.wfrobotics.reuse.config.HerdJoystick.BUTTON;
import org.wfrobotics.reuse.config.Xbox;
import org.wfrobotics.reuse.config.Xbox.AXIS;
import org.wfrobotics.robot.commands.TogglingBrake;
import org.wfrobotics.robot.commands.climb.ClimbToggle;
import org.wfrobotics.robot.commands.climb.Winch;
import org.wfrobotics.robot.commands.intake.IntakeManual;
import org.wfrobotics.robot.commands.intake.JawsToggle;
import org.wfrobotics.robot.commands.intake.SmartIntakeEnableToggle;
import org.wfrobotics.robot.commands.intake.SmartOutput;
import org.wfrobotics.robot.commands.intake.WristSet;
import org.wfrobotics.robot.commands.intake.WristToggle;
import org.wfrobotics.robot.commands.lift.LiftToHeight;
import org.wfrobotics.robot.commands.lift.LiftZero;

import edu.wpi.first.wpilibj.GenericHID.Hand;

/** Maps controllers to Commands **/
public class IO
{
    private final double kLiftStickManualDownMultiplier;

    private static IO instance = null;

    private final Xbox operator;
    private final HerdJoystick driverL, driverR;

    private IO(HerdJoystick driverL, HerdJoystick driverR, Xbox operator)
    {
        kLiftStickManualDownMultiplier = RobotConfig.getInstance().LIFT_MANUAL_PERCENTAGE_DOWN;

        this.driverL = driverL;
        this.driverR = driverR;
        this.operator = operator;
        //this.joystick = joystick;
    }

    /** Configure each Button to run a Command */
    public void assignButtons()
    {
        ButtonFactory.makeButton(driverL, BUTTON.BASE_TOP_LEFT, TRIGGER.WHEN_PRESSED, new LiftZero());
        //ButtonFactory.makeButton(driverR, BUTTON.BASE_TOP_RIGHT, TRIGGER.WHEN_PRESSED, new DrivePathCal("Path")));
        ButtonFactory.makeButton(driverR, BUTTON.THUMB_SIDE, TRIGGER.WHEN_PRESSED, new TogglingBrake());

        ButtonFactory.makeButton(operator, Xbox.BUTTON.B, TRIGGER.WHEN_PRESSED, new LiftToHeight(LiftHeight.Intake.get()));
        //ButtonFactory.makeButton(driver, Xbox.BUTTON.B, TRIGGER.WHEN_PRESSED, new LiftToHeight(12)));
        ButtonFactory.makeButton(operator, Xbox.BUTTON.Y, TRIGGER.WHEN_PRESSED, new LiftToHeight(LiftHeight.Scale.get()));

        ButtonFactory.makeButton(operator, Xbox.BUTTON.A, TRIGGER.WHEN_PRESSED, new SmartOutput());
        ButtonFactory.makeButton(operator, Xbox.BUTTON.RB, TRIGGER.WHEN_PRESSED, new SmartIntakeEnableToggle());

        ButtonFactory.makeButton(operator, Xbox.AXIS.RIGHT_Y, .02, -0.2, TRIGGER.WHILE_HELD, new IntakeManual());
        ButtonFactory.makeButton(operator, Xbox.AXIS.RIGHT_TRIGGER, .02, TRIGGER.WHILE_HELD, new IntakeManual());
        ButtonFactory.makeButton(operator, Xbox.AXIS.LEFT_TRIGGER, .02, TRIGGER.WHILE_HELD, new IntakeManual());

        ButtonFactory.makeButton(operator, Xbox.BUTTON.X, TRIGGER.WHEN_PRESSED, new WristToggle());
        ButtonFactory.makeButton(operator, Xbox.DPAD.DOWN, TRIGGER.WHEN_PRESSED, new WristSet(false));
        ButtonFactory.makeButton(operator, Xbox.DPAD.UP, TRIGGER.WHEN_PRESSED, new WristSet(true));

        ButtonFactory.makeButton(operator, Xbox.BUTTON.LB, TRIGGER.WHEN_PRESSED, new JawsToggle());

        // TODO
        ButtonFactory.makeButton(driverL, HerdJoystick.BUTTON.THUMB_TOP_LEFT, TRIGGER.WHILE_HELD, new Winch(1));
        ButtonFactory.makeButton(driverL, HerdJoystick.BUTTON.THUMB_BOTTOM_RIGHT, TRIGGER.WHILE_HELD, new Winch(-1));
        ButtonFactory.makeButton(driverL, HerdJoystick.BUTTON.TRIGGER, TRIGGER.WHEN_PRESSED, new ClimbToggle());

        //ButtonFactory.makeButton(operator, Xbox.BUTTON.BACK, TRIGGER.WHEN_PRESSED, new AutoScaleLeftLeft()));
        ButtonFactory.makeButton(operator, Xbox.BUTTON.BACK, TRIGGER.WHEN_PRESSED, new DriveDistance(-12*1));
        ButtonFactory.makeButton(operator, Xbox.BUTTON.START, TRIGGER.WHEN_PRESSED, new DriveDistance(12*10));
        //ButtonFactory.makeButton(operator, Xbox.BUTTON.BACK, TRIGGER.WHEN_PRESSED, new DrivePathCal("Path")));

    }

    public static IO getInstance()
    {
        if (instance == null) { instance = new IO(new HerdJoystick(0), new HerdJoystick(1), new Xbox(2)); }
        return instance;
    }

    public double getThrottle()
    {
        return -driverL.getY();
    }

    public double getTurn()
    {
        // scale the turning back if quick turning
        double turn = driverR.getX() * (getDriveQuickTurn() ? .75 : 1);
        return Math.signum(turn) * Math.abs(Math.pow(turn, 2));
    }

    public boolean getDriveQuickTurn()
    {
        //return driver.getButtonPressed(BUTTON.LEFT_STICK);
        return Math.abs(getThrottle()) < .1;
    }

    //TODO: Rename this to something more appropriate
    public double getLiftStick()
    {
        double val = operator.getAxis(AXIS.LEFT_Y);
        return val > 0 ? val : val * kLiftStickManualDownMultiplier;
    }

    public int getAutonomousSide()
    {
        // TODO get the auto side
        return 0; //panel.getRotary();
    }

    public void setRumble(boolean rumble)
    {
        float state = (rumble) ? 1 : 0;
        operator.setRumble(Hand.kLeft, state);
        operator.setRumble(Hand.kRight, state);
    }

    public double getIntakeManual()
    {
        return -operator.getAxis(AXIS.LEFT_TRIGGER) + operator.getAxis(AXIS.RIGHT_TRIGGER) // triggers
        + operator.getAxis(AXIS.RIGHT_Y); // right thumb
    }

    public double getWinchSpeed()
    {
        //TODO
        return 0;//driver.getAxis(AXIS.LEFT_Y);
    }
}
