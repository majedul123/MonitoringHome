package ws.design.com.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ws.design.com.R;

public class signIn extends AppCompatActivity {

    EditText user;
    EditText pass;
    TextView login;
    TextView signup,signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");

        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        signup = (TextView) findViewById(R.id.signup);
        user.setTypeface(custom_font);


        pass.setTypeface(custom_font);
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");

        login = (TextView) findViewById(R.id.login);
        login.setTypeface(custom_font1);

        signup.setTypeface(custom_font);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(signIn.this, signUp.class);
                startActivity(it);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(signIn.this, MainActivity.class);
                startActivity(it);

            }
        });


    }
}
