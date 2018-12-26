package org.wfrobotics.robot.subsystems;

import org.wfrobotics.reuse.hardware.TalonFactory;
import org.wfrobotics.reuse.hardware.sensors.SharpDistance;
import org.wfrobotics.reuse.subsystems.EnhancedSubsystem;
import org.wfrobotics.reuse.subsystems.background.BackgroundUpdate;
import org.wfrobotics.reuse.utilities.CircularBuffer;
import org.wfrobotics.robot.RobotState;
import org.wfrobotics.robot.commands.intake.SmartIntake;
import org.wfrobotics.robot.config.RobotConfig;
import org.wfrobotics.robot.config.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeSubsystem extends EnhancedSubsystem implements BackgroundUpdate
{
    static class SingletonHolder
    {
        static IntakeSubsystem instance = new IntakeSubsystem(RobotConfig.getInstance());
    }

    public static IntakeSubsystem getInstance()
    {
        return SingletonHolder.instance;
    }

    public static RobotConfig config;

    private final double bufferSize = 10;
    private final boolean kIsCompBot;

    /*
     * when the block is further away from the sensor the motors are at speed x
     * when it comes closer to the sensor we want to ramp down the motors to speed 2/3x
     * when distances is equal to 0, the motor speed is set to 0
     */

    protected final RobotState state = RobotState.getInstance();

    private Spark masterRight;
    private Spark followerLeft;
    private TalonSRX masterCompRight;
    private TalonSRX followerCompLeft;

    private final DoubleSolenoid horizontalIntake;
    private final DoubleSolenoid vertIntake;

    private final SharpDistance distanceSensorR;
    private CircularBuffer buffer;

    private boolean lastHorizontalState;
    private boolean lastVertState;

    private double lastHorizontalTime;
    private double lastVertTime;

    private PowerDistributionPanel pdp;
    private double lastCurrentDrawTime;

    private double latestDistance;
    //    private CircularBuffer currentM;
    //    private CircularBuffer currentF;

    public boolean SmartIntakeEnabled = true;

    public IntakeSubsystem(RobotConfig Config)
    {
        config = Config;
        kIsCompBot = config.isCompBot();

        if(kIsCompBot)
        {
            masterCompRight = new TalonSRX(RobotMap.CAN_COMPETITION_INTAKE_R);
            followerCompLeft = new TalonSRX(RobotMap.CAN_COMPETITION_INTAKE_L);
            TalonFactory.configCurrentLimiting(masterCompRight, 30, 50, (int)config.INTAKE_CURRENT_LIMIT );
            TalonFactory.configCurrentLimiting(followerCompLeft, 30, 50, (int)config.INTAKE_CURRENT_LIMIT );
        }
        else
        {
            masterRight = new Spark(RobotMap.PWM_PRACTICE_INTAKE_R);
            followerLeft = new Spark(RobotMap.PWM_PRACTICE_INTAKE_L);
        }
        pdp = new PowerDistributionPanel();

        //        currentM = new CircularBuffer(100);
        //        currentF = new CircularBuffer(100);


        if(kIsCompBot)
        {
            masterCompRight.setNeutralMode(NeutralMode.Coast);
            followerCompLeft.setNeutralMode(NeutralMode.Coast);
        }
        else
        {
            //masterRight.setNeutralMode(NeutralMode.Brake);
            //followerLeft.setNeutralMode(NeutralMode.Brake);
        }

        // set inverted motors
        if(kIsCompBot)
        {
            masterCompRight.setInverted(config.INTAKE_MOTOR_INVERTED_RIGHT);
            followerCompLeft.setInverted(config.INTAKE_MOTOR_INVERTED_LEFT);
        }
        else
        {
            masterRight.setInverted(config.INTAKE_MOTOR_INVERTED_RIGHT);
            followerLeft.setInverted(config.INTAKE_MOTOR_INVERTED_LEFT);
        }

        horizontalIntake = new DoubleSolenoid(RobotMap.CAN_PNEUMATIC_CONTROL_MODULE, RobotMap.PNEUMATIC_INTAKE_HORIZONTAL_FORWARD, RobotMap.PNEUMATIC_INTAKE_HORIZONTAL_REVERSE);
        vertIntake = new DoubleSolenoid(RobotMap.CAN_PNEUMATIC_CONTROL_MODULE, RobotMap.PNEUMATIC_INTAKE_VERTICAL_FORWARD, RobotMap.PNEUMATIC_INTAKE_VERTICAL_REVERSE);

        distanceSensorR = new SharpDistance(RobotMap.SENSOR_DISTANCE);
        buffer = new CircularBuffer((int) bufferSize);
        for (int index = 0; index < bufferSize; index++)
        {
            buffer.addFirst(9999);  // Filled with zeros by default, which is bad because it's a valid value
        }

        // Force defined states
        lastHorizontalTime = Timer.getFPGATimestamp() - config.INTAKE_WRIST_TIMEOUT_LENTH * 1.01;
        lastHorizontalState = false;
        setHorizontal(!lastHorizontalState);

        lastVertTime = Timer.getFPGATimestamp() - config.INTAKE_WRIST_TIMEOUT_LENTH * 1.01;
        lastVertState = false;
        latestDistance = 9999;
        setVert(!lastVertState);

        //lastDistance = uSensor.getDistance();

        lastCurrentDrawTime = Timer.getFPGATimestamp() - config.INTAKE_CURRENT_DISABLE_DELAY * 1.01;
    }

    public void initDefaultCommand()
    {
        //setDefaultCommand(new CyborgIntake());
        setDefaultCommand(new SmartIntake());
    }

    public void updateSensors()
    {
        latestDistance = buffer.getAverage();
        state.updateIntakeSensor(latestDistance);

        //        try
        //        {
        //            currentM.addFirst(pdp.getCurrent(config.INTAKE_PDP_CHANNELS[0]));
        //            currentF.addFirst(pdp.getCurrent(config.INTAKE_PDP_CHANNELS[1]));
        //        }
        //        catch(Exception e)
        //        {
        //
        //        }
    }

    public void reportState()
    {
        SmartDashboard.putNumber("Cube", latestDistance);
        //SmartDashboard.putNumber("IntakeCurrent", masterCompRight.getOutputCurrent());

        SmartDashboard.putBoolean("SmartIntakeEnabled", SmartIntakeEnabled);
    }

    public boolean getHorizontal()
    {
        return lastHorizontalState;
    }

    public void onStartUpdates(boolean isAutonomous)
    {

    }

    public synchronized void onBackgroundUpdate()
    {
        buffer.addFirst(distanceSensorR.getDistanceInches() - config.INTAKE_DISTANCE_TO_CUBE);

        SmartDashboard.putNumber("test", buffer.getAverage());

        // we only want to limit the current on the intake motion
        //        if(lastPercentageOutward < 0)
        //        {
        //            if(currentM.getAverage() > config.INTAKE_CURRENT_LIMIT ||
        //                                            currentF.getAverage() > config.INTAKE_CURRENT_LIMIT)
        //            {
        //                lastCurrentDrawTime = Timer.getFPGATimestamp();
        //                masterRight.set(0);
        //                followerLeft.set(0);
        //            }
        //        }
    }

    public void setMotor(double percentageOutward)
    {
        // if the last excessive current draw was over x seconds ago
        // actually set the motors
        if((Timer.getFPGATimestamp() - lastCurrentDrawTime > config.INTAKE_CURRENT_DISABLE_DELAY)
                                        || percentageOutward > 0)
        {
            // if the desired speed is less than the max input speed, limit it
            if(percentageOutward < -config.INTAKE_SPEED_MAX_IN_PERCENT)
                percentageOutward = -config.INTAKE_SPEED_MAX_IN_PERCENT;

            // set outputs
            if(kIsCompBot)
            {
                masterCompRight.set(ControlMode.PercentOutput, percentageOutward);
                followerCompLeft.set(ControlMode.PercentOutput, percentageOutward);
            }
            else
            {
                masterRight.set(percentageOutward);
                followerLeft.set(percentageOutward);
            }
        }
        else
        {
            // try and protect the limits
            if(kIsCompBot)
            {
                masterCompRight.set(ControlMode.PercentOutput, 0);
                followerCompLeft.set(ControlMode.PercentOutput, 0);
            }
            else
            {
                masterRight.set(0);
                followerLeft.set(0);
            }
        }
    }

    public boolean setHorizontal(boolean extended)
    {
        boolean delayedEnough = Timer.getFPGATimestamp() - lastHorizontalTime > config.INTAKE_WRIST_TIMEOUT_LENTH;
        boolean different = extended != lastHorizontalState;
        boolean stateChanged = false;

        SmartDashboard.putBoolean("Jaws Requested", extended);

        if (delayedEnough && different)
        {
            horizontalIntake.set(extended ? Value.kForward : Value.kReverse);
            lastHorizontalTime = Timer.getFPGATimestamp();
            lastHorizontalState = extended;
            stateChanged = true;
        }
        return stateChanged;
    }

    public boolean setVert(boolean extended)
    {

        boolean delayedEnough = Timer.getFPGATimestamp() - lastVertTime > config.INTAKE_WRIST_TIMEOUT_LENTH;
        boolean different = extended != lastVertState;
        boolean stateChanged = false;

        SmartDashboard.putBoolean("Wrist Requested", extended);

        if (delayedEnough && different)
        {
            vertIntake.set(extended ? Value.kForward : Value.kReverse);
            lastVertTime = Timer.getFPGATimestamp();
            lastVertState = extended;
            stateChanged = true;
        }
        return stateChanged;
    }

    public boolean getVertical()
    {
        return lastVertState;
    }

    public boolean runFunctionalTest(boolean includeMotion)
    {
        return false;
    }

    public TestReport runFunctionalTest()
    {
        return null;
    }

    public void onStopUpdates()
    {
    }

    public void cacheSensors(boolean isDisabled)
    {
    }
}

