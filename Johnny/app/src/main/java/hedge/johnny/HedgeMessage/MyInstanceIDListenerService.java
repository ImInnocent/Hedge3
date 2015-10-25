package hedge.johnny.HedgeMessage;

/**
 * Created by EDGE01 on 2015-10-08.
 */
import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by EDGE01 on 2015-10-02.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}