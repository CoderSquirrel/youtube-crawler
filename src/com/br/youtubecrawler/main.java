package com.br.youtubecrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.br.crawler.domain.Comment;
import com.br.crawler.domain.Video;
import com.github.axet.vget.VGet;

public class main {

	private static List<Video> pagesToVisit = new ArrayList<Video>();

	public static void main(String[] args) {
		List<String> videoList = new ArrayList<String>();
		List<String> accountsList = new ArrayList<String>();
		String toSearch = "Depois das Onze";
		Search search = new Search(toSearch, 2);
		List<Video> videos = search.doSearch();

		if (videos != null && !videos.isEmpty()) {
			videos.forEach(video -> {
				System.out.println(video.getComments().size());
				if (video.getComments() != null) {
					video.getComments().forEach(comment -> {
						comment.getAutor().setLikedVideos(
								LikedVideos.getLikedVideos(mountLikedURL(comment.getAutor().getChannel())));
						videoList.addAll(comment.getAutor().getLikedVideos());
						comment.getAutor().setFollowedAccounts(FollowedAccounts
								.getFollowedAccounts(mountFollowedURL(comment.getAutor().getChannel())));
						accountsList.addAll(comment.getAutor().getFollowedAccounts());

					});
				}
			});
			createArquivosGerais(toSearch, videoList, accountsList);
			createFiles(videos);
		}

		// accountsList e videoList contem todos os video e contas indicadas
	}

	public static void createArquivosGerais(String search, List<String> videoList, List<String> accountsList) {
		try {
			Writer writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("result/" + search + "-Video.txt"), "utf-8"));
			for (String s : videoList) {
				writer.write(s + "\n");
			}
			writer.close();
			writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("result/" + search + "-Accounts.txt"), "utf-8"));
			for (String s : accountsList) {
				writer.write(s);
			}
			writer.close();

		} catch (

		FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void createFiles(List<Video> videos) {
		Writer writer = null, writer2 = null, comments= null;

		try {
			for (Video video : videos) {
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("result/" + video.getTitle() + "-Videos.txt"), "utf-8"));

				writer2 = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("result/" + video.getTitle() + "-Accounts.txt"), "utf-8"));
				comments = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("result/" + video.getTitle() + "-Comments.txt"), "utf-8"));
				if (video.getComments() != null) {
					for (Comment comment : video.getComments()) {
						for (String s : comment.getAutor().getFollowedAccounts()) {
							writer2.write(s + "\n");
						}

						for (String s : comment.getAutor().getLikedVideos()) {
							writer.write(s + "\n");
						}
						
						comments.write(comment.getComment() + "\n");
						comments.write("\t"+comment.getAutor().getAuthor() + "\n----------------------------\n");
					}
				}
				writer.close();
				comments.close();
				writer2.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}

	}

	private static String mountLikedURL(String channel) {
		return channel + "/videos?sort=dd&view=15&shelf_id=0";
	}

	private static String mountFollowedURL(String channel) {
		return channel + "/channels?view=56&shelf_id=0";
	}
}
