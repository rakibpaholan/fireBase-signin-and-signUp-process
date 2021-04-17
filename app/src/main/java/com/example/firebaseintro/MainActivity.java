package com.example.firebaseintro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText user_name, user_pass;
    private Button login_button, sign_up_button;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        user_name = (EditText)findViewById(R.id.user_name_id);
        user_pass = (EditText)findViewById(R.id.user_pass);
        login_button = (Button)findViewById(R.id.login_id);
        sign_up_button = (Button)findViewById(R.id.sign_up_button);
        progressBar = (ProgressBar)findViewById(R.id.circular_progress_bar);


        login_button.setOnClickListener(this);
        sign_up_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_id:
                userRegister();
                break;

            case R.id.sign_up_button:
                Intent intent = new Intent(getApplicationContext(),SignUpButton.class);
                startActivity(intent);
                break;

        }
    }

    private void userRegister() {

        String user_name_value = user_name.getText().toString().trim();
        String user_pass_value = user_pass.getText().toString().trim();

        if (user_name_value.isEmpty()){
            user_name.setError("Email or User is not Valid");
            user_name.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user_name_value).matches()){
            user_name.setError("Enter a Valid Email or User name ");
            user_name.requestFocus();
            return;
        }

        if (user_pass_value.isEmpty()){
            user_pass.setError("Should not be Empty");
            user_pass.requestFocus();
            return;
        }
        if (user_pass_value.length()<6){
            user_pass.setError("Password must be 6 char");
            user_pass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(user_name_value,user_pass_value).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    finish();
                    Intent intent = new Intent(MainActivity.this,ProfileAcitcivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"User Successfuly login",Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),"User Not successfuly login",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
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