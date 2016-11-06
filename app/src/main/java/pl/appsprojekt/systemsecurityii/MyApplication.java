package pl.appsprojekt.systemsecurityii;

import android.app.Application;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.Security;

import pl.appsprojekt.systemsecurityii.injection.ApplicationInjector;
import pl.appsprojekt.systemsecurityii.injection.DaggerApplicationInjector;
import pl.appsprojekt.systemsecurityii.injection.DaggerPresenterInjector;
import pl.appsprojekt.systemsecurityii.injection.PresenterInjector;
import pl.appsprojekt.systemsecurityii.injection.module.PresenterModule;
import pl.appsprojekt.systemsecurityii.injection.module.WorldGeneratorModule;

/**
 * Created by redione1 on 02.11.2016.
 */

public class MyApplication extends Application {

	private static ApplicationInjector applicationInjector;
	private static PresenterInjector presenterInjector;

	public static PresenterInjector getPresenterInjector() {
		return presenterInjector;
	}

	public static ApplicationInjector getApplicationInjector() {
		return applicationInjector;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Security.insertProviderAt(new BouncyCastleProvider(), 1);

		applicationInjector = DaggerApplicationInjector.builder()
				.worldGeneratorModule(new WorldGeneratorModule(getResources()))
				.build();

		presenterInjector = DaggerPresenterInjector.builder().build();

	}
}
