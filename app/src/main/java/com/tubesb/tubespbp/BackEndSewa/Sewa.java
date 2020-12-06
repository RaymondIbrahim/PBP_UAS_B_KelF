package com.tubesb.tubespbp.BackEndSewa;

import java.io.Serializable;

public class Sewa implements Serializable {
    private int id;
    private String nama, id_penyewa, alamat, pilihan_mobil, tgl_sewa, lama_sewa;

    public Sewa (int id, String nama, String id_penyewa, String alamat, String pilihan_mobil, String tgl_sewa, String lama_sewa) {
        this.id = id;
        this.nama = nama;
        this.id_penyewa = id_penyewa;
        this.alamat = alamat;
        this.pilihan_mobil = pilihan_mobil;
        this.tgl_sewa = tgl_sewa;
        this.lama_sewa = lama_sewa;
    }

    public Sewa (String nama, String id_penyewa, String alamat, String pilihan_mobil, String tgl_sewa, String lama_sewa) {
        this.nama = nama;
        this.id_penyewa = id_penyewa;
        this.alamat = alamat;
        this.pilihan_mobil = pilihan_mobil;
        this.tgl_sewa = tgl_sewa;
        this.lama_sewa = lama_sewa;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getId_penyewa() {
        return id_penyewa;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getPilihan_mobil() {
        return pilihan_mobil;
    }

    public String getTgl_sewa() {
        return tgl_sewa;
    }

    public String getLama_sewa() {
        return lama_sewa;
    }

}
