package com.EE5.server.data;

import com.EE5.util.Tuple;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DeviceList implements Serializable {
    //private Map<String, Position> list = new HashMap<String, Position>();
    private Map<String, Tuple<Position, String>> list = new HashMap<String, Tuple<Position, String>>();

    public DeviceList() {
    }

    /** @deprecated */
    public void addDevice(Device device) {
        //list.put(device.getId(), device.getPosition());
        //pclist.put(device.getId(),device.getPattern());
    }

    public void add(String id, Position position, String data){
        list.put(id, new Tuple<Position, String>(position, data));
    }

    public void removeDevice(Device device) {
        list.remove(device.getId());
    }

    public void clear() {
        list.clear();
    }

    public Map<String, Tuple<Position,String>> getMap(){
        return this.list;
    }

    public Set<Map.Entry<String,Tuple<Position,String>>> getAll(){
        return this.getMap().entrySet();
    }

    public boolean contains(Device device){
        return this.list.containsKey(device.getId());
    }
}
