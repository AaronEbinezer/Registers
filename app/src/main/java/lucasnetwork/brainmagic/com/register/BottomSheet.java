package lucasnetwork.brainmagic.com.register;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import adapter.ViewDataAdapter;

public class BottomSheet extends BottomSheetDialogFragment {
    private EditText Email;
    private Button ResetPass;
    private int a=0;
    private FirebaseAuth firebaseAuth;
    BottomSheetDialogFragment bottomSheet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottmsheet,container,false);
        bottomSheet = new BottomSheet();
        Email = (EditText)view.findViewById(R.id.emailidsent);
        ResetPass = (Button)view.findViewById(R.id.resetbtn);

        ResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getContext(),"Please Enter Your Email",Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(),"Check your Email to Reset your Password",Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getContext(),"Fail to send Email \n Please try again..",Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
            }
        });

        return  view;
    }
}
