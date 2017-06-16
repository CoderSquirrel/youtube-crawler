package com.br.crawler.domain;

public class Comment {

	private String comment;
	private Autor autor;

	public Comment() {
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
