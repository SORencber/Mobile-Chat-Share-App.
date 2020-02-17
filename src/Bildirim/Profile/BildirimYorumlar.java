package com.yavuz.rencber.rencber.Bildirim.Profile;

import java.io.Serializable;

/**
 * Created by Serhat Ömer RENÇBER on 2018.
 */
public class BildirimYorumlar implements Serializable {
    String id, name, title, thumbnailUrl, lastMessage, timestamp,foto,to_user_id,cinsiyet,email,grup,sahibi,resim_no;
    int unreadCount;

    public BildirimYorumlar() {
    }

    public BildirimYorumlar(String id, String name, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
        this.foto=foto;
        this.to_user_id=to_user_id;
        this.cinsiyet=cinsiyet;
        this.email=email;
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.resim_no = resim_no;
        this.sahibi=sahibi;
        this.grup=grup;
        this.thumbnailUrl = thumbnailUrl;

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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
    public void setResim_no(String resim_no) {
        this.resim_no = resim_no;
    }

    public String getResim_no() {  return resim_no;    }
}
