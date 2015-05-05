package com.EE5.server.socketTask;

import com.EE5.server.Server;

import java.io.InputStream;
import java.io.OutputStream;

public class SocketTaskFactory {
    public static SocketTask produce(SocketTaskType type, InputStream i, OutputStream o, Server server ){
        SocketTask task;
        switch(type) {
            case OBJECT:
                task = new ObjectSocketTask(i, o, server);
                break;
            case PRIMITIVE_DATA:
                task = new PrimitiveSocketTask(i,o,server);
                break;
            default:
                task = new ObjectSocketTask(i,o,server);
                break;
        }
        return task;
    }
}
