package Model;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Dijkstra
{
    public static Iterable<Point> find(HashMap<Point, ArrayList<Point>> graph, Point start, Point end) {
        ArrayList<Point> notVisited = new ArrayList<>(graph.keySet());
        HashMap<Point, Data> track = new HashMap<>();
        HashMap<Line2D, Double> lens = new HashMap<>();

        track.put(start, new Data(null, 0));

        while (true) {
            Point toOpen = null;

            double bestPrice = Double.POSITIVE_INFINITY;

            for (Point e : notVisited) {
                if (track.containsKey(e) && track.get(e).price < bestPrice) {
                    bestPrice = track.get(e).price;
                    toOpen = e;
                }
            }

            if (toOpen == null) return null;
            if (toOpen == end) break;

            for (Point e : graph.get(toOpen)) {

                Line2D line = new Line2D.Double(toOpen, e);
                double len = 0;

                if (lens.containsKey(line))  {
                    len = lens.get(line);
                } else {
                    double xPart = (line.getX1() - line.getX2());
                    double yPart = (line.getY1() - line.getY2());
                    len = xPart * xPart + yPart * yPart;
                    lens.put(line, len);
                }

                double currentPrice = track.get(toOpen).price + len;

                if (!track.containsKey(e) || track.get(e).price > currentPrice) {
                    track.put(e, new Data(toOpen, currentPrice));
                }
            }

            notVisited.remove(toOpen);
        }

        LinkedList<Point> result = new LinkedList<>();

        while (end != null) {
            result.addFirst(end);
            end = track.get(end).prev;
        }

        return result;
    }

    private static class Data {
        public Point prev;
        public double price;

        public Data(Point prev, double price) {
            this.prev = prev;
            this.price = price;
        }
    }
}
