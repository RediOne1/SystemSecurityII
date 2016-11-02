package pl.appsprojekt.systemsecurityii.injection;

import dagger.Component;
import pl.appsprojekt.systemsecurityii.injection.module.WorldParserModule;
import pl.appsprojekt.systemsecurityii.usecase.WorldUsecase;

/**
 * Created by redione1 on 02.11.2016.
 */
@Component(modules = {WorldParserModule.class})
public interface ApplicationInjector {
	void inject(WorldUsecase worldUsecase);
}
