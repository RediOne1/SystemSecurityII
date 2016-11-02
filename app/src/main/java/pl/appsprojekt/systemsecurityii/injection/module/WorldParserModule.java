package pl.appsprojekt.systemsecurityii.injection.module;

import android.content.res.Resources;

import dagger.Module;
import dagger.Provides;
import pl.appsprojekt.systemsecurityii.parser.WorldParser;

/**
 * Created by redione1 on 02.11.2016.
 */
@Module
public class WorldParserModule {

	private Resources res;

	public WorldParserModule(Resources res) {
		this.res = res;
	}

	@Provides
	public WorldParser provideWorldParser() {
		return new WorldParser(res);
	}
}
