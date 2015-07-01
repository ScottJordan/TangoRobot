package pl.gdan.elsy.tool;

/**
 * Some helpful math methods.
 * Contains methods returning precomputed values of sigmoid function
 * (used by neural network).
 *
 * @author Elser
 */
public class Mat {
    private static double sigmoPrecomputed[];
    private static double SIGMO_RANGE = 30.0; // -4.0..4.0

    static {
        sigmoPrecomputed = new double[800];
        for (int i = 0; i < sigmoPrecomputed.length; i++) {
            double d = SIGMO_RANGE * (i - sigmoPrecomputed.length / 2) / sigmoPrecomputed.length;
            sigmoPrecomputed[i] = 1.0 / (1.0 + Math.exp(-d));
        }
    }

    /**
     * Fast version of unipolar sigmoid function.
     */
    public static double sigmoidUniFast(double d) {
        int i = (int) (d / SIGMO_RANGE * sigmoPrecomputed.length + sigmoPrecomputed.length / 2);
        if (i < 0) return 0.01;
        if (i >= sigmoPrecomputed.length) return 0.99;
        return sigmoPrecomputed[i];
    }

    /**
     * Fast version of bipolar sigmoid function.
     */
    public static double sigmoidBiFast(double d) {
        int i = (int) (d / SIGMO_RANGE * sigmoPrecomputed.length + sigmoPrecomputed.length / 2);
        if (i < 0) return -0.99;
        if (i >= sigmoPrecomputed.length) return 0.99;
        return sigmoPrecomputed[i] * 2 - 1.0;
    }

    /**
     * Bipolar sigmoid function.
     */
    public static double sigmoidBi(double d) {
        return 2.0 / (1.0 + Math.exp(-d)) - 1.0;
    }

    /**
     * Unipolar sigmoid function.
     */
    public static double sigmoidUni(double d) {
        return 1.0 / (1.0 + Math.exp(-d));
    }

    /**
     * @param x
     * @param dMin
     * @param dMax
     * @return true if x is inside (dMin..dMax)
     */
    public static boolean inside(double x, double dMin, double dMax) {
        return x > dMin && x < dMax;
    }

    /**
     * @param x
     * @param dMax
     * @return true if x is inside (-dMax..dMax)
     */
    public static boolean inside(double x, double dMax) {
        return x > -dMax && x < dMax;
    }

    /**
     * @param x
     * @param dMax
     * @return true if x is inside <-dMax..dMax>
     */
    public static boolean insideInclusive(double x, double dMax) {
        return x >= -dMax && x <= dMax;
    }

    /**
     * Returns x limited to (min..max)
     *
     * @param x
     * @param min
     * @param max
     */
    static public double lim(double x, double min, double max) {
        if (x < min) return min;
        if (x > max) return max;
        return x;
    }
}
