package com.affiliate.utils;
import java.sql.Timestamp;

public class Deals {
	private String id;
	private String sha1;
	private String message;
	private Timestamp time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public String getMessage() {
		return message;
	}

	public Deals(String id, String sha1, String message, Timestamp time) {
		super();
		this.id = id;
		this.sha1 = sha1;
		this.message = message;
		this.time = time;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
}
