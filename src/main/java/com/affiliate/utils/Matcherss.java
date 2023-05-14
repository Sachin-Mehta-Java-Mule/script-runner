package com.affiliate.utils;

/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matcherss {
	private static final String LINK_REGEX = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,2083}\\.){1,4}([a-zA-Z]){2,6}(\\/(([a-zA-Z-_\\/\\.0-9#:?=&;,]){0,2083})?){0,2083}?[^ \\n]*)";
	private static final String TEXT_WITH_LINKS_EXAMPLE = "link1:http://example.com link2: https://example.com.ua link3 www.example.ua\n"
			+ "link4- https://stackoverflow.com/questions/5713558/detect-and-extract-url-from-a-string\n"
			+ "link5 https://www.google.com/search?q=how+to+extract+link+from+text+java+example&rlz=1C1GCEU_en-GBUA932UA932&oq=how+to+extract+link+from+text+java+example&aqs=chrome..69i57j33i22i29i30.15020j0j7&sourceid=chrome&ie=UTF-8";

	public static void main(String[] args) throws Exception {
		// Pull all links from the body for easy retrieval
		String message = " Dr Trust Digital Weight Machine @ 699\r\n" + "\r\n" + "https://amzn.to/3le2QkY\r\n" + "\r\n"
				+ "**Flipkart Selling for Rs.999\r\n" + "\r\n" + " Amazon Fresh : New Stock Deals Starts@ 1\r\n"
				+ "\r\n" + "https://amzn.to/3Lv12Pv\r\n" + "\r\n" + "More Fresh Deals :\r\n"
				+ "https://t.me/nonstopdeals/259528\r\n" + "\r\n"
				+ " SIZE L,XL : Fabme Women's Panties (Pack of 6) @207\r\n" + "\r\n" + "https://amzn.to/39nEnHg\r\n"
				+ "\r\n" + " Back: Nivea Oil Control Face Wash, 100ml (Pack of 3) @287\r\n" + "\r\n"
				+ " https://amzn.to/39tXY8Y\r\n" + "\r\n"
				+ "  Amazon Collect offer: Ola Cabs Get 50% back up to  Rs 100 ( min  order value  Rs 49)\r\n" + "\r\n"
				+ "https://amzn.to/3sEFych";
//		Deals deal = new Deals(null, EncryptUtils.encryptThisString(message), message, new Timestamp(new Date().getTime()));
//		System.out.println(DBUtils.insertDeals(deal));
		System.out.println(pullLinks(message));
//		System.out.println(extractUrls(message));
	}

	static ArrayList<String> ObseletepullLinks(String text) {
		ArrayList<String> links = new ArrayList<>();
		Pattern p = Pattern.compile(LINK_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		while (m.find()) {
			links.add(m.group());
		}
		return links;
	}

	public static ArrayList<String> pullLinks(String text) {
		ArrayList<String> containedUrls = new ArrayList<String>();
		String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
		Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = pattern.matcher(text);

		while (urlMatcher.find()) {
			containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
		}

		return containedUrls;
	}
}
