package com.br.youtubecrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LikedVideos {

	private static List<String> likedVideos;

	public static List<String> getLikedVideos(String url) {
		likedVideos = new ArrayList<String>();
		try {
			Document document = ConnectionFactory.getConnection(url);
			if (document != null) {
				Elements ul = document.select("ul#browse-items-primary li a[href]");
				ul.forEach(li -> {
					if (li.select("a[href]") != null && !li.select("a[href]").isEmpty())
						likedVideos.add(li.select("a[href]").get(0).absUrl("href"));

				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return likedVideos;
	}
}
