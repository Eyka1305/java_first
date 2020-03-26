package ru.stqa.pft.sandbox;

public class MySecondProgram {
    public static void main(String[] args) {
        Point p1 = new Point(4, 3);
        Point p2 = new Point(10, 12);

        System.out.println("Расстояние между точками P1 и P2 равно " + p1.distance(p2));
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt((p2.x - p1.x) + (p2.y - p1.y));
    }
}
