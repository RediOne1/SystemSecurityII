package pl.appsprojekt.systemsecurityii.presenter;

import android.support.annotation.IntDef;

import com.google.gson.Gson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.view.IMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrSignatureWorldSigner;
import pl.appsprojekt.systemsecurityii.world.SchnorrSignatureWorldVerifier;
import rx.Observable;
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
	public static final int STAGE_GENERATE_SIGN = 4;
	public static final int STAGE_SET_SIGN = 5;
	public static final int STAGE_VERIFY = 9;
	private Gson gson = new Gson();
	private int selectedMode;
	private int currentStage = STAGE_CHOOSE_MODE;
	private IMainView view;

	private SchnorrSignatureWorldSigner worldSigner;
	private SchnorrSignatureWorldVerifier worldVerifier;

	public MainPresenter() {
		worldSigner = new SchnorrSignatureWorldSigner();
		worldVerifier = new SchnorrSignatureWorldVerifier();
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

	private int getPreviousStage() {
		int previousStage;
		switch (currentStage) {
			case STAGE_GENERATE_WORLD:
			case STAGE_SET_WORLD:
				previousStage = STAGE_CHOOSE_MODE;
				break;
			case STAGE_GENERATE_SIGN:
				previousStage = STAGE_GENERATE_WORLD;
				break;
			case STAGE_SET_SIGN:
				previousStage = STAGE_SET_WORLD;
				break;
			case STAGE_VERIFY:
				previousStage = STAGE_SET_SIGN;
				break;

			default:
				previousStage = STAGE_CHOOSE_MODE;
		}
		return previousStage;
	}

	private int getNextStage() {
		int nextStage;
		switch (currentStage) {
			case STAGE_CHOOSE_MODE:
				nextStage = selectedMode == MODE_PROVER ? STAGE_GENERATE_WORLD : STAGE_SET_WORLD;
				break;
			case STAGE_GENERATE_WORLD:
				nextStage = STAGE_GENERATE_SIGN;
				break;
			case STAGE_SET_WORLD:
				nextStage = STAGE_SET_SIGN;
				break;
			case STAGE_SET_SIGN:
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
		Observable.just(worldSigner.getWorldParameters())
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

	public void generateSign() {
		Observable.just(worldSigner.getSign())
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(gson::toJson)
				.subscribe(
						s -> {
							showNextStage();
							view.showJson(s);
						},
						Throwable::printStackTrace
				);
	}

	public void setWorld(String jsonWorld) {
		Observable.just(jsonWorld)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(s -> gson.fromJson(s, Response.class))
				.map(worldVerifier::setWorldParams)
				.map(gson::toJson)
				.subscribe(
						s -> {
							view.showJson(s);
							showNextStage();
						},
						Throwable::printStackTrace
				);
	}

	public void setSign(String json) {
		Observable.just(json)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(s -> gson.fromJson(s, Response.class))
				.map(worldVerifier::setSignerParams)
				.map(gson::toJson)
				.subscribe(
						s -> {
							view.showJson(s);
							showNextStage();
						},
						Throwable::printStackTrace
				);
	}

	public void verify() {
		Observable.just(worldVerifier.getVerification())
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(gson::toJson)
				.subscribe(
						s -> {
							showNextStage();
							view.showJson(s);
						},
						Throwable::printStackTrace
				);
	}

	@IntDef({MODE_PROVER, MODE_VERIFIER})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Mode {
	}


}
