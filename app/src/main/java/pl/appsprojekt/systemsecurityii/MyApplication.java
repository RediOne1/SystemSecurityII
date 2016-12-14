package pl.appsprojekt.systemsecurityii;

import android.app.Application;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * author:  redione1
 * date:    02.11.2016
 */

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Security.insertProviderAt(new BouncyCastleProvider(), 1);

	}
}
