package com.EE5.server.socketTask;

/**
 * Defines the different behaviours for handling the connection streams.
 *
 * The classic OBJECT type was the first implemented.
 * It uses the ObjectInputStreams/ObjectOutputStreams to serialize an object and transmit it over the network.
 *
 * A second method - PRIMITIVE_DATA - introduces less overhead by using only primitive types.
 * The objects are converted to a sequence of primitive datatypes and transmitted.
 * The receiving end must then recompose the objects.
 *
 * Practical tests show a better performance for the second method.
 */
public enum SocketTaskType {
    OBJECT, PRIMITIVE_DATA
}
