package ir.mod.tavana.toranj.services.mediator;

public class MediatorMapper {

	public Register toRegister(Profile profile) {
		Register register = new Register();
		register.setProfile(profile);
		return register;
	}

	public Register toRegister(String name, Integer age, String type, String quest) {

		Register register = new Register();
		Profile profile = new Profile();
		profile.setAge(age);
		profile.setName(name);
		profile.setType(type);
		profile.setQuest(quest);
		register.setProfile(profile);

		return register;
	}
}