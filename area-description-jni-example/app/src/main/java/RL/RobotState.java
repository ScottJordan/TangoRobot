package RL;

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

    private RobotState() {
        update();
    }

    public void update() {
        updatePose();
        calcReward();
    }

    private void calcReward() {
        reward = 0;
    }

    public void updatePose() {
        String posString = TangoJNINative.getPoseStringMinimal(0);
        String idv[] = posString.split(",");
        if (idv[0].contentEquals("N/A")) {
            position = new Position(0, 0, 0);
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
}
