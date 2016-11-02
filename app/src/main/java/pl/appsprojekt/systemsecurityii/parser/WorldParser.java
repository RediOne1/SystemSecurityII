package pl.appsprojekt.systemsecurityii.parser;

import android.content.res.Resources;
import android.support.annotation.RawRes;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import pl.appsprojekt.systemsecurityii.R;
import pl.appsprojekt.systemsecurityii.model.World;

/**
 * Created by redione1 on 02.11.2016.
 */

public class WorldParser {

	private Resources res;

	public WorldParser(Resources res) {
		this.res = res;
	}

	public World get() {
		Gson gson = new Gson();
		String json = "";
		try {
			json = getJson(R.raw.test);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gson.fromJson(json, World.class);
	}

	public String getJson(@RawRes int jsonId) throws IOException {
		InputStream is = res.openRawResource(jsonId);
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		try {
			Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		} finally {
			is.close();
		}

		return writer.toString();
	}
}
