package com.lotus.conteos_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    //DECLARACION DE VARIABLES
    Button btn_login;
    EditText txtu,txtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ASOCIACION DE LOS CAMPOS Y BOTONES
        txtu=(EditText)findViewById(R.id.txt_user);
        txtp=(EditText)findViewById(R.id.txt_pass);
        btn_login=(Button) findViewById(R.id.btn_login);
    }

    //METODO PARA VALIDAR EL LOGIN
    public void btn_login(View v){
        String txt_user=txtu.getText().toString();
        String txt_pass=txtp.getText().toString();

            if(txt_user.equals("123") && txt_pass.equals("123")){
                        Toast.makeText(this,"Bienvenido",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(v.getContext(),MainActivity.class);
                        startActivityForResult(intent, 0);
            }else{
                        Toast.makeText(this,"usuario y clave incorrectas",Toast.LENGTH_SHORT).show();
            }
    }
}
