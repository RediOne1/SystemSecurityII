package pl.appsprojekt.systemsecurityii.state.sigma;

import com.google.gson.Gson;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.sigma.SigmaResponder;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:  redione1
 * date:    01.02.2017
 */

public class ResponderVerificationState implements State {

	private SigmaResponder responder;
	private INewMainView view;

	public ResponderVerificationState(SigmaResponder responder) {
		this.responder = responder;
	}

	@Override
	public void onPrepare(INewMainView view) {
		this.view = view;
		view.printOutputMessage(new Message("Insert Initiator Sign and MAC"));
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		Gson gson = new Gson();
		Single.create((Single.OnSubscribe<Response>) subscriber -> {
			Response inputResponse = gson.fromJson(input, Response.class);
			Response response = responder.verifyCorrectness(inputResponse);
			subscriber.onSuccess(response);
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
