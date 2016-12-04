package Connection;

/**
 * Created by Akash on 27-11-2016.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;

public class ConnectorClass {

    private Context _context;

    public ConnectorClass(Context context){
        this._context = context;
    }

    @SuppressWarnings("deprecation")
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void showSnackBar(View v, String msg)
    {
        Snackbar.make(v,msg, Snackbar.LENGTH_LONG)
                .setAction("Action",null).show();
    }
}
