package com.example.hwa522internal;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText mLogin;
    private EditText mPassword;
    Button btnLogin;
    Button btnRegistration;
    CheckBox checkBox;
    boolean isChecked;
    private SharedPreferences mySharedPref;
    private static final String CHOSE = "CHOSE";
    private static final String LOG = "myLogs";
    private static final String LOGIN_PASSWORD = "LOGIN_PASSWORD";
    private static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBox = findViewById(R.id.btnCheckbox);

        isChecked = checkBox.isChecked();

        if (!isChecked) {
            mRegLogInternal();
        } else {
            mRegLogExternal();
        }



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    mRegLogInternal();
                } else {
                    mRegLogExternal();
                }

                SharedPreferences.Editor myEditor = mySharedPref.edit();
                myEditor.putBoolean(CHOSE, isChecked);
                myEditor.apply();
                Toast.makeText(MainActivity.this, "Ваш выбор сохранён!",
                        Toast.LENGTH_LONG).show();
            }
        });

        mySharedPref = getSharedPreferences(CHOSE, MODE_PRIVATE);

        checkBox.setChecked(mySharedPref.getBoolean(CHOSE, false));
    }

    private void mRegLogInternal() {
        mLogin = findViewById(R.id.login);
        mPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistration = findViewById(R.id.btnRegistration);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileLogin = mLogin.getText().toString().trim();
                String filePassword = mPassword.getText().toString().trim();
                FileOutputStream outputStream = null;
                BufferedWriter bw = null;


                Log.d(LOG, "Нажал на кнопку регестрации");
                Log.d(LOG, "login =" + fileLogin);
                Log.d(LOG, "password =" + filePassword);

                if (!reedAndSearchForInternal()) {

                    try {
                        if (!filePassword.equals("")) {
                            String total = fileLogin + ";" + filePassword;
                            outputStream = openFileOutput(LOGIN_PASSWORD, Context.MODE_APPEND);
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                            bw = new BufferedWriter(outputStreamWriter);
                            Log.d(LOG, "Запись: " + total);
                            bw.write(total);
                            bw.write("\n");

                            Toast.makeText(MainActivity.this, fileLogin + " успешно зарегистрирован", Toast.LENGTH_LONG).show();
                            Log.d(LOG, "Создание и запись логина и пароля");
                        } else {
                            Toast.makeText(MainActivity.this, "Убедитесь, что вы заполнели все строки", Toast.LENGTH_LONG).show();
                            Log.d(LOG, "Пустой пароль");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, fileLogin + " " + filePassword, Toast.LENGTH_LONG).show();
                        Log.d(LOG, "Ошибка создания и записи логина и пароля");
                    } finally {
                        if (bw != null) {
                            try {
                                bw.close();
                                Log.d(LOG, "Закрыли поток BufferedWriter");
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d(LOG, "Ошибка закрытие патока BufferedWriter");
                            }
                        }

                        if (outputStream != null) {
                            try {
                                outputStream.close();
                                Log.d(LOG, "Закрыли поток outputStream");
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d(LOG, "Ошибка закрытие патока outputStream");
                            }
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Такая учётная запись уже существует.", Toast.LENGTH_LONG).show();
                }
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileLogin = mLogin.getText().toString().trim();
                String filePassword = mPassword.getText().toString().trim();
                FileInputStream fileInputStream = null;
                StringBuilder result = null;
                BufferedReader reader = null;
                String[] mLoginPassword2;
                ArrayList<String> mLoginPassword = new ArrayList<>();
                int mYesOrNo = 0;
                Log.d(LOG, "Нажал на кнопку логин");

                try {
                    fileInputStream = openFileInput(LOGIN_PASSWORD);
                    result = new StringBuilder();
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    reader = new BufferedReader(inputStreamReader);
                    String line = reader.readLine();
                    Log.d(LOG, "Пробуем прочитать учётную запись");

                    while (line != null) {
                        result.append(line);
                        result.append("\n");
                        line = reader.readLine();
                        Log.d(LOG, "Пробуем записать учетку в массив");
                    }

                    String text = result.toString();
                    Log.d(LOG, "Результат чтения: " + result.toString());
                    mLoginPassword2 = text.split("\n");
                    mLoginPassword.addAll(Arrays.asList(mLoginPassword2));

                    for (String mLP : mLoginPassword) {
                        String[] mL0P1 = mLP.split(";");
                        ArrayList<String> m2L0P1 = new ArrayList<>();
                        m2L0P1.addAll(Arrays.asList(mL0P1));
                        Log.d(LOG, "Разбор логина и пароля по штучно " + m2L0P1);

                        try {
                            if (m2L0P1.get(0).equals(fileLogin) &&
                                    m2L0P1.get(1).equals(filePassword)) {
                                Toast.makeText(MainActivity.this, "Добро пожаловать : "
                                        + fileLogin + " ваш пороль : " + filePassword, Toast.LENGTH_LONG).show();
                                mYesOrNo = 1;
                                break;
                            } else {
                                Log.d(LOG, "Комбинация не верна");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            Log.d(LOG, "Такой учётной записи не существует.");
                        }

                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Такой учётной записи не существует. Вы можете зарегистрироваться.", Toast.LENGTH_LONG).show();
                    Log.d(LOG, "Ошибка, гдето null");
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Такая учётная запись уже существует.", Toast.LENGTH_LONG).show();
                    Log.d(LOG, "Ошибка создания");
                } finally {

                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                            Log.d(LOG, "Закрыли поток fileInputStream");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(LOG, "Ошибка закрытие патока fileInputStream");
                        }
                    }

                    if (reader != null) {
                        try {
                            reader.close();
                            Log.d(LOG, "Закрыли поток reader");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(LOG, "Ошибка закрытие патока reader");
                        }
                    }
                }

                if (mYesOrNo == 0) {
                    Toast.makeText(MainActivity.this, "Неверен логин или пароль!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean reedAndSearchForInternal() {

        boolean search = false;

        String fileLogin = mLogin.getText().toString().trim();
        String filePassword = mPassword.getText().toString().trim();
        FileInputStream fileInputStream = null;
        StringBuilder result = null;
        BufferedReader reader = null;
        String[] mLoginPassword2;
        ArrayList<String> mLoginPassword = new ArrayList<>();
        Log.d(LOG, "reedAndSearch Нажал на кнопку логин");

        try {
            fileInputStream = openFileInput(LOGIN_PASSWORD);
            result = new StringBuilder();
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            Log.d(LOG, "reedAndSearch Пробуем прочитать учётную запись");

            while (line != null) {
                result.append(line);
                result.append("\n");
                line = reader.readLine();
                Log.d(LOG, "reedAndSearch Пробуем записать учетку в массив");
            }

            String text = result.toString();
            Log.d(LOG, "reedAndSearch Результат чтения: " + result.toString());
            mLoginPassword2 = text.split("\n");
            mLoginPassword.addAll(Arrays.asList(mLoginPassword2));

            for (String mLP : mLoginPassword) {
                String[] mL0P1 = mLP.split(";");
                ArrayList<String> m2L0P1 = new ArrayList<>();
                m2L0P1.addAll(Arrays.asList(mL0P1));
                Log.d(LOG, "reedAndSearch Разбор логина и пароля по штучно " + m2L0P1);

                try {
                    if (m2L0P1.get(0).equals(fileLogin)) {
                        Toast.makeText(MainActivity.this, "Такой пользователь уже существует: "
                                + fileLogin, Toast.LENGTH_LONG).show();
                        search = true;
                        Log.d(LOG, "reedAndSearch Есть совпадение: " + m2L0P1.get(0));
                        break;
                    } else {
                        Log.d(LOG, "reedAndSearch Комбинация не верна");
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.d(LOG, "reedAndSearch Такой учётной записи не существует.");
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(LOG, "reedAndSearch Ошибка, гдето null");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(LOG, "reedAndSearch Ошибка создания");
        } finally {

            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    Log.d(LOG, "reedAndSearch Закрыли поток fileInputStream");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(LOG, "reedAndSearch Ошибка закрытие патока fileInputStream");
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                    Log.d(LOG, "reedAndSearch Закрыли поток reader");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(LOG, "reedAndSearch Ошибка закрытие патока reader");
                }
            }
        }

        return search;
    }

    private void mRegLogExternal() {
        mLogin = findViewById(R.id.login);
        mPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistration = findViewById(R.id.btnRegistration);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_STORAGE);
        }

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileLogin = mLogin.getText().toString().trim();
                String filePassword = mPassword.getText().toString().trim();
                String total = fileLogin + ";" + filePassword;

                if (!reedAndSearchForExternal()) {

                    if (isExternalStorageWritable()) {
                        File textFile = new File(getExternalFilesDir(null), LOGIN_PASSWORD);
                        BufferedWriter bw = null;

                        try {
                            if (!filePassword.equals("")) {
                                bw = new BufferedWriter(new FileWriter(textFile, true));
                                bw.append(total);
                                bw.append("\n");

                                Toast.makeText(MainActivity.this, fileLogin + " успешно зарегистрирован", Toast.LENGTH_LONG).show();
                                Log.d(LOG, "mRegLogExternal Создание и запись логина и пароля");
                            } else {
                                Toast.makeText(MainActivity.this, "Убедитесь, что вы заполнели все строки", Toast.LENGTH_LONG).show();
                                Log.d(LOG, "mRegLogExternal Пустой пароль");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (bw != null) {
                                try {
                                    bw.close();
                                    Log.d(LOG, "mRegLogExternal Закрыли поток BufferedWriter");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.d(LOG, "mRegLogExternal Закрыли поток BufferedWriter");
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Такая учётная запись уже существует.", Toast.LENGTH_LONG).show();
                }
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileLogin = mLogin.getText().toString().trim();
                String filePassword = mPassword.getText().toString().trim();

                StringBuilder result = null;
                BufferedReader reader = null;

                String[] mLoginPassword2;
                ArrayList<String> mLoginPassword = new ArrayList<>();

                int mYesOrNo = 0;
                Log.d(LOG, "Нажал на кнопку логин");

                if (isExternalStorageWritable()) {

                    File textFile = new File(getExternalFilesDir(null), LOGIN_PASSWORD);
                    result = new StringBuilder();
                    try {
                        reader = new BufferedReader(new FileReader(textFile));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                            result.append("\n");
                        }

                        String text = result.toString();
                        Log.d(LOG, "mRegLogExternal Результат чтения: " + result.toString());
                        mLoginPassword2 = text.split("\n");
                        mLoginPassword.addAll(Arrays.asList(mLoginPassword2));

                        for (String mLP : mLoginPassword) {
                            String[] mL0P1 = mLP.split(";");
                            ArrayList<String> m2L0P1 = new ArrayList<>();
                            m2L0P1.addAll(Arrays.asList(mL0P1));
                            Log.d(LOG, "mRegLogExternal Разбор логина и пароля по штучно " + m2L0P1);

                            try {
                                if (m2L0P1.get(0).equals(fileLogin) &&
                                        m2L0P1.get(1).equals(filePassword)) {
                                    Toast.makeText(MainActivity.this, "Добро пожаловать : "
                                            + fileLogin + " ваш пороль : " + filePassword, Toast.LENGTH_LONG).show();
                                    mYesOrNo = 1;
                                    break;
                                } else {
                                    Log.d(LOG, "mRegLogExternal Комбинация не верна");
                                }
                            } catch (IndexOutOfBoundsException e) {
                                Log.d(LOG, "mRegLogExternal Такой учётной записи не существует.");
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                                Log.d(LOG, "mRegLogExternal Закрытие потока чтения");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }
                if (mYesOrNo == 0) {
                    Toast.makeText(MainActivity.this, "Неверен логин или пароль!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean reedAndSearchForExternal() {

        boolean search = false;

        String fileLogin = mLogin.getText().toString().trim();
        String filePassword = mPassword.getText().toString().trim();

        StringBuilder result = null;
        BufferedReader reader = null;

        String[] mLoginPassword2;
        ArrayList<String> mLoginPassword = new ArrayList<>();

        Log.d(LOG, "reedAndSearchForExternal проверка на логин");

        if (isExternalStorageWritable()) {

            File textFile = new File(getExternalFilesDir(null), LOGIN_PASSWORD);
            result = new StringBuilder();
            try {
                reader = new BufferedReader(new FileReader(textFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    result.append("\n");
                }

                String text = result.toString();
                Log.d(LOG, "mRegLogExternal Результат чтения: " + result.toString());
                mLoginPassword2 = text.split("\n");
                mLoginPassword.addAll(Arrays.asList(mLoginPassword2));

                for (String mLP : mLoginPassword) {
                    String[] mL0P1 = mLP.split(";");
                    ArrayList<String> m2L0P1 = new ArrayList<>();
                    m2L0P1.addAll(Arrays.asList(mL0P1));
                    Log.d(LOG, "mRegLogExternal Разбор логина и пароля по штучно " + m2L0P1);

                    try {
                        if (m2L0P1.get(0).equals(fileLogin)) {
                            search = true;
                            Log.d(LOG, "mRegLogExternal Есть совпадение: " + m2L0P1.get(0));
                            break;
                        } else {
                            Log.d(LOG, "mRegLogExternal Комбинация не верна");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        Log.d(LOG, "mRegLogExternal Такой учётной записи не существует.");
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                        Log.d(LOG, "mRegLogExternal Закрытие потока чтения");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
        return search;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}

