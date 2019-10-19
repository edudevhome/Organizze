package com.devhome.eduardobastos.organizze.activitys.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference databaseReference;

    //retorna a instancia do firebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao() {

        if (autenticacao == null) {

            autenticacao = FirebaseAuth.getInstance();

        }
        return autenticacao;
    }

    public static DatabaseReference getDatabaseReference() {

        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();

        }
        return databaseReference;
    }
}