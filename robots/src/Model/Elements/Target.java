package Model.Elements;

import java.awt.Point;
import java.io.Serializable;

public class Target implements Serializable {
    private double x;
    private double y;

    public Target(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Target(Point.Double point) {
        x = point.getX();
        y = point.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point.Double getPosition() {
        return new Point.Double(x, y);
    }
}
