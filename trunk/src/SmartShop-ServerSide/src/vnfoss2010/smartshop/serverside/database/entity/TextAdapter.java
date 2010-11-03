package vnfoss2010.smartshop.serverside.database.entity;

import java.lang.reflect.Type;

import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author VoMinhTam
 */
public class TextAdapter implements JsonSerializer<Text>, JsonDeserializer<Text> {

	@Override
	public JsonElement serialize(Text src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive(src.getValue());
	}

	@Override
	public Text deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		return new Text(json.getAsString());
	}

}
