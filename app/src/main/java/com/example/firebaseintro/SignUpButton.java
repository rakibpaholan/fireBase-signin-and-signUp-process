package com.example.firebaseintro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpButton extends AppCompatActivity implements View.OnClickListener {
    private EditText user_name,user_pass;
    private Button sign_Up,sign_in;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_button);
        mAuth = FirebaseAuth.getInstance();
        user_name = (EditText)findViewById(R.id.user_name_signUp);
        user_pass = (EditText)findViewById(R.id.user_password_signUp);
        sign_Up = (Button)findViewById(R.id.sign_up_button);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar_id);
        sign_in = (Button)findViewById(R.id.sign_in_id);


        sign_Up.setOnClickListener(this);
        sign_in.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.sign_up_button:
                registered();
                break;
            case R.id.sign_in_id:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
        }
    }

    private void registered() {
        String user_name_value = user_name.getText().toString().trim();
        String user_pass_value =  user_pass.getText().toString().trim();

        if (user_name_value.isEmpty()){
            user_name.setError("User name or Email ! ");
            user_name.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user_name_value).matches()){
            user_name.setError("Not a Valid Email !");
            user_name.requestFocus();
            return;
        }
        if (user_pass_value.isEmpty()){
            user_pass.setError("Pass Correct koren !");
            user_pass.requestFocus();
            return;
        }
        if (user_pass_value.length()<6){
            user_pass.setError("Pass Correct koren !");
            user_pass.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(user_name_value,user_pass_value).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"User Successfuly Sign Up",Toast.LENGTH_SHORT).show();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"User Alrady Exist",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(getApplicationContext(),"Error is : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }
        });

        InputMethodManager imm = (InputMethodManager)getSystemService(
                getApplicationContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(user_name.getWindowToken(), 0);

        user_name.setText("");
        user_pass.setText("");


    }
}