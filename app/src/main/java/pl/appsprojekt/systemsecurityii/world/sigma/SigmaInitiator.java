package pl.appsprojekt.systemsecurityii.world.sigma;

import android.util.Log;

import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

import pl.appsprojekt.systemsecurityii.model.Response;

public class SigmaInitiator extends SigmaParticipant {

	public SigmaInitiator(WorldParameters w, BigInteger sessionID, BigInteger myID, BigInteger secretKey) {
		super(w, sessionID, myID, secretKey);
	}

	public Response getFirstParams() {
		Response response = new Response();
		response.addParam("sessionID", sessionID);
		response.addParam("ephemeralPublicX", this.ephemeralPublic.getAffineXCoord().toBigInteger());
		response.addParam("ephemeralPublicY", this.ephemeralPublic.getAffineYCoord().toBigInteger());
		response.addParam("publicX", this.publicKey.getAffineXCoord().toBigInteger());
		response.addParam("publicY", this.publicKey.getAffineYCoord().toBigInteger());
		return response;
	}

	public Response getSignAndMAC(Response responderParams) {
		//Compute sessionKey;
		ECPoint Y = world.createPoint(responderParams.getParam("ephemeralPublicX"), responderParams.getParam("ephemeralPublicY")).normalize();
		sessionKey = Y.multiply(ephemeralSecret).normalize();
		Log.d("DEBUG_TAG", "Initiator session key:" + sessionKey);
		//Generate K1
		BigInteger seed = sessionKey.getAffineXCoord().toBigInteger().add(sessionKey.getAffineYCoord().toBigInteger());
		BigInteger K1 = world.PRF(seed, 1);
		//Verify MAC
		BigInteger receivedResponderMAC = responderParams.getParam("MAC");
		ECPoint responderPublic = world.createPoint(responderParams.getParam("publicX"), responderParams.getParam("publicY")).normalize();
		BigInteger computedResponderMAC = world.MAC(K1, responderPublic.toString());

		Log.d("DEBUG_TAG", "MAC(B) verification: " + receivedResponderMAC.equals(computedResponderMAC));
		//Verify signature
		ECPoint responderEphemeralPublic = world.createPoint(responderParams.getParam("ephemeralPublicX"), responderParams.getParam("ephemeralPublicY")).normalize();
		ISchnorrSignature.Signature receivedSignature = responderParams.signature;
		String messageFromResponder = publicKey.toString()
				+ responderEphemeralPublic.toString() + sessionID;
		//System.out.println("Message:"+messageFromResponder);
		SchnorrSignature schnorr = new SchnorrSignature();
		boolean verificationResult = schnorr.verify(world,
				responderParams.getParam("publicX"), responderParams.getParam("publicY"),
				messageFromResponder,
				receivedSignature);
		Log.d("DEBUG_TAG", "Signature(B) verification: " + verificationResult);


		//Create message authenticaton code for my public
		BigInteger MACA = world.MAC(K1, this.publicKey.toString());

		//Create signature for ephemerals
		String message = responderPublic.toString() + this.ephemeralPublic.toString() + sessionID;
		//System.out.println("Message:"+message);
		ISchnorrSignature.Signature signature = schnorr.sign(world, message, secretKey);

		Response response = new Response();
		response.addParam("sessionID", sessionID);
		response.addParam("MAC", MACA);
		response.signature = signature;

		return response;
	}

}
