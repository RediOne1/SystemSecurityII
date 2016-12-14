package pl.appsprojekt.systemsecurityii;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by redione1 on 02.11.2016.
 */

public class Utils {

	public static String jsonToPrettyString(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.toString(4);
		} catch (JSONException e) {
			e.printStackTrace();
			return json;
		}

	}
}
