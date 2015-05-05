package com.EE5.server.data;

import com.EE5.image_manipulation.PatternCoordinator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DeviceList implements Serializable {
    private Map<String, Position> list = new HashMap<String, Position>();
    private Map<String, PatternCoordinator> pclist = new HashMap<String, PatternCoordinator>();

    public DeviceList() {
    }

    public void addDevice(Device device) {
        list.put(device.getId(), device.getPosition());
        pclist.put(device.getId(),device.getPattern());
    }

    public void removeDevice(Device device) {
        list.remove(device.getId());
        pclist.remove(device.getId());
    }

    public void clear() {
        list.clear();
        pclist.clear();
    }

    /*public Iterator<Device> getIterator() {
        return list.iterator();
    }*/

    public Map<String, Position> getMap(){
        return this.list;
    }
    public Map<String, PatternCoordinator> getPatternMap(){
        return this.pclist;
    }



    public boolean contains(Device device){
        return this.list.containsKey(device.getId());
    }

    /*public boolean containsAll(Collection<Device> devices){
        return this.list.containsAll(devices);
    }*/

    /*public Device get(){
        this.list.

    }*/
}
