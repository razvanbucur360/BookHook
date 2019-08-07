package com.example.bookhook2.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;
import com.example.bookhook2.R;
import com.example.bookhook2.util.SendMail;
import com.google.firebase.auth.FirebaseAuth;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    CardForm cardForm;
    Button buy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(PaymentActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        buy.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == buy){
            if (cardForm.isValid()) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PaymentActivity.this);
                alertBuilder.setTitle("Confirm before purchase");
                alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                        "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                        "Card CVV: " + cardForm.getCvv() + "\n" +
                        "Postal code: " + cardForm.getPostalCode() + "\n" +
                        "Phone number: " + cardForm.getMobileNumber());
                alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        sendTicketEmail();
                        Toast.makeText(PaymentActivity.this, "Thank you for the purchase. Check email for confirmation.", Toast.LENGTH_LONG).show();
                    }

                    private void sendTicketEmail() {
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        String recipient = firebaseAuth.getCurrentUser().getEmail();
                        String subject = "[BookHook] Your ticket reservation!";
                        String message = "Thank you for the reservation!" + "\n\n" +
                                "You can present this email at the entrance to confirm the ticket reservation." +
                                "\n\n" + " The BookHook team";
                        SendMail sm = new SendMail(PaymentActivity.this, recipient, subject, message);
                        sm.execute();
                    }
                });
                alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            }else {
                Toast.makeText(PaymentActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
            }
        }
    }
}
