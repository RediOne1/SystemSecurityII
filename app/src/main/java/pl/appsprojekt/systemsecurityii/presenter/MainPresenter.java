package pl.appsprojekt.systemsecurityii.presenter;

import android.support.annotation.IntDef;

import com.google.gson.Gson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.view.IMainView;
import pl.appsprojekt.systemsecurityii.world.SchnorrRingSignatureWorldSigner;
import pl.appsprojekt.systemsecurityii.world.SchnorrRingSignatureWorldVerifier;
import pl.appsprojekt.systemsecurityii.world.SchnorrSignatureWorldSigner;
import pl.appsprojekt.systemsecurityii.world.SchnorrSignatureWorldVerifier;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by redione1 on 02.11.2016.
 */

public class MainPresenter implements Presenter<IMainView> {

	private static final int LENGTH = 5;

	public static final int MODE_PROVER = 1;
	public static final int MODE_VERIFIER = 2;

	public static final int STAGE_NONE = 0;
	public static final int STAGE_CHOOSE_MODE = 1;
	public static final int STAGE_GENERATE_WORLD = 2;
	public static final int STAGE_SET_WORLD = 3;
	public static final int STAGE_SET_PUBLIC_KEYS = 4;
	public static final int STAGE_SET_SIGN = 5;
	public static final int STAGE_VERIFY = 9;
	private Gson gson = new Gson();
	private int selectedMode;
	private int currentStage = STAGE_CHOOSE_MODE;
	private IMainView view;

	private SchnorrRingSignatureWorldSigner worldSigner;
	private SchnorrRingSignatureWorldVerifier worldVerifier;

	public MainPresenter() {
		worldSigner = new SchnorrRingSignatureWorldSigner(LENGTH);
		worldVerifier = new SchnorrRingSignatureWorldVerifier(LENGTH);
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
			case STAGE_SET_PUBLIC_KEYS:
				previousStage = STAGE_SET_WORLD;
				break;
			case STAGE_SET_SIGN:
				previousStage = STAGE_SET_PUBLIC_KEYS;
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
			case STAGE_SET_WORLD:
				nextStage = STAGE_SET_PUBLIC_KEYS;
				break;
			case STAGE_SET_PUBLIC_KEYS:
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
		Single.create((Single.OnSubscribe<Response>) subscriber -> {
			worldSigner.generateWorld();
			Response worldParameters = worldSigner.getWorldParameters();
			subscriber.onSuccess(worldParameters);
		})
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

	public void getPublicKeys() {
		Single.create((Single.OnSubscribe<Response>) subscriber -> {
			worldSigner.generateKeys();
			Response worldParameters = worldSigner.getPublicKeys();
			subscriber.onSuccess(worldParameters);
		})
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

	public void getSignature() {
		Single.create((Single.OnSubscribe<Response>) subscriber -> {
			worldSigner.generateRingSign(3);
			Response worldParameters = worldSigner.getSign();
			subscriber.onSuccess(worldParameters);
		})
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

	public void setPublicKeys(String jsonPublicKeys) {
		Observable.just(jsonPublicKeys)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.map(s -> gson.fromJson(s, Response.class))
				.map(worldVerifier::setPublicKeys)
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
		Observable.just(worldVerifier.verify())
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
