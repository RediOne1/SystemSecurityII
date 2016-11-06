package pl.appsprojekt.systemsecurityii.injection;

import dagger.Component;
import pl.appsprojekt.systemsecurityii.injection.module.PresenterModule;
import pl.appsprojekt.systemsecurityii.ui.activity.MainActivity;

/**
 * Created by redione1 on 02.11.2016.
 */

@Component(modules = {PresenterModule.class})
public interface PresenterInjector {
	public void inject(MainActivity mainActivity);
}
