package pl.appsprojekt.systemsecurityii.injection.module;

import android.content.res.Resources;

import dagger.Module;
import dagger.Provides;
import pl.appsprojekt.systemsecurityii.parser.World;

/**
 * Created by redione1 on 02.11.2016.
 */
@Module
public class WorldGeneratorModule {

	private Resources res;

	public WorldGeneratorModule(Resources res) {
		this.res = res;
	}

	@Provides
	public World provideWorldParser() {
		return new World(res);
	}
}
