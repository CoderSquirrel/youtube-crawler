package com.br.youtubecrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class FollowedAccounts {

	private static List<String> followedAccounts;

	public static List<String> getFollowedAccounts(String url) {
		followedAccounts = new ArrayList<String>();
		try {
			Document document = ConnectionFactory.getConnection(url);
			if (document != null) {
				Elements ul = document.select("ul#browse-items-primary li a[href]");
				ul.forEach(li -> {
					if (li.select("a[href]") != null && !li.select("a[href]").isEmpty())
						followedAccounts.add(li.select("a[href]").get(0).absUrl("href"));

				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return followedAccounts;
	}
}
