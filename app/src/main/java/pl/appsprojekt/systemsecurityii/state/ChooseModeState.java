package pl.appsprojekt.systemsecurityii.state;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.view.INewMainView;

/**
 * author:  redione1
 * date:    13.12.2016
 */

public class ChooseModeState implements State {

	private ChooseProtocolState.PROTOCOL selectedProtocol;
	private boolean isVerifier;

	public ChooseModeState(ChooseProtocolState.PROTOCOL selectedProtocol) {
		this.selectedProtocol = selectedProtocol;
	}

	@Override
	public void onPrepare(INewMainView view) {
		Message message;
		if (selectedProtocol == ChooseProtocolState.PROTOCOL.SCHNORR)
			message = new Message("Choose mode: type PROVER or VERIFIER");
		else if (selectedProtocol == ChooseProtocolState.PROTOCOL.SIGMA)
			message = new Message("Choose mode: type INITIATOR or RESPONDER");
		else
			message = new Message("Choose mode: type SIGNER or VERIFIER");
		view.printOutputMessage(message);
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		isVerifier = input.equalsIgnoreCase("verifier") || input.equalsIgnoreCase("responder");
		listener.onComplete();
	}

	@Override
	public State getNextState() {
		return isVerifier ? selectedProtocol.firstVerifierState : selectedProtocol.firstProverState;
	}
}
