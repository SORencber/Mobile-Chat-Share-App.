package com.yavuz.rencber.rencber.GCM;

import java.io.Serializable;
/**
 * Created by Serhat Ömer RENÇBER on 2018.
 */
public class Message implements Serializable {
    String id, message, createdAt,image,foto,to_eposta,okundu,ses;
    User user;

    public Message() {
    }

    public Message(String id, String message, String createdAt,String image,String okundu,String ses,String foto,String to_eposta, User user) {
        this.id = id;
        this.message = message;
        this.image = image;
        this.to_eposta=to_eposta;
        this.createdAt = createdAt;
        this.user = user;
        this.foto=foto;
        this.okundu=okundu;
        this.ses=ses;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setOkundu(String okundu) {
        this.okundu = okundu;
    }

    public String getSes() {
        return ses;
    }
    public void setSes(String ses) {
        this.ses = ses;
    }

    public String getOkundu() {
        return okundu;
    }


    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTo_eposta() {
        return to_eposta;
    }

    public void setTo_eposta(String to_eposta) {
        this.to_eposta = to_eposta;
    }

    public String getFoto() {
        return foto;
    }




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
