package pl.appsprojekt.systemsecurityii.state.sigma;

import com.google.gson.Gson;

import java.math.BigInteger;

import pl.appsprojekt.systemsecurityii.interfaces.IOnCompletionListener;
import pl.appsprojekt.systemsecurityii.model.Message;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.state.State;
import pl.appsprojekt.systemsecurityii.view.INewMainView;
import pl.appsprojekt.systemsecurityii.world.sigma.SigmaInitiator;
import pl.appsprojekt.systemsecurityii.world.sigma.WorldParameters;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author:  redione1
 * date:    01.02.2017
 */

public class InitiatorCreateWorldState implements State {

	private SigmaInitiator initiator;
	private INewMainView view;

	@Override
	public void onPrepare(INewMainView view) {
		view.printOutputMessage(new Message("Press SEND to generate World"));
		this.view = view;
	}

	@Override
	public void processInput(String input, IOnCompletionListener listener) {
		Gson gson = new Gson();

		Single.create((Single.OnSubscribe<Response>) singleSubscriber -> {

			WorldParameters worldParameters = new WorldParameters("prime256v1");
			BigInteger sessionId = worldParameters.generateRandomInTheWorld();
			BigInteger myId = worldParameters.generateRandomInTheWorld();
			BigInteger secretKey = worldParameters.generateRandomInTheWorld();

			initiator = new SigmaInitiator(worldParameters, sessionId, myId, secretKey);

			Response response = new Response();
			BigInteger A = worldParameters.A;
			BigInteger B = worldParameters.B;
			BigInteger Q = worldParameters.Q;
			BigInteger Gx = worldParameters.Gx;
			BigInteger Gy = worldParameters.Gy;
			BigInteger N = worldParameters.N;

			response.addParam("A", A);
			response.addParam("B", B);
			response.addParam("Q", Q);
			response.addParam("Gx", Gx);
			response.addParam("Gy", Gy);
			response.addParam("N", N);
			response.addParam("sessionID", sessionId);
			singleSubscriber.onSuccess(response);
		})
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(gson::toJson)
				.map(Message::new)
				.subscribe(
						message -> {
							view.printOutputMessage(message);
							listener.onComplete();
						}
				);

	}

	@Override
	public State getNextState() {
		return new InitiatorGetParams(initiator);
	}
}
