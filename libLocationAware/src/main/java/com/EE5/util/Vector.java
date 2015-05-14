package com.EE5.util;

public class Vector {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void normalize(){
        double length = this.getLength();
        this.x /= length;
        this.y /= length;
    }

    public double getLength(){
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double dotProduct(Vector vector2){
        double dotProduct = this.x * vector2.x + this.y * vector2.y;
        return dotProduct;
    }

    public double crossProduct(Vector vector2){
        return (this.getX()*vector2.getY() - this.getY()*vector2.getX());
    }
}
