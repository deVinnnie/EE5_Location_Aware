package com.EE5.util;

import com.EE5.communications.connection.Connection;
import com.EE5.image_manipulation.PatternDetector;
import com.EE5.server.Server;
import com.EE5.server.data.Device;
import com.EE5.server.data.DeviceList;

/**
 * Contains objects that need to be available to all Activities.
 * This class is a Singleton meaning the constructor cannot be called and the method 'getInstance' needs to be used instead.
 * 'getInstance()' will make a new GlobalResources Object if one does not exist yet. Or: If a GlobalResources Object already exists it will return this instance.
 * This way each Activity will be dealing with the same GlobalResources instance.
 */
public class GlobalResources {
    private static GlobalResources instance;

    private Connection connection;
    private Server server;

    /**
     * Information about THIS device.
     */
    private Device device = new Device();

    /**
     * Only Positions of other Devices.
     */
    private DeviceList devices = new DeviceList();

    /**
     * Custom data field to be send for this device.
     */
    private String data = "";

    private PatternDetector patternDetector;

    // The constructor is private so that it can only be called from within the class.
    private GlobalResources(){}

    public static synchronized GlobalResources getInstance(){
        if(instance==null){
            instance=new GlobalResources();
        }
        return instance;
    }

    //<editor-fold desc="Getters/Setters">
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public DeviceList getDevices() {
        return devices;
    }

    public void setDevices(DeviceList devices) {
        this.devices = devices;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public PatternDetector getPatternDetector() {
        return patternDetector;
    }

    public void setPatternDetector(PatternDetector patternDetector) {
        this.patternDetector = patternDetector;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    //</editor-fold>
}
