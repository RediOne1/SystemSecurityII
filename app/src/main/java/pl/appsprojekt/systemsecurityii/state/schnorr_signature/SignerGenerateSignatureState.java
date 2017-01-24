package pl.appsprojekt.systemsecurityii.state.schnorr_signature;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrSignatureWorldSigner;
import rx.Observable;

/**
 * author:  redione1
 * date:    14.12.2016
 */

public class SignerGenerateSignatureState implements State {

	private SchnorrSignatureWorldSigner worldSigner;

	public SignerGenerateSignatureState(SchnorrSignatureWorldSigner worldSigner) {
		this.worldSigner = worldSigner;
	}

	@Override
	public void onPrepare(INewMainView view) {
		Gson gson = new Gson();
		worldSigner = new SchnorrSignatureWorldSigner();
		Observable.create((Observable.OnSubscribe<Response>) subscriber -> {
			subscriber.onNext(worldSigner.getSign());
			subscriber.onCompleted();
		})
				.map(response -> {
					response.protocol = "Schnorr Signature";
					response.sender = "S";
					response.success = true;
					response.stage = "generate signature";
					return response;
				})
				.map(gson::toJson)
				.map(Message::new)
				.subscribe(view::printOutputMessage,
						Throwable::printStackTrace,
						() -> view.printOutputMessage(new Message("The End! Type 'reset' to clear output and start from the beginning")));
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
