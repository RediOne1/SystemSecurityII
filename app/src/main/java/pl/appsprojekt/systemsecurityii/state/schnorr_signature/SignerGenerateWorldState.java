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

public class SignerGenerateWorldState implements State {

	private SchnorrSignatureWorldSigner worldSigner;

	@Override
	public void onPrepare(INewMainView view) {
		view.printOutputMessage(new Message("Generated world"));
		Gson gson = new Gson();
		worldSigner = new SchnorrSignatureWorldSigner();
		Observable.create((Observable.OnSubscribe<Response>) subscriber -> {
			worldSigner.generateWorld();
			subscriber.onNext(worldSigner.getWorldParameters());
			subscriber.onCompleted();
		})
				.map(response -> {
					response.protocol = "Schnorr Signature";
					response.sender = "S";
					response.success = true;
					response.stage = "generate world";
					return response;
				})
				.map(gson::toJson)
				.map(Message::new)
				.subscribe(view::printOutputMessage,
						Throwable::printStackTrace,
						() -> view.printOutputMessage(new Message("press SEND to generate signature")));
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		listener.onComplete();
	}

	@Override
	public State getNextState() {
		return new SignerGenerateSignatureState(worldSigner);
	}
}
