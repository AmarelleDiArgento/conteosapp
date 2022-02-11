package com.lotus.conteos_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
            recibirUsuario();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }


        class MyKeyListerner implements View.OnKeyListener {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
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
                    Toast.makeText(getApplicationContext(), "Se guardo exitosamente tu usuario", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        });


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

    public void btn_download(View v){
        Intent i = new Intent(this, splash_activity.class);
        i.putExtra("redireccion", 1);
        startActivity(i);
    }

    public void validacion_user() {
        String txt_user = txtu.getText().toString();
        String txt_pass = txtp.getText().toString();

        try {
            monitorTab m = iM.login(txt_user, txt_pass);

            if (m != null) {
                guardarUsuario(m);

                Toast.makeText(this, "Bienvenido", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, HistorialMainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Usuario o clave incorrectas", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarUsuario(monitorTab u) {
        try {

            Log.i("INGRESO", "user : "+u.getNombres());

            SharedPreferences.Editor edit = sp.edit();
            edit.putString("codigo", u.getCodigo());
            edit.putString("nombre", u.getNombres()+" "+u.getApellidos());
            edit.putString("pass", u.getPassword());
            edit.putInt("idFinca", u.getIdFinca());
            edit.commit();
            edit.apply();
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
