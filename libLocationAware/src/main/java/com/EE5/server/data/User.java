package com.EE5.server.data;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private int Id;
    private String User_name;
    private String Password;
    private String Ip;
    private int Port;
    private boolean isOnline;
    private int img;
    private String Operation;

    public User(){}
    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public String getUser_name() {
        return User_name;
    }
    public void setUser_name(String user_name) {
        User_name = user_name;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public String getIp() {
        return Ip;
    }
    public void setIp(String ip) {
        Ip = ip;
    }
    public int getPort() {
        return Port;
    }
    public void setPort(int port) {
        Port = port;
    }
    public boolean isOnline() {
        return isOnline;
    }
    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
    public int getImg() {
        return img;
    }
    public void setImg(int img) {
        this.img = img;
    }
    public String getOperation() {
        return Operation;
    }
    public void setOperation(String operation) {
        Operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User user = (User) o;
            if (user.getId() == Id && user.getIp().equals(Ip)
                    && user.getPort() == Port) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "User [id=" + Id + ", name=" + User_name + ", "
                + ", password=" + Password + ", isOnline=" + isOnline
                + ", img=" + img + ", ip=" + Ip
                + ", port=" + Port + "]";
    }



}
