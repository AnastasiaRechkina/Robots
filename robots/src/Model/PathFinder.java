package Model;

import Model.Elements.Barrier;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PathFinder implements Serializable
{
    public HashMap<Point, ArrayList<Point>> graph;
    private Iterable<Barrier> barriers;
    private ArrayList<Point> points;

    public PathFinder(Iterable<Barrier> barriers) {
        this.barriers = barriers;
        points = allPoints(barriers);
        graph = makeGraph();
    }

    public Iterable<Point> findPathTo(Point from, Point to) {
        HashMap<Point, ArrayList<Point>> tmpGraph = cloneHM(graph);

        tmpGraph.put(from, new ArrayList<>());
        tmpGraph.put(to, new ArrayList<>());

        if (!contains(barriers, new Line2D.Double(from, to))) {
            tmpGraph.get(from).add(to);
            tmpGraph.get(to).add(from);
        }

        for (Point p : points) {

            if (!contains(barriers, new Line2D.Double(from, p))) {
                tmpGraph.get(from).add(p);
                tmpGraph.get(p).add(from);
            }

            if (!contains(barriers, new Line2D.Double(p, to))) {
                tmpGraph.get(p).add(to);
                tmpGraph.get(to).add(p);
            }
        }

        return Dijkstra.find(tmpGraph, from, to);
    }

    public static HashMap<Point, ArrayList<Point>> cloneHM(HashMap<Point, ArrayList<Point>> hm) {
        HashMap<Point, ArrayList<Point>> newHM = new HashMap<>();

        for (Point key : hm.keySet()) {
            newHM.put(key, new ArrayList<>());
        }

        for (Point key : hm.keySet()) {
            newHM.put(key, (ArrayList<Point>)hm.get(key).clone());
        }

        return newHM;
    }

    public static ArrayList<Point> allPoints(Iterable<Barrier> barriers) {
        ArrayList<Point> points = new ArrayList<>();

        for (Barrier obst : barriers)
            points.addAll(obst.getPivots());

        return points;
    }

    public HashMap<Point, ArrayList<Point>> makeGraph() {
        HashMap<Point, ArrayList<Point>> graph = new HashMap<>();
        points.forEach((x) -> graph.put(x, new ArrayList<>()));



        for (int i = 0, count = points.size(); i < count; i++)
        {
            for (int j = i + 1; j < count; j++)
            {
                Point fir = points.get(i);
                Point sec = points.get(j);

                Line2D line = new Line2D.Double(fir, sec);

                if (!contains(barriers, line)) {
                    graph.get(fir).add(sec);
                    graph.get(sec).add(fir);
                }
            }
        }

        return graph;
    }

    public HashMap<Point, ArrayList<Point>> getGraph() {
        return graph;
    }

    public static boolean contains(Iterable<Barrier> barriers, Line2D line) {
        for (Barrier obst : barriers)  {
            if (obst.getRectangle().intersectsLine(line)) {
                return true;
            }
        }

        return false;
    }
}