package iot.hello.com.helloiot.model;


public class InstanceDto {
	private Integer id;

	private final String appType;

	private final String pushToken;

	public static InstanceDto get(String pushToken) {
		return new InstanceDto("ANDROID", pushToken);
	}

	public InstanceDto(String appType, String pushToken) {
		this.appType = appType;
		this.pushToken = pushToken;
	}
}
