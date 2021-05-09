package com.example.bakalaurinis_v2.ResponseForms;

public class FoodForm {

    private Integer id;
    private String food;
    private Integer cal;
    private Double carbs;
    private Double proteins;
    private Double fats;

    public FoodForm(Integer id, String food, Integer cal, Double carbs, Double proteins, Double fats) {
        this.id = id;
        this.food = food;
        this.cal = cal;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fats = fats;
    }

    public FoodForm() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public Integer getCal() {
        return cal;
    }

    public void setCal(Integer cal) {
        this.cal = cal;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getProteins() {
        return proteins;
    }

    public void setProteins(Double proteins) {
        this.proteins = proteins;
    }

    public Double getFats() {
        return fats;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }
}
