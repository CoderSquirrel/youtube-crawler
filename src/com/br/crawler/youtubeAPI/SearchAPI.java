package com.br.crawler.youtubeAPI;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.br.crawler.domain.Video;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

public class SearchAPI {
	Properties properties;
	private static YouTube youtube;
	public static final String PROPERTIES_FILENAME = "youtube.properties";
	public static final String APPLICATION_NAME = "youtube-cmdline-search-sample";

	public SearchAPI() {
		properties = getProperties();

		youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
			}
		}).setApplicationName(APPLICATION_NAME).build();
	}

	public List<Video> doSearch(String searchTerm, Long amount) throws IOException {
		System.out.println("Searching videos...");
		YouTube.Search.List search = youtube.search().list("id,snippet");
		String apiKey = properties.getProperty("youtube.apikey");
		search.setKey(apiKey);
		search.setQ(searchTerm);
		search.setType("video");
		search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
		search.setMaxResults(amount);
		SearchListResponse searchResponse = search.execute();
		List<SearchResult> searchResultList = searchResponse.getItems();
		System.out.println("Found " + searchResultList.size() + " videos");
		if (searchResultList != null) {
			return createVideoList(searchResultList.iterator());
		}
		return null;
	}

	private List<Video> createVideoList(Iterator<SearchResult> iteratorSearchResults) {
		System.out.println("Gathering initial info...");
		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" There aren't any results for your query.");
			return null;
		}
		List<Video> videos = new ArrayList<Video>();
		while (iteratorSearchResults.hasNext()) {

			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();
			if (rId.getKind().equals("youtube#video")) {
				videos.add(new Video(rId.getVideoId()));
			}
		}
		return videos;
	}

	private Properties getProperties() {
		Properties properties = new Properties();
		InputStream in = SearchAPI.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}