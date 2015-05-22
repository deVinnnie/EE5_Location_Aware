package com.EE5.server.data;

import com.EE5.util.Tuple;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DeviceList implements Serializable {
    private Map<String, Tuple<Position, String>> list = new HashMap<String, Tuple<Position, String>>();

    public DeviceList() {}

    /**
     * Add or update a device's position and/or data.
     *
     * @param id Id of the device.
     * @param position Current position of the device.
     * @param data Optionally a custom data field associated witht the device.
     */
    public void add(String id, Position position, String data){
        list.put(id, new Tuple<Position, String>(position, data));
    }

    public void removeDevice(Device device) {
        list.remove(device.getId());
    }

    /**
     * Remove the device with the specified ID.
     * @param device ID of the device.
     */
    public void remove(String device){
        list.remove(device);
    }

    /**
     * Remove all devices from the list.
     */
    public void clear() {
        list.clear();
    }

    /**
     * @return The inner map containing all Device -> (Position, String) pairs.
     *          The String value is the data field of the device.
     */
    private Map<String, Tuple<Position,String>> getMap(){
        return this.list;
    }

    public Set<Map.Entry<String,Tuple<Position,String>>> getAll(){
        return this.getMap().entrySet();
    }

    public boolean contains(Device device){
        return this.list.containsKey(device.getId());
    }

    public int get_Size(){
        return list.size();
    }
}
