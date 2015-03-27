package com.EE5.server.data;

import java.io.Serializable;

public class Position implements Serializable {
    private static final long serialVersionUID = 2L;
    private double x;
    private double y;

    /**
     * Rotation in radians.
     */
    private double rotation;

    /**
     * Distance from camera to code.
     */
    private double height;

    public Position(){}

    public Position(double x, double y, double rotation, double height) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.height = height;
    }

    //<editor-fold desc="Getters_Setters">
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRotation() {
        return rotation;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.rotation) ^ (Double.doubleToLongBits(this.rotation) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.height) ^ (Double.doubleToLongBits(this.height) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Position other = (Position) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.rotation) != Double.doubleToLongBits(other.rotation)) {
            return false;
        }
        if (Double.doubleToLongBits(this.height) != Double.doubleToLongBits(other.height)) {
            return false;
        }
        return true;
    }
}
