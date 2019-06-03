package Model.Elements;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by Людмила Борисовна on 28.05.2017.
 */
public interface IRobot extends Serializable {
    void moveRobot(double vel, double angVel, double dur);
    void moveRobot(double vel, double angVel);

    Point getPosition();

    double getX();

    double getY();

    void setX(int x);

    void setY(int y);

    int getWidth();

    int getHeight();

    double getMaxVelocity();

    double getMaxAngularVelocity();

    double getDirection();

    Rectangle getRectangle();

    String getState();

    void setState(String state);
}
