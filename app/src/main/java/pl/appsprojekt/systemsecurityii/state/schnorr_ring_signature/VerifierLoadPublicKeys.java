package pl.appsprojekt.systemsecurityii.state.schnorr_ring_signature;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
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
public class VerifierLoadPublicKeys implements State {

	private SchnorrRingSignatureWorldVerifier verifier;

	public VerifierLoadPublicKeys(SchnorrRingSignatureWorldVerifier verifier) {
		this.verifier = verifier;
	}

	@Override
	public void onPrepare(INewMainView view) {
		view.printOutputMessage(new Message("Enter PublicKeys JSON & tap SEND"));
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		Gson gson = new Gson();
		Observable.just(input)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(s -> gson.fromJson(s, Response.class))
				.doOnNext(response -> verifier.setPublicKeys(response))
				.subscribe(
						response -> {
						},
						Throwable::printStackTrace,
						listener::onComplete
				);
	}

	@Override
	public State getNextState() {
		return new VerifierLoadSignerParams(verifier);
	}
}
