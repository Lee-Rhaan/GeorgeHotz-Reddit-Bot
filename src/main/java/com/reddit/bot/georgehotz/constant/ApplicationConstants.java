package com.reddit.bot.georgehotz.constant;

/**
 * Contains the application specific constants
 */
public class ApplicationConstants {

    //Database Configuration Constants
    public static final String DB_URL = "jdbc:mysql://localhost:3306/geohot_reddit_bot?autoReconnect=true";
    public static final String DB_USER = "provide your database username";
    public static final String DB_PWD = "provide your database password";

    //BOT Information
    public final static String APP_ID = "APP_ID";
    public final static String SECRET = "SECRET";
    public final static String AUTHOR = "AUTHOR";
    public final static String ACCOUNT_PASSWORD = "ACCOUNT_PASSWORD";

    //Additional Helper Values
    public static String NEXT_ARTICLE = "*"; //reddit post title
    public static String PREVIOUS_ARTICLE = "*";
    public static String NEXT_ID = "="; //reddit post id
    public static String PREVIOUS_ID = "=";

    //REDDIT BOT Action URLs
    public final static String REDDIT_ACCESS_TOKEN_URL = "https://www.reddit.com/api/v1/access_token";
    public final static String SUBREDDIT_NEW_POSTS_URL = "https://oauth.reddit.com/r/ProgrammerHumor/hot.json";
    public static String SUBREDDIT_POST_COMMENTS_URL = "https://oauth.reddit.com/r/ProgrammerHumor/comments/=/".concat(NEXT_ARTICLE).concat(".json");
    public static String REDDIT_ADD_COMMENT_URL = "https://oauth.reddit.com/api/comment?api_type=json&text=";

}
