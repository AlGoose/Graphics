public class LegendFunction {
    private double a;
    private double b;
    private double c;
    private double d;

    private double max;
    private double min;

    public LegendFunction() {
        a = 0;
        b = 0;
        c = 0;
        d = 10;

        findExtrema();
    }

    public double findValue(double y) {
        return -y;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    private void findExtrema() {
        max = 0;
        min = -d;
    }
}
