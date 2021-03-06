package pl.appsprojekt.systemsecurityii.model;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import pl.appsprojekt.systemsecurityii.world.sigma.ISchnorrSignature;

/**
 * Created by redione1 on 02.11.2016.
 */

public class Response {

	public String protocol;
	public String stage;
	public String sender;
	public boolean success;
	public Map<String, String> params;
	public ISchnorrSignature.Signature signature;

	public Response() {
		params = new HashMap<>();
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public BigInteger getParam(String key) {
		String value = params.get(key);
		return new BigInteger(value, 16);
	}

	public void addParam(String key, BigInteger value) {
		params.put(key, value.toString(16));
	}

	public void addParam(String key, String value) {
		params.put(key, value);
	}
}
