package com.devhome.eduardobastos.organizze.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devhome.eduardobastos.organizze.R;
import com.devhome.eduardobastos.organizze.activitys.config.ConfiguracaoFirebase;
import com.devhome.eduardobastos.organizze.activitys.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText editLogin, editSenha;
    private Button button;
    private Usuario usuario;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById (R.id.buttonEntrar);
        editLogin = findViewById(R.id.editTextEmailLogin);
        editSenha = findViewById(R.id.editTextSenhaLogin);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoEmail = editLogin.getText().toString();
                String textoSenha = editSenha.getText().toString();

                if (!textoEmail.isEmpty()){
                    if (!textoSenha.isEmpty()){

                        usuario = new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarLogin();

                    }else {
                        Toast.makeText(LoginActivity.this, "Preencha a senha",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Preencha o email",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public  void validarLogin(){

        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.signInWithEmailAndPassword(
            usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    abrirTelaPrincipal();

                }else {

                        String excecao = "";

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Email e/ou senha não válido(s).";

                    }catch (FirebaseAuthInvalidUserException e){

                        excecao = "Usuário não cadastrado.";
                    }catch (Exception e){
                        excecao = "Erro ao logar: " +e.getMessage();
                        e.printStackTrace();
                    }


                    Toast.makeText(LoginActivity.this,
                            excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, TelaPrincipalActivity.class));
        finish();
    }

}
