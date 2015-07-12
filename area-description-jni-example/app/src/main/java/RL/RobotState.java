package RL;

import android.util.Log;

import com.projecttango.experiments.nativearealearning.TangoJNINative;


/**
 * Created by scott on 6/29/15.
 */
public class RobotState {
    private static RobotState ourInstance = new RobotState();

    public static RobotState getInstance() {
        return ourInstance;
    }

    private static Position position;
    private static Orientation orientation;
    private static int motorLeft = 0;
    private static int motorRight = 0;
    private static int reward = 0;
    private static int motorLeftAction = 0;
    private static int motorRightAction = 0;

    private static int rewardState = 0;
    private static int lapCounter = 0;

    private RobotState() {
        update();
    }

    public void update() {
        updatePose();
        calcReward();
        Log.v("Cycle", "Reward = " + reward + " Y pos = " + position.getY());
    }

    private void calcReward() {
        if (rewardState == 0) {
            if (position.getY() < .75) {
                reward = 1000 - (int) ((Math.abs(.75 - position.getY()) / 1.5) * 1000.0);
            } else if (position.getY() >= .75) {
                reward = 1000;
                rewardState = 1;
            } else {
                reward = 0;
            }
        } else if (rewardState == 1) {
            if (position.getY() > -.75) {
                reward = 1000 - (int) ((Math.abs(-.75 - position.getY()) / 1.5) * 1000.0);
                rewardState = 0;
                lapCounter++;
            } else if (position.getY() <= -.75) {
                reward = 1000;
                rewardState = 0;
                lapCounter++;
            } else {
                reward = 0;
            }
        }
    }

    public void updatePose() {
        String posString = TangoJNINative.getPoseStringMinimal(0);
        String idv[] = posString.split(",");
        if (idv[3].contentEquals("N/A")) {
            position = new Position(0, 0, 0);
            orientation = new Orientation(0, 0, 0, 0);
            return;
        }
        position = new Position(Float.parseFloat(idv[3]), //x
                Float.parseFloat(idv[4]), //y
                Float.parseFloat(idv[5]));//z
        orientation = new Orientation(Float.parseFloat(idv[6]), //x
                Float.parseFloat(idv[7]), //y
                Float.parseFloat(idv[8]), //z
                Float.parseFloat(idv[9]));//w
    }

    public void setAction(MotorAction action) {
        motorLeftAction = action.getLeftMotorPower();
        motorRightAction = action.getRightMotorPower();
    }

    public void updateMotors() {
        motorLeft = motorLeftAction;
        motorRight = motorRightAction;
        motorRightAction = 0;
        motorLeftAction = 0;
    }

    public void reset() {
        updatePose();
        motorLeft = 0;
        motorRight = 0;
        reward = 0;
        rewardState = 0;
        ourInstance = this;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public int getMotorLeft() {
        return motorLeft;
    }

    public void setMotorLeft(int motorLeft) {
        this.motorLeft = motorLeft;
    }

    public int getMotorRight() {
        return motorRight;
    }

    public void setMotorRight(int motorRight) {
        this.motorRight = motorRight;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getMotorLeftAction() {
        return motorLeftAction;
    }

    public void setMotorLeftAction(int motorLeftAction) {
        this.motorLeftAction = motorLeftAction;
    }

    public int getMotorRightAction() {
        return motorRightAction;
    }

    public void setMotorRightAction(int motorRightAction) {
        this.motorRightAction = motorRightAction;
    }

    public int getLapCounter() {
        return lapCounter;
    }

    public int getRewardState() {
        return rewardState;
    }
}
