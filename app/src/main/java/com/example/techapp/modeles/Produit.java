package com.example.techapp.modeles;

public class Produit {
    private String imageurl;
    private String name,brand,description,type;

    int price,quantite;

    public Produit() {

    }

    public Produit( String imageurl, String name,String brand, int price) {
        this.imageurl = imageurl;
        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    public Produit( String imageurl, String name,String brand) {
        this.imageurl = imageurl;
        this.name = name;
        this.brand = brand;

    }

    public Produit(String imageurl, String name, String brand, String description, int price,String type) {
        this.imageurl = imageurl;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.type=type;
    }


    public Produit(String imageurl, String name, String brand, String description, String type, int price, int quantite) {
        this.imageurl = imageurl;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.type = type;
        this.price = price;
        this.quantite = quantite;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
