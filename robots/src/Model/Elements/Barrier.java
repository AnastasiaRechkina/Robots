package Model.Elements;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Barrier implements Serializable {
    private double x;
    private double y;
    private int width;
    private int height;

    public Barrier(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(Point point) {
        return getRectangle().contains(point);
    }

    public boolean contains(IRobot robot) {
        return getRectangle().intersects(robot.getRectangle());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static Barrier random() {
        Random rnd = new Random();
        int width = rnd.nextInt(90) + 10;
        int height = rnd.nextInt(90) + 10;
        int x = rnd.nextInt(1200) + 10;
        int y = rnd.nextInt(600) + 10;
        return new Barrier(x, y, width, height);
    }

    public Collection<Point> getPivots() {
        int i_x = (int)(x + 0.5);
        int i_y = (int)(y + 0.5);
        int delta = 20;
        return new ArrayList<Point>() {{
            add(new Point(i_x - delta, i_y - delta));
            add(new Point(i_x + width + delta, i_y - delta));
            add(new Point(i_x + width + delta, i_y + height + delta));
            add(new Point(i_x - delta, i_y + height + delta));
        }};
    }

    public Rectangle getRectangle() {
        int i_x = (int)(x + 0.5);
        int i_y = (int)(y + 0.5);
        return new Rectangle(i_x, i_y, width, height);
    }
}
