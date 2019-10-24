package com.devhome.eduardobastos.organizze.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.devhome.eduardobastos.organizze.R;
import com.devhome.eduardobastos.organizze.activitys.config.ConfiguracaoFirebase;
import com.devhome.eduardobastos.organizze.activitys.helper.Base64Custom;
import com.devhome.eduardobastos.organizze.activitys.helper.DateCustom;
import com.devhome.eduardobastos.organizze.activitys.model.Movimentacao;
import com.devhome.eduardobastos.organizze.activitys.model.Usuario;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DespesasActivity extends AppCompatActivity {

    private EditText editTextData, editTextValor, editTextCategoria, editTextDscr;
    private Movimentacao movimentacao;
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double despesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);


        editTextData = findViewById(R.id.editTextData);
        editTextValor = findViewById(R.id.editTextValor);
        editTextCategoria = findViewById(R.id.editTextCategoria);
        editTextDscr = findViewById(R.id.editTextDescricao);


        //preenche o campo com a data atual
        editTextData.setText(DateCustom.dataAtual());
        recuperarDespTotal();
    }

    public void salvarDespesas(View view) {

        if ( validarCamposD() ) {
            movimentacao = new Movimentacao();
            String data = editTextData.getText().toString();
            Double valorRecuperado = Double.parseDouble(editTextValor.getText().toString());

            movimentacao.setValor(valorRecuperado);
            movimentacao.setData(data);
            movimentacao.setCategoria(editTextCategoria.getText().toString());
            movimentacao.setDescricao(editTextDscr.getText().toString());
            movimentacao.setTipo("D");


            Double despesaAtualizada = despesaTotal + valorRecuperado;
            atualizarDespesas(despesaAtualizada);

            movimentacao.salvar(data);
            finish();
        }
    }

    public Boolean validarCamposD() {

        String textoValor = editTextValor.getText().toString();
        String textoData = editTextData.getText().toString();
        String textoCategoria = editTextCategoria.getText().toString();
        String textoDescricao = editTextDscr.getText().toString();

        if (!textoValor.isEmpty()){
            if (!textoData.isEmpty()){
                if (!textoCategoria.isEmpty()){
                    if (!textoDescricao.isEmpty()){

                        return true;

                    }else {

                        Toast.makeText(this,
                                "Descrição não preenchida",
                                Toast.LENGTH_SHORT).show();
                                return false;
                    }


                }else {

                    Toast.makeText(this,
                            "Categoria não preenchida",
                            Toast.LENGTH_SHORT).show();
                            return false;
                }


            }else {

                Toast.makeText(this,
                        "Data não preenchida",
                        Toast.LENGTH_SHORT).show();
                        return false;
            }

        }else{
            Toast.makeText(this,
                    "Valor não preenchido",
                    Toast.LENGTH_SHORT).show();
                    return false;
        }


    }

    public void recuperarDespTotal(){

        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = databaseReference.child("usuarios")
                .child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void atualizarDespesas(Double despesa){

        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = databaseReference.child("usuarios")
                .child(idUsuario);
        usuarioRef.child("despesaTotal").setValue(despesa);
    }

}


