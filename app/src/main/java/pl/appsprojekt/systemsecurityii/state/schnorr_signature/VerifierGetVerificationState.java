package pl.appsprojekt.systemsecurityii.state.schnorr_signature;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.Utils;
import pl.appsprojekt.systemsecurityii.model.Message;
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

public class VerifierGetVerificationState implements State {

	private SchnorrSignatureWorldVerifier worldVerifier;

	public VerifierGetVerificationState(SchnorrSignatureWorldVerifier worldVerifier) {
		this.worldVerifier = worldVerifier;
	}

	@Override
	public void onPrepare(INewMainView view) {
		Gson gson = new Gson();
		Observable.just(worldVerifier.getVerification())
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(gson::toJson)
				.map(Utils::jsonToPrettyString)
				.map(Message::new)
				.subscribe(view::printOutputMessage);
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
