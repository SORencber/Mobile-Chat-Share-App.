package com.yavuz.rencber.rencber.Bildirim.Profile;
////////////////////////////////////////////////
//
//   Created by Serhat Ömer RENÇBER   2018
//
////////////////////////////////////////////////
import java.io.Serializable;
import java.util.ArrayList;


public class BildirimResimler implements Serializable {
    private String title, thumbnailUrl,mesaj,username,Eposta,diL,adi,soyadi;
    private int year;
    private int asagi,yukari,resim_no,yorumsayisi;
    private double rating;
    private ArrayList<String> genre;
    private int Sil;


    public BildirimResimler() {
    }

    public BildirimResimler(String name, int resim_no, int asagi, int yukari, String thumbnailUrl, int year, double rating,
                            ArrayList<String> genre) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.yukari = yukari;
        this.asagi = asagi;
        this.yorumsayisi = yorumsayisi;
        this.resim_no = resim_no;
        this.rating = rating;
        this.adi = adi;
        this.soyadi = soyadi;

        this.Sil = Sil;
        this.Eposta=Eposta;
        this.username=username;
        this.diL=diL;

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

    public String getDiL() {
        return diL;
    }

    public void setDiL(String diL) {
        this.diL = diL;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYukari() {  return yukari;    }

    public void setYukari(int yukari) {  this.yukari = yukari;  }

    public int getAsagi() {  return asagi;    }

    public void setAsagi(int asagi) {
        this.asagi = asagi;
    }
    public int getYorumsayisi() {  return yorumsayisi;    }

    public void setYorumsayisi(int yorumsayisi) {  this.yorumsayisi = yorumsayisi;  }
    public void setResim_no(int resim_no) {
        this.resim_no = resim_no;
    }

    public int getResim_no() {  return resim_no;    }
    public void setSil(int Sil) {
        this.Sil = Sil;
    }

    public int getSil() {  return Sil;    }





    //list yorum icin xml degerleri hazirlaniyoru
    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }
    public String getMesaj() { return mesaj;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEposta() {
        return Eposta;
    }
    public void setEposta(String Eposta) {
        this.Eposta = Eposta;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public void setAdi(String adi) {
        this.adi = adi;
    }
    public String getAdi() {
        return adi;
    }
    public void setSoyadi(String soyadi) {
        this.soyadi = soyadi;
    }
    public String getSoyadi() {
        return soyadi;
    }
    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

}