package pl.appsprojekt.systemsecurityii.injection;

import dagger.Component;
import pl.appsprojekt.systemsecurityii.injection.module.WorldGeneratorModule;
import pl.appsprojekt.systemsecurityii.usecase.MyWorldUsecase;

/**
 * Created by redione1 on 02.11.2016.
 */
@Component(modules = {WorldGeneratorModule.class})
public interface ApplicationInjector {
	void inject(MyWorldUsecase myWorldUsecase);
}
