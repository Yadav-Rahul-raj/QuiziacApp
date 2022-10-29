package com.example.quiziac;

public class FeatureModel {
    private String catId,FeaturesId, categoryNameFeatures,categoryImageFeatures;



    public FeatureModel(String featuresId, String categoryNameFeatures, String categoryImageFeatures, String catId) {
        FeaturesId = featuresId;
        this.categoryImageFeatures = categoryImageFeatures;
        this.categoryNameFeatures = categoryNameFeatures;
        this.catId = catId;
    }

    public FeatureModel(){

    }

    public String getFeaturesId() {
        return FeaturesId;
    }

    public void setFeaturesId(String featuresId) {
        FeaturesId = featuresId;
    }

    public String getCategoryImageFeatures() {
        return categoryImageFeatures;
    }

    public void setCategoryImageFeatures(String categoryImageFeatures) {
        this.categoryImageFeatures = categoryImageFeatures;
    }

    public String getCategoryNameFeatures() {
        return categoryNameFeatures;
    }

    public void setCategoryNameFeatures(String categoryNameFeatures) {
        this.categoryNameFeatures = categoryNameFeatures;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }
}
