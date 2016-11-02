package pl.appsprojekt.systemsecurityii;

import android.app.Application;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

import pl.appsprojekt.systemsecurityii.injection.ApplicationInjector;
import pl.appsprojekt.systemsecurityii.injection.DaggerApplicationInjector;
import pl.appsprojekt.systemsecurityii.injection.module.WorldParserModule;

/**
 * Created by redione1 on 02.11.2016.
 */

public class MyApplication extends Application {

	private static ApplicationInjector applicationInjector;

	@Override
	public void onCreate() {
		super.onCreate();
		Security.insertProviderAt(new BouncyCastleProvider(), 1);

		applicationInjector = DaggerApplicationInjector.builder()
				.worldParserModule(new WorldParserModule(getResources()))
				.build();
	}

	public static ApplicationInjector getApplicationInjector() {
		return applicationInjector;
	}
}
