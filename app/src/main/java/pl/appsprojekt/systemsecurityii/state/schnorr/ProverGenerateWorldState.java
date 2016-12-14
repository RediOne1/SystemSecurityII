package pl.appsprojekt.systemsecurityii.state.schnorr;

import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;

/**
 * author:  redione1
 * date:    14.12.2016
 */

public class ProverGenerateWorldState implements State {
	@Override
	public void onPrepare(INewMainView view) {
		view.printOutputMessage(new Message("super"));
	}

	@Override
	public void processInput(String input) {
	}

	@Override
	public boolean canGoToNextState() {
		return false;
	}

	@Override
	public State getNextState() {
		return null;
	}
}
