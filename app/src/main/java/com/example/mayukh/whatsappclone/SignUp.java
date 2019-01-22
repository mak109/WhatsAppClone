package com.example.mayukh.whatsappclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
   private EditText edtSignUpEmail,edtSignUpUserName,edtSignUpPassword;
   private Button btnSignUp,btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpUserName = findViewById(R.id.edtSignUpUserName);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassWord);

        btnLogIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(SignUp.this);
        btnLogIn.setOnClickListener(SignUp.this);

        btnSignUp.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnSignUp);
                }
                return true;
            }
        });

        if(ParseUser.getCurrentUser() != null)
        {
            transitionToUsersActivity();
            finish();
        }
    }

    private void transitionToUsersActivity() {
        Intent intent = new Intent(SignUp.this,Users.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSignUp:
                if(edtSignUpPassword.getText().toString().equals("") || edtSignUpUserName.getText().toString().equals("") ||
                        edtSignUpEmail.getText().toString().equals(""))
                {
                    FancyToast.makeText(SignUp.this,"User name/Password/Email is required",FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,true).show();
                }
                else
                signUp();
                break;
            case R.id.btnLogIn:
                logIn();
                break;
        }

    }

    private void logIn() {
        Intent intent = new Intent(SignUp.this,LogIn.class);
        startActivity(intent);
        finish();
    }

    private void signUp() {
        try {
            final ParseUser appUser = new ParseUser();
            appUser.setEmail(edtSignUpEmail.getText().toString());
            appUser.setUsername(edtSignUpUserName.getText().toString());
            appUser.setPassword(edtSignUpPassword.getText().toString());
            final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
            progressDialog.setMessage("Signing Up User....");
            progressDialog.show();
            appUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUp.this,appUser.getCurrentUser().getUsername()+
                                " is signed up successfully ",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                        transitionToUsersActivity();

                    }
                    else
                    {
                        FancyToast.makeText(SignUp.this,e.getMessage(),FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR,true).show();
                    }
                    progressDialog.dismiss();

                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void rootLayoutSignUpTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    }

