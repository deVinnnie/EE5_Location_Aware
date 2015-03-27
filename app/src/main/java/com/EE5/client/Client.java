package com.EE5.client;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.EE5.server.socketTask.SocketTaskType;
import com.EE5.server.data.Device;

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

    public static Device currentDevice;

    public Client(){
        mutex = null;
    }

    public Client(Object mutex, ArrayAdapter<String> historyAdapter, SocketTaskType socketTaskType){
        this.mutex = mutex;
        this.historyAdapter = historyAdapter;
        this.socketTaskType = socketTaskType;
    }

    public Object getMutex(){
        return this.mutex;
    }

    /**
     *
     * @return True when connection successful, false when failed.
     * @throws IOException
     */
    public abstract boolean start() throws IOException;

    public AbstractClientThread getClientInputThread() {
        return this.clientInputThread;
    }
    public AbstractClientThread getClientOutputThread() {
        return this.clientOutputThread;
    }

    public void setIsStart(boolean isStart) {
        this.getClientInputThread().setKeepRunning(isStart);
        this.getClientOutputThread().setKeepRunning(isStart);
    }

    public void quit(){
        Log.i("Client", "Stopping Client...");
        this.setIsStart(false);
    }

    public void update(String string){
        this.publishProgress(string);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        historyAdapter.insert(values[0], 0);
        historyAdapter.notifyDataSetChanged();
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
}
