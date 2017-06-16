package com.br.youtubecrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.br.crawler.domain.Video;
import com.br.crawler.youtubeAPI.CommentAPI;
import com.br.crawler.youtubeAPI.SearchAPI;

public class Search {
	private String search;
	private List<Video> pagesToVisit;
	private SearchAPI api;
	private CommentAPI commentHandling;

	public Search(String search) {
		this.search = search;
		pagesToVisit = new ArrayList<Video>();
		api = new SearchAPI();
		commentHandling = new CommentAPI();
	}

	public List<Video> doSearch() {
		List<Video> videos = null;
		try {
			videos = api.doSearch(search, Long.valueOf("1"));
			System.out.print("Gathering further info");
			videos.forEach(video -> completeObject(video));
			System.out.println("\nDone");
			// videos.forEach(video -> {
			// System.out.println("Video " + video.getTitle() + "\tPossui: " +
			// video.getViews() + ", "
			// + video.getLikes() + " likes, " + video.getDislikes() + "
			// deslikes e "
			// + video.getComments().size() + " comentarios");
			// System.out.println("\tComentarios: ");
			// video.getComments().forEach(comentario -> {
			// System.out.println("\tPor " + comentario.getAuthor() + ", canal:
			// " + comentario.getChannel());
			// System.out.println("\t Comentario " + comentario.getComment());
			// });
			// });

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return videos;
	}

	private void completeObject(Video video) {
		System.out.print(".");
		Document htmlDocument;
		try {
			htmlDocument = ConnectionFactory.getConnection(video.getUrl());
			if (htmlDocument != null) {
				Elements keywords = htmlDocument.select("meta[name=keywords]");
				Elements title = htmlDocument.select("meta[property=og:title]");
				Elements id = htmlDocument.select("meta[itemprop=videoId]");
				Elements viewCount = htmlDocument.getElementsByClass("watch-view-count");
				Elements like = htmlDocument.select("button.like-button-renderer-like-button").select("span");
				Elements dislike = htmlDocument.select("button.like-button-renderer-dislike-button").select("span");
				video.setTitle(title.get(0).attr("content"));
				video.setTags(keywords.get(0).attr("content").split(","));
				video.setViews(viewCount.text());
				video.setDislikes(dislike.text());
				video.setLikes(like.text());
				video.setComments(commentHandling.getComments(video.getId()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
