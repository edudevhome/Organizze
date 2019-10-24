package com.devhome.eduardobastos.organizze.activitys;

import android.content.Intent;
import android.os.Bundle;

import com.devhome.eduardobastos.organizze.activitys.adapter.AdapterMovimentacao;
import com.devhome.eduardobastos.organizze.activitys.config.ConfiguracaoFirebase;
import com.devhome.eduardobastos.organizze.activitys.helper.Base64Custom;
import com.devhome.eduardobastos.organizze.activitys.model.Movimentacao;
import com.devhome.eduardobastos.organizze.activitys.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.devhome.eduardobastos.organizze.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TelaPrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView materialCalendarView;
    private TextView textViewSaldo, textoSaudacao;
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private Double receitaTotal, despesaTotal, resumoUsuario = 0.0;
    private  DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacao;
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);


        textViewSaldo = findViewById(R.id.textViewSaldo);
        textoSaudacao = findViewById(R.id.textViewSaudacao);
        materialCalendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);
        configuraCalendarView();

        //configura adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        //configura RecyclerView
        RecyclerView.LayoutManager layoutManager = new  LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
    }

    public void recuperarMovimentacao(){
        String emailUsuario = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        movimentacaoRef = databaseReference.child("movimentacao")
                                            .child(idUsuario)
                                            .child(mesAnoSelecionado);
        //Log.i("dadosRetorno", "dados" + mesAnoSelecionado);
        valueEventListenerMovimentacao = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                movimentacoes.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    //Log.i("dados", "retorno" + dados.toString());
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    //Log.i("dadosRetorno", "dados" + movimentacao.getCategoria());
                    movimentacoes.add(movimentacao);
                }

                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void recuperarResumo() {

            String emailUsuario = auth.getCurrentUser().getEmail();
            String idUsuario = Base64Custom.codificarBase64(emailUsuario);
            usuarioRef = databaseReference.child("usuarios").child(idUsuario);

            Log.i("Evento", "evento foi adicionado!");

             valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                    despesaTotal = usuario.getDespesaTotal();
                    receitaTotal = usuario.getReceitaTotal();
                    resumoUsuario = receitaTotal - despesaTotal;

                    DecimalFormat decimalFormat = new DecimalFormat("0.##");
                    String resultadoFormat = decimalFormat.format(resumoUsuario);
                    textoSaudacao.setText("Olá " + usuario.getNome());
                    textViewSaldo.setText("R$ " + resultadoFormat);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_sair:

                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void AddDespesa(View view){

        startActivity(new Intent(this,DespesasActivity.class));

    }

    public void AddReceita(View view){

        startActivity(new Intent(this, ReceitasActivity.class));

    }

    public void configuraCalendarView(){

        CalendarDay dataAtual = materialCalendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth()));
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear());

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth()));
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());
                //Log.i("MES", "mes" + mesAnoSelecionado);
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
                recuperarMovimentacao();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacao();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Evento", "evento foi removido!");
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);

    }
}
