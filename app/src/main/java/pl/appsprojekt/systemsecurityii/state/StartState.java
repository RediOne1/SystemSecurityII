package pl.appsprojekt.systemsecurityii.state;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.view.INewMainView;

/**
 * author:  redione1
 * date:    14.12.2016
 */
public class StartState implements State {
	@Override
	public void onPrepare(INewMainView view) {

	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		listener.onComplete();
	}

	@Override
	public State getNextState() {
		return new ChooseModeState(ChooseProtocolState.PROTOCOL.SIGMA);
	}
}
