package com.yajith.harrypotter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button voice;
    int count=1;
    int randNumber,told_number;
    Map<String, Integer> numbers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        voice=findViewById(R.id.voice);
        numbers=new HashMap<>();
        addNumbers();
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==1||count==4){
                    randNumber=getrandom();
                    count=1;
                }
                stt(100);
            }
        });

    }

    private void addNumbers() {
        numbers.put("one",1);
        numbers.put("two",2);
        numbers.put("three",3);
        numbers.put("four",4);
        numbers.put("five",5);
        numbers.put("six",6);
        numbers.put("seven",7);
        numbers.put("eight",8);
        numbers.put("nine",9);
        numbers.put("ten",10);
    }

    public void stt(int REQUEST_CODE_SPEECH_INPUT){
        Intent intent
                = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a number between 0 to 10");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e) {
            Toast
                    .makeText(MainActivity.this, " " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);
                    String re=Objects.requireNonNull(result).get(0).toString();
                    Log.i("told",re);
                    if(!re.equals("")){
                        try {
                            told_number=Integer.parseInt(re);
                            count++;
                            if(randNumber==told_number)
                            {
                                //todo tell crt and open
                                Toast.makeText(this, "crt", Toast.LENGTH_SHORT).show();

                                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Congrats!");
                                builder.setMessage("You Found the number. Book is now unlocked");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        count=1;
                                        randNumber=getrandom();
                                       dialog.dismiss();
                                    }
                                });
                                AlertDialog alertDialog=builder.create();
                                alertDialog.show();
                            }
                            else {
                                //todo tell wrong
                                Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
                                if(count==4)
                                {
                                    Toast.makeText(this, "over", Toast.LENGTH_SHORT).show();
                                    //todo tell last chance over
                                    count=1;
                                }
                            }
                        }
                        catch (Exception e){
                            if(numbers.containsKey(re))
                            {
                                told_number=numbers.get(re);
                                if(randNumber==told_number)
                                {
                                    //todo tell crt and open
                                    Toast.makeText(this, "crt", Toast.LENGTH_SHORT).show();

                                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Congrats!");
                                    builder.setMessage("You Found the number. Book is now unlocked");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            count=1;
                                            randNumber=getrandom();
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog alertDialog=builder.create();
                                    alertDialog.show();
                                }
                                else {
                                    //todo tell wrong
                                    Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
                                    if(count==4)
                                    {
                                        Toast.makeText(this, "over the number is "+randNumber, Toast.LENGTH_SHORT).show();
                                        //todo tell last chance over

                                        count=1;
                                        randNumber=getrandom();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public int getrandom(){
        int min = 0;
        int max = 10;
        return (int)(Math.random()*(max-min+1)+min);
    }
}