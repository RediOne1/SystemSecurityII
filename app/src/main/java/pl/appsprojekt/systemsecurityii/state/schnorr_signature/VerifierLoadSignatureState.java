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

public class VerifierLoadSignatureState implements State {

	private SchnorrSignatureWorldVerifier worldVerifier;
	private boolean success;
	private INewMainView view;

	public VerifierLoadSignatureState(SchnorrSignatureWorldVerifier worldVerifier) {
		this.worldVerifier = worldVerifier;
	}

	@Override
	public void onPrepare(INewMainView view) {
		this.view = view;
		view.printOutputMessage(new Message("Input signature json and press SEND"));
	}

	@Override
	public void processInput(String input) {
		Gson gson = new Gson();
		Observable.just(input)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(s -> gson.fromJson(s, Response.class))
				.subscribe(worldVerifier::setSignerParams,
						throwable -> {
							view.printOutputMessage(new Message("Something went wrong"));
							view.printOutputMessage(new Message(throwable.getMessage()));
							view.printOutputMessage(new Message("Input signature json and press SEND"));
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
		return new VerifierGetVerificationState(worldVerifier);
	}
}
