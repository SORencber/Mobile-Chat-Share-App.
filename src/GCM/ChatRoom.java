package com.yavuz.rencber.rencber.GCM;

import java.io.Serializable;

/**
 * Created by Serhat Ömer RENÇBER on 2018.
 */
public class ChatRoom implements Serializable {
    String id, name,grup, lastMessage, timestamp,foto,to_user_id,cinsiyet,email,sahibi,longitude,latitude,mesafe;
    int unreadCount;

    public ChatRoom() {
    }

    public ChatRoom(String id, String name, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
        this.latitude=latitude;
        this.longitude=longitude;
        this.foto=foto;
        this.to_user_id=to_user_id;
        this.cinsiyet=cinsiyet;
        this.email=email;
this.mesafe=mesafe;
        this.sahibi=sahibi;
        this.grup=grup;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getMesafe() {
        return mesafe;
    }

    public void setMesafe(String mesafe) {
        this.mesafe = mesafe;
    }

    public String getLatitude() {
        return latitude;
    }

    public String setLatitude(String latitude) {
        this.latitude = latitude;
        return latitude;
    }


    public String getLongitude() {
        return longitude;
    }

    public String setLongitude(String longitude) {
        this.longitude = longitude;
        return longitude;
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
