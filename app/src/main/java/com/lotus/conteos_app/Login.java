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

import com.lotus.conteos_app.Model.iMonitor;
import com.lotus.conteos_app.Model.tab.monitorTab;

import java.io.File;
import java.util.List;

public class Login extends AppCompatActivity {

    SharedPreferences sp = null;

    //DECLARACION DE VARIABLES
    Button btn_login;
    EditText txtu, txtp;
    Switch switchx;

    iMonitor iM = null;
    List<monitorTab> ml;
    String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        sp = getBaseContext().getSharedPreferences("share", MODE_PRIVATE);

        //ASOCIACION DE LOS CAMPOS Y BOTONES
        txtu = (EditText) findViewById(R.id.txt_user);
        txtp = (EditText) findViewById(R.id.txt_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
        switchx = (Switch) findViewById(R.id.switch1);

        path = getExternalFilesDir(null) + File.separator;


        try {
            iM = new iMonitor(path);
            ml = iM.all();
            Toast.makeText(this, ml.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }


        class MyKeyListerner implements View.OnKeyListener {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //Toast.makeText(Login.this,"se oprimio el boton",Toast.LENGTH_SHORT).show();
                    validacion_user();
                    return true;
                }
                return false;
            }
        }

        View.OnKeyListener listener = new MyKeyListerner();
        txtp.setOnKeyListener(listener);


        switchx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && txtu != null) {
                } else {
                    Toast.makeText(getApplicationContext(), "se cerrorn el chek", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recibirUsuario();
    }

    //REDIRECCIONAR A LA INFORMACION DE LA APP
    public void quien(View v) {
        Intent i = new Intent(Login.this, quienesomos.class);
        startActivity(i);
    }

    //METODO PARA VALIDAR EL LOGIN
    public void btn_login(View v) {
        validacion_user();
    }

    public void validacion_user() {
        String txt_user = txtu.getText().toString();
        String txt_pass = txtp.getText().toString();

        try {
            monitorTab m = iM.login(txt_user, txt_pass);

            if (m != null) {
                if (m.isEstado()) {

                    guardarUsuario(m);

                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, HistorialMainActivity.class);
                    startActivity(intent);

                    finish();
                } else {
                    Toast.makeText(this, "Usuario inactivo", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Usuario o clave incorrectas", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void guardarUsuario(monitorTab u) {
        try {
            SharedPreferences.Editor edit = sp.edit();
            if (u != null) {
                edit.putString("codigo", u.getCodigo());
                edit.putString("nombre", u.getFullName());
                edit.putString("pass", u.getPassword());
                edit.putInt("idFinca", u.getIdFinca());
                edit.commit();
                edit.apply();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error \n" + e, Toast.LENGTH_LONG).show();
        }
    }

    public void recibirUsuario() {

        try {
            if (sp != null) {
                txtu.setText(sp.getString("codigo", ""));
                txtp.setText(sp.getString("pass", ""));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error \n" + e.toString(), Toast.LENGTH_LONG).show();
        }


    }

    //CERRAR APP
    public void onBackPressed() {
        System.exit(0);
        finish();
    }

}
