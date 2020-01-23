import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Tweet {
	private String tweet_date;
	private String tweet_id;
	private String tweet_text;
	private List<String> tweet_list_hashtags;

	public Tweet(JSONObject json) throws JSONException {
		setTweet_date( (String) json.get("created_at"));
		setTweet_id( (String) json.get("id_str"));
		setTweet_text( (String) json.get("text"));
		
		JSONObject entities = (JSONObject) json.get("entities"); 
		JSONArray array_hashtag = (JSONArray) entities.get("hashtags");
		
		tweet_list_hashtags = new ArrayList<String>();
		for(int i = 0; i < array_hashtag.size(); i++) {
			JSONObject hashtag = (JSONObject) array_hashtag.get(i);
			tweet_list_hashtags.add( (String) hashtag.get("text"));
		}
	}

	public String getTweet_date() {
		return tweet_date;
	}

	public void setTweet_date(String tweet_date) {
		this.tweet_date = tweet_date;
	}

	public String getTweet_id() {
		return tweet_id;
	}

	public void setTweet_id(String tweet_id) {
		this.tweet_id = tweet_id;
	}

	public String getTweet_text() {
		return tweet_text;
	}

	public void setTweet_text(String tweet_text) {
		this.tweet_text = tweet_text;
	}

	public List<String> getTweet_list_hashtags() {
		return tweet_list_hashtags;
	}

	public void setTweet_list_hashtags(List<String> tweet_list_hashtags) {
		this.tweet_list_hashtags = tweet_list_hashtags;
	}
	
}
