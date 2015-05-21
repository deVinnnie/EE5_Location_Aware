package com.EE5.client;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.EE5.server.socketTask.SocketTaskType;
import com.EE5.util.ConnectionException;

import java.io.IOException;

/**
 * Manages the connection to and from the server.
 * The Client runs on a separate AsyncTask (provided by Android)
 * which is similar to a BackgroundWorker in .NET.
 * This is a convenient way (read: wrapper) to deal with threads.
 */
public abstract class Client extends AsyncTask<Void, String, Void> {
    private AbstractClientThread clientInputThread;
    private AbstractClientThread clientOutputThread;

    private final Object mutex;
    private ArrayAdapter historyAdapter;

    private SocketTaskType socketTaskType;
    private Context context;

    /**
     * Place to store an exception should an error occur during the lifetime of the Client instance.
     * This variable is checked and handled in {@link #onPostExecute(Void aVoid)}
     */
    private Exception exception;

    private boolean isConnected = false;

    public Client(Object mutex, ArrayAdapter<String> historyAdapter, SocketTaskType socketTaskType,Context context){
        this.mutex = mutex;
        this.historyAdapter = historyAdapter;
        this.socketTaskType = socketTaskType;
        this.context = context;
    }

    /**
     * Initiates the connection.
     * @return True when connection successful, false when failed.
     * @throws IOException
     */
    public abstract boolean start() throws ConnectionException;

    public void quit(){
        Log.i("Client", "Stopping Client...");
        this.getClientInputThread().stopRunning();
        this.getClientOutputThread().stopRunning();
    }

    public void update(String string){
        this.publishProgress(string);
    }

    /**Methods Of Async Task*/
    //<editor-fold desc="Async Task">
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            this.isConnected = this.start();
            Log.i("Connection", "[ BUSY ] Preparing to release lock.");
            //The notify must be done _AFTER_ 'isConnected' is assigned.
            synchronized (this.getMutex()) {
                this.getMutex().notify();
                Log.i("Connection","[ OK ] Released lock");
            }
        } catch (ConnectionException e) {
            this.exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(this.exception != null) {
            this.exception.printStackTrace();
            Toast.makeText(this.context, "Client End:"+this.exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        historyAdapter.insert(values[0], 0);
        historyAdapter.notifyDataSetChanged();
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public Object getMutex(){
        return this.mutex;
    }

    public void setClientInputThread(AbstractClientThread clientInputThread) {
        this.clientInputThread = clientInputThread;
    }

    public void setClientOutputThread(AbstractClientThread clientOutputThread) {
        this.clientOutputThread = clientOutputThread;
    }

    public SocketTaskType getSocketTaskType() {
        return socketTaskType;
    }

    public ArrayAdapter getHistoryAdapter(){
        return this.historyAdapter;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public AbstractClientThread getClientInputThread() {
        return this.clientInputThread;
    }
    public AbstractClientThread getClientOutputThread() {
        return this.clientOutputThread;
    }
    //</editor-fold>
}
