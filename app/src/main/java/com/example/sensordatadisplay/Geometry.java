package com.example.sensordatadisplay;

import static java.lang.Math.sqrt;

public class Geometry {

    public static class Point{
        public final float x, y, z;
        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public Point translateX(float distance) {return new Point(x + distance, y, z);}
        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }
        public Point translateZ(float distance) {
            return new Point(x, y, z + distance);
        }
        public Point translate(Vector vector){
            return new Point(x + vector.x, y + vector.y, z + vector.z);
        }
    }
    public static class Ray {
        public final Point point;
        public final Vector vector;
        public Ray(Point point, Vector vector){
            this.point = point;
            this.vector = vector;
        }
    }

    public static class Vector {
        public final float x, y, z;
        public Vector(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float length(){
            return (float)sqrt(x * x + y * y + z * z);
        }

        public Vector crossProduct(Vector other) {
            return new Vector((y * other.z) - (z * other.y), (z * other.x) - (x * other.z), (x * other.y) - (y * other.x));
        }
    }

    public static Vector vectorBetween (Point from, Point to) {
        return new Vector(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    public static class Sphere {
        public final Point center;
        public final float radius;
        public Sphere(Point center, float radius){
            this.center = center;
            this.radius = radius;
        }
    }
    //intersection test
    public static boolean intersects(Sphere sphere,Ray ray) {
        return distanceBetween (sphere.center, ray) < sphere.radius;
    }

    public static float distanceBetween(Point point, Ray ray) {
        Vector p1ToPoint = vectorBetween(ray.point, point);
        Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point);

        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        float lengthOfBase = ray.vector.length();

        float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;
        return distanceFromPointToRay;
    }
}
