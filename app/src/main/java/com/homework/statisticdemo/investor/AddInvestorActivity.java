package com.homework.statisticdemo.investor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.homework.statisticdemo.R;
import com.homework.statisticdemo.model.Investor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddInvestorActivity extends AppCompatActivity {
    private EditText name, companyID, phone, nrc, address, amount811, percent811, amount58, percent58, amount456, percent456;
    private Button date811, date58, date456, btnAdd, btnOne, btnTwo, btnThree;
    private String cashAmount, cashPercent, cashProfit;
    private ImageView imageView1, imageView2, imageView3;
    private Uri imageUri1, imageUri2, imageUri3;
    private Toolbar toolbar;
    private DatePickerDialog.OnDateSetListener listener1, listener2, listener3;
    private CollectionReference collRef;
    private ProgressBar progressBar;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private String strImgOne = "";
    private String strImgTwo = "";
    private String strImgThree = "";
    private String preProfit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_investor);

        toolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Adding new investor...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collRef = FirebaseFirestore.getInstance().collection("Investors");
        progressBar = findViewById(R.id.add_progress_bar);

        cashAmount = "";
        cashPercent = "";
        cashProfit = "";

        name = findViewById(R.id.name);
        companyID = findViewById(R.id.company_id);
        phone = findViewById(R.id.phone);
        nrc = findViewById(R.id.nrc);
        address = findViewById(R.id.address);

        amount811 = findViewById(R.id.plan_811_amount);
        percent811 = findViewById(R.id.plan_811_percent);
        date811 = findViewById(R.id.date_811);

        amount58 = findViewById(R.id.plan_58_amount);
        percent58 = findViewById(R.id.plan_58_percent);
        date58 = findViewById(R.id.date_58);

        amount456 = findViewById(R.id.plan_456_amount);
        percent456 = findViewById(R.id.plan_456_percent);
        date456 = findViewById(R.id.date_456);

        imageView1 = findViewById(R.id.img_one);
        imageView2 = findViewById(R.id.img_two);
        imageView3 = findViewById(R.id.img_three);

        btnAdd = findViewById(R.id.btn_add_investor);

        date811.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date811Pick();
            }
        });
        date58.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date58Pick();
            }
        });
        date456.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date456Pick();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                AddInvestor();
            }
        });

        btnOne = findViewById(R.id.btn_one);
        btnTwo = findViewById(R.id.btn_two);
        btnThree = findViewById(R.id.btn_three);


        listener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String s = day + "/" + month + "/" + year;
                date811.setText(s);
            }
        };

        listener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String s = day + "/" + month + "/" + year;
                date58.setText(s);
            }
        };

        listener3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String s = day + "/" + month + "/" + year;
                date456.setText(s);
            }
        };

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser1();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser2();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser3();
            }
        });

        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setBackgroundResource(R.color.solid_red);
                imageView1.setImageURI(null);
                imageUri1 = null;
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView2.setBackgroundResource(R.color.light_blue);
                imageView2.setImageURI(null);
                imageUri2 = null;
            }
        });

        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView3.setBackgroundResource(R.color.solid_yellow);
                imageView3.setImageURI(null);
                imageUri3 = null;
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uri));
    };

    private void OpenFileChooser1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void OpenFileChooser2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
    }

    private void OpenFileChooser3() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageView1.setBackgroundResource(android.R.color.transparent);
            imageUri1 = data.getData();
            imageView1.setImageURI(imageUri1);
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageView2.setBackgroundResource(android.R.color.transparent);
            imageUri2 = data.getData();
            imageView2.setImageURI(imageUri2);
        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageView3.setBackgroundResource(android.R.color.transparent);
            imageUri3 = data.getData();
            imageView3.setImageURI(imageUri3);
        }
    }

    private void Date456Pick() {
        Calendar c = Calendar.getInstance();
        int dp1Year = c.get(Calendar.YEAR);
        int dp1Month = c.get(Calendar.MONTH);
        int dp1Day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener3, dp1Year, dp1Month, dp1Day);
        datePickerDialog.show();
    }

    private void Date58Pick() {
        Calendar c = Calendar.getInstance();
        int dp1Year = c.get(Calendar.YEAR);
        int dp1Month = c.get(Calendar.MONTH);
        int dp1Day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener2, dp1Year, dp1Month, dp1Day);
        datePickerDialog.show();
    }

    private void Date811Pick() {
        Calendar c = Calendar.getInstance();
        int dp1Year = c.get(Calendar.YEAR);
        int dp1Month = c.get(Calendar.MONTH);
        int dp1Day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener1, dp1Year, dp1Month, dp1Day);
        datePickerDialog.show();
    }

    private void AddInvestor() {
        String strName = name.getText().toString();
        String strCompanyID = companyID.getText().toString();
        String strPhone = phone.getText().toString();
        String strNRC = nrc.getText().toString();
        String strAddress = address.getText().toString();

        String str811amount = amount811.getText().toString();
        String str811percent = percent811.getText().toString();
        String str811date = date811.getText().toString();

        String str58amount = amount58.getText().toString();
        String str58percent = percent58.getText().toString();
        String str58date = date58.getText().toString();

        String str456amount = amount456.getText().toString();
        String str456percent = percent456.getText().toString();
        String str456date = date456.getText().toString();

        if (strName.isEmpty() | strCompanyID.isEmpty() | strPhone.isEmpty() | strNRC.isEmpty() | strAddress.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Warning!!!")
                    .setMessage("Insert all fields")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        } else {
            collRef.add(new Investor(strName, strCompanyID, strPhone, strNRC, strAddress,
                    str811amount, str811percent, str811date,
                    str58amount, str58percent, str58date,
                    str456amount, str456percent, str456date,
                    cashAmount, cashPercent, cashProfit,
                    strImgOne, strImgTwo, strImgThree, preProfit))
                    .addOnSuccessListener(this, new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            if (imageUri1 != null) {
                                StorageReference fileRef = storageReference.child(strName + "/Ongoing Contract/" + "First Contract"
                                        + "." + getFileExtension(imageUri1));
                                fileRef.putFile(imageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                collRef.document(documentReference.getId()).update("imgUrlOne", uri.toString());
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(AddInvestorActivity.this, "Investor added successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }

                            if (imageUri2 != null) {
                                StorageReference fileRef = storageReference.child(strName + "/Ongoing Contract/" + "Second Contract"
                                        + "." + getFileExtension(imageUri2));
                                fileRef.putFile(imageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                collRef.document(documentReference.getId()).update("imgUrlTwo", uri.toString());
                                                progressBar.setVisibility(View.GONE);
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }

                            if (imageUri3 != null) {
                                StorageReference fileRef = storageReference.child(strName + "/Ongoing Contract/" + "Final Contract"
                                        + "." + getFileExtension(imageUri3));
                                fileRef.putFile(imageUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                collRef.document(documentReference.getId()).update("imgUrlThree", uri.toString());
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(AddInvestorActivity.this, "Investor added successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }

                            if (imageUri1 == null && imageUri2 == null && imageUri3 == null) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AddInvestorActivity.this, "Investor added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                    });
        }
    }
}