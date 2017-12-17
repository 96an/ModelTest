package com.example.eban.modeltest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eban.modeltest.Model.User;
import com.example.eban.modeltest.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    MaterialEditText edtNewUser, edtNewPass, edtNewEmail;
    MaterialEditText mUserIn, mPassIn;

    Button mSignInBtn, mSignUpBtn;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        mUserIn = (MaterialEditText) findViewById(R.id.edtUser);
        mPassIn = (MaterialEditText) findViewById(R.id.edtPass);

        mSignInBtn = (Button) findViewById(R.id.sign_in);
        mSignUpBtn = (Button) findViewById(R.id.sign_up);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(mUserIn.getText().toString(), mPassIn.getText().toString());
            }
        });
    }

    private void signIn(final String userName, final String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(userName).exists()) {
                    if (!userName.isEmpty()) {
                        User login = dataSnapshot.child(userName).getValue(User.class);
                        if (login.getPassword().equals(pwd)) {
//                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = login;
                            startActivity(homeIntent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Please Enter Correct user Name", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "User Does Not Exits", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showSignUpDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please Full Fill The Information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);

        edtNewUser = (MaterialEditText) sign_up_layout.findViewById(R.id.edtNewUser);
        edtNewPass = (MaterialEditText) sign_up_layout.findViewById(R.id.edtNewPass);
        edtNewEmail = (MaterialEditText) sign_up_layout.findViewById(R.id.edtNewEmail);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User user = new User(
                        edtNewUser.getText().toString(),
                        edtNewPass.getText().toString(),
                        edtNewEmail.getText().toString()
                );

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists())
                            Toast.makeText(MainActivity.this, "User Exits", Toast.LENGTH_LONG).show();
                        else {
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}