package pl.appsprojekt.systemsecurityii.state.schnorr;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrWorldVerifier;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:  redione1
 * date:    14.12.2016
 */

public class VerifierRecreateWorldState implements State {

	private SchnorrWorldVerifier worldVerifier;
	private INewMainView view;

	public VerifierRecreateWorldState() {
		worldVerifier = new SchnorrWorldVerifier();
	}

	@Override
	public void onPrepare(INewMainView view) {
		this.view = view;
		view.printOutputMessage(new Message("Input world params and press SEND"));
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		Gson gson = new Gson();
		Observable.just(input)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(s -> gson.fromJson(s, Response.class))
				.map(worldVerifier::createFromJson)
				.map(gson::toJson)
				.map(Message::new)
				.subscribe(message -> {
						},
						throwable -> {
							view.printOutputMessage(new Message("Something went wrong"));
							view.printOutputMessage(new Message(throwable.getMessage()));
							view.printOutputMessage(new Message("Input world params and press SEND"));
						},
						listener::onComplete);
	}

	@Override
	public State getNextState() {
		return new VerifierSetXState(worldVerifier);
	}
}
