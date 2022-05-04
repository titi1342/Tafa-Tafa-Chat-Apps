package com.ispmimtic.minicrypto.Models;

import com.ispmimtic.minicrypto.AffineCipher;

public class User {

    private String uid;
    private String name;
    private String numeroTel;
    private String photoProfil;


    public User() {
        
    }

    public User(String uid, String name, String numeroTel, String photoProfil) {
        this.uid = uid;
        this.name = AffineCipher.crypterDonnee(name);
        this.numeroTel = numeroTel;
        this.photoProfil = photoProfil;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setame(String name) {
        this.name = name;
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }
}
