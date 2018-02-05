package com.example.colin.subbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "subList.sav";
    private ListView oldSubList;
    private ArrayList<Subscription> subList;
    private ArrayAdapter<Subscription> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.parseColor("#FF99CC00"));
        //toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //toolbar.setTitleMargin(50,0,60,0);
        toolbar.setTitleMarginStart(600);

        oldSubList = (ListView) findViewById(R.id.oldSubList);
       // oldSubList.setText
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newSub();
            }
        });
    //https://stackoverflow.com/questions/2558591/remove-listview-items-in-android

        oldSubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                if(!subList.get(position).getComment().equalsIgnoreCase("")){
                    Toast.makeText(MainActivity.this,subList.get(position).getComment() , Toast.LENGTH_LONG).show();
                }
            }
        });

        oldSubList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Options");
                adb.setMessage("What do you want to do?");
                final int positionToRemove = position;

                adb.setNegativeButton("Edit", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editSub(positionToRemove);
                    }});
                adb.setPositiveButton("DELETE", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        subList.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                        setTotal();
                        saveSubscriptions();
                    }});
                adb.show();
                return true;
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        loadSubscriptions();
        //adapter = new ArrayAdapter<Subscription>(this,R.layout.list_item,subList);
        adapter = new ArrayAdapter<Subscription>(this,android.R.layout.simple_list_item_1,subList);
        //adapter.

        oldSubList.setAdapter(adapter);
        setTotal();
    }

    
    private void loadSubscriptions(){
        try{
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            subList = gson.fromJson(in, listType);

        }catch (FileNotFoundException e) {
            subList = new ArrayList<Subscription>();
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    private void saveSubscriptions(){
        try{
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(subList, out);
            out.flush();
        }catch (FileNotFoundException e) {
            throw new RuntimeException();
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
    private void newSub(){
        Intent intent = new Intent(this,SubActivity.class);
        startActivityForResult(intent,1);
    }
    private void editSub(int index){
        Intent intent = new Intent(this,SubActivity.class);
        intent.putExtra("index",Integer.toString(index));
        intent.putExtra("name",subList.get(index).getName());
        intent.putExtra("price",Double.toString(subList.get(index).getPrice()));
        intent.putExtra("date",subList.get(index).getDate());
        intent.putExtra("comment",subList.get(index).getComment());
        startActivityForResult(intent,2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String name =data.getStringExtra("name");
                String date=data.getStringExtra("date");
                String comment=data.getStringExtra("comment");
                Double price = Double.parseDouble(data.getStringExtra("price"));
                Subscription sub = new Subscription(name,date, price, comment);
                subList.add(sub);
                saveSubscriptions();
                setTotal();
                adapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                String name =data.getStringExtra("name");
                String date=data.getStringExtra("date");
                String comment=data.getStringExtra("comment");
                Double price = Double.parseDouble(data.getStringExtra("price"));
                String index = data.getStringExtra("index");

                subList.get(Integer.parseInt(index)).setComment(comment);
                subList.get(Integer.parseInt(index)).setDate(date);
                subList.get(Integer.parseInt(index)).setPrice(price);
                subList.get(Integer.parseInt(index)).setName(name);
                saveSubscriptions();
                setTotal();
                adapter.notifyDataSetChanged();
            }
        }
    }//onActivityResult
    private void setTotal(){
        Double total = 0.0;
        for (int i = 0; i < subList.size(); i++) {
            total = total + subList.get(i).getPrice();
        }

        DecimalFormat cents = new DecimalFormat("#0.00");

        setTitle("$"+cents.format(total));

    }



}
