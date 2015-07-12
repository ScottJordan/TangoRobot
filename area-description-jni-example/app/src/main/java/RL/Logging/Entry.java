package RL.Logging;

import RL.Orientation;
import RL.Position;
import RL.RobotState;

/**
 * Created by scott on 6/22/15.
 */
public class Entry {
    private Position pos;
    private Orientation ori;
    private int leftMotor;
    private int rightMotor;
    private int leftMotorAction;
    private int rightMotorAction;
    private double reward;
    private double rewardState;
    private long deltaTime;

    public Entry(RobotState robotState, long deltaTime) {
        this.pos = robotState.getPosition();
        this.ori = robotState.getOrientation();
        this.leftMotor = robotState.getMotorLeft();
        this.rightMotor = robotState.getMotorRight();
        this.leftMotorAction = robotState.getMotorLeftAction();
        this.rightMotorAction = robotState.getMotorRightAction();
        this.reward = robotState.getReward();
        this.rewardState = robotState.getRewardState();
        this.deltaTime = deltaTime;
    }

    @Override
    public String toString() {
        return deltaTime + "," +
                pos.toString() + "," +
                ori.toString() + "," +
                leftMotor + "," +
                rightMotor + "," +
                leftMotorAction + "," +
                rightMotorAction + "," +
                rewardState + "," +
                reward +
                "\n";
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Orientation getOri() {
        return ori;
    }

    public void setOri(Orientation ori) {
        this.ori = ori;
    }

    public long getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(long deltaTime) {
        this.deltaTime = deltaTime;
    }
}
