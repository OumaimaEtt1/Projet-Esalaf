package com.exemple.model;

public class Credit {
    private int ID_credit;
    private Float montant_recu;
    private Float montant_restant;

    private String Nom;
    private Float montant_credit;

    public Credit(int ID_credit, Float montant_recu, Float montant_restant, String nom) {
        this.ID_credit = ID_credit;
        this.montant_recu = montant_recu;
        this.montant_restant = montant_restant;
        Nom = nom;
    }

    public Credit() {
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public Float getMontant_credit() {
        return montant_credit;
    }

    public void setMontant_credit(Float montant_credit) {
        this.montant_credit = montant_credit;
    }

    public int getID_credit() {
        return ID_credit;
    }

    public void setID_credit(int ID_credit) {
        this.ID_credit = ID_credit;
    }

    public Float getMontant_recu() {
        return montant_recu;
    }

    public void setMontant_recu(Float montant_recu) {
        this.montant_recu = montant_recu;
    }

    public Float getMontant_restant() {
        return montant_restant;
    }

    public void setMontant_restant(Float montant_restant) {
        this.montant_restant = montant_restant;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "ID_credit=" + ID_credit +
                ", montant_recu=" + montant_recu +
                ", montant_restant=" + montant_restant +
                ", Nom='" + Nom + '\'' +
                ", montant_credit=" + montant_credit +
                '}';
    }
}
