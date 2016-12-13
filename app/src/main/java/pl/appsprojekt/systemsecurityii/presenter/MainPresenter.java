package pl.appsprojekt.systemsecurityii.presenter;

import android.support.annotation.IntDef;

import com.google.gson.Gson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.usecase.WorldUsecase;
import pl.appsprojekt.systemsecurityii.view.IMainView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by redione1 on 02.11.2016.
 */

public class MainPresenter implements Presenter<IMainView> {

	public static final int MODE_PROVER = 1;
	public static final int MODE_VERIFIER = 2;

	public static final int STAGE_NONE = 0;
	public static final int STAGE_CHOOSE_MODE = 1;
	public static final int STAGE_GENERATE_WORLD = 2;
	public static final int STAGE_SET_WORLD = 3;
	public static final int STAGE_SEND_X = 4;
	public static final int STAGE_SET_X = 5;
	public static final int STAGE_SEND_C = 6;
	public static final int STAGE_RECEIVE_C = 7;
	public static final int STAGE_SEND_S = 8;
	public static final int STAGE_VERIFY = 9;
	private Gson gson = new Gson();
	private int selectedMode;
	private
	@Stage
	int currentStage = STAGE_CHOOSE_MODE;
	private IMainView view;
	private WorldUsecase<Response> worldUsecase;

	public MainPresenter(WorldUsecase<Response> worldUsecase) {
		this.worldUsecase = worldUsecase;
	}

	@Override
	public void attachView(IMainView view) {
		this.view = view;
	}

	public void onModeSelected(@Mode int selectedMode) {
		this.selectedMode = selectedMode;
		showNextStage();
	}

	private void showNextStage() {
		currentStage = getNextStage();
		view.showStage(currentStage);
	}

	private void showPreviousStage() {
		currentStage = getPreviousStage();
		view.showStage(currentStage);
	}

	private
	@Stage
	int getPreviousStage() {
		int previousStage;
		switch (currentStage) {
			case STAGE_GENERATE_WORLD:
			case STAGE_SET_WORLD:
				previousStage = STAGE_CHOOSE_MODE;
				break;
			case STAGE_SEND_S:
				previousStage = STAGE_RECEIVE_C;
				break;
			case STAGE_RECEIVE_C:
				previousStage = STAGE_SEND_X;
				break;
			case STAGE_SEND_X:
				previousStage = STAGE_GENERATE_WORLD;
				break;
			case STAGE_VERIFY:
				previousStage = STAGE_SEND_C;
				break;
			case STAGE_SEND_C:
				previousStage = STAGE_SET_X;
				break;
			case STAGE_SET_X:
				previousStage = STAGE_SET_WORLD;
				break;

			default:
				previousStage = STAGE_CHOOSE_MODE;
		}
		return previousStage;
	}

	private
	@Stage
	int getNextStage() {
		int nextStage;
		switch (currentStage) {
			case STAGE_CHOOSE_MODE:
				nextStage = selectedMode == MODE_PROVER ? STAGE_GENERATE_WORLD : STAGE_SET_WORLD;
				break;
			case STAGE_GENERATE_WORLD:
				nextStage = STAGE_SEND_X;
				break;
			case STAGE_SEND_X:
				nextStage = STAGE_RECEIVE_C;
				break;
			case STAGE_RECEIVE_C:
				nextStage = STAGE_SEND_S;
				break;
			case STAGE_SET_WORLD:
				nextStage = STAGE_SET_X;
				break;
			case STAGE_SET_X:
				nextStage = STAGE_SEND_C;
				break;
			case STAGE_SEND_C:
				nextStage = STAGE_VERIFY;
				break;
			default:
				nextStage = currentStage;
		}
		return nextStage;
	}

	public void onBackPressed() {
		showPreviousStage();
	}

	public void generateWorld() {
		worldUsecase.generateWorld()
				.map(gson::toJson)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.subscribe(
						s -> {
							showNextStage();
							view.showJson(s);
						},
						Throwable::printStackTrace
				);
	}

	public void setWorld(String jsonWorld) {
		worldUsecase.setWorld(jsonWorld)
				.map(gson::toJson)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						s -> {
							showNextStage();
							view.showJson(s);
						},
						Throwable::printStackTrace
				);
	}

	public void sendX() {
		worldUsecase.getX()
				.map(gson::toJson)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						s -> {
							showNextStage();
							view.showJson(s);
						},
						Throwable::printStackTrace
				);
	}

	public void insertX(String jsonX) {
		Response responseWithX = gson.fromJson(jsonX, Response.class);
		worldUsecase.setX(responseWithX)
				.map(gson::toJson)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(s -> {
							showNextStage();
							view.showJson(s);
						},
						Throwable::printStackTrace);
	}

	public void sendC() {
		worldUsecase.getC()
				.map(gson::toJson)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						s -> {
							showNextStage();
							view.showJson(s);
						},
						Throwable::printStackTrace
				);
	}

	public void insertJsonC(String jsonC) {
		Response responseWithC = gson.fromJson(jsonC, Response.class);
		worldUsecase.setC(responseWithC)
				.map(gson::toJson)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(s -> {
							showNextStage();
							view.showJson(s);
						},
						Throwable::printStackTrace);
	}


	public void sendS() {
		worldUsecase.getS()
				.map(gson::toJson)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						s -> {
							showNextStage();
							view.showJson(s);
						},
						Throwable::printStackTrace
				);
	}

	public void verify(String jsonS) {
		Response responseWithS = gson.fromJson(jsonS, Response.class);
		worldUsecase.getVerification(responseWithS)
				.map(gson::toJson)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						s -> {
							showNextStage();
							view.showJson(s);
						},
						Throwable::printStackTrace
				);
	}

	@IntDef({STAGE_NONE, STAGE_CHOOSE_MODE, STAGE_GENERATE_WORLD, STAGE_SET_WORLD, STAGE_SEND_X, STAGE_SET_X, STAGE_SEND_C, STAGE_RECEIVE_C, STAGE_SEND_S, STAGE_VERIFY})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Stage {
	}

	@IntDef({MODE_PROVER, MODE_VERIFIER})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Mode {
	}


}
