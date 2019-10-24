package com.devhome.eduardobastos.organizze.activitys.model;

import com.devhome.eduardobastos.organizze.activitys.config.ConfiguracaoFirebase;
import com.devhome.eduardobastos.organizze.activitys.helper.Base64Custom;
import com.devhome.eduardobastos.organizze.activitys.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {

    private String data, categoria, descricao, tipo, key;
    private Double valor;


    public Movimentacao() {
    }

    public void salvar(String dataEsolhida){

        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());
        String mesAno = DateCustom.mesAnoDataEscolhida(dataEsolhida);

        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        databaseReference.child("movimentacao")
                .child(idUsuario)
                .child( mesAno)
                .push()
                .setValue(this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
