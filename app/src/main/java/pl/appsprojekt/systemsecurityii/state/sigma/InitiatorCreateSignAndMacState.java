package pl.appsprojekt.systemsecurityii.state.sigma;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.sigma.SigmaInitiator;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:  redione1
 * date:    01.02.2017
 */

public class InitiatorCreateSignAndMacState implements State {

	private SigmaInitiator initiator;
	private INewMainView view;

	public InitiatorCreateSignAndMacState(SigmaInitiator initiator) {
		this.initiator = initiator;
	}

	@Override
	public void onPrepare(INewMainView view) {
		view.printOutputMessage(new Message("Insert Public, Ephemeral Public, MAC and sign. Then press SEND"));
		this.view = view;
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		Gson gson = new Gson();
		Single.create((Single.OnSubscribe<Response>) singleSubscriber -> {
			Response response = gson.fromJson(input, Response.class);
			Response signAndMAC = initiator.getSignAndMAC(response);
			singleSubscriber.onSuccess(signAndMAC);
		})
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(gson::toJson)
				.map(Message::new)
				.doOnSuccess(message -> listener.onComplete())
				.subscribe(
						view::printOutputMessage
				);
	}

	@Override
	public State getNextState() {
		return null;
	}
}
