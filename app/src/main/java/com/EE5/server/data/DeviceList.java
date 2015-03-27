package com.EE5.server.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DeviceList implements Serializable {
    private Map<String, Position> list = new HashMap<String, Position>();

    public DeviceList() {
    }

    public void addDevice(Device device) {
        list.put(device.getId(), device.getPosition());
    }

    public void removeDevice(Device device) {
        list.remove(device.getId());
    }

    public void clear() {
        list.clear();
    }

    /*public Iterator<Device> getIterator() {
        return list.iterator();
    }*/

    public Map<String, Position> getMap(){
        return this.list;
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
