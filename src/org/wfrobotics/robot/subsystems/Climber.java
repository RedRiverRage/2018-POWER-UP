package org.wfrobotics.robot.subsystems;

import org.wfrobotics.reuse.subsystems.EnhancedSubsystem;
import org.wfrobotics.robot.commands.climb.Winch;
import org.wfrobotics.robot.config.RobotConfig;
import org.wfrobotics.robot.config.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Spark;

/**
 *
 */

public class Climber extends EnhancedSubsystem
{
    static class SingletonHolder
    {
        static Climber instance = new Climber();
    }

    public static Climber getInstance()
    {
        return SingletonHolder.instance;
    }

    Spark motor;
    TalonSRX motorComp;

    DoubleSolenoid climbSolenoid;


    private boolean lastSolRetracted;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public Climber()
    {
        if(RobotConfig.isCompBot())
        {
            motorComp = new TalonSRX(RobotMap.CAN_COMPETITION_WINCH_MOTOR);
        }
        else
        {
            motor = new Spark(RobotMap.PWM_PRACTICE_WINCH_MOTOR);
        }
        climbSolenoid  = new DoubleSolenoid(RobotMap.CAN_PNEUMATIC_CONTROL_MODULE, RobotMap.PNEUMATIC_CLIMB_UP, RobotMap.PNEUMATIC_CLIMB_DOWN);
        lastSolRetracted = false;
        setSolenoid(!lastSolRetracted);
    }

    public void setWinchSpeed(double speed)
    {
        if(lastSolRetracted)
        {
            if(RobotConfig.isCompBot())
            {
                motorComp.set(ControlMode.PercentOutput, speed);
            }
            else
            {
                motor.set(speed);
            }
        }
    }

    public void initDefaultCommand(){
        setDefaultCommand(new Winch(0));
    }

    public void setSolenoid(boolean retracted)
    {
        climbSolenoid.set(retracted ? Value.kForward : Value.kReverse);
        lastSolRetracted = retracted;
    }

    public boolean getSolenoid()
    {
        return lastSolRetracted;
    }

    public void reportState()
    {
    }

    public TestReport runFunctionalTest()
    {
        return null;
    }

    public void cacheSensors(boolean isDisabled)
    {
    }
}

