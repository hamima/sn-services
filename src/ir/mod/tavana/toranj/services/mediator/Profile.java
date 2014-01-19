package ir.mod.tavana.toranj.services.mediator;

import jade.content.Concept;

public class Profile implements Concept {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer age;
	private String quest;
	private String type;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getQuest() {
		return quest;
	}
	public void setQuest(String quest) {
		this.quest = quest;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
