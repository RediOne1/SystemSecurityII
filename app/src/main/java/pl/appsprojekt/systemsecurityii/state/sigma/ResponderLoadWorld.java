package pl.appsprojekt.systemsecurityii.state.sigma;

import com.google.gson.Gson;

import java.math.BigInteger;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.sigma.SigmaResponder;
import pl.appsprojekt.systemsecurityii.world.sigma.WorldParameters;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:  redione1
 * date:    01.02.2017
 */

public class ResponderLoadWorld implements State {

	SigmaResponder responder;
	private INewMainView view;

	@Override
	public void onPrepare(INewMainView view) {
		this.view = view;
		view.printOutputMessage(new Message("Insert World JSON and press SEND"));
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		Gson gson = new Gson();
		Single.create((Single.OnSubscribe<Response>) singleSubscriber -> {
			Response worldInput = gson.fromJson(input, Response.class);

			BigInteger A = worldInput.getParam("A");
			BigInteger B = worldInput.getParam("B");
			BigInteger Q = worldInput.getParam("Q");
			BigInteger Gx = worldInput.getParam("Gx");
			BigInteger Gy = worldInput.getParam("Gy");
			BigInteger N = worldInput.getParam("N");
			BigInteger sessionID = worldInput.getParam("sessionID");

			WorldParameters worldParameters = new WorldParameters(A, B, Q, Gx, Gy, N);

			BigInteger myID = worldParameters.generateRandomInTheWorld();
			BigInteger secretKey = worldParameters.generateRandomInTheWorld();

			responder = new SigmaResponder(worldParameters, sessionID, myID, secretKey);

			singleSubscriber.onSuccess(null);
		})
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						response -> listener.onComplete()
				);
	}

	@Override
	public State getNextState() {
		return new ResponderCreateSignAndMacState(responder);
	}
}
