package com.lotus.conteos_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

public class Login extends AppCompatActivity{

    //DECLARACION DE VARIABLES
    Button btn_login;
    EditText txtu,txtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //ASOCIACION DE LOS CAMPOS Y BOTONES
        txtu=(EditText)findViewById(R.id.txt_user);
        txtp=(EditText)findViewById(R.id.txt_pass);
        btn_login=(Button) findViewById(R.id.btn_login);

        class MyKeyListerner implements View.OnKeyListener{
            public  boolean onKey(View v,int keyCode, KeyEvent event){
                if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode ==  KeyEvent.KEYCODE_ENTER)){
                    //Toast.makeText(Login.this,"se oprimio el boton",Toast.LENGTH_SHORT).show();
                    validacion_user();
                    return true;
                }
            return  false;
            }
        }

        View.OnKeyListener listener = new MyKeyListerner();
        txtp.setOnKeyListener(listener);
    }

    //REDIRECCIONAR A LA INFORMACION DE LA APP
    public void quien(View v){
        Intent i = new Intent(Login.this,quienesomos.class);
        startActivity(i);
    }

    //METODO PARA VALIDAR EL LOGIN
    public void btn_login(View v){
        validacion_user();
    }

    public  void validacion_user(){
        String txt_user=txtu.getText().toString();
        String txt_pass=txtp.getText().toString();


        if(txt_user.equals("123") && txt_pass.equals("123")){
            Toast.makeText(this,"Bienvenido",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this,HistorialMainActivity.class);
            startActivity(intent);

            finish();
        }else{
            Toast.makeText(this,"usuario y clave incorrectas",Toast.LENGTH_SHORT).show();
        }
    }


    //CERRAR APP
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

}
