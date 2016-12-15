package pl.appsprojekt.systemsecurityii.world;

import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.math.ec.ECCurve;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pl.appsprojekt.systemsecurityii.model.Response;

/**
 * author:  redione1
 * date:    14.12.2016
 */

public class SchnorrSignatureWorldSigner extends SchnorrSignatureWorld {


	private final BigInteger secretKey;

	public SchnorrSignatureWorldSigner() {
		generateWorld();
		secretKey = generateRandomInTheWorld();
	}

	public void generateWorld() {

		Q = new BigInteger("ffffffff00000001000000000000000000000000ffffffffffffffffffffffff", 16);
		A = new BigInteger("ffffffff00000001000000000000000000000000fffffffffffffffffffffffc", 16);
		B = new BigInteger("5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b", 16);
		Gx = new BigInteger("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16);
		Gy = new BigInteger("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16);
		N = new BigInteger("ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc632551", 16);
		ec = new ECCurve.Fp(Q, A, B);
		G = ec.createPoint(Gx, Gy);
		ecSpecification = new ECParameterSpec(ec, G, N);
	}

	public Response getWorldParameters() {
		Response response = new Response();

		response.setParam("A", A);
		response.setParam("B", B);
		response.setParam("Q", Q);
		response.setParam("Gx", Gx);
		response.setParam("Gy", Gy);
		response.setParam("N", N);

		return response;
	}

	public Response getSign() {
		Y = G.multiply(secretKey);
		Y = Y.normalize();
		Yx = Y.getAffineXCoord().toBigInteger();
		Yy = Y.getAffineYCoord().toBigInteger();
		//Signer generates random message m
		m = "Hello World!!!";
		//Singer generates random number a
		random_a = generateRandomInTheWorld();
		R = G.multiply(random_a);
		R = R.normalize();
		Rx = R.getAffineXCoord().toBigInteger();
		Ry = R.getAffineYCoord().toBigInteger();

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
		h = new BigInteger(hHex).mod(N);
		BigInteger temp = secretKey.multiply(h);
		s = random_a.add(temp);


		Response response = new Response();
		//public key
		response.setParam("Yx", Yx);
		response.setParam("Yy", Yy);
		//R
		response.setParam("Rx", Rx);
		response.setParam("Ry", Ry);
		//S
		response.setParam("s", s);
		//message m
		response.params.put("m", m);

		return response;
	}
}
