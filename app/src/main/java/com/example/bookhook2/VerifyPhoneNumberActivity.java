package com.example.bookhook2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookhook2.fragments.UserProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumberActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "PhoneAuth";

    private EditText phoneText;
    private EditText codeText;
    private Button verifyButton;
    private Button sendCodeButton;
    private FirebaseAuth mAuth;
    private String codeSent;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);
        phoneText = (EditText) findViewById(R.id.phoneText);
        codeText = (EditText) findViewById(R.id.codeText);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        sendCodeButton = (Button) findViewById(R.id.sendButton);

        mAuth = FirebaseAuth.getInstance();

        sendCodeButton.setOnClickListener(this);
        verifyButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view == sendCodeButton){
            sendVerificationCode();
        }

        if(view == verifyButton){
            verifySignInCode();
        }
    }

    private void verifySignInCode() {
        String code = codeText.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        addPhoneNumber(credential);
    }

    private void addPhoneNumber(final PhoneAuthCredential credential){
        mAuth.getCurrentUser().updatePhoneNumber(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Phone number added", Toast.LENGTH_LONG).show();
                            finish();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment, new UserProfileFragment());
                            transaction.commit();
                        }
                        else{

                        }
                    }
                });
    }

    private void sendVerificationCode() {
        String phoneNumber = phoneText.getText().toString();

        if(phoneNumber.isEmpty()){
            phoneText.setError("Phone number is required");
            phoneText.requestFocus();
            return;
        }

        if(phoneNumber.length() < 10){
            phoneText.setError("Please enter a valid phone");
            phoneText.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };

}
