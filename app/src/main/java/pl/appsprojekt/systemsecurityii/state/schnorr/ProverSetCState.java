package pl.appsprojekt.systemsecurityii.state.schnorr;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrWorldProver;
import rx.Observable;

/**
 * author:  Adrian Kuta
 * date:    14.12.2016
 */
public class ProverSetCState implements State {
	private INewMainView view;
	private SchnorrWorldProver worldProver;

	public ProverSetCState(SchnorrWorldProver worldProver) {
		this.worldProver = worldProver;
	}

	@Override
	public void onPrepare(INewMainView view) {
		this.view = view;
		view.printOutputMessage(new Message("Input C json and press SEND"));
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		Gson gson = new Gson();
		Observable.just(input)
				.map(s -> gson.fromJson(s, Response.class))
				.map(worldProver::setC)
				.map(gson::toJson)
				.map(Message::new)
				.subscribe(
						view::printOutputMessage,
						throwable -> {
							throwable.printStackTrace();
							view.printOutputMessage(new Message(throwable.getMessage()));
						},
						listener::onComplete
				);
	}

	@Override
	public State getNextState() {
		return new ProverGetSState(worldProver);
	}
}
