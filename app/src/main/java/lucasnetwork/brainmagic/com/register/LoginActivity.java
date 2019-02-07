package lucasnetwork.brainmagic.com.register;

import android.content.Intent;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button LoginButton;
    private EditText LoginEmail,LoginPassword;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        LoginButton = (Button) findViewById(R.id.loginbutton);
        LoginEmail = (EditText)findViewById(R.id.loginemail);
        LoginPassword = (EditText)findViewById(R.id.loginpassword);

LoginButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        createMEhtos();
    }
});

    }

    private void createMEhtos() {
        String email = LoginEmail.getText().toString().trim();
        String password = LoginPassword.getText().toString().trim();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Intent i = new Intent(LoginActivity.this, MainMenu.class);
                    startActivity(i);
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        updateUI(user);
        
    }


    private void updateUI(FirebaseUser user) {
    }
}
