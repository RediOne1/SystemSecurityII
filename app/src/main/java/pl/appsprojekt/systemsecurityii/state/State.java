package pl.appsprojekt.systemsecurityii.state;

import pl.appsprojekt.systemsecurityii.view.INewMainView;

/**
 * author:  redione1
 * date:    13.12.2016
 */

public interface State {

	void onPrepare(INewMainView view);

	void processInput(String input);

	/**
	 * @return true if input was processed successfully and next state can be executed.
	 */
	boolean canGoToNextState();

	State getNextState();
}
