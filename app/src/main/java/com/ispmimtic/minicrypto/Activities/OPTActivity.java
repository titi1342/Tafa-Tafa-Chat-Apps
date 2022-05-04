package com.ispmimtic.minicrypto.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ispmimtic.minicrypto.databinding.ActivityOptactivityBinding;
import com.mukesh.OnOtpCompletionListener;
import java.util.concurrent.TimeUnit;

public class OPTActivity extends AppCompatActivity {

    ActivityOptactivityBinding binding;
    FirebaseAuth auth;
    String verificationId;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOptactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Envoie du message...");
        dialog.setCancelable(false);
        dialog.show();

        auth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();

        String numeroTel = getIntent().getStringExtra("numeroTel");
        String temp = numeroTel;

        String tab[] = numeroTel.split("0");
        numeroTel = "";
        for (int i = 1; i < tab.length; i++) {
            if (i != 1)
               tab[i] = 0 + tab[i];
            numeroTel += tab[i] ;
        }
        numeroTel = "+261" + numeroTel;

        binding.phoneNumberId.setText("Vérification " + numeroTel);
        //numeroTel = temp;

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(numeroTel)
                .setTimeout(100L, TimeUnit.SECONDS)
                .setActivity(OPTActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verifyId, forceResendingToken);
                        dialog.dismiss();
                        verificationId = verifyId;

                        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        binding.otpView.requestFocus();

                    }
                }).build();

        PhoneAuthProvider.verifyPhoneNumber(options);


        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Intent intent = new Intent(OPTActivity.this, SetupProfileActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                        else {
                            Toast.makeText(OPTActivity.this,"Une erreur s'est produite", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });



    }
}