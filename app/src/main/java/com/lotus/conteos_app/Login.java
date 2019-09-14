package com.lotus.conteos_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.Switch;
import android.widget.Toast;

public class Login extends AppCompatActivity{

    //DECLARACION DE VARIABLES
    Button btn_login;
    EditText txtu,txtp;
    Switch switchx;

    String txtus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //ASOCIACION DE LOS CAMPOS Y BOTONES
        txtu=(EditText)findViewById(R.id.txt_user);
        txtp=(EditText)findViewById(R.id.txt_pass);
        btn_login=(Button) findViewById(R.id.btn_login);
        switchx = (Switch) findViewById(R.id.switch1);


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


        switchx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked && txtu!=null){
                    guardarUsuario();
                }else {
                    Toast.makeText(getApplicationContext(),"se cerrorn el chek",Toast.LENGTH_SHORT).show();
                }
            }
        });

        recibirUsuario();
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


    //metodo para recordar el usuario
    public void recuerdame(View v){

        switchx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                try{

                    if(isChecked){
                        Toast.makeText(getApplicationContext(),"activo", Toast.LENGTH_LONG).show();
                        guardarUsuario();
                    }else {
                        Toast.makeText(getApplicationContext(),"inactivo", Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Error \n"+e, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void guardarUsuario(){
        try{
            Toast.makeText(getApplicationContext(),"llegamos antes del shared", Toast.LENGTH_LONG).show();
                SharedPreferences usuario = getBaseContext().getSharedPreferences("usuario",MODE_PRIVATE);
                SharedPreferences.Editor edit = usuario.edit();
                txtus=txtu.getText().toString();
                if(txtus!=null){
                    edit.putString("usuario",txtus);
                    edit.commit();
                    edit.apply();
                    Toast.makeText(getApplicationContext(),"se guardo \n", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Verifica si has ingresado un usuario", Toast.LENGTH_LONG).show();
                }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error \n"+e, Toast.LENGTH_LONG).show();
        }
    }

    public void recibirUsuario(){

        try{
            SharedPreferences usuario = getBaseContext().getSharedPreferences("usuario",MODE_PRIVATE);
            if(usuario!=null){
                txtus = (String)usuario.getString("usuario",txtus);
                txtu.setText(txtus);
            }
        }catch (Exception e){
            Toast.makeText(this,"Error \n"+e.toString(),Toast.LENGTH_LONG).show();
        }


    }

    //CERRAR APP
    public void onBackPressed() {
        System.exit(0);
        finish();
    }

}
