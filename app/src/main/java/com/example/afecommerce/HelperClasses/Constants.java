package com.example.afecommerce.HelperClasses;

public class Constants {

    public static final String KEY_USER = "user";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PREFERENCE_NAME = "groceryListPreference";

    public static String API_BASE_URL = "https://ecommerce.hashbitstudio.com";
    public static String GET_CATEGORIES_URL = API_BASE_URL + "/services/listCategory";
    public static String GET_PRODUCTS_URL = API_BASE_URL + "/services/listProduct";
    public static String GET_OFFERS_URL = API_BASE_URL + "/services/listFeaturedNews";
    public static String GET_PRODUCT_DETAILS_URL = API_BASE_URL + "/services/getProductDetails?id=";
    public static String POST_ORDER_URL = API_BASE_URL + "/services/submitProductOrder";
    public static String PAYMENT_URL = API_BASE_URL + "/services/paymentPage?code=";

    public static String NEWS_IMAGE_URL = API_BASE_URL + "/uploads/news/";
    public static String CATEGORIES_IMAGE_URL = API_BASE_URL + "/uploads/category/";
    public static String PRODUCTS_IMAGE_URL = API_BASE_URL + "/uploads/product/";


    public static final String PRODUCT_TITLE = "title";
    public static final String PRODUCT_IMAGE = "image";
    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_CATEGORY = "category";
    public static final String PRODUCT_SUBCATEGORY = "subcategory";
    public static final String PRODUCT_TYPE = "type";
    public static final String PRODUCT_USAGE = "usage";
    public static final String PRODUCT_GENDER = "gender";
    public static final String PRODUCT_COLOR = "color";
    public static final String PRODUCT_DATE_ADDED = "dateAdded";


}
