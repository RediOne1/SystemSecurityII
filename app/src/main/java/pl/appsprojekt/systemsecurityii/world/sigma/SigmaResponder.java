package pl.appsprojekt.systemsecurityii.world.sigma;

import android.util.Log;

import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

import pl.appsprojekt.systemsecurityii.model.Response;


public class SigmaResponder extends SigmaParticipant {


	private BigInteger initiatorPublicX;
	private BigInteger initiatorPublicY;
	private BigInteger initiatorEphemeralPublicX;
	private BigInteger initiatorEphemeralPublicY;

	public SigmaResponder(WorldParameters w, BigInteger sessionID, BigInteger ID, BigInteger secretKey) {
		super(w, sessionID, ID, secretKey);
	}

	public Response getSignAndMac(Response in) {
		//Compute sessionKey;
		initiatorPublicX = in.getParam("publicX");
		initiatorPublicY = in.getParam("publicY");
		initiatorEphemeralPublicX = in.getParam("ephemeralPublicX");
		initiatorEphemeralPublicY = in.getParam("ephemeralPublicY");

		ECPoint initiatorEphemeralPublic = world.createPoint(initiatorEphemeralPublicX, initiatorEphemeralPublicY).normalize();
		sessionKey = initiatorEphemeralPublic.multiply(ephemeralSecret).normalize();
		Log.d("DEBUG_TAG", "Responder session key:" + sessionKey);

		//Generate signature the X and my Y 
		SchnorrSignature schnorr = new SchnorrSignature();
		ECPoint initiatorPublic = world.createPoint(initiatorPublicX, initiatorPublicY).normalize();

		String message = initiatorPublic.toString() + this.ephemeralPublic.toString() + sessionID;
		//System.response.println("Message:"+message);

		ISchnorrSignature.Signature signature = schnorr.sign(world, message, secretKey);

		//Generate K0 and K1 ?
		BigInteger seed = sessionKey.getAffineXCoord().toBigInteger().add(sessionKey.getAffineYCoord().toBigInteger());
		BigInteger K1 = world.PRF(seed, 1);
		//Generate MACB with K1
		BigInteger MACB = world.MAC(K1, this.publicKey.toString());

		Response response = new Response();
		response.addParam("ephemeralPublicX", this.ephemeralPublic.getAffineXCoord().toBigInteger());
		response.addParam("ephemeralPublicY", this.ephemeralPublic.getAffineYCoord().toBigInteger());
		response.addParam("MAC", MACB);
		response.addParam("publicX", this.publicKey.getAffineXCoord().toBigInteger());
		response.addParam("publicY", this.publicKey.getAffineYCoord().toBigInteger());
		response.signature = signature;
		return response;
	}

	public Response verifyCorrectness(Response in3) {
		Response out = new Response();
		//Verify MAC
		BigInteger receivedInitiatorMAC = in3.getParam("MAC");
		ECPoint initiatorPublic = world.createPoint(initiatorPublicX, initiatorPublicY).normalize();
		//Generate  K1 ?
		BigInteger seed = sessionKey.getAffineXCoord().toBigInteger().add(sessionKey.getAffineYCoord().toBigInteger());
		BigInteger K1 = world.PRF(seed, 1);

		BigInteger computedInitiatorMAC = world.MAC(K1, initiatorPublic.toString());
		out.params.put("MAC(A) verification", "" + receivedInitiatorMAC.equals(computedInitiatorMAC));
		//Verify signature
		ECPoint initiatorEphemeralPublic = world.createPoint(initiatorEphemeralPublicX, initiatorEphemeralPublicY).normalize();
		ISchnorrSignature.Signature receivedSignature = in3.signature;
		String messageFromInitiator = publicKey.toString()
				+ initiatorEphemeralPublic.toString() + sessionID;
		//System.out.println("Message:"+messageFromInitiator);
		SchnorrSignature schnorr = new SchnorrSignature();
		boolean verificationResult = schnorr.verify(world,
				initiatorPublicX, initiatorPublicY,
				messageFromInitiator,
				receivedSignature);

		out.success = true;
		out.params.put("Signature(A) verification", "" + verificationResult);
		return out;
	}


}
