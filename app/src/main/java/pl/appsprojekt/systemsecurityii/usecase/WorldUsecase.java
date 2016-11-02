package pl.appsprojekt.systemsecurityii.usecase;

import javax.inject.Inject;

import pl.appsprojekt.systemsecurityii.MyApplication;
import pl.appsprojekt.systemsecurityii.model.World;
import pl.appsprojekt.systemsecurityii.parser.WorldParser;
import rx.Observable;

/**
 * Created by redione1 on 02.11.2016.
 */

public class WorldUsecase implements Usecase<World> {

	@Inject
	WorldParser worldParser;

	public WorldUsecase() {
		MyApplication.getApplicationInjector().inject(this);
	}

	@Override
	public Observable<World> execute() {
		return Observable.just(worldParser.get());
	}


}
