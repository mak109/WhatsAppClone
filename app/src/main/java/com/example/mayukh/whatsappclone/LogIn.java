package com.example.mayukh.whatsappclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLogInEmail,edtLogInPassword;
    private Button btnLogInActivity,btnSignUpActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        edtLogInEmail = findViewById(R.id.edtLogInEmail);
        edtLogInPassword = findViewById(R.id.edtLogInPassWord);

        btnLogInActivity = findViewById(R.id.btnLogInActivity);
        btnSignUpActivity = findViewById(R.id.btnSignUpActivity);

        btnSignUpActivity.setOnClickListener(LogIn.this);
        btnLogInActivity.setOnClickListener(LogIn.this);
        if(ParseUser.getCurrentUser() != null){

        transitionToUsersActivity();
        finish();
        }


    }

    private void transitionToUsersActivity() {
        Intent intent = new Intent(LogIn.this,Users.class);
        startActivity(intent);
    }

    public void rootLayoutLogInTapped(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUpActivity:
                signUpUser();
                break;
            case R.id.btnLogInActivity:
                if(edtLogInPassword.getText().toString().equals("") ||
                        edtLogInEmail.getText().toString().equals(""))
                {
                    FancyToast.makeText(LogIn.this,"Email/Password required",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();

                }
                else
                logInUser();
                break;
        }

    }

    private void logInUser() {
        final ParseUser appUser = new ParseUser();
        final ProgressDialog progressDialog = new ProgressDialog(LogIn.this);
        progressDialog.setMessage("Logging in....");
        progressDialog.show();
        appUser.logInInBackground(edtLogInEmail.getText().toString(), edtLogInPassword.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null && e == null)
                {
                    FancyToast.makeText(LogIn.this,appUser.getCurrentUser().getUsername()+
                            " logged in successfully",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                    transitionToUsersActivity();
                }
                else {
                    FancyToast.makeText(LogIn.this,e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void signUpUser() {
        Intent intent = new Intent(LogIn.this,SignUp.class);
        startActivity(intent);
        finish();
    }
}