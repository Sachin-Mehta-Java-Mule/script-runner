package com.affiliate.utils;
import com.affiliate.ScriptRunner;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class URLShortner {
	public static void main(String[] args) throws UnirestException {
		System.out.println(createShortURL("https://google.com"));
	}

	public static String createShortURL(String url) throws UnirestException {
		Unirest.setTimeouts(0, 0);
		HttpResponse<JsonNode> response = Unirest.post("https://api-ssl.bitly.com/v4/shorten")
				.header("Authorization", "Bearer " + ScriptRunner.token).header("Content-Type", "application/json")
				.body("{\n  \"long_url\": \"" + url + "\",\n  \"domain\": \"bit.ly\"\n}").asJson();
		return response.getBody().getObject().getString("link");
	}
}
