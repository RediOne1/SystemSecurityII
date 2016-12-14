package pl.appsprojekt.systemsecurityii.state.schnorr_signature;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.Utils;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrSignatureWorldSigner;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(response -> {
					response.protocol = "Schnorr Signature";
					response.sender = "S";
					response.success = true;
					response.stage = "generate signature";
					return response;
				})
				.map(gson::toJson)
				.map(Utils::jsonToPrettyString)
				.map(Message::new)
				.subscribe(view::printOutputMessage,
						Throwable::printStackTrace,
						() -> view.printOutputMessage(new Message("The End! Type 'reset' to clear output and start from the beginning")));
	}

	@Override
	public void processInput(String input) {

	}

	@Override
	public boolean canGoToNextState() {
		return true;
	}

	@Override
	public State getNextState() {
		return null;
	}
}
