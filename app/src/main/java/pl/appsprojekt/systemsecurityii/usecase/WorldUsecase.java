package pl.appsprojekt.systemsecurityii.usecase;

import pl.appsprojekt.systemsecurityii.model.Response;
import rx.Observable;

/**
 * author:  redione1
 * date:    05.11.2016
 */

public interface WorldUsecase<T> {

	public Observable<T> generateWorld();

	public Observable<T> setWorld(String json);

	public Observable<T> getX();

	public Observable<T> setX(Response responseWithX);

	public Observable<T> getC();

	public Observable<T> setC(Response responseWithC);

	public Observable<T> getS();

	public Observable<T> getVerification(Response responseWithS);
}
