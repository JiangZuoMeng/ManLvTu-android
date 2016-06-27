package com.jiangzuomeng.fleetingtime.viewConctroller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiangzuomeng.fleetingtime.R;
import com.jiangzuomeng.fleetingtime.models.User;
import com.jiangzuomeng.fleetingtime.network.FunctionResponseListener;
import com.jiangzuomeng.fleetingtime.network.NetworkJsonKeyDefine;
import com.jiangzuomeng.fleetingtime.network.NetworkManager;
import com.jiangzuomeng.fleetingtime.network.VolleyManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.MalformedURLException;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mSignInButton;
    private Button mRegisiterBtn;
    private VolleyManager volleyManager;
    private NetworkManager networkManager;
    private FunctionResponseListener functionResponseListener = new FunctionResponseListener(new NetworkManager.INetworkResponse() {
        @Override
        public void doResponse(String response) {
            try {
                handleNetworkEvent(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(getString(R.string.login));

        mUsernameView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    try {
                        attemptLogin();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(btnListener);

        mRegisiterBtn = (Button) findViewById(R.id.register_button);
        mRegisiterBtn.setOnClickListener(btnListener);

        networkManager = NetworkManager.getInstance(this);
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    try {
                        attemptLogin();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.register_button:
                    try {
                        attemptRegister();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private boolean checkInput() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        boolean result = true;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
            mPasswordView.setError(getString(R.string.error_empty_field_required));
            result = false;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(mUsernameView.getText().toString())) {
            mUsernameView.setError(getString(R.string.error_empty_field_required));
            result = false;
        }

        return result;
    }

    private void attemptLogin() throws MalformedURLException {
        if (checkInput()) {
            showProgress(true);
            User user = new User(-1, mUsernameView.getText().toString(),
                    mPasswordView.getText().toString());
            networkManager.login(user, functionResponseListener, errorListener);
        }
    }

    private void attemptRegister() throws MalformedURLException {
        if (checkInput()) {
            showProgress(true);
            User user = new User(-1, mUsernameView.getText().toString(),
                        mPasswordView.getText().toString());
            networkManager.registerUser(user, functionResponseListener, errorListener);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                handleNetworkEvent(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            System.out.println(error.getMessage());
        }
    };
    public void handleNetworkEvent(String response) throws JSONException {
        JSONTokener parser = new JSONTokener(response);
        JSONObject jsonObject = (JSONObject) parser.nextValue();
        String request = jsonObject.getString(NetworkJsonKeyDefine.REQUEST_KEY);
        String result = jsonObject.getString(NetworkJsonKeyDefine.RESULT_KEY);

        showProgress(false);
        switch (request) {
            case NetworkJsonKeyDefine.LOGIN:
                switch (result) {
                    case NetworkJsonKeyDefine.RESULT_SUCCESS:
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(NetworkJsonKeyDefine.ID,
                                jsonObject.getJSONObject(NetworkJsonKeyDefine.DATA_KEY)
                                        .getInt(NetworkJsonKeyDefine.ID));
                        startActivity(intent);
                        finish();
                        break;
                    case NetworkJsonKeyDefine.RESULT_FAILED:
                        mPasswordView.setError(getString(R.string.error_incorrect_username_or_password));
                        mPasswordView.requestFocus();
                        break;
                    default:
                        mUsernameView.setError(getString(R.string.error_of_server));
                        mUsernameView.requestFocus();
                        break;
                }
                break;

            case NetworkJsonKeyDefine.REGISTER:
                showProgress(false);
                switch (result) {
                    case NetworkJsonKeyDefine.RESULT_SUCCESS:
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(NetworkJsonKeyDefine.ID,
                                jsonObject.getJSONObject(NetworkJsonKeyDefine.DATA_KEY)
                                        .getInt(NetworkJsonKeyDefine.ID));
                        startActivity(intent);
                        finish();
                        break;
                    case NetworkJsonKeyDefine.RESULT_FAILED:
                        mUsernameView.setError(getString(R.string.error_in_register));
                        mUsernameView.requestFocus();
                    default:
                        mUsernameView.setError(getString(R.string.error_of_server));
                        mUsernameView.requestFocus();
                        break;
                }
                break;
        }
    }
}
