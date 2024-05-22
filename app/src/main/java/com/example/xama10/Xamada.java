package com.example.xama10;

public class Xamada {

    private long idClient;
    private int  table;

    public Xamada(long id, int table) {
        super();
        this.idClient = id;
        this.table = table;
    }

    public long getId() {
        return idClient;
    }
    public void setId(long id) {
        this.idClient = id;
    }
    public int getTable() {
        return table;
    }
    public void setTable(int table) {
        this.table = table;
    }
}