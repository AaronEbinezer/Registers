package lucasnetwork.brainmagic.com.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import InternetConnection.Internet;

public class SignupActivity extends AppCompatActivity {
    private EditText Name,Phonenumber,Email,Address,Password,otpNumber;
    private Button Submit,Generate,Verify;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private LinearLayout Bal;
    private Users users;
    private String Names;
    private Internet internet;

    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    private String getPhone,mVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Register");
        Name = (EditText)findViewById(R.id.name);
        progressDialog = new ProgressDialog(this);
        Bal = (LinearLayout)findViewById(R.id.balancedetails);
        otpNumber = (EditText)findViewById(R.id.otpnumber);
        Generate = (Button)findViewById(R.id.generateotp);
        Verify = (Button)findViewById(R.id.verifyotp);
        Password = (EditText)findViewById(R.id.password);
        Phonenumber = (EditText)findViewById(R.id.phonenumber);
        Email = (EditText)findViewById(R.id.email);
        internet = new Internet(this);
        Address = (EditText)findViewById(R.id.address);
        Submit = (Button)findViewById(R.id.submit);
        users =new Users();
        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpNumber.getText().toString().trim();

                if(code.isEmpty() || code.length()<6){
                    otpNumber.setError("Enter Valid OTP");
                    otpNumber.requestFocus();
                    return;
                }
                verifyVerificationCode(code);
                progressDialog.show();
                progressDialog.setMessage("Vefifying OTP");


            }
                   });
        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                progressDialog.setMessage("Sending OTP");
                sendVerificationCode(Phonenumber.getText().toString().trim());
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter the Name",Toast.LENGTH_SHORT).show();
                }else if(Phonenumber.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter the Phone number",Toast.LENGTH_SHORT).show();

                }else if(Phonenumber.getText().toString().length() < 10){
                    Toast.makeText(getApplicationContext(),"Enter Valid Phone number",Toast.LENGTH_SHORT).show();

                }else if(Email.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter the Email",Toast.LENGTH_SHORT).show();

                }else if(Address.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter the Address",Toast.LENGTH_SHORT).show();

                }else if(Password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter Your Password",Toast.LENGTH_SHORT).show();
                }else
                {
                    CheckInternet();
                }
            }
        });



    }

    private void VerifyVerificatioCode(EditText otpNumber) {
    }


    private void sendVerificationCode(String trim) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+trim,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,callbacks);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code =  phoneAuthCredential.getSmsCode();
            if(code!= null){
                otpNumber.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            Toast.makeText(getApplicationContext(),"OTP send to your Mobile number",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

        }
    };




    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Thanks for Phone Verify... Verify SUccess", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Bal.setVisibility(View.VISIBLE);
                        } else {

                            //verification unsuccessful.. display an error message
                                progressDialog.dismiss();
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }


                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Enter Your Valid OTP",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                    }
                });
    }


    private void CheckInternet() {
        if(internet.CheckInternet()){
            StoreData();
        }else
            Toast.makeText(getApplicationContext(),"Please Check your Interner Connection",Toast.LENGTH_SHORT).show();

    }

    private void StoreData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.setName(Name.getText().toString());
                users.setPhonenumber(Phonenumber.getText().toString());
                users.setEmail(Email.getText().toString());
                users.setAddress(Address.getText().toString());
                users.setPassword(Password.getText().toString());
                Names = Name.getText().toString();
                databaseReference.child(Names).setValue(users);

                Intent i = new Intent(SignupActivity.this,MainActivity.class);
                startActivity(i);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        progressDialog.setMessage("Progress...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext()," Your Details are Registerd",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getApplicationContext()," Register is not successful",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
