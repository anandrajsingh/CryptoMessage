package android.example.sch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES extends AppCompatActivity {

    EditText input_text, password_text;
    TextView output_text;
    Button enc, dec, clear, reset, send;
    String outputstring="";
    String AES = "AES";
    public static String pwdtext = "qwerty";
    String inptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes);

        input_text = findViewById(R.id.input_text);
        output_text = findViewById(R.id.output_text);
        enc = findViewById(R.id.encrypt);
        dec = findViewById(R.id.decrypt);
        clear = findViewById(R.id.clear_button);
        reset = findViewById(R.id.reset);
        send = findViewById(R.id.send);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    inptext = input_text.getText().toString();
                    outputstring = encrypt(inptext, pwdtext);
                    output_text.setText(outputstring);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    inptext = input_text.getText().toString();
                    outputstring = decrypt(inptext, pwdtext);
                    output_text.setText(outputstring);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    input_text.setText(" ");
                    output_text.setText("");
                    input_text.setHint("Enter Message");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(AES.this, ResetPassword.class);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(outputstring.length()>0){
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, outputstring);
                    sendIntent.setType("type/plain");
                    startActivity(sendIntent);
                }else{
                    Toast.makeText(AES.this, "empty output", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String encrypt(String data, String password_text) throws Exception{
        SecretKeySpec key = generateKey(password_text);
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(data.getBytes(StandardCharsets.UTF_8));
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private String decrypt(String data, String password_text) throws Exception{
        SecretKeySpec key = generateKey(password_text);
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedValue = Base64.decode(data, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue,"UTF-8");
        return decryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    public void getspeechinput(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,1001);
        }else{
            Toast.makeText(this, "Your Device does Not support this Feature", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1001:
                if(resultCode == RESULT_OK && data!=null){
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    input_text.setText(res.get(0));
                }
                break;
        }
    }
}