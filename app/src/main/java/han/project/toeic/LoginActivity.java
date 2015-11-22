package han.project.toeic;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button Signin, Signup;
    String strUsername, strPassword;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = (EditText) findViewById(R.id.edtUsername);
        etPassword = (EditText) findViewById(R.id.edtPassword);
        Signin = (Button) findViewById(R.id.btnSignin);
        Signup = (Button) findViewById(R.id.btnSignup);
        db = new MyDatabase(this);
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Taodialog();
            }
        });
    }

    public void validate() {
        try {
            strUsername = etUsername.getText().toString();
            strPassword = etPassword.getText().toString();
            String storedpass = db.selectusername(strUsername);
            if (strUsername.trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Enter username please!", Toast.LENGTH_SHORT).show();
            } else if (strPassword.trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Enter password please!", Toast.LENGTH_SHORT).show();
            } else if (strPassword.equals(storedpass)) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "username or  passwrod invalid !", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Taodialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_register);
        dialog.setTitle("Register ");
        final EditText et1_username = (EditText) dialog.findViewById(R.id.editText1);
        final EditText et2_password = (EditText) dialog.findViewById(R.id.editText2);
        final Button register = (Button) dialog.findViewById(R.id.button1);
        final Button cancel = (Button) dialog.findViewById(R.id.button3);
        final Button reset = (Button) dialog.findViewById(R.id.button2);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = et1_username.getText().toString();
                String password = et2_password.getText().toString();
                if (username.trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter username please ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter password please", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    register re = new register(username, password);
                    db.insert(re);
                    Toast.makeText(getApplicationContext(), "Your account have been created ", Toast.LENGTH_SHORT);
                    dialog.cancel();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "error: ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et1_username.setText("");
                et2_password.setText("");
            }
        });
        dialog.show();
    }

}
