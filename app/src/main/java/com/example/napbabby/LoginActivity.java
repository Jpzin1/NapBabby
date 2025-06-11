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

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.CredentialManager;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String TYPE_GOOGLE_ID_TOKEN_CREDENTIAL = "com.google.android.libraries.identity.googleid.GOOGLE_ID_TOKEN_CREDENTIAL";

    private EditText etEmail, etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegister;
    private ImageButton btnGoogleLogin;

    // Firebase Auth
    private FirebaseAuth mAuth;
    // Credential Manager
    private CredentialManager credentialManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar Credential Manager
        credentialManager = CredentialManager.create(this);

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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar navegação para tela de recuperação de senha
                Toast.makeText(LoginActivity.this, "Recuperação de senha", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar navegação para tela de cadastro
                Toast.makeText(LoginActivity.this, "Cadastro de novo usuário", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar login com Google
                signInWithGoogle();
            }
        });
    }

    private void validateAndLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean rememberMe = cbRememberMe.isChecked();

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
        try {
            // Criar a solicitação de login do Google
            GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .build();

            // Criar a solicitação do Gerenciador de credenciais
            GetCredentialRequest request = new GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build();

            // Iniciar o fluxo de autenticação
            credentialManager.getCredential(
                    this,
                    request,
                    null,
                    getMainExecutor(),
                    this::handleSignIn,
                    this::handleSignInError
            );
        } catch (Exception e) {
            Log.e(TAG, "Erro ao iniciar login com Google", e);
            Toast.makeText(this, "Erro ao iniciar login com Google: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Método para tratar o resultado da autenticação
    private void handleSignIn(GetCredentialResponse result) {
        try {
            // Verificar se a credencial é do tipo Google ID
            if (result.getCredential() instanceof CustomCredential) {
                CustomCredential credential = (CustomCredential) result.getCredential();

                if (TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                    // Criar token de ID do Google
                    GoogleIdTokenCredential googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.getData());

                    // Autenticar no Firebase com o token
                    firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
                } else {
                    Log.w(TAG, "Credencial não é do tipo Google ID!");
                    Toast.makeText(this, "Tipo de credencial não suportado",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao processar credencial", e);
            Toast.makeText(this, "Erro ao processar credencial: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Método para tratar erros na autenticação
    private void handleSignInError(Throwable e) {
        Log.e(TAG, "Erro na autenticação com Google", e);
        Toast.makeText(this, "Erro na autenticação com Google: " + e.getMessage(),
                Toast.LENGTH_SHORT).show();
    }

    // Método para autenticar no Firebase com o token do Google
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
