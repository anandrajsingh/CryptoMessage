package android.example.sch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

public class MD5 extends AppCompatActivity {
    String password = "";
    Button hash_button, clear;
    EditText input_text;
    TextView output_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md5);

        input_text = findViewById(R.id.input_text);
        output_text = findViewById(R.id.output_text);
        hash_button = findViewById(R.id.clear_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        hash_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = input_text.getText().toString();
                if(password.length()>0)
                    output_text.setText(hashPassword(password));
                else
                    Toast.makeText(MD5.this, "Empty Output", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String hashPassword(String password){
        String passwordtohash = password;
        String generatedPassword = null;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordtohash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();

            for(int i=0; i<bytes.length; i++){
                sb.append(Integer.toString((bytes[i]&0xff)+0x100,32).substring(1));
            }
            generatedPassword = sb.toString();
            Log.d("Ayush","generated password"+generatedPassword);
            return generatedPassword;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }

    public void getspeechinput(View view) {

        Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent,1001);
        }
        else
        {
            Toast.makeText(this,"your device does not support this feature",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case 1001:
                if(resultCode==RESULT_OK&&data!=null)
                {
                    ArrayList<String> res=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    input_text.setText(res.get(0));
                }
                break;
        }
    }
}