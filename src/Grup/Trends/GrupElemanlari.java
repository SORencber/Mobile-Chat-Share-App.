package com.yavuz.rencber.rencber.Grup.Trends;

import java.io.Serializable;

/**
 * Created by Serhat Ömer RENÇBER on 2018.
 */
public class GrupElemanlari implements Serializable {
    String id, name, lastMessage, timestamp,foto,to_user_id,cinsiyet,email;
    int unreadCount;

    public GrupElemanlari() {
    }

    public GrupElemanlari(String email,String id, String name, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
        this.foto=foto;
        this.to_user_id=to_user_id;
        this.cinsiyet=cinsiyet;
        this.email=email;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }
    public String getTo_user_id() {
        return to_user_id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }
    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
