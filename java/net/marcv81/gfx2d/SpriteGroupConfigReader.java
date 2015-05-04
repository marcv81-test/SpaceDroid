package net.marcv81.gfx2d;

import android.content.Context;
import org.json.*;

import java.io.*;
import java.util.HashMap;

public class SpriteGroupConfigReader {

    private HashMap<String, SpriteGroupConfig> configs = new HashMap<>();

    public SpriteGroupConfigReader(Context context) {

        try {
            // Open sprites.json
            int id = context.getResources().getIdentifier("sprites", "raw", context.getPackageName());
            InputStream inputStream = context.getResources().openRawResource(id);

            // Read InputStream
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String read = bufferedReader.readLine();
            while (read != null) {
                stringBuilder.append(read);
                read = bufferedReader.readLine();
            }

            // Instantiate JSONObject from String
            JSONObject json = new JSONObject(stringBuilder.toString());

            // Instantiate SpriteGroupConfigs
            JSONArray array = json.getJSONArray("sprites");
            for (int i = 0; i < array.length(); i++) {

                String name = array.getJSONObject(i).getString("name");
                String textureFilename = array.getJSONObject(i).getString("textureFilename");
                int animationsX = array.getJSONObject(i).getInt("animationsX");
                int animationsY = array.getJSONObject(i).getInt("animationsY");
                float size = (float) array.getJSONObject(i).getDouble("size");
                boolean supportAngle = array.getJSONObject(i).getBoolean("supportAngle");
                boolean supportTransparency = array.getJSONObject(i).getBoolean("supportTransparency");
                boolean supportScaling = array.getJSONObject(i).getBoolean("supportScaling");

                SpriteGroupConfig config = new SpriteGroupConfig(
                        textureFilename, animationsX, animationsY, size,
                        supportAngle, supportTransparency, supportScaling);

                configs.put(name, config);
            }
        }

        // Rethrow checked exceptions as runtime exceptions
        catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SpriteGroupConfig getConfig(String name) {
        return configs.get(name);
    }
}
