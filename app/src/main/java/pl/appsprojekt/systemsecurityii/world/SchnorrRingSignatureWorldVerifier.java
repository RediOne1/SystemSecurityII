package pl.appsprojekt.systemsecurityii.world;

import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

import pl.appsprojekt.systemsecurityii.BuildConfig;
import pl.appsprojekt.systemsecurityii.model.Response;

/**
 * author:  Adrian Kuta
 * date:    24.01.2017
 */
public class SchnorrRingSignatureWorldVerifier extends SchnorrRingSignatureWorld {
	private int length;    //number of signers

	public SchnorrRingSignatureWorldVerifier(int length) {
		this.length = length;
		this.Y = new ECPoint[length];
		this.Yx = new BigInteger[length];
		this.Yy = new BigInteger[length];
		this.R = new ECPoint[length];
		this.Rx = new BigInteger[length];
		this.Ry = new BigInteger[length];
		this.h = new BigInteger[length];
	}

	public void setWorldParams(Response response) {
		A = response.getParam("A");
		B = response.getParam("B");
		Q = response.getParam("Q");
		Gx = response.getParam("Gx");
		Gy = response.getParam("Gy");
		N = response.getParam("N");
		ec = new ECCurve.Fp(Q, A, B);
		G = ec.createPoint(Gx, Gy);
		ecSpecification = new ECParameterSpec(ec, G, N);

	}

	public void setPublicKeys(Response response) {
		for (int i = 0; i < length; i++) {
			Yx[i] = response.getParam("Yx" + i);
			Yy[i] = response.getParam("Yy" + i);
			if (BuildConfig.DEBUG) {
				System.out.println("Yx " + i + " " + Yx[i]);
				System.out.println("Yy " + i + " " + Yy[i]);
			}
			Y[i] = ec.createPoint(Yx[i], Yy[i]);
			Y[i] = Y[i].normalize();
		}
	}

	public void setSignerParams(Response response) {
		for (int i = 0; i < length; i++) {
			Rx[i] = response.getParam("Rx" + i);
			Ry[i] = response.getParam("Ry" + i);
			R[i] = ec.createPoint(Rx[i], Ry[i]);
			R[i] = R[i].normalize();

			if (BuildConfig.DEBUG) {
				System.out.println("Rx " + i + " " + Rx[i]);
				System.out.println("Ry " + i + " " + Ry[i]);
			}

		}
		s = response.getParam("s");
		m = response.params.get("m");
		if (BuildConfig.DEBUG) {
			System.out.println("s " + s);
			System.out.println("m " + m);
		}
	}

	//j  - signer id
	public Response verify() {
		Response response = new Response();
		computeHashes();
		boolean result = false;
		ECPoint left = G.multiply(s);
		left = left.normalize();
		ECPoint right = R[0];
		right = right.add(Y[0].multiply(h[0]));
		for (int i = 1; i < length; i++) {
			right = right.add(R[i]);
			right = right.add(Y[i].multiply((h[i])));
		}
		if (BuildConfig.DEBUG) {
			System.out.println(left);
			System.out.println(right.normalize());
		}
		response.success = right.equals(left);
		return response;
	}

	private void computeHashes() {
		for (int i = 0; i < length; i++) {
			h[i] = hash(m, Rx[i], Ry[i]);
			if (BuildConfig.DEBUG) {
				System.out.println("h[" + i + "] " + Ry[i]);
			}
		}
	}
}
