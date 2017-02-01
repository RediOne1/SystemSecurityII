package pl.appsprojekt.systemsecurityii.world.sigma;

import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class SchnorrSignature implements ISchnorrSignature{

	@Override
	public Signature sign(WorldParameters world, String message, BigInteger x) {
		Signature result = new Signature();
		BigInteger a = world.generateRandomInTheWorld();
		ECPoint R = world.G.multiply(a).normalize();
		BigInteger Rx = R.getAffineXCoord().toBigInteger();
		BigInteger Ry = R.getAffineYCoord().toBigInteger();
		BigInteger hash = world.hash(message, Rx, Ry);
		BigInteger signature = a.add(x.multiply(hash));
		result.Rx = Rx;
		result.Ry = Ry;
		result.s = signature;
		return result;
	}

	@Override
	public boolean verify(WorldParameters world, BigInteger Xx, BigInteger Xy, String message, Signature signature) {
		ECPoint left = world.G.multiply(signature.s).normalize();
		ECPoint X = world.createPoint(Xx, Xy);
		ECPoint R = world.createPoint(signature.Rx, signature.Ry);
		BigInteger hash = world.hash(message, signature.Rx, signature.Ry);
		ECPoint right = R.add(X.multiply(hash)).normalize();
		return left.equals(right);
	}

}
