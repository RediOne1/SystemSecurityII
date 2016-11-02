package pl.appsprojekt.systemsecurityii.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.appsprojekt.systemsecurityii.R;
import pl.appsprojekt.systemsecurityii.usecase.WorldUsecase;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}
