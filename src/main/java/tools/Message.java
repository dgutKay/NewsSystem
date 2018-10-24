package tools;

public class Message {
	private String message;
	private String redirectUrl;
	private Integer redirectTime;
	private Integer result;

	public Message() {
		redirectTime = 5;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public Integer getRedirectTime() {
		return redirectTime;
	}

	public void setRedirectTime(Integer redirectTime) {
		this.redirectTime = redirectTime;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}
	
	
}
