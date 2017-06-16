package com.br.youtubecrawler;

import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.Document;

public class ConnectionFactory {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

	public static Document getConnection(String url) throws IOException {
		Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
		Document documento = connection.get();
		return connection.response().statusCode() == 200 ? documento : null;
	}

}
