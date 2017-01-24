package pl.appsprojekt.systemsecurityii.state.schnorr_ring_signature;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrRingSignatureWorldVerifier;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:  Adrian Kuta
 * date:    24.01.2017
 */
public class VerifierVerify implements State {

	private SchnorrRingSignatureWorldVerifier verifier;

	public VerifierVerify(SchnorrRingSignatureWorldVerifier verifier) {
		this.verifier = verifier;
	}

	@Override
	public void onPrepare(INewMainView view) {
		Gson gson = new Gson();
		Observable.just(verifier.verify())
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(gson::toJson)
				.map(Message::new)
				.subscribe(
						view::printOutputMessage,
						Throwable::printStackTrace
				);

	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		listener.onComplete();
	}

	@Override
	public State getNextState() {
		return null;
	}
}
