package com.entity;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class Question {

	private int id;
	private String name;
	private Map<String,String> answers;
	private Properties city;

	public Question() {
		System.out.println("Default question constructor....");
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

	public Map<String, String> getAnswers() {
		return answers;
	}

	public void setAnswers(Map<String, String> answers) {
		this.answers = answers;
	}
	
	public Properties getCity() {
		return city;
	}

	public void setCity(Properties city) {
		this.city = city;
	}

	public void display() {
		System.out.println("Id:"+id);
		System.out.println("Name:"+name);
		System.out.println("City:"+city);
		Set<Entry<String, String>> set= answers.entrySet();
		Iterator<Entry<String, String>>  itr = set.iterator();
		while(itr.hasNext()) {
			Entry<String, String> entry = itr.next();
			System.out.println("Answer:"+entry.getKey()+"  Author:"+entry.getValue());
		}
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", name=" + name + ", answers=" + answers + "]";
	}
}
