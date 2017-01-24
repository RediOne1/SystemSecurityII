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
 * author:  redione1
 * date:    14.12.2016
 */

public class ProverGenerateWorldState implements State {

	private SchnorrWorldProver worldProver;

	public ProverGenerateWorldState() {
		worldProver = new SchnorrWorldProver();
	}

	@Override
	public void onPrepare(INewMainView view) {
		Gson gson = new Gson();
		Observable.just(worldProver.createNewWorld())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.map(gson::toJson)
				.map(Message::new)
				.subscribe(view::printOutputMessage,
						Throwable::printStackTrace,
						() -> view.printOutputMessage(new Message("Press SEND to get X")));


	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		listener.onComplete();
	}

	@Override
	public State getNextState() {
		return new ProverGetXState(worldProver);
	}
}
