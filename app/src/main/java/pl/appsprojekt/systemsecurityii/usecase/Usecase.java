package pl.appsprojekt.systemsecurityii.usecase;

import rx.Observable;

/**
 * Created by redione1 on 02.11.2016.
 */

public interface Usecase<T> {

	public Observable<T> execute();
}
