package com.jiangzuomeng.travelmap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jiangzuomeng.dataManager.DataManager;
import com.jiangzuomeng.dataManager.NetworkConnectActivity;
import com.jiangzuomeng.dataManager.NetworkHandler;
import com.jiangzuomeng.modals.User;
import com.jiangzuomeng.networkManager.NetworkJsonKeyDefine;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements NetworkConnectActivity {
    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        (findViewById(R.id.register_button)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }

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

    private void attemptLogin() {
        if (checkInput()) {
            showProgress(true);
            DataManager.getInstance(getApplicationContext()).login(
                    new User(-1, mUsernameView.getText().toString(), mPasswordView.getText().toString()),
                    new NetworkHandler(this));
        }
    }

    private void attemptRegister() {
        if (checkInput()) {
            showProgress(true);
            DataManager.getInstance(getApplicationContext()).registerUser(
                    new User(-1, mUsernameView.getText().toString(), mPasswordView.getText().toString()),
                    new NetworkHandler(this));
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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


    @Override
    public void handleNetworkEvent(String result, String request, String target, JSONObject originJSONObject) throws JSONException {
        showProgress(false);
        switch (request) {
            case NetworkJsonKeyDefine.LOGIN:
                switch (result) {
                    case NetworkJsonKeyDefine.RESULT_SUCCESS:
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(NetworkJsonKeyDefine.ID,
                                originJSONObject.getJSONObject(NetworkJsonKeyDefine.DATA_KEY)
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
                                originJSONObject.getJSONObject(NetworkJsonKeyDefine.DATA_KEY)
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

