package org.wfrobotics.robot.subsystems;

import org.wfrobotics.reuse.hardware.TalonFactory;
import org.wfrobotics.reuse.subsystems.EnhancedSubsystem;
import org.wfrobotics.robot.RobotState;
import org.wfrobotics.robot.commands.lift.LiftPercentVoltage;
import org.wfrobotics.robot.config.RobotConfig;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftSubsystem extends EnhancedSubsystem
{
    public static LiftSubsystem getInstance()
    {
        if (instance == null)
        {
            instance = new LiftSubsystem(RobotConfig.getInstance());
        }
        return instance;
    }

    private static LiftSubsystem instance = null;
    public static RobotConfig config;

    private final static double kSprocketDiameterInches = 1.225;  // 1.29 16 tooth 25 chain
    private final static double kTicksPerRev = 4096;
    private final static double kRevsPerInch = 1 / (kSprocketDiameterInches * Math.PI);

    private final RobotState state = RobotState.getInstance();
    private TalonSRX[] motors = new TalonSRX[2];

    private double desiredSetpoint;

    enum LimitSwitch
    {
        Bottom,
        Top
    }

    private LiftSubsystem(RobotConfig Config)
    {
        config = Config;

        final int kTimeout = 10;
        final int kSlot = 0;
        // TODO Use talon software limit switches
        motors = TalonFactory.makeClosedLoopTalon(config.LIFT_CLOSED_LOOP).toArray(new TalonSRX[0]);

        for (int index = 0; index < motors.length; index++)
        {
            motors[index].configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, config.LIFT_LIMIT_SWITCH_NORMALLY[index][0], kTimeout);
            motors[index].configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, config.LIFT_LIMIT_SWITCH_NORMALLY[index][1], kTimeout);
            if(config.LIFT_LIMITS_DISABLE)
            {
                motors[index].overrideLimitSwitchesEnable(false);
            }
            else
            {
                motors[index].overrideLimitSwitchesEnable(true);
            }
            motors[index].set(ControlMode.PercentOutput, 0);
            motors[index].setNeutralMode(NeutralMode.Brake);
            motors[index].setSelectedSensorPosition(0, kSlot, kTimeout);
            motors[index].configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_10Ms, kTimeout);
            motors[index].configVelocityMeasurementWindow(32, kTimeout);
            motors[index].configOpenloopRamp(.5, 10);
        }
        desiredSetpoint = 0;
    }

    public void initDefaultCommand()
    {
        setDefaultCommand(new LiftPercentVoltage());
    }

    public void updateSensors()
    {
        if (zeroPositionIfNeeded())
        {
            SmartDashboard.putString("LiftAuto", "Zeroing");
        }

        if(config.LIFT_DEBUG)
        {
            debug();
        }

        state.updateLiftHeight(ticksToInches(getHeightAverage()));
    }

    public void reportState()
    {
        SmartDashboard.putNumber("Velocity", motors[0].getSelectedSensorVelocity(0));
        double position0 = motors[0].getSelectedSensorPosition(0);
        double position1 = motors[1].getSelectedSensorPosition(0);
        SmartDashboard.putNumber("Height0", ticksToInches(position0));
        SmartDashboard.putNumber("Height1", ticksToInches(position1));

        //        Map<String, Double> values = new HashMap<String, Double>();
        //        values.put("Error", (double) motors[0].getClosedLoopError(0));
        //        values.put("Velocity", (double) motors[0].getSelectedSensorVelocity(0));
        //        AutoTune.getInstance().reportState(motors[0], 0, "MotionMagic", values);
    }

    /**
     * Initialize the Go To Height mode
     * @param heightInches desired height in inches
     */
    public synchronized void goToHeightInit(double heightInches)
    {
        desiredSetpoint = inchesToTicks(heightInches);
        set(ControlMode.MotionMagic, desiredSetpoint);  // Stalls motors
    }

    /**
     * Initialize the Go To Speed mode
     * @param percent speed in percent, -1 to 1
     */
    public synchronized void goToSpeedInit(double percent)
    {
        double speed = percent;
        if ((isAtTop(0) || isAtTop(1)) && speed > 0.0)
        {
            speed = 0.0;
        }
        set(ControlMode.PercentOutput, percent);
    }

    /**
     * Set both of the motors
     * @param mode Talon Control Mode
     * @param val
     */
    private void set(ControlMode mode, double val)
    {
        for (int index = 0; index < motors.length; index++)
        {
            motors[index].set(mode, val);
        }
    }

    /**
     * Convert inches to ticks
     * @param inches
     * @return ticks
     */
    private static double inchesToTicks(double inches)
    {
        return inches * kRevsPerInch * kTicksPerRev;
    }

    /**
     * Convert ticks to inches
     * @param ticks
     * @return inches
     */
    private double ticksToInches(double ticks)
    {
        return ticks / kRevsPerInch / kTicksPerRev;
    }

    /**
     * print debug information
     */
    private void debug()
    {
        TalonSRX motor = motors[0];
        double position0 = motors[0].getSelectedSensorPosition(0);
        double position1 = motors[1].getSelectedSensorPosition(0);
        double error0 = motors[0].getClosedLoopError(0);
        double error1 = motors[1].getClosedLoopError(0);

        SmartDashboard.putNumber("Position0", position0);
        SmartDashboard.putNumber("Position1", position1);
        SmartDashboard.putNumber("Velocity", motor.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("TargetPosition", desiredSetpoint);

        SmartDashboard.putNumber("Error0", error0);
        SmartDashboard.putNumber("Error1", error1);

        SmartDashboard.putBoolean("AtBottom", isAtBottom());
        SmartDashboard.putBoolean("AtTop", isAtTop());

        double p = Preferences.getInstance().getDouble("lift_p", 0);
        double i = Preferences.getInstance().getDouble("lift_i", 0);
        double d = Preferences.getInstance().getDouble("lift_d", 0);

        motors[0].config_kP(0, p, 10);
        motors[1].config_kP(0, p, 10);
        motors[0].config_kI(0, i, 10);
        motors[1].config_kI(0, i, 10);
        motors[0].config_kD(0, d, 10);
        motors[1].config_kD(0, d, 10);
    }

    public void zeroPosition()
    {
        for (int index = 0; index < motors.length; index++)
        {
            motors[index].setSelectedSensorPosition(0, 0, 0);
        }
    }

    /**
     * Zero the encoder position if both sides are at the bottom
     * @return true if both sides are at the bottom
     */
    private boolean zeroPositionIfNeeded()
    {
        if(isAtBottom())
        {
            zeroPosition();
            return true;
        }
        return false;
    }

    /**
     * Are all sides at the top?
     * @return
     */
    public boolean isAtTop()
    {
        return isAtLimit(LimitSwitch.Top);
    }

    /**
     * Are all sides at the bottom?
     * @return
     */
    public boolean isAtBottom()
    {
        return isAtLimit(LimitSwitch.Bottom);
    }

    /**
     * Is one side at the top?
     * @param index
     * @return
     */
    public boolean isAtTop(int index)
    {
        return isAtLimit(LimitSwitch.Top, index);
    }

    /**
     * Is one side at the bottom?
     * @param index
     * @return
     */
    public boolean isAtBottom(int index)
    {
        return isAtLimit(LimitSwitch.Bottom, index);
    }

    /**
     * Are all sides at one of the limits?
     * @param limit
     * @return
     */
    public boolean isAtLimit(LimitSwitch limit)
    {
        boolean allAtLimit = true;
        for (int index = 0; index < motors.length; index++)
        {
            allAtLimit &= isAtLimit(limit, index);
        }
        return allAtLimit;
    }

    /**
     * Is one side at one of the limits?
     * @param limit
     * @param index
     * @return
     */
    public boolean isAtLimit(LimitSwitch limit, int index)
    {
        if(limit == LimitSwitch.Bottom)
        {
            if(config.LIFT_LIMIT_SWITCH_NORMALLY[index][1] == LimitSwitchNormal.NormallyClosed)
                return !motors[index].getSensorCollection().isRevLimitSwitchClosed();
            else
                return motors[index].getSensorCollection().isRevLimitSwitchClosed();
        }
        else
        {
            if(config.LIFT_LIMIT_SWITCH_NORMALLY[index][0] == LimitSwitchNormal.NormallyClosed)
                return !motors[index].getSensorCollection().isFwdLimitSwitchClosed();
            else
                return motors[index].getSensorCollection().isFwdLimitSwitchClosed();
        }
    }

    public boolean isAtDesiredHeight(double TOLERANCE_INCHES)
    {
        return Math.abs(motors[0].getSelectedSensorPosition(0) - desiredSetpoint) < inchesToTicks(TOLERANCE_INCHES);
    }

    public double getHeightAverage()
    {
        return ticksToInches(motors[0].getSelectedSensorPosition(0) + motors[1].getSelectedSensorPosition(0)) / 2.0;
    }

    public boolean runFunctionalTest(boolean includeMotion)
    {
        return false;
    }

    public TestReport runFunctionalTest()
    {
        return null;
    }

    public void cacheSensors(boolean isDisabled)
    {
    }
}
