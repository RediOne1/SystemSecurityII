package pl.appsprojekt.systemsecurityii.model;

/**
 * author:  redione1
 * date:    13.12.2016
 */

public class Message {

	private String content;
	private boolean isFromMe;

	public Message(String content) {
		this.content = content;
	}

	public Message(String content, boolean isFromMe) {
		this.content = content;
		this.isFromMe = isFromMe;
	}

	public String getContent() {
		return content;
	}

	public boolean isFromMe() {
		return isFromMe;
	}
}
