package RL;

import pl.gdan.elsy.qconf.Perception;

/**
 * Perception feeding Curiosity neural network. Provides information for the
 * ErrorBackpropagationNN that tries to predict player's perception output in
 * the next step.
 */
public class RobotPerc extends Perception {
    private static final long serialVersionUID = 1L;

    private final Perception perception;

    private final RobotBrain brain;

    public RobotPerc(Perception perception, RobotBrain brain) {
        this.perception = perception;
        this.brain = brain;
    }

    public double getReward() {
        return 0;
    }

    public boolean isUnipolar() {
        return this.perception.isUnipolar();
    }

    protected void updateInputValues() {
        double[] percOut = this.perception.getOutput();
        for (int i = 1; i < percOut.length; i++) {
            setNextValueAbsolute(percOut[i]);
        }
        setNextValue(this.perception.getReward());
        int action = brain.getAction();
        for (int i = 0; i < brain.getOutput().length; i++) {
            setNextValue(i == action);
        }
    }

}
