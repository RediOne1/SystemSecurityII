package pl.appsprojekt.systemsecurityii.state.schnorr;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrWorldVerifier;
import rx.Observable;

/**
 * author:  Adrian Kuta
 * date:    14.12.2016
 */
public class VerifierGetVerificationState implements State {

	private SchnorrWorldVerifier worldVerifier;
	private INewMainView view;

	public VerifierGetVerificationState(SchnorrWorldVerifier worldVerifier) {
		this.worldVerifier = worldVerifier;
	}

	@Override
	public void onPrepare(INewMainView view) {
		this.view = view;
		view.printOutputMessage(new Message("Input S and press SEND"));
	}

	@Override
	public void processInput(String input) {
		Gson gson = new Gson();
		Observable.just(input)
				.map(s -> gson.fromJson(s, Response.class))
				.map(worldVerifier::getVerification)
				.map(gson::toJson)
				.map(Message::new)
				.subscribe(view::printOutputMessage,
						Throwable::printStackTrace,
						() -> view.printOutputMessage(new Message("The End")));

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
