package pl.appsprojekt.systemsecurityii.world;

import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * author:  Adrian Kuta
 * date:    24.01.2017
 */
public abstract class SchnorrRingSignatureWorld {

	public ECParameterSpec ecSpecification;
	public BigInteger Q, A, B;
	public ECCurve.Fp ec;
	public ECPoint G;
	public BigInteger Gx, Gy;
	public BigInteger N;
	public ECPoint[] Y;
	public BigInteger Yx[], Yy[];
	public String m;
	public BigInteger random_a[];
	public BigInteger s;
	public BigInteger h[];

	public BigInteger Rx[], Ry[];
	public ECPoint R[];


	public BigInteger generateRandomInTheWorld() {
		Random r = new Random();
		BigInteger result = new BigInteger(Q.bitLength(), r);
		//System.out.println(Q);
		while (result.compareTo(Q) >= 0) {
			result = new BigInteger(Q.bitLength(), r);
		}
		return result;
	}

	public BigInteger hash(String m, BigInteger Rx, BigInteger Ry) {
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
			System.err.println("Cos poszlo nie tak przy hashu");
			e.printStackTrace();
		}
		return new BigInteger(hHex).mod(Q);
	}

	protected byte[] concatenateArrays(byte[] a, byte[] b) {
		byte[] result = new byte[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
}
