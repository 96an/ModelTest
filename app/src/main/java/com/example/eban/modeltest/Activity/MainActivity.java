package com.example.eban.modeltest.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.eban.modeltest.BroadcastReceiver.AlarmReceiver;
import com.example.eban.modeltest.Model.User;
import com.example.eban.modeltest.R;
import com.example.eban.modeltest.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    MaterialEditText edtNewUser, edtNewPass, edtNewEmail;
    MaterialEditText mUserIn, mPassIn;

    Button mSignInBtn, mSignUpBtn;

    FirebaseDatabase database;
    DatabaseReference users;

    CheckBox mCheckBox;

    public static String PRESS = "PRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerAlarm();

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        mUserIn = findViewById(R.id.edtUser);
        mPassIn = findViewById(R.id.edtPass);

        mSignInBtn = findViewById(R.id.sign_in);
        mSignUpBtn = findViewById(R.id.sign_up);

        mCheckBox = findViewById(R.id.check);

        receiveData();

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCheckBox.isChecked()) {
                    saveData();
                    signIn(mUserIn.getText().toString(), mPassIn.getText().toString());
                } else {
                    signIn(mUserIn.getText().toString(), mPassIn.getText().toString());
                }
            }
        });
    }

    public void saveData() {

        String userName = mUserIn.getText().toString();
        String userPass = mPassIn.getText().toString();

        SharedPreferences preferences = getSharedPreferences(PRESS, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER", userName);
        editor.putString("PASS", userPass);
        editor.commit();
    }

    public void receiveData() {
        SharedPreferences preferences = getSharedPreferences(PRESS, 0);
        String u = preferences.getString("USER", null);
        String p = preferences.getString("PASS", null);

        mUserIn.setText(u);
        mPassIn.setText(p);

    }

    private void registerAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 10);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

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

        edtNewUser =  sign_up_layout.findViewById(R.id.edtNewUser);
        edtNewPass =  sign_up_layout.findViewById(R.id.edtNewPass);
        edtNewEmail = sign_up_layout.findViewById(R.id.edtNewEmail);

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
