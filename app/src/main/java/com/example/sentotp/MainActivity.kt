package com.example.sentotp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var atuh:FirebaseAuth
    private lateinit var numberEdit:EditText
    private lateinit var otpEdit:EditText
    private lateinit var sendOtpButton:AppCompatButton
    private lateinit var verifyButton:AppCompatButton
    var verificationID = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        atuh = FirebaseAuth.getInstance()
        numberEdit = findViewById(R.id.number_EditText)
        otpEdit = findViewById(R.id.otp_EditText)
        sendOtpButton = findViewById(R.id.sentOtp_Button)
        verifyButton = findViewById(R.id.verifieOtp_Button)

        sendOtpButton.setOnClickListener {
            sendOtp()
        }
        verifyButton.setOnClickListener {
            verifyOtp()
        }
    }
    private fun sendOtp(){
        val phoneAuthOptions = PhoneAuthOptions.newBuilder()
            .setActivity(this)
            .setPhoneNumber("+91${numberEdit.text}")
            .setTimeout(30L,TimeUnit.SECONDS)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Toast.makeText(this@MainActivity, "Verification completed", Toast.LENGTH_SHORT).show()
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@MainActivity, "Verification is failed ${p0.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, p1)
                    verificationID = verificationId
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }
   private fun verifyOtp(){
        val otpText = otpEdit.text.toString()
        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID,otpText)

        atuh.signInWithCredential(phoneAuthCredential)
            .addOnSuccessListener {
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Login filed", Toast.LENGTH_SHORT).show()
            }
    }
}