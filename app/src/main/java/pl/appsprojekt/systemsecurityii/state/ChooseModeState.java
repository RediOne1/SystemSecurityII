package pl.appsprojekt.systemsecurityii.state;

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
		else
			message = new Message("Choose mode: type SIGNER or VERIFIER");
		view.printOutputMessage(message);
	}

	@Override
	public void processInput(String input) {
		isVerifier = input.equalsIgnoreCase("verifier");
	}

	@Override
	public boolean canGoToNextState() {
		return true;
	}

	@Override
	public State getNextState() {
		return isVerifier ? selectedProtocol.firstVerifierState : selectedProtocol.firstProverState;
	}
}
