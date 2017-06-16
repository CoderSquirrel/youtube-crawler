package com.br.crawler.domain;

import java.util.ArrayList;
import java.util.List;

public class Autor {

	private String author;
	private String channel;
	private List<String> likedVideos;
	private List<String> followedAccounts;

	public Autor() {
		likedVideos = new ArrayList<String>();
		followedAccounts = new ArrayList<String>();
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void addLikedVideo(String video) {
		likedVideos.add(video);
	}
	public void addFollowedAccount(String video) {
		followedAccounts.add(video);
	}

	public List<String> getLikedVideos() {
		return likedVideos;
	}

	public void setLikedVideos(List<String> likedVideos) {
		this.likedVideos = likedVideos;
	}

	public List<String> getFollowedAccounts() {
		return followedAccounts;
	}

	public void setFollowedAccounts(List<String> followedAccounts) {
		this.followedAccounts = followedAccounts;
	}

}
