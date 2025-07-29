package com.examples.mahasiswasqlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    EditText editNIM, editNama, editJurusan, editKelas;
    RadioGroup radioGroupJK;
    RadioButton radioLaki, radioPerempuan;
    Button btnSimpan, btnLihatData;
    DBHelper db;

    boolean isEdit = false;
    String editNIMValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        // Hubungkan ke komponen layout
        editNIM = (EditText) findViewById(R.id.editNIM);
        editNama = (EditText) findViewById(R.id.editNama);
        editJurusan = (EditText) findViewById(R.id.editJurusan);
        editKelas = (EditText) findViewById(R.id.editKelas);
        radioGroupJK = (RadioGroup) findViewById(R.id.radioGroupJK);
        radioLaki = (RadioButton) findViewById(R.id.radioLaki);
        radioPerempuan = (RadioButton) findViewById(R.id.radioPerempuan);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnLihatData = (Button) findViewById(R.id.btnLihatData);

        // Mode Edit (jika data dikirim dari SecondActivity)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("mode") && intent.getStringExtra("mode").equals("edit")) {
            isEdit = true;
            editNIMValue = intent.getStringExtra("nim");

            editNIM.setText(editNIMValue);
            editNIM.setEnabled(false);
            editNama.setText(intent.getStringExtra("nama"));
            editJurusan.setText(intent.getStringExtra("jurusan"));
            editKelas.setText(intent.getStringExtra("kelas"));

            String jk = intent.getStringExtra("jk");
            if (jk.equals("Laki-laki")) {
                radioLaki.setChecked(true);
            } else if (jk.equals("Perempuan")) {
                radioPerempuan.setChecked(true);
            }

            btnSimpan.setText("Update");
        }

        // Tombol Simpan / Update
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nim = editNIM.getText().toString().trim();
                String nama = editNama.getText().toString().trim();
                String jurusan = editJurusan.getText().toString().trim();
                String kelas = editKelas.getText().toString().trim();

                String jk = "";
                int selectedId = radioGroupJK.getCheckedRadioButtonId();
                if (selectedId == R.id.radioLaki) {
                    jk = "Laki-laki";
                } else if (selectedId == R.id.radioPerempuan) {
                    jk = "Perempuan";
                }

                // Validasi input
                if (nim.isEmpty() || nama.isEmpty()) {
                    Toast.makeText(MainActivity.this, "NIM dan Nama wajib diisi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!nim.matches("\\d+")) {
                    Toast.makeText(MainActivity.this, "NIM hanya boleh berupa angka", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (jk.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Pilih Jenis Kelamin", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean result;
                if (isEdit) {
                    result = db.updateData(nim, nama, jk, jurusan, kelas);
                    if (result) {
                        Toast.makeText(MainActivity.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Gagal update data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    result = db.insertData(nim, nama, jk, jurusan, kelas);
                    if (result) {
                        Toast.makeText(MainActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        resetForm();
                    } else {
                        Toast.makeText(MainActivity.this, "Gagal menyimpan (NIM mungkin sudah ada)", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Tombol Lihat Data
        btnLihatData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    // Reset semua form
    private void resetForm() {
        editNIM.setText("");
        editNama.setText("");
        editJurusan.setText("");
        editKelas.setText("");
        radioGroupJK.clearCheck();
        editNIM.setEnabled(true);
        btnSimpan.setText("Simpan");
        isEdit = false;
    }
}
