package frc.robot.subsystems.arm;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.arm.ArmConstants.ArmState;

/**
 * @author Max
 * @author Yijia
 * @author Oliver
 * @author Bennentt
 */
public class CommandArm extends Arm { // runs commands

    // Private Static Instance
    private static CommandArm instance;

    // Private Static Synchronized GetInstance Method
    public static synchronized CommandArm getInstance() {
        if (CommandArm.instance == null)
            CommandArm.instance = new CommandArm();
        return CommandArm.instance;
    }

    public CommandArm(){
        super();
    }

    public Command goToZeroCommand()
    {
        return Commands.runOnce(
            () -> setState(ArmState.ZERO), this
        ).andThen(
            Commands.waitSeconds(1.0)
        );
    }
    
     /**
     * sets arm to shoot in amp
     * @author Bennett
     */
    public Command goToAmpCommand()
    {
        return Commands.runOnce(
            () -> setState(ArmState.AMP), this
        ).andThen(
            Commands.waitSeconds(1.0)
        );
    }
}
