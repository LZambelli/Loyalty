package joaopogiolli.com.br.loyalty;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by jlago on 20/02/2018.
 */

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}