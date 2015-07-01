package RL;

import pl.gdan.elsy.qconf.Perception;

public class RobotPlayerPerception extends Perception {
    private RobotState robotState;
    private boolean unipolar = true;
    private Perception foreseePerc = this;

    public RobotPlayerPerception(boolean unipolar, RobotState robotState) {
        this.unipolar = unipolar;
        this.robotState = robotState;
    }

    @Override
    public boolean isUnipolar() {
        return unipolar;
    }

    public double getReward() {
        if (robotState == null) return 0;
        return robotState.getReward();
    }

    /**
     * This method must be implemented in your class extending this class.
     * Here you set all the input values coming from the agent's sensors.
     * This method shall invoke the setNextValue(double d) method for each
     * input with the input value as a parameter. In other words, setNextValue should
     * be called as many times as the number of the agent sensors.
     */
    @Override
    protected void updateInputValues() {
        robotState = RobotState.getInstance();
        if (robotState == null) {
            for (int i = 0; i < 9; i++) {
                setNextValue(0);
            }
        }
        setNextValue(robotState.getPosition().getX());
        setNextValue(robotState.getPosition().getY());
        setNextValue(robotState.getPosition().getZ());
        setNextValue(robotState.getOrientation().getX());
        setNextValue(robotState.getOrientation().getY());
        setNextValue(robotState.getOrientation().getZ());
        setNextValue(robotState.getOrientation().getW());
        setNextValue(robotState.getMotorLeft());
        setNextValue(robotState.getMotorRight());
    }

    public void start() {
        super.start();
        if (foreseePerc != this) {
            foreseePerc.start();
        }
    }

    public void perceive() {
        super.perceive();
        if (getOutput() != null && foreseePerc != this) {
            foreseePerc.perceive();
        }
    }

    public void setUnipolar(boolean unipolar) {
        this.unipolar = unipolar;
    }

    public double[] getForeseeOutput() {
        return foreseePerc.getOutput();
    }

    public Perception getForeseePerc() {
        return foreseePerc;
    }

    public void setForeseePerc(Perception foreseePerc) {
        this.foreseePerc = foreseePerc;
        this.foreseePerc.setInputPerception(this);
    }
}
