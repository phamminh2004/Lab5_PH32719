package com.minhpt.lab5_ph32719;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Bai2 extends AppCompatActivity {
    ListStudentAdapter listStudentAdapter;
    SearchView searchView;
    Toolbar toolbar;
    ArrayList<Student> list = new ArrayList<>();
    public static final String KEY_SV_MODEL = "sv_model";
    String FILE_NAME = "student.txt";

    private Student svModel;
    ActivityResultLauncher<Intent> getData = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 1) {
                        Intent intent = result.getData();
                        Bundle bundle = intent.getExtras();
                        String name = bundle.getString("name");
                        String address = bundle.getString("address");
                        String branch = bundle.getString("branch");
                        list.add(new Student(branch, name, address));
                        listStudentAdapter.notifyDataSetChanged();
                        luuListDulieu();
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> updateData = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 1) {
                        Intent intent = result.getData();
                        Bundle bundle = intent.getExtras();
                        String name = bundle.getString("name");
                        String address = bundle.getString("address");
                        String branch = bundle.getString("branch");

                        svModel.name = name;
                        svModel.address = address;
                        svModel.branch = branch;

                        listStudentAdapter.notifyDataSetChanged();
                        luuListDulieu();
                    }
                }
            }
    );

    public void updateSV(int position) {
        Intent intent = new Intent(Bai2.this, Bai1.class);
        svModel = list.get(position);
        intent.putExtra(KEY_SV_MODEL, svModel);
        updateData.launch(intent);
    }

    public void luuListDulieu() {
        try {
            FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(list);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }

    }

    public void docListDulieu() {
        try {
            FileInputStream fileInputStream = this.openFileInput(FILE_NAME);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            list = (ArrayList<Student>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai2);
        ListView lv_student = findViewById(R.id.lv_student);
        docListDulieu();
        Button btn_add = findViewById(R.id.btn_add);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (list.size() == 0) {
            list.add(new Student("Hà Nội", "Phạm Minh", "Vĩnh Phúc"));
            list.add(new Student("Đà Nẵng", "Quốc Anh", "Vĩnh Phúc"));
            list.add(new Student("Tây Nguyên", "Văn Quân", "Hà Nội"));
            list.add(new Student("Cần Thơ", " Phạm Linh", "Cần Thơ"));
        }
        listStudentAdapter = new ListStudentAdapter(this, list);
        lv_student.setAdapter(listStudentAdapter);
        luuListDulieu();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bai2.this, Bai1.class);
                getData.launch(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listStudentAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listStudentAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.dx) {
            Intent intent = new Intent(this, DangNhap.class);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.item_add) {
            Intent intent = new Intent(Bai2.this, Bai1.class);
            getData.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
