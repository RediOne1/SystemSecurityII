package pl.appsprojekt.systemsecurityii.world.sigma;

import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class WorldParameters {

	public ECPoint G;
	public BigInteger Gx;
	public BigInteger Gy;
	public BigInteger N;
	public BigInteger A;
	public BigInteger B;
	public BigInteger Q;
	
	private ECParameterSpec ecSpecification;
	private ECCurve.Fp ec;
	
	public WorldParameters(String specificationName) {
		ecSpecification = ECNamedCurveTable.getParameterSpec(specificationName);
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
	
	public WorldParameters(BigInteger A, BigInteger B, BigInteger Q, BigInteger Gx, BigInteger Gy, BigInteger N) {
		this.A = A;
		this.B = B;
		this.Q = Q;
		this.Gx = Gx;
		this.Gy =Gy;
		this.N = N;
		this.ec = new ECCurve.Fp(Q, A, B);
		this.G = ec.createPoint(Gx, Gy);
		ecSpecification = new ECParameterSpec(ec, G, this.N);
	}
	
	public ECPoint createPoint(BigInteger x, BigInteger y) {
		return ec.createPoint(x, y).normalize();
	}
	
	public BigInteger generateRandomInTheWorld() {
	    Random r = new Random();
		BigInteger result = new BigInteger(Q.bitLength(), r);
		//System.out.println(Q);
	    while( result.compareTo(Q) >= 0 ) {
	        result = new BigInteger(Q.bitLength(), r);
	    }
	    return result;
	}
	
	public BigInteger hash(String m, BigInteger Rx, BigInteger Ry) {
		//Generate Hash
		byte hHex[] = {0x00};;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] mHex = m.getBytes("UTF-8");
			byte[] rxHex = Rx.toByteArray();
			byte[] ryHex = Ry.toByteArray();
			byte[] mr = concatenateArrays(mHex, rxHex);
			hHex = md.digest(concatenateArrays(mr, ryHex));
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Cos poszlo nie tak przy hashu");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.err.println("Bledne kodowanie");
			e.printStackTrace();
		} 
		return new BigInteger(hHex).mod(N);
	}
	
	public BigInteger hash(BigInteger m) {
		//Generate Hash
		byte hHex[] = {0x00};;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] mHex = m.toByteArray();
			hHex = md.digest(mHex);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Cos poszlo nie tak przy hashu");
			e.printStackTrace();
		} 
		return new BigInteger(hHex).mod(N);
	}
	
	
	public BigInteger PRF(BigInteger seed, int k) {
		//k=k+1;
		for (int i = 0; i <= k; i++) {
			seed = hash(seed.add(new BigInteger(Integer.toString(i))));
		}
		return seed;
	}
	
	protected BigInteger MAC(BigInteger key, String message) {
		byte[] digest ={0x00};
		try {
		    byte[] secret = key.toByteArray();
			SecretKeySpec keyMd5 = new SecretKeySpec(secret , "HmacSHA512");
		    // create a MAC and initialize with the above key
		    Mac mac = Mac.getInstance(keyMd5.getAlgorithm());
		    mac.init(keyMd5);

		    //String message = "This is a confidential message";
		    
		    // get the string as UTF-8 bytes
		    byte[] b = message.getBytes("UTF-8");
		    
		    // create a digest from the byte array
		    digest = mac.doFinal(b);
	
  
		}
		catch (NoSuchAlgorithmException e) {
			System.out.println("No Such Algorithm:" + e.getMessage());
		}
		catch (UnsupportedEncodingException e) {
			System.out.println("Unsupported Encoding:" + e.getMessage());
		}
		catch (InvalidKeyException e) {
			System.out.println("Invalid Key:" + e.getMessage());
		}
		return new BigInteger(digest).mod(N);

	}
	
	protected byte[] concatenateArrays(byte[] a, byte[] b) {
		byte[] result = new byte[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
}
