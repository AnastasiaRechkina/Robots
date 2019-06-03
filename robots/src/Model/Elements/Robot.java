package Model.Elements;

import java.awt.*;

public class Robot implements IRobot {
    private volatile double x = 100;
    private volatile double y = 100;
    private int width;
    private int height;
    private volatile double direction = 0;

    private double duration =  6;

    public Robot(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public Robot() {

    }

    @Override
    public void moveRobot(double vel, double angVel, double dur) {
        x = x + vel * dur * Math.cos(direction);
        y = y + vel * dur * Math.sin(direction);
        direction -= angVel * dur;
    }
    @Override
    public void moveRobot(double vel, double angVel) {
        x = x + vel * duration * Math.cos(direction);
        y = y + vel * duration * Math.sin(direction);
        direction -= angVel * duration;
    }
    @Override
    public Point getPosition() {
        return new Point((int)(x + 0.5), (int)(y + 0.5));
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public double getMaxVelocity() {
        return 0.1;
    }

    @Override
    public double getMaxAngularVelocity() {
        return 0.001;
    }

    @Override
    public double getDirection() {
        return direction;
    }

    @Override
    public Rectangle getRectangle() {
        int r_x = (int)(x + 0.5);
        int r_y = (int)(y + 0.5);
        return new Rectangle(r_x, r_y, width, height);
    }

    @Override
    public String getState() {
        return x + "," + y + "," + direction;
    }

    @Override
    public void setState(String state) {
        String[] axis = state.split(",");
        x = Double.valueOf(axis[0]);
        y = Double.valueOf(axis[1]);
        direction = Double.valueOf(axis[2]);
    }
}
