package com.example.teletibu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText;
    private Spinner roleSpinner;
    private FirebaseAuth mAuth;
    private String selectedRole = "Patient"; // default role

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        roleSpinner = findViewById(R.id.roleSpinner);
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        // Set up Spinner with roles
        String[] roles = {"Patient", "Doctor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = roles[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // default remains "Patient"
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(LoginActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Use Firebase Auth to verify credentials
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Check user role
                                    if (selectedRole.equals("Patient")) {
                                        Toast.makeText(LoginActivity.this, "Login successful as Patient!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, PatientDashboardActivity.class));
                                        finish();
                                    } else if (selectedRole.equals("Doctor")) {
                                        // For Doctor, perform additional password verification if needed
                                        // Example: check for a specific hardcoded doctor password pattern (this is for demonstration only)
                                        if (verifyDoctorPassword(password)) {
                                            Toast.makeText(LoginActivity.this, "Login successful as Doctor!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, DoctorDashboardActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Invalid doctor credentials", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login failed: " +
                                            task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });
    }

    // Dummy function for extra doctor password check
    // Modify this method with your own logic if needed
    private boolean verifyDoctorPassword(String password) {
        // For demonstration, assume valid doctor password should start with "doc_"
        return password.startsWith("doc_");
    }
}
