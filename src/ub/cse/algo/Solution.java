package ub.cse.algo;

import java.util.*;

public class Solution {
    
    private Integer _n_points;          // Number of points in the plane
    private ArrayList<Point> _points;  // This ArrayList contains all points in the plane

    public Solution (Integer n_points, ArrayList<Point> points){
        _n_points = n_points;
        _points = points;
    }
    
    public double outputClosestDistance(){

        // if only one point in the input, distance is 0
        if (this._n_points < 2) {
            return 0;
        }

        // the closest distance between 2 points is the distance between those
        if (this._n_points == 2) {
            return Math.sqrt(outputDistanceSquare(this._points));
        }

        // at least 3 points to compute the closest pair
        ArrayList<Point> PxSorted = new ArrayList<>();
        ArrayList<Point> PySorted = new ArrayList<>();
        for (int i = 0; i < this._n_points; i++) {
            PxSorted.add(this._points.get(i));
            PySorted.add(this._points.get(i));
        }

        /* Sort the points first by their x(or y) co-ordinate and for points that have the same x(or y) co-ordinate,
           sort them by their y(or x) co-ordinate
         */
        Comparator<Point> sortedInX = Comparator.comparingInt((Point p) -> p._x).thenComparingInt((Point p)-> p._y);
        Comparator<Point> sortedInY = Comparator.comparingInt((Point p) -> p._y).thenComparingInt((Point p)-> p._x);
        PxSorted.sort(sortedInX);
        PySorted.sort(sortedInY);

        ArrayList<Point> result = outputClosestPair(PxSorted, PySorted);

        double distance = Math.sqrt(outputDistanceSquare(result));

        return distance;
    }

    private ArrayList<Point> outputClosestPair(ArrayList<Point> Px, ArrayList<Point> Py) {

        double num = Px.size();

        // There exists no pair with only one point
        if (num < 2) {
            return new ArrayList<>();
        // The closest pair of 2 point is the pair of those
        } else if (num == 2) {
            return Px;
        // Brute-force way
        } else if (num < 4) {
            return bruteForce(Px, (int) num);
        // Divide the given points into 2 parts - Q:left or R:right
        } else {
            double numStar = Math.ceil(num / 2);
            int indexStar = (int) numStar - 1;
            Point pointStar = Px.get(indexStar);

            // P is divided into Q and R  2 lists of points
            // Sort the list of points by x-coordinates and by y-coordinates, respectively
            ArrayList<Point> Qx = new ArrayList<>();
            ArrayList<Point> Rx = new ArrayList<>();
            ArrayList<Point> Qy = new ArrayList<>();
            ArrayList<Point> Ry = new ArrayList<>();
            // Construct Qx and Rx
            for (int i = 0; i < num; i++) {
                Point curr_P = Px.get(i);
                if (i <= indexStar) {
                    Qx.add(curr_P);
                } else {
                    Rx.add(curr_P);
                }
            }
            // Construct Qy and Ry
            for (int i = 0; i < num; i++) {
                Point curr_P = Py.get(i);
                if (curr_P._x < pointStar._x) {
                    Qy.add(curr_P);
                } else if (curr_P._x.equals(pointStar._x)) {
                    if (curr_P._y <= pointStar._y) {
                        Qy.add(curr_P);
                    } else {
                        Ry.add(curr_P);
                    }
                } else {
                    Ry.add(curr_P);
                }
            }

            // The closest pair of each part
            ArrayList<Point> Qpair = outputClosestPair(Qx, Qy);
            ArrayList<Point> Rpair = outputClosestPair(Rx, Ry);


            // Compute the closest pair
            return closestAmong3(Qpair, Rpair, Py, pointStar);
        }
    }

    private ArrayList<Point> bruteForce(ArrayList<Point> P, int size) {
        HashMap<Double, ArrayList<Point>> pairMap = new HashMap<>();
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < size; i++) {
            Point p = P.get(i);
            for (int j = i + 1; j < size; j++) {
                Point otherP = P.get(j);
                double distance = distanceSquare(p, otherP);
                if (distance < min) {
                    min = distance;
                    pairMap.put(min, new ArrayList<>(){{add(p);add(otherP);}});
                }
            }
        }
        return pairMap.get(min);
    }

    private ArrayList<Point> closestAmong3(ArrayList<Point> Qpair,ArrayList<Point> Rpair, ArrayList<Point> Py, Point pointStar) {

        double distQ = Math.sqrt(outputDistanceSquare(Qpair));
        double distR = Math.sqrt(outputDistanceSquare(Rpair));
        double difference = Math.min(distQ,distR);  // The closer distance between the two pairs

        ArrayList<Point> midSection_y = new ArrayList<>();

        // Construct Sy sorted in y-coordinate
        for (Point p : Py) {
            double d = Math.abs(p._x - pointStar._x);
            if (d < difference) {
                midSection_y.add(p);
            }
        }

        double minSquare = difference * difference;

        int size = midSection_y.size();
        HashMap<Double, ArrayList<Point>> distAsKeyforPair = new HashMap<>();

        // Find (p,p') such that d(p, p') < difference
        for (int i = 0; i < size; i++) {
            Point p = midSection_y.get(i);
            if (i + 16 >= size) {
                for (int j = i + 1; j < size; j++){
                    Point otherP = midSection_y.get(j);
                    double distance = distanceSquare(p, otherP);
                    if (distance < minSquare) {
                        minSquare = distance;
                        distAsKeyforPair.put(minSquare, new ArrayList<>(){{add(p);add(otherP);}});
                    }
                }
            } else {
                for (int j = i + 1; j < i + 16; j++) {
                    Point otherP = midSection_y.get(j);
                    double distance = distanceSquare(p, otherP);
                    if (distance < minSquare) {
                        minSquare = distance;
                        distAsKeyforPair.put(minSquare, new ArrayList<>(){{add(p);add(otherP);}});
                    }
                }
            }
        }

        double min = Math.sqrt(minSquare);

        if (min < difference) {
            return distAsKeyforPair.get(minSquare);
        } else if (distQ < distR) {
            return Qpair;
        } else {
            return Rpair;
        }
    }

    private double outputDistanceSquare(ArrayList<Point> pts) {
        return Math.pow((pts.get(0)._x - pts.get(1)._x),2) + Math.pow((pts.get(0)._y - pts.get(1)._y), 2);
    }

    private double distanceSquare(Point p1, Point p2) {
        return Math.pow((p1._x - p2._x),2) + Math.pow((p1._y - p2._y), 2);
    }
}

