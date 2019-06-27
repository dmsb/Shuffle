package com.example.shuffle;

import android.app.Activity;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionBuilder {

    private static final String API_URL = "https://api.spotify.com/";
    public static final String REDIRECT_URI = "https://open.spotify.com/";
    public static SpotifyAppRemote mSpotifyAppRemote;

    // public static final String CLIENT_ID = "d7ebba782ca24b48a51094cfc9dbd152"; //jorge id
    public static final String CLIENT_ID = "0599d96797ef4cd19d655b778aacaa27"; //diogo id
    public static final int AUTH_TOKEN_REQUEST_CODE = 1337;

    public static String ACCESS_TOKEN;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    private static AuthenticationRequest getAuthenticationRequest(AuthenticationResponse.Type type) {
        String[] scopes = "streaming user-read-recently-played user-top-read user-library-modify user-library-read playlist-read-private playlist-modify-public playlist-modify-private playlist-read-collaborative user-read-email user-read-birthdate user-read-private user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming".split("\\s+");
        return new AuthenticationRequest.Builder(ConnectionBuilder.CLIENT_ID, type, ConnectionBuilder.REDIRECT_URI)
                .setScopes(scopes)
                .build();
    }

    public static void obtainAccessToken(Activity activity) {
        final AuthenticationRequest request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN);
        AuthenticationClient.openLoginActivity(activity, ConnectionBuilder.AUTH_TOKEN_REQUEST_CODE, request);
    }
}
