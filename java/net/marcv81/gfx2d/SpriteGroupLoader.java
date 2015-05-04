package net.marcv81.gfx2d;

import android.content.Context;
import org.json.*;

import java.io.*;
import java.util.HashMap;

public class SpriteGroupLoader {

    private HashMap<String, SpriteGroup> configs = new HashMap<>();

    public SpriteGroupLoader(Context context) {

        try {
            // Open graphics.json
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

                SpriteTexture texture = new SpriteTexture(textureFilename, animationsX, animationsY);
                SpriteGeometry geometry = new SpriteGeometry(size);
                SpriteGroup spriteGroup = new SpriteGroup(texture, geometry,
                        supportAngle, supportTransparency, supportScaling);

                configs.put(name, spriteGroup);
            }
        }

        // Rethrow checked exceptions as runtime exceptions
        catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SpriteGroup getSpriteGroup(String name) {
        return configs.get(name);
    }
}
