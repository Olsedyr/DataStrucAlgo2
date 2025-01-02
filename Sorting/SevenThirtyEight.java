package Sorting;//Write a program that reads N points in a plane and outputs any group of four
//or more colinear points (i.e., points on the same line). The obvious brute-force
//algorithm requires O(N^4) time. However, there is a better algorithm that makes use
//of sorting and runs in O(N^2 logN) time.


//How It Works in Simple Terms:
//
//    Imagine a set of points on a 2D grid.
//    Pick one point as a reference. We'll call this the "origin" point.
//    Draw a line from the origin to each of the other points and calculate the angle of the line (the slope).
//    Sort the other points by their slopes. If two or more points have the same slope with the origin, they are on the same line.
//    Group collinear points. For any set of points with the same slope, you’ve found collinear points.
//    Repeat this process for each point as the origin until all points are checked.



import java.util.Arrays;
import java.util.Comparator;

public class SevenThirtyEight {
    // Class to represent a point in a 2D plane
    static class Point implements Comparable<Point> {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // Comparator to compare two points based on their slopes relative to a reference point
        public Comparator<Point> slopeOrder() {
            return new Comparator<Point>() {
                public int compare(Point p1, Point p2) {
                    double slope1 = slopeTo(p1);
                    double slope2 = slopeTo(p2);
                    return Double.compare(slope1, slope2);
                }
            };
        }

        // Calculate the slope between this point and another point p
        public double slopeTo(Point p) {
            if (this.x == p.x && this.y == p.y) return Double.NEGATIVE_INFINITY; // Same point
            if (this.x == p.x) return Double.POSITIVE_INFINITY; // Vertical line
            return (double) (p.y - this.y) / (p.x - this.x); // Normal slope
        }

        // Override compareTo to allow sorting by x and then y coordinate
        public int compareTo(Point other) {
            if (this.y == other.y) {
                return Integer.compare(this.x, other.x);
            } else {
                return Integer.compare(this.y, other.y);
            }
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    public static void findCollinearPoints(Point[] points) {
        int N = points.length;

        // Iterate through each point
        for (int i = 0; i < N; i++) {
            Point origin = points[i];

            // Sort points by the slope they make with origin
            Point[] sortedPoints = Arrays.copyOf(points, N);
            Arrays.sort(sortedPoints, origin.slopeOrder());

            // Check for collinear points in the sorted list
            int count = 1;
            for (int j = 1; j < N; j++) {
                // While points have the same slope
                if (origin.slopeTo(sortedPoints[j]) == origin.slopeTo(sortedPoints[j - 1])) {
                    count++;
                } else {
                    if (count >= 3) {
                        // Output the points if there are 3 or more points collinear with origin
                        System.out.print("Collinear points: " + origin);
                        for (int k = j - count; k < j; k++) {
                            System.out.print(" -> " + sortedPoints[k]);
                        }
                        System.out.println();
                    }
                    count = 1; // Reset count
                }
            }

            // Handle the last group of collinear points
            if (count >= 3) {
                System.out.print("Collinear points: " + origin);
                for (int k = N - count; k < N; k++) {
                    System.out.print(" -> " + sortedPoints[k]);
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Point[] points = {
                new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3),  // Collinear
                new Point(1, 0), new Point(2, 1), new Point(3, 2),                   // Another line
                new Point(1, 2), new Point(2, 3), new Point(3, 4)                    // Yet another line
        };

        findCollinearPoints(points);
    }
}