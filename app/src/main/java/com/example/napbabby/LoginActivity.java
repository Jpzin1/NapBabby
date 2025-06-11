package com.example.napbabby;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etEmail, etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegister;
    private ImageButton btnGoogleLogin;

    // Firebase Auth
    private FirebaseAuth mAuth;
    // Google Sign In Client
    private GoogleSignInClient mGoogleSignInClient;

    // ActivityResultLauncher para substituir startActivityForResult
    private ActivityResultLauncher<Intent> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configurar Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Configurar ActivityResultLauncher para Google Sign In
        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Resultado retornado da intent de login do Google
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            // Login Google bem-sucedido, autenticar com Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            // Falha no login Google
                            Log.w(TAG, "Google sign in failed", e);
                            Toast.makeText(LoginActivity.this, "Falha no login com Google: " + e.getStatusCode(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Inicializar componentes
        initViews();

        // Configurar listeners
        setupListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verificar se o usuário já está logado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> validateAndLogin());

        tvForgotPassword.setOnClickListener(v -> {
            // Implementar navegação para tela de recuperação de senha
            Toast.makeText(LoginActivity.this, "Recuperação de senha", Toast.LENGTH_SHORT).show();
        });

        tvRegister.setOnClickListener(v -> {
            // Implementar navegação para tela de cadastro
            Toast.makeText(LoginActivity.this, "Cadastro de novo usuário", Toast.LENGTH_SHORT).show();
        });

        btnGoogleLogin.setOnClickListener(v -> {
            // Implementar login com Google
            signInWithGoogle();
        });
    }

    private void validateAndLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validação simples
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Digite seu e-mail");
            return;
        }

        if (!isValidEmail(email)) {
            etEmail.setError("E-mail inválido");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Digite sua senha");
            return;
        }

        // Aqui você implementaria a lógica de autenticação real
        // Por enquanto, apenas exibimos uma mensagem de sucesso
        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Método para iniciar o processo de login com Google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    // Método para autenticar no Firebase com o token do Google
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login bem-sucedido
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // Login falhou
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Falha na autenticação: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    // Método para atualizar a interface após autenticação
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Usuário está logado, atualizar UI ou navegar para próxima tela
            Toast.makeText(this, "Bem-vindo, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            // Aqui você pode iniciar uma nova Activity
            // Intent intent = new Intent(this, MainActivity.class);
            // startActivity(intent);
            // finish();
        } else {
            // Usuário não está logado, manter na tela de login
        }
    }
}
