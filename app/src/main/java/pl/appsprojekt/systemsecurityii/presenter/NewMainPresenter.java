package pl.appsprojekt.systemsecurityii.presenter;

import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.state.StartState;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;

/**
 * author:  redione1
 * date:    13.12.2016
 */

public class NewMainPresenter implements Presenter<INewMainView> {


	private State currentState;

	private INewMainView view;

	public NewMainPresenter() {
		init();
	}

	private void init() {
		currentState = new StartState();
	}

	@Override
	public void attachView(INewMainView view) {
		this.view = view;
		nextState();
	}

	private void nextState() {
		if (currentState.canGoToNextState()) {
			currentState = currentState.getNextState();
			currentState.onPrepare(view);
		}
	}

	public void onUserInput(String userInput) {
		view.printOutputMessage(new Message(userInput, true));
		if (userInput.equalsIgnoreCase("reset")) {
			view.cleatOutput();
			init();
		}
		if (currentState != null) {
			currentState.processInput(userInput);
			nextState();
		}
	}
}
