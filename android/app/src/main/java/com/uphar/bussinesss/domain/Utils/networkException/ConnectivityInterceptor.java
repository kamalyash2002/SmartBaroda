package com.uphar.bussinesss.domain.Utils.networkException;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    private final Context mContext;

    public ConnectivityInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtil.isOnline(mContext)) {

            /*try {
                Intent intent = new Intent(mContext,Class.forName("com.agrireach.common.ui.activities.noNetwork.NoNetworkActivity"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }*/

            throw new NoConnectivityException();
        }

        try {
            Request.Builder builder = chain.request().newBuilder();
            return chain.proceed(builder.build());
        } catch (Exception e){
            throw new AccessDeniedException();
        }

    }

}
