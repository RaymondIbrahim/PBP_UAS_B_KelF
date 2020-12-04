package com.tubesb.tubespbp.BackEndMobil;

import java.io.Serializable;

public class Mobil implements Serializable {
    private int id;
    private String nama_mobil, jenis_transmisi, imageURL, harga, jumlah_seat;

    public String getJumlah_seat() {
        return jumlah_seat;
    }

    public Mobil (int id, String nama_mobil, String jenis_transmisi, String harga, String jumlah_seat, String gambar) {
        this.id = id;
        this.nama_mobil = nama_mobil;
        this.jenis_transmisi = jenis_transmisi;
        this.harga = harga;
        this.jumlah_seat = jumlah_seat;
        this.imageURL = gambar;
    }

    public Mobil (String nama_mobil, String jenis_transmisi, String harga, String jumlah_seat, String gambar) {
        this.nama_mobil = nama_mobil;
        this.jenis_transmisi = jenis_transmisi;
        this.harga = harga;
        this.jumlah_seat = jumlah_seat;
        this.imageURL = gambar;
    }

    public int getId() {
        return id;
    }

    public String getNama_mobil() {
        return nama_mobil;
    }

    public String getJenis_transmisi() {
        return jenis_transmisi;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getHarga() {
        return harga;
    }

}
