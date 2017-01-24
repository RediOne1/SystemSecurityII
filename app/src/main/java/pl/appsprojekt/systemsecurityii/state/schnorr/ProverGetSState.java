package pl.appsprojekt.systemsecurityii.state.schnorr;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrWorldProver;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:  Adrian Kuta
 * date:    14.12.2016
 */
public class ProverGetSState implements State {

	private SchnorrWorldProver worldProver;

	public ProverGetSState(SchnorrWorldProver worldProver) {
		this.worldProver = worldProver;
	}

	@Override
	public void onPrepare(INewMainView view) {

		Gson gson = new Gson();
		Observable.just(worldProver.getS())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.map(gson::toJson)
				.map(Message::new)
				.subscribe(view::printOutputMessage,
						Throwable::printStackTrace,
						() -> view.printOutputMessage(new Message("The end, wait for verification")));
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
