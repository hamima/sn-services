package ir.mod.tavana.toranj.services.avatar.polling;

import jade.content.Concept;

public class PollRequest implements Concept {
	
	private String question;
	private String option;

	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}

}
