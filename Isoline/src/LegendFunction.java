public class LegendFunction implements Function{
    private double a;
    private double b;
    private double c;
    private double d;

    private double max;
    private double min;

    public LegendFunction() {
        this.a = 0;
        this.b = 0;
        this.c = 0;
        this.d = 10;

        findExtrema();
    }

    public double findValue(double x, double y) {
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
        this.max = 0;
        this.min = -d;
    }
}
