package pl.appsprojekt.systemsecurityii.usecase;

import javax.inject.Inject;

import pl.appsprojekt.systemsecurityii.MyApplication;
import pl.appsprojekt.systemsecurityii.model.Response;
import pl.appsprojekt.systemsecurityii.parser.World;
import rx.Observable;

/**
 * author:  redione1
 * date:    02.11.2016
 */

public class MyWorldUsecase implements WorldUsecase<Response> {

	@Inject
	World world;

	public MyWorldUsecase() {
		MyApplication.getApplicationInjector().inject(this);
	}

	@Override
	public Observable<Response> generateWorld() {
		return Observable.just(world.generateNewWorld());
	}

	@Override
	public Observable<Response> setWorld(String json) {
		return Observable.just(world.createFromJson(json));
	}

	@Override
	public Observable<Response> getX() {
		return Observable.just(world.getX());
	}

	@Override
	public Observable<Response> setX(Response responseWithX) {
		return Observable.just(world.setX(responseWithX));
	}

	@Override
	public Observable<Response> getC() {
		return Observable.just(world.getC());
	}

	@Override
	public Observable<Response> setC(Response responseWithC) {
		return Observable.just(world.setC(responseWithC));
	}

	@Override
	public Observable<Response> getS() {
		return Observable.just(world.getS());
	}

	@Override
	public Observable<Response> getVerification(Response responseWithS) {
		return Observable.just(world.getVerification(responseWithS));
	}

}
