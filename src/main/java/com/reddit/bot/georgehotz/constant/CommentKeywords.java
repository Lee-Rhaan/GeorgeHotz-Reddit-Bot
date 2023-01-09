package com.reddit.bot.georgehotz.constant;

/**
 * Contains the predefined replies to each keyword
 */
public enum CommentKeywords {
    KEYWORD_1("geohot", "The Conor Mcgregor of Internships"),
    KEYWORD_2("elon", "free neuralink implants for all the twitter devs yay!!"),
    KEYWORD_3("twitter", "How to center twitter divs site:stackoverflow.com"),
    KEYWORD_4("intern", "We interns should unite and start our own internship program on stack overflow"),
    KEYWORD_5("elon musk", "Eloneth Musketh or whatever you call him should buy Reddit next, then charge an $8 monthly fee to all the subreddit moderators"),
    KEYWORD_6("george hotz", "The man the myth, The Mythical Man Moth"),
    KEYWORD_7("open source", "What is this open source thingy you're talking about?"),
    KEYWORD_8("12 week", "I can rewrite your whole codebase in 12 weeks"),
    KEYWORD_9("side project", "Open source your side project and licence it under MIT"),
    KEYWORD_10("hack", "So are you a hacker? Tell me more about how you want to hack this hackable thingy"),
    KEYWORD_11("playstation", "PTSD"),
    KEYWORD_12("fired", "I don't always get fired, but when I do, I am escorted out by a Scrum Master"),
    KEYWORD_13("connection", "2 days ago I named my wifi to Hack it if you can and yesterday it was changed to Challenge accepted"),
    KEYWORD_14("github", "I don't watch github repos, I fork them"),
    KEYWORD_15("stackoverflow", "duplicate, question closed"),
    KEYWORD_16("scrum", "Tell me more about what a great scrum master you are"),
    KEYWORD_17("bot", "We don't get twitter merch, we get reddit bots"),
    KEYWORD_18("bug", "I'm feeling a little buggy, it must be my failed test cases"),
    KEYWORD_19("money", "Upvotes is my preferred currency"),
    KEYWORD_20("company", "Beautiful day, huh? Meeting at 5"),
    KEYWORD_21("sprint", "We'll ask for estimates and then treat them as deadlines"),
    KEYWORD_22("wfh", "Asked my boss if I could WFH this friday, he said he'll be back on monday to discuss it further");

    private final String keyword;
    private final String reply;

    CommentKeywords(String keyword, String reply) {
        this.keyword = keyword;
        this.reply = reply;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getReply() {
        return reply;
    }
}
