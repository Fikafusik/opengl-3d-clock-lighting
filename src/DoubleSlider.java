
import javax.swing.*;

public class DoubleSlider extends JSlider {

    private final double minimum;
    private final double maximum;
    private final double scale;

    public DoubleSlider(double min, double max, double value, int scale) {
        super(0, scale, (int)(scale * (value - min) / (max - min)));

        this.minimum = min;
        this.maximum = max;
        this.scale = scale;
    }

    public double getDoubleValue() {
        return (minimum + (maximum - minimum) * getValue() / scale);
    }
}
