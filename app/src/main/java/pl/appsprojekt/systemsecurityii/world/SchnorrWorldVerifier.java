package pl.appsprojekt.systemsecurityii.world;

import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

import pl.appsprojekt.systemsecurityii.model.Response;

/**
 * author:  Adrian Kuta
 * date:    14.12.2016
 */
public class SchnorrWorldVerifier extends SchnorrWorld {

	public Response createFromJson(Response response) {
		Q = response.getParam("Q");
		A = response.getParam("A");
		B = response.getParam("B");

		ECCurve.Fp myCurve = new ECCurve.Fp(Q, A, B);

		BigInteger Gx = response.getParam("Gx");
		BigInteger Gy = response.getParam("Gy");

		BigInteger PKx = response.getParam("PKx");
		BigInteger PKy = response.getParam("PKy");

		G = myCurve.createPoint(Gx, Gy);
		PK = myCurve.createPoint(PKx, PKy);
		secretKey = generateRandomInTheWorld(Q);
		response.success = true;
		return response;
	}

	public Response setX(Response responseWithX) {
		Response response = new Response();
		BigInteger Xx = responseWithX.getParam("Xx");
		BigInteger Xy = responseWithX.getParam("Xy");
		ECCurve.Fp myCurve = new ECCurve.Fp(Q, A, B);
		X = myCurve.createPoint(Xx, Xy);
		response.success = true;
		return response;
	}

	public Response getC() {
		Response response = new Response();
		c = generateRandomInTheWorld(Q);
		response.protocol = "Schnorr";
		response.sender = "V";
		response.stage = "C";
		response.addParam("C", c);
		return response;
	}

	public Response getVerification(Response responseWithS) {
		Response response = new Response();
		BigInteger S = responseWithS.getParam("S");
		ECPoint left = G.multiply(S);
		ECPoint right = X;

		right = right.add(PK.multiply(c));

		response.success = (left.normalize().getAffineXCoord().toBigInteger()
				.equals(right.normalize().getAffineXCoord().toBigInteger())
				&& left.normalize().getAffineYCoord().toBigInteger()
				.equals(right.normalize().getAffineYCoord().toBigInteger())
		);

		return response;
	}
}
