package pl.appsprojekt.systemsecurityii.state.schnorr;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrWorldVerifier;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:  Adrian Kuta
 * date:    14.12.2016
 */
public class VerifierGetCState implements State {

	private SchnorrWorldVerifier worldVerifier;

	public VerifierGetCState(SchnorrWorldVerifier worldVerifier) {
		this.worldVerifier = worldVerifier;
	}

	@Override
	public void onPrepare(INewMainView view) {

		Gson gson = new Gson();
		Observable.just(worldVerifier.getC())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.map(gson::toJson)
				.map(Message::new)
				.subscribe(view::printOutputMessage,
						Throwable::printStackTrace,
						() -> view.printOutputMessage(new Message("Press SEND to go next")));
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
		return new VerifierGetVerificationState(worldVerifier);
	}
}
