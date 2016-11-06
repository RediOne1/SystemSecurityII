package pl.appsprojekt.systemsecurityii.injection.module;

import dagger.Module;
import dagger.Provides;
import pl.appsprojekt.systemsecurityii.presenter.MainPresenter;
import pl.appsprojekt.systemsecurityii.usecase.MyWorldUsecase;

/**
 * Created by redione1 on 02.11.2016.
 */
@Module
public class PresenterModule {

	@Provides
	public MainPresenter providesMainPresenter() {
		return new MainPresenter(new MyWorldUsecase());
	}
}
