package com.mathisonian.android.whisprabbit;

public class TextPost {
	private String id;
	private String content;
	private String filename;
	
	public TextPost(String t, String c, String f) {
		id = t;
		content = c;
		filename = f;
	}
	
	// Generated setters and getters
	// we probably only want getters
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
