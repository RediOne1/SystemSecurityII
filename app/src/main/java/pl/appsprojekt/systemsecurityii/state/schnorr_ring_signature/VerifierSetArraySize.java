package pl.appsprojekt.systemsecurityii.state.schnorr_ring_signature;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrRingSignatureWorldVerifier;

/**
 * author:  Adrian Kuta
 * date:    24.01.2017
 */
public class VerifierSetArraySize implements State {

	private SchnorrRingSignatureWorldVerifier verifier;

	@Override
	public void onPrepare(INewMainView view) {
		view.printOutputMessage(new Message("Enter size of array"));
		view.printOutputMessage(new Message("Press SEND to run Schnorr Ring Signer"));
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		int length;
		try {
			length = Integer.parseInt(input);
		} catch (Exception e) {
			length = 2;
		}
		verifier = new SchnorrRingSignatureWorldVerifier(length);
		listener.onComplete();
	}

	@Override
	public State getNextState() {
		return new VerifierLoadWorld(verifier);
	}
}
