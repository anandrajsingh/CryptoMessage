package android.example.sch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPassword extends AppCompatActivity {
    EditText old, newpwd, cnfpwd;
    Button save;
    String oldPassword, newPassword, cnfPassword;
    String pwd = AES.pwdtext.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        old = findViewById(R.id.old);
        newpwd =  findViewById(R.id.newp);
        cnfpwd = findViewById(R.id.cnfpwd);

        save = findViewById(R.id.save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassword = old.getText().toString();
                newPassword = newpwd.getText().toString();
                cnfPassword = cnfpwd.getText().toString();

                if(pwd.equals(oldPassword)){
                    if(newPassword.equals(cnfPassword)){
                        AES.pwdtext = newPassword;
                        System.out.println("Updated Successfully");
                        Toast.makeText(ResetPassword.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ResetPassword.this, "Password Does Not Match", Toast.LENGTH_SHORT).show();
                        System.out.println("Password does not match");
                    }
                }else{
                    System.out.println(pwd);
                    Toast.makeText(ResetPassword.this, "Old Password Does Not Match", Toast.LENGTH_SHORT).show();
                    System.out.println("Old Password Does Not Match");
                    System.out.println(oldPassword);
                }
            }
        });
    }
}