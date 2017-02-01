package pl.appsprojekt.systemsecurityii.world.sigma;

import java.math.BigInteger;

/**
 * @author Micha� Staniucha
 */
public interface ISchnorrSignature{

	/**
	 * Signature structure based on 
	 */
	public class Signature {
		public BigInteger Rx;
		public BigInteger Ry;
		public BigInteger s;
	}

	/** 
	 * Sign a message
	 * @param world
	 * @param message
	 * @param x - signer secret key
	 */
	public Signature sign(
			WorldParameters world,
			String message,
			BigInteger x);

	/**
	 * Verification function
	 * @param world
	 * @param message
	 * @param signature
	 * @return verification result (true - positive, false - negative)
	 */
	public boolean verify(
			WorldParameters world,
			BigInteger Xy,
			BigInteger Xx,
			String message,
			Signature signature);
}
