package com.seniorproject.rheanna.diabetesapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Button login_button;
    EditText Username, Password;
    TextView register_link;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button  = (Button) findViewById(R.id.login_button);
        Username = (EditText) findViewById(R.id.Username);
        Password = (EditText) findViewById(R.id.Password);
        register_link = (TextView) findViewById(R.id.register_link);
        login_button.setOnClickListener(this);
        register_link.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_button:
                String username = Username.getText().toString();
                String password = Password.getText().toString();

                User user = new User(username, password);

                authenticate(user);

                break;

            case R.id.register_link:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void authenticate(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if(returnedUser == null){
                    showErrorMessage();
                }else{
                    logUserIn(returnedUser);
                }

            }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(Login.this);
        dialogueBuilder.setMessage("Incorrect username or password");
        dialogueBuilder.setPositiveButton("OK", null);
        dialogueBuilder.show();
    }


    private void logUserIn(User returnedUser){
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this, MainActivity.class));
    }
    /*
    private void logUserIn(View view){
        username = Username.getText().toString();

    }*/
}

