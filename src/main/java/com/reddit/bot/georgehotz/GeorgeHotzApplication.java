package com.reddit.bot.georgehotz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reddit.bot.georgehotz.constant.CommentKeywords;
import com.reddit.bot.georgehotz.persistence.BotRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static com.reddit.bot.georgehotz.constant.ApplicationConstants.*;

public class GeorgeHotzApplication {

	private static ObjectMapper mapper = new ObjectMapper();

	//Retrieving Sensitive Bot MetaData
	private final static String author = BotRepository.getInstance().retrieveBotInformation(AUTHOR);
	private final static String appId = BotRepository.getInstance().retrieveBotInformation(APP_ID);
	private final static String secret = BotRepository.getInstance().retrieveBotInformation(SECRET);
	private final static String accountPassword = BotRepository.getInstance().retrieveBotInformation(ACCOUNT_PASSWORD);

	public static void main(String[] args) throws Exception {
		String accessToken = testIfAccessTokenIsValid(retrieveAccessToken());
		retrieveHotPosts(accessToken);
	}

	/**
	 * Retrieves Reddit Access Token in order to be able to communicate with the Reddit API (token-based authentication)
	 *
	 * @return reddit access token
	 */
	private static String retrieveAccessToken() {
		String request = "grant_type=password&username=" + author + "&password=" + accountPassword;
		String botDetails = appId + ":" + secret;
		Process p = null;
		String accessToken = null;

		try {
			ProcessBuilder pb = new ProcessBuilder("curl", "-X", "POST", "-d", request, "--user", botDetails, REDDIT_ACCESS_TOKEN_URL);
			p = pb.start();

			String line;
			while ((line = retrieveOutput(p.getInputStream())) != null) {
				//extracting access token from reddit response
				accessToken = line.substring(18, line.indexOf(",") -1);
			}

			p.waitFor();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return accessToken;
	}

	/**
	 * Retrieves the "HOT" posts from the r/ProgrammerHumour subreddit
	 *
	 * @param token used as a credential when it calls the target API
	 */
	private static void retrieveHotPosts(String token) {
		String username = "Retrieving new posts from subreddit/0.1 by ".concat(author);
		Process p = null;

		try {
			ProcessBuilder pb = new ProcessBuilder("curl", "-H", createAuthHeader(token), "-A", username, SUBREDDIT_NEW_POSTS_URL);
			p = pb.start();

			String line;
			while ((line = retrieveOutput(p.getInputStream())) != null) {
				//Extracting Reddit Posts Metadata
				Map post = mapper.readValue(line, Map.class);
				Map data = (Map) post.get("data");
				List<Map> children = (List<Map>) data.get("children");
				Map dataObject = Collections.EMPTY_MAP;
				//retrieving the id and title of each new post in subreddit
				for (int i = 0; i < children.size(); i++) {
					dataObject = (Map) children.get(i).get("data");
					NEXT_ID = dataObject.get("id").toString();
					NEXT_ARTICLE = dataObject.get("title").toString().replace(" ", "+");
					//manipulating post comments url with the different post id's & titles
					SUBREDDIT_POST_COMMENTS_URL = SUBREDDIT_POST_COMMENTS_URL.replace(PREVIOUS_ID, NEXT_ID)
							.replace(PREVIOUS_ARTICLE, NEXT_ARTICLE);
					//Retrieving Reddit Post Comment Section by using the Post ID & Title "Article" as a reference
					retrievePostComments(testIfAccessTokenIsValid(retrieveAccessToken()), SUBREDDIT_POST_COMMENTS_URL);
					//updating post id & title
					PREVIOUS_ID = NEXT_ID;
					PREVIOUS_ARTICLE = NEXT_ARTICLE;
				}
			}

			p.waitFor();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Retrieving the First & Second Layer comments from each "HOT" post in the r/ProgrammerHumour subreddit
	 *
	 * @param token used as a credential when it calls the target API
	 * @param postCommentsURL reddit api endpoint url for retrieving post comments
	 */
	private static void retrievePostComments(String token, String postCommentsURL) {
		String username = "Retrieving Post Comments/0.1 by ".concat(author);
		Process p = null;

		try {
			ProcessBuilder pb = new ProcessBuilder("curl", "-H", createAuthHeader(token), "-A", username, postCommentsURL);
			p = pb.start();

			String line;
			while ((line = retrieveOutput(p.getInputStream())) != null) {
				String id = null;
				String body = null;
				//Extracting the first layer of comments
				List<Map> comments = mapper.readValue(line, List.class);
				Map post = comments.get(1);
				Map data = (Map) post.get("data");
				List<Map> children = (List<Map>) data.get("children");
				Map dataObject = Collections.EMPTY_MAP;
				//Retrieving comment as well as the comment ID
				for (int i = 0; i < children.size(); i++) {
					dataObject = (Map) children.get(i).get("data");
					id = dataObject.get("name").toString();
					body = dataObject.get("body").toString();
					extractCommentMetaData(id, body);

					//try to extract the second layer as well (comments under the first comment)
					if (dataObject.containsKey("replies")) {
						List<Map> repliesChildren = retrieveCommentChildren(dataObject);
						Map repliesDataObject = Collections.EMPTY_MAP;
						//Retrieving comment as well as the comment ID
						for (int j = 0; j < repliesChildren.size(); j++) {
							repliesDataObject = (Map) repliesChildren.get(j).get("data");
							id = repliesDataObject.get("name").toString();
							body = repliesDataObject.get("body").toString();
							extractCommentMetaData(id, body);
						}
					}
				}
			}

			p.waitFor();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Posting a predefined reply if the reddit comment possess any of the required keywords
	 *
	 * @param token used as a credential when it calls the target API
	 * @param commentId the unique id of the comment the bot is replying to
	 * @param commentReply the comment the bot is replying with
	 */
	private static void addComment(String token, String commentId, String commentReply) {
		String username = "Adding a Comment/0.1 by ".concat(author);
		Process p = null;
		String url = REDDIT_ADD_COMMENT_URL.concat(commentReply + "&thing_id=").concat(commentId);

		try {
			ProcessBuilder pb = new ProcessBuilder("curl","-X", "POST", "-H", createAuthHeader(token), "-A", username, url);
			p = pb.start();

			String line;
			while ((line = retrieveOutput(p.getInputStream())) != null) {
				//Retrieving reply MetaData
				Map fullReplyData = mapper.readValue(line, Map.class);
				Map json = (Map) fullReplyData.get("json");
				Map outerData = (Map) json.get("data");
				List<Map> things = (List<Map>) outerData.get("things");
				Map replyData = things.get(0);
				Map innerData = (Map) replyData.get("data");
				//Printing reply MetaData to Console
				System.out.println("=============================================================================");
				System.out.println("SubReddit:");
				System.out.println(innerData.get("subreddit_name_prefixed"));
				System.out.println("Replying To User With Reddit ID:");
				System.out.println(innerData.get("id"));
				System.out.println("Account That Is Replying:");
				System.out.println(innerData.get("author"));
				System.out.println("Link to Comment Section:");
				System.out.println("https://www.reddit.com" + innerData.get("permalink"));
				System.out.println("Bot Reply Comment: ");
				System.out.println(innerData.get("body"));
				System.out.println("=============================================================================");
			}

			p.waitFor();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * The request for an access token from the reddit api returns an error in some cases.
	 * This method retries the call for an access token if the retrieval of an access token results in an error.
	 *
	 * @param accessToken used as a credential when it calls the target API
	 * @return reddit access token
	 * @throws Exception
	 */
	private static String testIfAccessTokenIsValid(String accessToken) throws Exception {
		//whenever the retrieval of an access token fails, the reddit api returns a constant error with the same values in
		//the error response
		while (accessToken.contains("any Requests")) {
			accessToken = retrieveAccessToken();
		}
		return accessToken;
	}

	/**
	 * Checks if the comment possess any of the required keywords.
	 * Then saving the comment id in a db table if it does not yet exist in the table.
	 *
	 * The reason I'm doing this is for in case this bot is redeployed, it ensures that it does not reply to a comment it
	 * already posted a reply to.
	 *
	 * @param commentId the unique id of the comment the user posted
	 * @param commentBody the comment the user posted
	 * @throws Exception
	 */
	private static void testCommentKeywords(String commentId, String commentBody) throws Exception {
		for (CommentKeywords keyword : CommentKeywords.values()) {
			if (commentBody.contains(keyword.getKeyword())) {
				//Store commentId in DB, then whenever a new comment is about to be added, the bot checks in the DB if that commentId
				//exists, if it exists, it means that the bot already replied to this comment and should not do it again
				Optional<String> commentIdCheck = BotRepository.getInstance().checkIfCommentIdExist(commentId);

				if (commentIdCheck.isPresent()) {
					//If the commentId exists in the comment_tracker db table then exit this method
					return;
				}
				if (!commentIdCheck.isPresent()) {
					//If the commentId does not exist, then add the comment id to the comment_tracker db table
					BotRepository.getInstance().saveCommentId(commentId);
				}
				addComment(testIfAccessTokenIsValid(retrieveAccessToken()), commentId, keyword.getReply().replace(" " , "+"));
			}
		}
	}

	/**
	 * Retrieves required response from the reddit api
	 *
	 * @param inputStream the stream retrieved from the process
	 * @return lines of text (response from reddit api)
	 * @throws IOException
	 */
	private static String retrieveOutput(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		return reader.readLine();
	}

	/**
	 * Creating the auth header to be sent with each request in this token-based authentication api.
	 *
	 * @param accessToken used as a credential when it calls the target API
	 * @return authorization header
	 */
	private static String createAuthHeader(String accessToken) {
		return "Authorization: bearer ".concat(accessToken);
	}

	/**
	 * Extracts a list of all the comments from the reddit post
	 *
	 * @param dataObject contains a list of comments
	 * @return list of comments
	 */
	private static List<Map> retrieveCommentChildren(Map dataObject) {
		Map replies = (Map) dataObject.get("replies");
		Map repliesData = (Map) replies.get("data");
		return (List<Map>) repliesData.get("children");
	}

	/**
	 * Retrieving comment id & body, then printing it to the console before testing if it contains any
	 * keywords
	 *
	 * @param id unique comment identifier
	 * @param body actual comment plain text
	 */
	private static void extractCommentMetaData(String id, String body) throws Exception {
		System.out.println("=============================================================================");
		System.out.println("Comment ID:");
		System.out.println(id);
		System.out.println("Comment:");
		System.out.println(body);
		testCommentKeywords(id, body.toLowerCase());
	}

}
