package pl.appsprojekt.systemsecurityii.world;

import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Random;

/**
 * author:  redione1
 * date:    14.12.2016
 */

public abstract class SchnorrSignatureWorld {

	ECParameterSpec ecSpecification;
	BigInteger Q, A, B;
	ECCurve.Fp ec;
	ECPoint G;
	BigInteger Gx, Gy;
	BigInteger N;
	ECPoint Y;
	BigInteger Yx, Yy;
	String m;
	BigInteger random_a;
	BigInteger s;
	BigInteger h;

	BigInteger Rx, Ry;
	ECPoint R;


	BigInteger generateRandomInTheWorld() {
		Random r = new Random();
		BigInteger result = new BigInteger(Q.bitLength(), r);
		//System.out.println(Q);
		while (result.compareTo(Q) >= 0) {
			result = new BigInteger(Q.bitLength(), r);
		}
		return result;
	}

	byte[] concatenateArrays(byte[] a, byte[] b) {
		byte[] result = new byte[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
}
