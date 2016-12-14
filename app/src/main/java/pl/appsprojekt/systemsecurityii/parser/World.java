package pl.appsprojekt.systemsecurityii.parser;

import com.google.gson.Gson;

import org.spongycastle.asn1.x9.X962NamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Random;

import pl.appsprojekt.systemsecurityii.model.Response;

/**
 * Created by redione1 on 02.11.2016.
 */

public class World {

	private BigInteger secretKey;
	private ECPoint PK, G, X;
	private BigInteger x, c;
	private BigInteger Q, A, B, C;


	public World() {

	}

	public BigInteger generateRandomInTheWorld(BigInteger Q) {
		Random r = new Random();
		BigInteger result = new BigInteger(Q.bitLength(), r);
		System.out.println(Q);
		while (result.compareTo(Q) >= 0) {
			result = new BigInteger(Q.bitLength(), r);
		}
		return result;
	}

	public Response generateNewWorld() {
		Response response = createNewWorld();
		response.protocol = "Schnorr";
		response.sender = "P";
		response.stage = "Setup";
		return response;
	}

	private Response createNewWorld() {
		Response response = new Response();
		X9ECParameters ecp = X962NamedCurves.getByName("prime256v1");
		ECCurve.Fp myCurve = (ECCurve.Fp) ecp.getCurve();
		Q = myCurve.getQ();
		A = myCurve.getA().toBigInteger();
		B = myCurve.getB().toBigInteger();

		/*Odtworzenie krzywej z przesłanych Q,A,B */
		//ECCurve.Fp myCurve2 = new ECCurve.Fp(Q, A, B);

		/*Generator i rząd podgrupy */
		G = ecp.getG();
		BigInteger N = ecp.getN();


        /*Odtworzenie generatora */
		BigInteger gX = G.normalize().getXCoord().toBigInteger();
		BigInteger gY = G.normalize().getYCoord().toBigInteger();

		x = generateRandomInTheWorld(Q);
		secretKey = generateRandomInTheWorld(Q);
		PK = G.multiply(secretKey);

		BigInteger PKx = PK.normalize().getXCoord().toBigInteger();
		BigInteger PKy = PK.normalize().getAffineYCoord().toBigInteger();

		response.addParam("Q", Q);
		response.addParam("A", A);
		response.addParam("B", B);
		response.addParam("N", N);
		response.addParam("Gx", gX);
		response.addParam("Gy", gY);
		response.addParam("PKx", PKx);
		response.addParam("PKy", PKy);


		return response;
	}


	public Response createFromJson(String json) {
		Gson gson = new Gson();
		Response response = gson.fromJson(json, Response.class);
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

	public Response getX() {
		Response response = new Response();
		ECPoint X = G.multiply(x);
		BigInteger Xx = X.normalize().getXCoord().toBigInteger();
		BigInteger Xy = X.normalize().getYCoord().toBigInteger();
		response.protocol = "Schnorr";
		response.sender = "P";
		response.stage = "X";
		response.addParam("Xx", Xx);
		response.addParam("Xy", Xy);
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

	public Response setC(Response responseWithC) {
		Response response = new Response();
		response.success = true;
		C = responseWithC.getParam("C");
		return response;
	}

	public Response getS() {
		Response response = new Response();
		BigInteger S = x.add(secretKey.multiply(C));
		response.protocol = "Schnorr";
		response.sender = "P";
		response.stage = "S";
		response.addParam("S", S);
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

