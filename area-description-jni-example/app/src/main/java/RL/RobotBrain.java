package RL;

import pl.gdan.elsy.qconf.Action;
import pl.gdan.elsy.qconf.Brain;

public class RobotBrain extends Brain {
    private static final long serialVersionUID = 1L;

    /**
     * Probability with which random action is selected instead
     * of being selected by the NN
     */

    private Robot robot;

    public RobotBrain(RobotPlayerPerception perception, Action[] actionsArray) {
        this(perception, actionsArray, new int[]{}, new int[]{20});

    }

    public RobotBrain(RobotPlayerPerception perception, Action[] actionArray, int[] hiddenNeuronsNo, int[] predictionNetHiddenNeurons) {
        super(perception, actionArray, hiddenNeuronsNo);
        robot = new Robot(perception, this, predictionNetHiddenNeurons);
    }

    public RobotBrain(RobotPlayerPerception perception, Action[] actionArray, int[] hiddenNeuronsNo, int[] predictionNetHiddenNeurons,
                      double alpha, double lambda, double gamma, boolean useBoltzmann, double temperature, double randActions, double maxWeight) {
        super(perception, actionArray, hiddenNeuronsNo, alpha, lambda, gamma, useBoltzmann, temperature, randActions, maxWeight);
        robot = new Robot(perception, this, predictionNetHiddenNeurons);
    }

    public void count() {

        getPerception().perceive(); // perc(t)
        robot.learn();
        super.count(); // act(t)
        robot.countExpectations(); //perceive, propagate
        //executeAction();
    }


    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public MotorAction getMotorAction() {
        return (MotorAction) this.getActionsArray()[getAction()];
    }

}
