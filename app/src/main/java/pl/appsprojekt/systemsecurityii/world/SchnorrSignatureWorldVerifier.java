package pl.appsprojekt.systemsecurityii.world;

import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pl.appsprojekt.systemsecurityii.model.Response;

/**
 * author:  redione1
 * date:    14.12.2016
 */

public class SchnorrSignatureWorldVerifier extends SchnorrSignatureWorld {

	public Response setWorldParams(Response response) {

		A = response.getParam("A");
		B = response.getParam("B");
		Q = response.getParam("Q");

		Gx = response.getParam("Gx");
		Gy = response.getParam("Gy");
		N = response.getParam("N");
		ec = new ECCurve.Fp(Q, A, B);
		G = ec.createPoint(Gx, Gy);
		ecSpecification = new ECParameterSpec(ec, G, N);

		Response res = new Response();
		res.success = true;
		return res;
	}

	public Response setSignerParams(Response response) {
		Yx = response.getParam("Yx");
		Yy = response.getParam("Yy");
		Y = ec.createPoint(Yx, Yy);
		Y = Y.normalize();
		Rx = response.getParam("Rx");
		Ry = response.getParam("Ry");
		R = ec.createPoint(Rx, Ry);
		R = R.normalize();
		s = response.getParam("s");
		m = response.params.get("m");

		Response res = new Response();
		res.success = true;
		return res;
	}

	public Response getVerification() {
		//Generate Hash
		byte hHex[] = {0x00};
		;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] mHex = m.getBytes();
			byte[] rxHex = Rx.toByteArray();
			byte[] ryHex = Ry.toByteArray();
			byte[] mr = concatenateArrays(mHex, rxHex);
			hHex = md.digest(concatenateArrays(mr, ryHex));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		h = new BigInteger(hHex).mod(Q);


		ECPoint left = G.multiply(s);//.normalize();
		ECPoint right = R.add(Y.multiply(h));


		Response response = new Response();

		if ((left.normalize().getAffineXCoord().toBigInteger()
				.equals(right.normalize().getAffineXCoord().toBigInteger())
				&& left.normalize().getAffineYCoord().toBigInteger()
				.equals(right.normalize().getAffineYCoord().toBigInteger())
		)) {
			response.success = true;
		}

		return response;
	}
}
