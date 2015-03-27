package com.EE5.util;

import com.EE5.communications.connection.Connection;

/**
 * Contains objects that need to be available to all Activities.
 * This class is a Singleton meaning the constructor cannot be called and the method 'getInstance' needs to be used instead.
 * 'getInstance()' will make a new GlobalResources Object if one does not exist yet. Or: If a GlobalResources Object already exists it will return this instance.
 * This way each Activity will be dealing with the same GlobalResources instance.
 */
public class GlobalResources {
    private static GlobalResources instance;

    public Connection connection;

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
    //</editor-fold>
}
