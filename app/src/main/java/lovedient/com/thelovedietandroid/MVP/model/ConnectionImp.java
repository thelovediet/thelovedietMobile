package lovedient.com.thelovedietandroid.MVP.model;


import lovedient.com.thelovedietandroid.MVP.presenter.ConnectionPresenter;
import lovedient.com.thelovedietandroid.MVP.views.ShowConnectionView;
import lovedient.com.thelovedietandroid.activities.MyApplications;
import lovedient.com.thelovedietandroid.utils.ServiceManager;
import lovedient.com.thelovedietandroid.utils.SystemUtils;

/**
 * Created by MME on 7/24/2018.
 */

public class ConnectionImp implements ConnectionPresenter {
    ShowConnectionView showConnectionView;

    public ConnectionImp(ShowConnectionView showConnectionView) {
        this.showConnectionView = showConnectionView;
    }

    @Override
    public void takeConnectionStatus() {
        if (MyApplications.isActivityVisible()) {
            if (new ServiceManager(SystemUtils.getActivity()).isNetworkAvailable()) {
                showConnectionView.showOnline();
            }
            else{
                showConnectionView.showOffline();
            }
        }
    }
}
