package pl.appsprojekt.systemsecurityii.state.schnorr_signature;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrSignatureWorldVerifier;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:  redione1
 * date:    14.12.2016
 */

public class VerifierLoadWorldState implements State {

	private SchnorrSignatureWorldVerifier worldVerifier;
	private INewMainView view;
	private boolean success;

	@Override
	public void onPrepare(INewMainView view) {
		this.view = view;
		worldVerifier = new SchnorrSignatureWorldVerifier();
		view.printOutputMessage(new Message("Input world params and press SEND"));
	}

	@Override
	public void processInput(String input) {
		Gson gson = new Gson();
		Observable.just(input)
				.map(s -> gson.fromJson(s, Response.class))
				.subscribe(worldVerifier::setWorldParams,
						throwable -> {
							view.printOutputMessage(new Message("Something went wrong"));
							view.printOutputMessage(new Message(throwable.getMessage()));
							view.printOutputMessage(new Message("Input world params and press SEND"));
							success = false;
						},
						() -> success = true);
	}

	@Override
	public boolean canGoToNextState() {
		return success;
	}

	@Override
	public State getNextState() {
		return new VerifierLoadSignatureState(worldVerifier);
	}
}
