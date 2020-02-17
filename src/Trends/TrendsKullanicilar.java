package com.yavuz.rencber.rencber.Trends;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class TrendsKullanicilar implements Serializable {
    String id, name, lastMessage, timestamp,foto,to_user_id,cinsiyet,email,grup,sahibi;;
    int unreadCount;

    public TrendsKullanicilar() {
    }

    public TrendsKullanicilar(String id, String name, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
        this.foto=foto;
        this.to_user_id=to_user_id;
        this.cinsiyet=cinsiyet;
        this.email=email;

        this.sahibi=sahibi;
        this.grup=grup;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getGrup() {
        return grup;
    }
    public void setGrup(String grup) {
        this.grup = grup;
    }

    public String getSahibi() {
        return sahibi;
    }
    public void setSahibi(String sahibi) {
        this.sahibi = sahibi;
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
