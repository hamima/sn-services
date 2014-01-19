package ir.mod.tavana.toranj.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="polls")
public class PollBean {
	
	@Id 
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@Column(name="requester")
	private String requester;
	@Column(name="requestee")
	private String requestee;
	@Column(name="timeout")
	private float timeout;
	@Column(name="options")
	private String options;
	@Column(name="question")
	private String question;
	@Column(name="response")
	private String response;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRequester() {
		return requester;
	}
	public void setRequester(String requester) {
		this.requester = requester;
	}
	public String getRequestee() {
		return requestee;
	}
	public void setRequestee(String requestee) {
		this.requestee = requestee;
	}
	public float getTimeout() {
		return timeout;
	}
	public void setTimeout(float timeout) {
		this.timeout = timeout;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
}