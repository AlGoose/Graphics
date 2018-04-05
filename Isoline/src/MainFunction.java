import java.util.Arrays;

public class MainFunction {
    private double a;
    private double b;
    private double c;
    private double d;

    private double max;
    private double min;

    private int gridWidth;
    private int gridHeight;

    public MainFunction(double a, double b, double c, double d, int gridWidth, int gridHeight) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        findExtreme();
    }

    public double findValue(double x, double y) {
        return (x*x - y*y);
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

    private void findExtreme() {
        double deltaX = (b - a) / (gridWidth - 1);
        double deltaY = (d - c) / (gridHeight - 1);

        double[] values = new double[gridWidth * gridHeight];

        for (int y = 0; y < gridHeight; y++){
            for (int x = 0; x < gridWidth; x++){
                values[y * gridWidth + x] = findValue(a + x * deltaX, c + y * deltaY);
            }
        }
        Arrays.sort(values);

        min = values[0];
        max = values[values.length - 1];
    }
}
