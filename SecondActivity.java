
package com.examples.mahasiswasqlite;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class SecondActivity extends Activity {

    //Deklarasi Variabel UI (di atas onCreate):	
    ListView listView;
    Spinner spinnerJurusan;
    Button btnEdit, btnHapus, btnKembali, btnFilter;
    DBHelper db;
    ArrayList<String> listData;
    ArrayList<String> listNIM;
    ArrayAdapter<String> adapter;
    String selectedNIM = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    
	    //Inisialisasi ID Komponen (dalam onCreate):        
		db = new DBHelper(this);
        listView = (ListView) findViewById(R.id.listData);
        spinnerJurusan = (Spinner) findViewById(R.id.spinnerJurusan);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnHapus = (Button) findViewById(R.id.btnHapus);
        btnKembali = (Button) findViewById(R.id.btnKembali);
        btnFilter = (Button) findViewById(R.id.btnFilter);

        isiSpinnerJurusan();
        tampilkanData();

     // Pilih data dari ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNIM = listNIM.get(position);
                Toast.makeText(SecondActivity.this, "Dipilih NIM: " + selectedNIM, Toast.LENGTH_SHORT).show();
            }
        });
 
 
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedNIM.isEmpty()) {
                    Cursor res = db.getAllData();
                    String nama = "", jk = "", jurusan = "", kelas = "";

                    while (res.moveToNext()) {
                        if (res.getString(0).equals(selectedNIM)) {
                            nama = res.getString(1);
                            jk = res.getString(2);
                            jurusan = res.getString(3);
                            kelas = res.getString(4);
                            break;
                        }
                    }

                    Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                    intent.putExtra("mode", "edit");
                    intent.putExtra("nim", selectedNIM);
                    intent.putExtra("nama", nama);
                    intent.putExtra("jk", jk);
                    intent.putExtra("jurusan", jurusan);
                    intent.putExtra("kelas", kelas);
                    startActivity(intent);
                } else {
                    Toast.makeText(SecondActivity.this, "Pilih data terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedNIM.isEmpty()) {
                    db.deleteData(selectedNIM);
                    tampilkanData();
                    Toast.makeText(SecondActivity.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                    selectedNIM = "";
                } else {
                    Toast.makeText(SecondActivity.this, "Pilih data terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Tambahkan Aksi pada Tombol Filter:
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedJurusan = spinnerJurusan.getSelectedItem().toString();
                if (selectedJurusan.equals("Semua Jurusan")) {
                    tampilkanData();
                } else {
                    tampilkanDataFilter(selectedJurusan);
                }
            }
        });
    }

    //Tambahkan Method: isiSpinnerJurusan()	
    private void isiSpinnerJurusan() {
        ArrayList<String> daftarJurusan = db.getAllJurusan();
        if (daftarJurusan == null || daftarJurusan.isEmpty()) {
            daftarJurusan = new ArrayList<String>();
        }
        daftarJurusan.add(0, "Semua Jurusan");
        Log.d("DEBUG", "Jurusan ditemukan: " + daftarJurusan.toString());

        ArrayAdapter<String> jurusanAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, daftarJurusan);
        jurusanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJurusan.setAdapter(jurusanAdapter);
    }

    //Tambahkan Fungsi tampilkanDataFilter(String jurusan)
    private void tampilkanData() {
        listData = new ArrayList<String>();
        listNIM = new ArrayList<String>();
        Cursor res = db.getAllData();
        while (res.moveToNext()) {
            String nim = res.getString(0);
            String data = "NIM: " + nim +
                    "\nNama: " + res.getString(1) +
                    "\nJK: " + res.getString(2) +
                    "\nJurusan: " + res.getString(3) +
                    "\nKelas: " + res.getString(4);
            listNIM.add(nim);
            listData.add(data);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
    }

    private void tampilkanDataFilter(String jurusan) {
        listData = new ArrayList<String>();
        listNIM = new ArrayList<String>();
        Cursor res = db.getAllData();
        while (res.moveToNext()) {
            if (res.getString(3).equalsIgnoreCase(jurusan)) {
                String nim = res.getString(0);
                String data = "NIM: " + nim +
                        "\nNama: " + res.getString(1) +
                        "\nJK: " + res.getString(2) +
                        "\nJurusan: " + res.getString(3) +
                        "\nKelas: " + res.getString(4);
                listNIM.add(nim);
                listData.add(data);
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
    }
}
