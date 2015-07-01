package RL;

import java.io.Serializable;

import pl.gdan.elsy.qconf.ErrorBackpropagationNN;
import pl.gdan.elsy.qconf.Perception;

public class Robot implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final double AVG_FORGET = 0.2;

    private ErrorBackpropagationNN nn;

    private final RobotPlayerPerception perception;

    private final RobotBrain brain;

    private Perception robotPerc;

    private double avgError;

    private double[] desiredOutput;

    private double[] inputBkp;

    private double[] outputBkp;

    private double[] desOutputBkp;

    public Robot(RobotPlayerPerception perception, RobotBrain brain, int[] hiddenNeurons) {
        this.perception = perception;
        this.brain = brain;
        this.brain.setRobot(this);
        this.robotPerc = new RobotPerc(this.perception, this.brain);
        this.perception.start();
        this.desiredOutput = this.perception.getForeseeOutput();
        nn = new ErrorBackpropagationNN(robotPerc, desiredOutput,
                hiddenNeurons);
        nn.setAlpha(0.2);
        nn.setMomentum(0.5);
        inputBkp = new double[nn.getInput().length];
        outputBkp = new double[nn.getOutput().length];
        desOutputBkp = new double[nn.getDesiredOutput().length];
    }

    private void arraycopy(double[] src, double[] dest, boolean check) {
        if (check && src.length != dest.length) {
            System.out.println("src.length != dest.length");
        }
        System.arraycopy(src, 0, dest, 0, dest.length);
    }

    public void learn() {
        double[] percOut = this.perception.getForeseeOutput();
        arraycopy(percOut, desiredOutput, false);
        arraycopy(nn.getInput(), inputBkp, true);
        arraycopy(nn.getOutput(), outputBkp, true);
        arraycopy(nn.getDesiredOutput(), desOutputBkp, true);
        nn.learn();
        avgError = avgError * (1 - AVG_FORGET) + nn.getError() * AVG_FORGET;
    }

    public void countExpectations() {
        this.robotPerc.perceive();
        nn.propagate();
    }

    public double getError() {
        return nn.getError();
    }

    public double getAvgError() {
        return (nn != null) ? avgError : 10;
    }

    public ErrorBackpropagationNN getNn() {
        return nn;
    }

    public void setNn(ErrorBackpropagationNN nn) {
        this.nn = nn;
    }

    public double[] getInputBkp() {
        return inputBkp;
    }

    public double[] getOutputBkp() {
        return outputBkp;
    }

    public double[] getDesOutputBkp() {
        return desOutputBkp;
    }

}
