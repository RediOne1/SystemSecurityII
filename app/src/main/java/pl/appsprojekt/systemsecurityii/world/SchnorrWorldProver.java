package pl.appsprojekt.systemsecurityii.world;

import org.spongycastle.asn1.x9.X962NamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

import pl.appsprojekt.systemsecurityii.model.Response;

/**
 * author:  Adrian Kuta
 * date:    14.12.2016
 */
public class SchnorrWorldProver extends SchnorrWorld {

	public Response createNewWorld() {
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
}
