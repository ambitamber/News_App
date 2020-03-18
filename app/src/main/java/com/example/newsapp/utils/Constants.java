package com.example.newsapp.utils;

public class Constants {

    private Constants() {
    }
    /** URL for news data from the guardian data set */
    public static final String NEWS_REQUEST_URL = "http://newsapi.org/v2";
    /** URL Query Parameters */
    public static final String EVERYTHING = "everything";
    public static final String HEADLINE = "top-headlines";
    public static final String QUERY_PARAM = "q";
    public static final String ORDER_BY_PARAM = "sort-by";
    public static final String PAGE_SIZE_PARAM = "pagesize";
    public static final String TO_DATE_PARAM = "to";
    public static final String FROM_DATE_PARAM = "from";
    public static final String API_KEY_PARAM = "apikey";
    public static final String SECTION_PARAM = "section";
    /** API Key */
    public static final String APIKEY = "5b5ab3f933184289a68cae008cd352d1"; // Use your API Key when API rate limit exceeded
    /** Constants value for each fragment */
    public static final int HOME = 0;
    public static final int WORLD = 1;
    public static final int SCIENCE = 2;
    public static final int SPORT = 3;
    public static final int ENVIRONMENT = 4;
    public static final int SOCIETY = 5;
    public static final int FASHION = 6;
    public static final int BUSINESS = 7;
    public static final int CULTURE = 8;
}
