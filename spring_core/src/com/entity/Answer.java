package com.entity;

public class Answer {

	private int id;
	private String name;
	private String author;

	public Answer() {
		System.out.println("Default Answer constructor...");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "Answer [id=" + id + ", name=" + name + ", author=" + author + "]";
	}
}
