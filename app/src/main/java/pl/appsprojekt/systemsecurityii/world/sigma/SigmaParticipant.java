package pl.appsprojekt.systemsecurityii.world.sigma;

import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class SigmaParticipant {

	protected WorldParameters world;
	protected BigInteger sessionID;
	protected BigInteger ID;
	protected BigInteger secretKey;
	protected ECPoint publicKey;
	protected BigInteger ephemeralSecret;
	protected ECPoint ephemeralPublic;
	protected ECPoint sessionKey;
	
	public class Communicat1 {
		public BigInteger sessionID; 
		public BigInteger initiatorPublicX;
		public BigInteger initiatorPublicY;
		public BigInteger initiatorEphemeralPublicX;
		public BigInteger initiatorEphemeralPublicY;
	}
	
	public class Communicat2 {
		public BigInteger sessionID;
		public BigInteger responderPublicX;
		public BigInteger responderPublicY;
		public BigInteger responderEphemeralPublicX;
		public BigInteger responderEphemeralPublicY;
		public ISchnorrSignature.Signature responderSignature;
		public BigInteger responderMAC;
	}
	
	public class Communicat3 {
		public BigInteger sessionID; 
		public ISchnorrSignature.Signature initiatorSignature;
		public BigInteger initiatorMAC;
	}
	
	public SigmaParticipant(
			WorldParameters w, 
			BigInteger sessionID, 
			BigInteger ID,
			BigInteger secretKey){
		this.world = w;
		this.sessionID = sessionID;
		this.ID = ID;
		this.secretKey = secretKey;
		this.publicKey = w.G.multiply(secretKey).normalize();
		this.ephemeralSecret = w.generateRandomInTheWorld();
		this.ephemeralPublic = w.G.multiply(ephemeralSecret).normalize();
	}
}
