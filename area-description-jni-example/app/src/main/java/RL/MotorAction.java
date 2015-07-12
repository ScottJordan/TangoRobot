package RL;

import java.util.ArrayList;
import java.util.List;

import USBComm.ArduinoCommunicator;
import pl.gdan.elsy.qconf.Action;

/**
 * Created by scott on 7/1/15.
 */
public class MotorAction extends Action {
    private String command;
    private int leftMotorPower;
    private int rightMotorPower;
    private ArduinoCommunicator communicator;

    public MotorAction(int leftMotorPower, int rightMotorPower, ArduinoCommunicator communicator) {
        this.leftMotorPower = leftMotorPower;
        this.rightMotorPower = rightMotorPower;
        this.communicator = communicator;
        generateCommandString();
    }

    @Override
    public int execute() {
        communicator.sendMessage(command);
        return 0;
    }

    private void generateCommandString() {
        command = "L" + leftMotorPower + "R" + rightMotorPower + "\n";
    }

    public ArduinoCommunicator getCommunicator() {
        return communicator;
    }

    public void setCommunicator(ArduinoCommunicator communicator) {
        this.communicator = communicator;
    }

    public int getLeftMotorPower() {
        return leftMotorPower;
    }

    public void setLeftMotorPower(int leftMotorPower) {
        this.leftMotorPower = leftMotorPower;
    }

    public int getRightMotorPower() {
        return rightMotorPower;
    }

    public void setRightMotorPower(int rightMotorPower) {
        this.rightMotorPower = rightMotorPower;
    }

    public static MotorAction[] generateCommands(ArduinoCommunicator communicator) {
        MotorAction actions[] = new MotorAction[2];
//        actions[0] = new MotorAction(0, 0, communicator);
//        actions[1] = new MotorAction(-30, 0, communicator);
//        actions[2] = new MotorAction(30, 0, communicator);
//        actions[3] = new MotorAction(0, -30, communicator);
//        actions[4] = new MotorAction(0, 30, communicator);
        actions[0] = new MotorAction(-15, -15, communicator);
        actions[1] = new MotorAction(15, 15, communicator);
//        actions[7] = new MotorAction(-30, 30, communicator);
//        actions[8] = new MotorAction(30, -30, communicator);
        return actions;
    }
}
