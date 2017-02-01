package pl.appsprojekt.systemsecurityii.world;

import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

import pl.appsprojekt.systemsecurityii.BuildConfig;
import pl.appsprojekt.systemsecurityii.model.Response;

/**
 * author:  Adrian Kuta
 * date:    24.01.2017
 */
public class SchnorrRingSignatureWorldSigner extends SchnorrRingSignatureWorld {
	private BigInteger secretKeys[]; //private keys
	private int length;    //number of signers

	public SchnorrRingSignatureWorldSigner(int length) {
		this.length = length;
		this.secretKeys = new BigInteger[length];
		this.Y = new ECPoint.Fp[length];
		this.Yx = new BigInteger[length];
		this.Yy = new BigInteger[length];
		this.random_a = new BigInteger[length];
		this.R = new ECPoint[length];
		this.Rx = new BigInteger[length];
		this.Ry = new BigInteger[length];
		this.h = new BigInteger[length];
		this.m = "Lorem ipsum";
	}

	public void generateWorld() {
		ecSpecification = ECNamedCurveTable.getParameterSpec("prime256v1");
		G = ecSpecification.getG();
		G = G.normalize();
		Gx = G.getAffineXCoord().toBigInteger();
		Gy = G.getAffineYCoord().toBigInteger();

		N = ecSpecification.getN();
		ec = (ECCurve.Fp) ecSpecification.getCurve();
		A = ec.getA().toBigInteger();
		B = ec.getB().toBigInteger();
		Q = ec.getQ();

	}

	public Response getWorldParameters() {
		Response response = new Response();
		response.addParam("A", A);
		response.addParam("B", B);
		response.addParam("Q", Q);
		response.addParam("Gx", Gx);
		response.addParam("Gy", Gy);
		response.addParam("N", N);

		return response;
	}

	public Response getSecretKeys() {
		Response response = new Response();
		for (int i = 0; i < length; i++) {
			response.addParam("X" + i, secretKeys[i]);
			if (BuildConfig.DEBUG) {
				System.out.println("X" + i + " " + secretKeys[i]);
			}
		}
		return response;
	}


	public Response getPublicKeys() {
		Response response = new Response();
		for (int i = 0; i < length; i++) {
			response.addParam("Yx" + i, Yx[i]);
			response.addParam("Yy" + i, Yy[i]);
			if (BuildConfig.DEBUG) {
				System.out.println("Yx " + i + " " + Yx[i]);
				System.out.println("Yy " + i + " " + Yy[i]);
			}
		}
		return response;
	}

	public Response getSign() {
		Response response = new Response();
		for (int i = 0; i < length; i++) {
			response.addParam("Rx" + i, Rx[i]);
			response.addParam("Ry" + i, Ry[i]);
			if (BuildConfig.DEBUG) {
				System.out.println("Rx " + i + " " + Rx[i]);
				System.out.println("Ry " + i + " " + Ry[i]);
			}
		}
		response.addParam("m", m);
		response.addParam("s", s);
		if (BuildConfig.DEBUG) {
			System.out.println("m " + m);
			System.out.println("s " + s.toString(16));
		}

		return response;
	}

	public void generateKeys() {
		for (int i = 0; i < length; i++) {
			secretKeys[i] = generateRandomInTheWorld();
			Y[i] = (ECPoint.Fp) G.multiply(secretKeys[i]);
			Y[i] = (ECPoint.Fp) Y[i].normalize();
			Yx[i] = Y[i].getAffineXCoord().toBigInteger();
			Yy[i] = Y[i].getAffineYCoord().toBigInteger();
		}
	}

	//j  - signer id
	public void generateRingSign(int j) {
		//For all users except signer
		for (int i = 0; i < this.length; i++) {
			random_a[i] = generateRandomInTheWorld();
			if (i != j) {
				R[i] = G.multiply(random_a[i]);
				R[i] = R[i].normalize();
				Rx[i] = R[i].getAffineXCoord().toBigInteger();
				Ry[i] = R[i].getAffineYCoord().toBigInteger();
				h[i] = hash(m, Rx[i], Ry[i]);
				if (BuildConfig.DEBUG) {
					System.out.println("h[" + i + "]: " + h[i]);
				}
			}
		}

		//For signer
		R[j] = G.multiply(random_a[j]);
		R[j] = R[j].normalize();
		for (int i = 0; i < this.length; i++) {
			if (i != j) {
				R[j] = R[j].add(Y[i].multiply(N.subtract(h[i])));
				R[j] = R[j].normalize();
			}
		}
		Rx[j] = R[j].getAffineXCoord().toBigInteger();
		Ry[j] = R[j].getAffineYCoord().toBigInteger();
		h[j] = hash(m, Rx[j], Ry[j]);

		if (BuildConfig.DEBUG) {
			System.out.println("h[" + j + "]: " + h[j]);
		}

		//Generate s
		s = random_a[0];
		for (int i = 1; i < length; i++) {
			s = s.add(random_a[i]);
		}
		s = s.add(secretKeys[j].multiply(h[j]));

	}
}
