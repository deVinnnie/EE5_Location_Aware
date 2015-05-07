package com.EE5.server.data;

import com.EE5.image_manipulation.PatternCoordinator;

import java.io.Serializable;

public class Device implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * Unique ID to identify this phone among the other connected devices.
     */
    private String id;

    /**
     * Current position of the device.
     */
    private Position position;

    public Device(){
        this.id = "001";
    }

    public Device(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Device other = (Device) obj;
        if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
}