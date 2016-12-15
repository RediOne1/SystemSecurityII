package pl.appsprojekt.systemsecurityii.state.schnorr_signature;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
	public void processInput(String input) {

	}

	@Override
	public boolean canGoToNextState() {
		return true;
	}

	@Override
	public State getNextState() {
		return new SignerGenerateSignatureState(worldSigner);
	}
}
