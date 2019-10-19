package com.devhome.eduardobastos.organizze.activitys.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual(){

        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");//"dd/MM/yyyy hh:mm:ss"
        String dataString = simpleDateFormat.format(date);
        return dataString;

    }
    public static String mesAnoDataEscolhida(String data){

       String retornoData[] = data.split("/");
       String dia = retornoData[0];//dia
       String mes = retornoData[1];//mes
       String ano = retornoData[2];//ano

       String mesAno = mes + ano;
       return mesAno;


    }
}
