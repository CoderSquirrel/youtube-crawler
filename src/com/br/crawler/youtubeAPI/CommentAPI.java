/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.br.crawler.youtubeAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.br.crawler.domain.Autor;
import com.br.crawler.domain.Comment;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.common.collect.Lists;

public class CommentAPI {

	private YouTube youtube;

	public CommentAPI() {
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");

		Credential credential;
		try {
			credential = Auth.authorize(scopes, "commentthreads");
			youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
					.setApplicationName("youtube-cmdline-commentthreads-sample").build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Comment> getComments(String videoId) throws IOException {
		List<Comment> comments = new ArrayList<>();
		CommentThreadListResponse videoCommentsListResponse = youtube.commentThreads().list("snippet")
				.setVideoId(videoId).setTextFormat("plainText").execute();
		List<CommentThread> videoComments = videoCommentsListResponse.getItems();
		videoComments.forEach(vc -> {
			Autor aut = new Autor();
			aut.setAuthor(vc.getSnippet().getTopLevelComment().getSnippet().getAuthorDisplayName());
			aut.setChannel(vc.getSnippet().getTopLevelComment().getSnippet().getAuthorChannelUrl());
			Comment temp = new Comment();
			temp.setAutor(aut);
			temp.setComment(vc.getSnippet().getTopLevelComment().getSnippet().getTextOriginal());
			comments.add(temp);
		});

		return comments;
	}

}
