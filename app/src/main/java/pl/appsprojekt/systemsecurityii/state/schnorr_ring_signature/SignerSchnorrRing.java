package pl.appsprojekt.systemsecurityii.state.schnorr_ring_signature;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrRingSignatureWorldSigner;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:  Adrian Kuta
 * date:    24.01.2017
 */
public class SignerSchnorrRing implements State {

	private SchnorrRingSignatureWorldSigner signer;
	private INewMainView view;

	@Override
	public void onPrepare(INewMainView view) {
		this.view = view;
		view.printOutputMessage(new Message("Enter size of array"));
		view.printOutputMessage(new Message("Press SEND to run Schnorr Ring Signer"));
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		int length;
		try {
			length = Integer.parseInt(input);
		} catch (Exception e) {
			length = 2;
		}
		signer = new SchnorrRingSignatureWorldSigner(length);
		Gson gson = new Gson();
		Observable.create((Observable.OnSubscribe<Response>) subscriber -> {
			signer.generateWorld();
			signer.generateKeys();
			signer.generateRingSign(3);
			subscriber.onNext(signer.getWorldParameters());
			subscriber.onNext(signer.getSecretKeys());
			subscriber.onNext(signer.getPublicKeys());
			subscriber.onNext(signer.getSign());
			subscriber.onCompleted();
		})
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(gson::toJson)
				.map(Message::new)
				.subscribe(
						view::printOutputMessage,
						Throwable::printStackTrace,
						listener::onComplete
				);
	}

	@Override
	public State getNextState() {
		return null;
	}
}
