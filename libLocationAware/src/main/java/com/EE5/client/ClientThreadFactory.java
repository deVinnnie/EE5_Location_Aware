package com.EE5.client;

import com.EE5.client.primitive_client.PrimitiveClientInputThread;
import com.EE5.client.primitive_client.PrimitiveClientOutputThread;
import com.EE5.client.object.ObjectClientInputThread;
import com.EE5.client.object.ObjectClientOutputThread;
import com.EE5.server.socketTask.SocketTaskType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ClientThreadFactory {

    /**
     * @param type Type of the OutputThread.
     * @param o Outputstream for the created OutputThread
     * @param client Client for the created OutputThread
     * @return A new OutputThread.
     * @throws IOException
     */
    public static AbstractClientOutputThread produceOutputThread(SocketTaskType type, OutputStream o, Client client) throws IOException {
        AbstractClientOutputThread outputThread;
        switch(type){
            case OBJECT:
                outputThread = new ObjectClientOutputThread(o, client);
                break;
            case PRIMITIVE_DATA:
                outputThread = new PrimitiveClientOutputThread(o, client);
                break;
            default:
                outputThread = new PrimitiveClientOutputThread(o, client);
                break;
        }
        return outputThread;
    }

    public static AbstractClientInputThread produceInputThread(SocketTaskType type, InputStream i, Client client) throws IOException {
        AbstractClientInputThread inputThread;
        switch(type){
            case OBJECT:
                inputThread = new ObjectClientInputThread(i, client);
                break;
            case PRIMITIVE_DATA:
                inputThread = new PrimitiveClientInputThread(i, client);
                break;
            default:
                inputThread = new PrimitiveClientInputThread(i, client);
                break;
        }
        return inputThread;
    }
}
