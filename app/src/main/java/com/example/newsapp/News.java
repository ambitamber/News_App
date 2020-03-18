package com.example.newsapp;

public class News {
    private String newsTitle;
    private String newsSection;
    private String updatedDate;
    private String imageURL;
    private String newsURL;
    private String descriptionText;

    public News(String mNewsTitle,String mNewsSection,String mUpdatedDate,String mImageURL,String mNewsURL,String mDescriptionText){
        newsTitle=mNewsTitle;
        newsSection=mNewsSection;
        updatedDate=mUpdatedDate;
        imageURL=mImageURL;
        newsURL=mNewsURL;
        descriptionText=mDescriptionText;
    }
    public String getNewsTitle(){
        return newsTitle;
    }
    public String getNewsSection(){
        return newsSection;
    }
    public String getUpdatedDate(){
        return updatedDate;
    }
    public String getImageURL(){
        return imageURL;
    }
    public String getNewsURL(){
        return newsURL;
    }
    public String getDescriptionText(){
        return descriptionText;
    }
}
