package com.example.gim;

public class Medicament {
    private int id;
    private String nom;
    private String formePharmaceutique;
    private String fabricant;

    public Medicament(String nom, String formePharmaceutique) {
        this.nom = nom;
        this.formePharmaceutique = formePharmaceutique;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getFormePharmaceutique() {
        return formePharmaceutique;
    }

    public void setFormePharmaceutique(String formePharmaceutique) {
        this.formePharmaceutique = formePharmaceutique;
    }

    public String getFabricant() {
        return fabricant;
    }

    public void setFabricant(String fabricant) {
        this.fabricant = fabricant;
    }
}
