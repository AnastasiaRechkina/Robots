package Model;

import Model.Elements.*;

import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

public class Model extends Observable implements Serializable {
    private int selectedRobot;
    private ArrayList<IRobot> robots;
    private ArrayList<LinkedList<Target>> targets;
    private ArrayList<Target> curTargets;
    private int loops = 0;
    private ArrayList<Barrier> barriers;
    private PathFinder pathFinder;

    public Model(IRobot robot) {
        robots = new ArrayList<>();
        targets = new ArrayList<>();
        curTargets = new ArrayList<>();
        barriers = new ArrayList<>();
        addRobot(robot);
        selectedRobot = 0;
        pathFinder = new PathFinder(barriers);

    }

    public void updateRobot(int index) {
        loops = (loops + 1) % 12;

        double alpha = MathUtils.angleBetween(robots.get(index), curTargets.get(index));
        double distance = distanceBetween(index);

        double v  = robots.get(index).getMaxVelocity();
        double w = robots.get(index).getMaxAngularVelocity();

        double vTo = 0;
        double wTo = w;

        if (Math.abs(alpha) < 0.01) {
            wTo = 0;
            vTo = v;
        }

        if (distance < 0.5) {
            if (targets.get(index).size() == 0)
                return;
            nextTarget(index);
        }

        vTo = MathUtils.applyLimits(vTo, -v, v);
        wTo = MathUtils.applyLimits(wTo, -w, w);

        robots.get(index).moveRobot(vTo, wTo, 0.01);

        double alp = MathUtils.angleBetween(robots.get(index), curTargets.get(index));

        if (alpha > alp) {
            robots.get(index).moveRobot(vTo, wTo);
        } else {
            robots.get(index).moveRobot(vTo, -wTo);
        }

        setChanged();

        if (loops == 0) {
            notifyObservers();
        }
    }

    public void update() {
        for (int i = 0; i < getSize(); i++)
            updateRobot(i);
    }

    public void setTargetPosition(Point point) {
        Iterable<Point> path = pathFinder.findPathTo(robots.get(selectedRobot).getPosition(), point);
        if (path != null) {
            LinkedList<Target> queue = new LinkedList<>();
            path.forEach((x) -> queue.add(new Target(x.getX(), x.getY())));
            targets.set(selectedRobot, queue);
        }
    }

    public Point.Double getTargetPosition(int index) {
        return curTargets.get(index).getPosition();
    }

    public Point getRobotPosition(int index) {
        return robots.get(index).getPosition();
    }

    public double distanceBetween(int index) {
        return getRobotPosition(index).distance(getTargetPosition(index));
    }

    public IRobot getRobot(int index) {
        return robots.get(index);
    }

    public Target getTarget(int index) {
        if (targets.get(index).size() > 0)
            return targets.get(index).getLast();
        return curTargets.get(index);
    }

    public ArrayList<Barrier> getBarriers() {
        return barriers;
    }

    public void setBarriers(ArrayList<Barrier> barriers) {
        this.barriers = barriers;
        pathFinder = new PathFinder(this.barriers);
    }

    public void addBarrier(Barrier barrier) {
        barriers.add(barrier);
        pathFinder = new PathFinder(barriers);
    }

    public void addRandomBarrier() {
        addBarrier(Barrier.random());
    }

    public void addBarrier(Point first, Point second) {
        Rectangle r = new Rectangle();
        r.setFrameFromDiagonal(first, second);
        addBarrier(new Barrier(r.getX(), r.getY(), (int)r.getWidth(), (int)r.getHeight()));
    }

    public boolean inObstacle(Point point) {
        return barriers.stream().anyMatch((x) -> x.contains(point));
    }

    public void nextTarget(int index) {
        curTargets.set(index, targets.get(index).remove());
    }

    public void addRobot(IRobot robot) {
        curTargets.add(new Target(robot.getX(), robot.getY()));
        targets.add(new LinkedList<>());
        robots.add(robot);
    }

    public void removeRobot() {
        robots.remove(robots.size() - 1);
        curTargets.remove(curTargets.size() - 1);
        targets.remove(targets.size() - 1);
    }

    public int getSize() {
        return robots.size();
    }

    public Iterable<IRobot> getRobots() {
        return robots;
    }

    public void selectRobot(int index) {
        selectedRobot = index;
    }

    public HashMap<Point, ArrayList<Point>> getGraph() {
        return pathFinder.getGraph();
    }
}

 /*try {
            URL[] classLoaderUrls = new URL[] {
                    new URL("file:///" +  System.getProperty("user.home") + "/robotsData/math.jar")};
            URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls);
            Class<?> logicClass = urlClassLoader.loadClass("Model.PathFinder");
            Constructor<?> constructor = logicClass.getConstructor(Iterable.class);
            pathFinder = (PathFinder) constructor.newInstance(barriers);
        }
        catch (Exception e) {
            System.out.println("sorry");
        }*/