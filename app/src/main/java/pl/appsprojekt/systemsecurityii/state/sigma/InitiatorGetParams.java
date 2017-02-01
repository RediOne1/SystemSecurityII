package pl.appsprojekt.systemsecurityii.state.sigma;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.sigma.SigmaInitiator;
import rx.Observable;

/**
 * author:  redione1
 * date:    01.02.2017
 */

public class InitiatorGetParams implements State {

	private SigmaInitiator initiator;
	private INewMainView view;

	public InitiatorGetParams(SigmaInitiator initiator) {
		this.initiator = initiator;
	}

	@Override
	public void onPrepare(INewMainView view) {
		view.printOutputMessage(new Message("Press SEND, to get Public and Ephemeral Public"));
		this.view = view;
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		Gson gson = new Gson();
		Observable.just(initiator.getFirstParams())
				.map(gson::toJson)
				.map(Message::new)
				.doOnCompleted(listener::onComplete)
				.subscribe(
					view::printOutputMessage
				);
	}

	@Override
	public State getNextState() {
		return new InitiatorCreateSignAndMacState(initiator);
	}
}
