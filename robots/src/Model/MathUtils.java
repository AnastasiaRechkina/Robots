package Model;

import Model.Elements.IRobot;
import Model.Elements.Target;

public class MathUtils {

    public static double asNormalizedRadians(double angle) {
        double newAngle = angle;
        while (newAngle <= -180) newAngle += 360;
        while (newAngle > 180) newAngle -= 360;
        return newAngle;
    }

    public static double angleBetween(IRobot robot, Target target) {
        double dir = robot.getDirection();

        double rx = Math.cos(dir);
        double ry = Math.sin(dir);

        double tx = target.getX() - robot.getX();
        double ty = target.getY() - robot.getY();

        double len = Math.sqrt(tx * tx + ty * ty);
        double pseudo = (rx * tx + ty * ry) / len;

        return asNormalizedRadians(Math.acos(pseudo));
    }

    public static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
}
