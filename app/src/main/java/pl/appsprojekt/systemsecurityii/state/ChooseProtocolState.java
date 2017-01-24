package pl.appsprojekt.systemsecurityii.state;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.state.schnorr.ProverGenerateWorldState;
import pl.appsprojekt.systemsecurityii.state.schnorr.VerifierRecreateWorldState;
import pl.appsprojekt.systemsecurityii.state.schnorr_ring_signature.SignerSchnorrRing;
import pl.appsprojekt.systemsecurityii.state.schnorr_ring_signature.VerifierSetArraySize;
import pl.appsprojekt.systemsecurityii.state.schnorr_signature.SignerGenerateWorldState;
import pl.appsprojekt.systemsecurityii.state.schnorr_signature.VerifierLoadWorldState;
import pl.appsprojekt.systemsecurityii.view.INewMainView;

/**
 * author:  redione1
 * date:    13.12.2016
 */

public class ChooseProtocolState implements State {

	private PROTOCOL selectedProtocol;

	@Override
	public void onPrepare(INewMainView view) {
		view.showAvailableProtocols(PROTOCOL.values());
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		for (PROTOCOL protocol : PROTOCOL.values())
			if (protocol.toString().equalsIgnoreCase(input))
				selectedProtocol = protocol;
		listener.onComplete();
	}

	@Override
	public State getNextState() {
		return new ChooseModeState(selectedProtocol);
	}

	public enum PROTOCOL {
		SCHNORR(new ProverGenerateWorldState(), new VerifierRecreateWorldState()),
		SCHNORR_SIGNATURE(new SignerGenerateWorldState(), new VerifierLoadWorldState()),
		SCHNORR_RING_SIGNATURE(new SignerSchnorrRing(), new VerifierSetArraySize());

		State firstProverState, firstVerifierState;

		PROTOCOL(State firstProverState, State firstVerifierState) {
			this.firstProverState = firstProverState;
			this.firstVerifierState = firstVerifierState;
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase().replace("_", " ");
		}
	}
}
