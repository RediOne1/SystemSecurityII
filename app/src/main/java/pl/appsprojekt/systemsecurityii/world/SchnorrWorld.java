package pl.appsprojekt.systemsecurityii.world;

import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Random;

/**
 * author:  Adrian Kuta
 * date:    14.12.2016
 */
public abstract class SchnorrWorld {

	BigInteger secretKey;
	ECPoint PK, G, X;
	BigInteger x, c;
	BigInteger Q, A, B, C;

	public BigInteger generateRandomInTheWorld(BigInteger Q) {
		Random r = new Random();
		BigInteger result = new BigInteger(Q.bitLength(), r);
		System.out.println(Q);
		while (result.compareTo(Q) >= 0) {
			result = new BigInteger(Q.bitLength(), r);
		}
		return result;
	}
}
