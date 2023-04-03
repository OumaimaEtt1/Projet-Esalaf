package com.exemple.model;

public class Client {
    private String Nom;
    private String Telephone;
    private Float Montant_credit;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    private int Id;

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public Float getMontant_credit() {
        return Montant_credit;
    }

    public void setMontant_credit(Float montant_credit) {
        Montant_credit = montant_credit;
    }

    public Client(String nom, String telephone, Float montant_credit, int id) {
        Nom = nom;
        Telephone = telephone;
        Montant_credit = montant_credit;
        Id = id;
    }
}
