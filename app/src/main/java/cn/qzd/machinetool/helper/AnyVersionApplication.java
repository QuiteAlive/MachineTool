package cn.qzd.machinetool.helper;

import android.app.Application;

import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.Version;
import com.github.yoojia.anyversion.VersionParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by admin on 2018/7/13.
 */

public class AnyVersionApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AnyVersion.init(this, new VersionParser() {
            @Override
            public Version onParse(String response) {
                final JSONTokener tokener = new JSONTokener(response);
                try {
                    JSONObject json = (JSONObject) tokener.nextValue();
                    return new Version(
                            json.getString("name"),
                            json.getString("note"),
                            json.getString("url"),
                            json.getInt("code")
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}