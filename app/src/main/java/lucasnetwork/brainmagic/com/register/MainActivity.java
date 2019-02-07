package lucasnetwork.brainmagic.com.register;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import InternetConnection.Internet;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity  {
    private EditText etEmail,etPassowrd;
    private Button btnSignup,buttonupload,UserLogin;
    private TextView ForgotPass;
    private Context context;
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.btn_bottom_sheet)
    Button btnBottomSheet;

    @BindView(R.id.bottom_sheet)
    CoordinatorLayout layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;

    private TextView textviewSign,textNewuser;

    private Internet net;
    private FirebaseAuth firebaseAuthException;
     private  FirebaseApp firebaseApp;
    private ProgressDialog progressDialog;
    BottomSheetBehavior bottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
       // ButterKnife.bind(this);
    UserLogin = (Button) findViewById(R.id.userlogin);
    UserLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i  =new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        }
    });
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        //sheetBehavior = sheetBehavior.from(layoutBottomSheet);
       // android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        firebaseAuthException = FirebaseAuth.getInstance();
       // firebaseApp.setAutomaticResourceManagementEnabled(true);
       // android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setIcon(R.drawable.download);
        firebaseApp.initializeApp(context);
        ForgotPass = (TextView)findViewById(R.id.forgot);

       // bottomSheetBehavior = BottomSheetBehavior.from(bottmsheet);
       // actionBar.setDisplayHomeAsUpEnabled(true);
      // actionBar.setIcon(R.drawable.download);
        textNewuser = (TextView)findViewById(R.id.textnew);
        net = new Internet(MainActivity.this);

        etEmail = (EditText)findViewById(R.id.edittextemail);
        buttonupload = (Button)findViewById(R.id.buttonupload);
        etPassowrd = (EditText)findViewById(R.id.edittextpassowrd);
        btnSignup = (Button)findViewById(R.id.buttonsubmit);
        textviewSign = (TextView)findViewById(R.id.textSign);


       /* bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:{
                        textNewuser.setText("Expand");
                        break;
                    }
                    case  BottomSheetBehavior.STATE_COLLAPSED:{
                        textNewuser.setText("Exapnd Sheet");
                        break;
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });*/
        progressDialog = new ProgressDialog(this);
        ForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.show(getSupportFragmentManager(),"Expand");
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInternet();

            }
        });
        textNewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });
        buttonupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(net.CheckInternet()){
                    Intent i = new Intent(MainActivity.this,FilesUpload.class);
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(),"Please Check youur Interet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
        textviewSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(net.CheckInternet()) {


                    Intent i = new Intent(MainActivity.this, ListData.class);
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(),"Please Check your Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

   /* @OnClick(R.id.btn_bottom_sheet)
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            btnBottomSheet.setText("Expand sheet");
        }
    }*/



    private void CheckInternet() {
        if(net.CheckInternet()){
            ValidateregisterUser();
        }else {
            Toast.makeText(this,"Please Enable your Internet connection...",MODE_PRIVATE).show();
        }
    }

    private void ValidateregisterUser() {
            if(etEmail.getText().toString().equals("")){
                Toast.makeText(this,"Please Enter your Email",MODE_PRIVATE).show();
            }else if(etPassowrd.getText().toString().equals("")){
                Toast.makeText(this,"Please Enter your Password",MODE_PRIVATE).show();
            }else
                registerUser();
    }




    private void registerUser() {
        String Email = etEmail.getText().toString().trim();
        String Password = etPassowrd.getText().toString().trim();

        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this,"Enter your Email",MODE_PRIVATE).show();
        }

        if(TextUtils.isEmpty(Password)){
            Toast.makeText(this,"Enter your Password",MODE_PRIVATE).show();

        }

    progressDialog.setMessage("Progress...");
        progressDialog.show();

        firebaseAuthException.signInWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"SignIn Successful..",MODE_PRIVATE).show();
                            progressDialog.dismiss();
                            myNotification();
                            Intent i =new Intent(MainActivity.this,MapsActivity.class);
                            startActivity(i);
                        }else {
                                Toast.makeText(MainActivity.this,"Could not Register..",MODE_PRIVATE).show();
                                progressDialog.dismiss();
                            }

                    }
                });

    }

    private void myNotification() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder

                (getApplicationContext()).
                setContentTitle(getResources().getString(R.string.app_name)).
                setContentText("Login Successfully!!!").
                setVibrate(new long[]{100,250,100,250,100,250}).
                setSound(alarmSound).
                setSmallIcon(R.drawable.download).build();
        notif.notify(1,notify);

    }

}
