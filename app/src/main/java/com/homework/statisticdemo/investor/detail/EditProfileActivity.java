package com.homework.statisticdemo.investor.detail;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.homework.statisticdemo.R;
import com.homework.statisticdemo.model.Date;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {
    public static final String ID_PASS = "IntentPass";
    String intentPass, id;

    private EditText name, companyID, phone, nrc, address, amount811, percent811, amount58, percent58, amount456, percent456,
            cashAmount, cashPercent, cashProfit, preProfit;
    private ImageView imageView1, imageView2, imageView3, exImgView1, exImgView2, exImgView3, downBtn1, downBtn2, downBtn3, clearBtn1, clearBtn2, clearBtn3;
    private Button date811, date58, date456, saveFirst, saveSecond, saveFinal, btnUpdate;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private Toolbar toolbar;
    private FirebaseFirestore db;
    private CollectionReference collRef;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatePickerDialog.OnDateSetListener listener1, listener2, listener3;

    private Uri imgUri1, imgUri2, imgUri3, imgUri4, imgUri5, imgUri6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        intentPass = intent.getStringExtra(ID_PASS);

        toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Updating the investor...");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        db = FirebaseFirestore.getInstance();
        collRef = db.collection("Investors");

        name = findViewById(R.id.edit_name);
        companyID = findViewById(R.id.edit_company_id);
        phone = findViewById(R.id.edit_phone);
        nrc = findViewById(R.id.edit_nrc);
        address = findViewById(R.id.edit_address);

        imageView1 = findViewById(R.id.edit_img_one);
        imageView2 = findViewById(R.id.edit_img_two);
        imageView3 = findViewById(R.id.edit_img_three);

        amount811 = findViewById(R.id.edit_plan_811_amount);
        percent811 = findViewById(R.id.edit_plan_811_percent);
        date811 = findViewById(R.id.edit_date_811);

        amount58 = findViewById(R.id.edit_plan_58_amount);
        percent58 = findViewById(R.id.edit_plan_58_percent);
        date58 = findViewById(R.id.edit_date_58);

        amount456 = findViewById(R.id.edit_plan_456_amount);
        percent456 = findViewById(R.id.edit_plan_456_percent);
        date456 = findViewById(R.id.edit_date_456);

        saveFirst = findViewById(R.id.save_date_first_plan);
        saveSecond = findViewById(R.id.save_date_second_plan);
        saveFinal = findViewById(R.id.save_date_final_plan);
        btnUpdate = findViewById(R.id.btn_update_investor);

        cashAmount = findViewById(R.id.edit_cash_amount);
        cashPercent = findViewById(R.id.edit_cash_percent);
        cashProfit = findViewById(R.id.edit_cash_profit);

        preProfit = findViewById(R.id.edit_pre_profit);

        progressBar = findViewById(R.id.edit_progress_bar);
        linearLayout = findViewById(R.id.edit_linear_layout);

        exImgView1 = findViewById(R.id.first_ex_img);
        exImgView2 = findViewById(R.id.second_ex_img);
        exImgView3 = findViewById(R.id.third_ex_img);

        downBtn1 = findViewById(R.id.download_one);
        downBtn2 = findViewById(R.id.download_two);
        downBtn3 = findViewById(R.id.download_three);

        clearBtn1 = findViewById(R.id.clear_one);
        clearBtn2 = findViewById(R.id.clear_two);
        clearBtn3 = findViewById(R.id.clear_three);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                UpdateData();
            }
        });

        saveFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFirstDate();
            }
        });

        saveSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSecondDate();
            }
        });

        saveFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFinalDate();
            }
        });

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

        exImgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser4();
            }
        });

        exImgView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser5();
            }
        });

        exImgView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser6();
            }
        });

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

        clearBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgUri1 = null;
                imageView1.setImageURI(null);
                imageView1.setBackgroundResource(R.color.solid_red);
            }
        });

        clearBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgUri2 = null;
                imageView2.setImageURI(null);
                imageView2.setBackgroundResource(R.color.light_blue);
            }
        });

        clearBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgUri3 = null;
                imageView3.setImageURI(null);
                imageView3.setBackgroundResource(R.color.solid_yellow);
            }
        });
    }

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

    private void OpenFileChooser4() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 4);
    }

    private void OpenFileChooser5() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 5);
    }

    private void OpenFileChooser6() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 6);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri1 = data.getData();
            imageView1.setBackgroundResource(android.R.color.transparent);
            imageView1.setImageURI(imgUri1);
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri2 = data.getData();
            imageView2.setBackgroundResource(android.R.color.transparent);
            imageView2.setImageURI(imgUri2);
        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri3 = data.getData();
            imageView3.setBackgroundResource(android.R.color.transparent);
            imageView3.setImageURI(imgUri3);
        }else if (requestCode == 4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri4 = data.getData();
            exImgView1.setBackgroundResource(android.R.color.transparent);
            Picasso.get().load(imgUri4).fit().into(exImgView1);
        }else if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri5 = data.getData();
            exImgView2.setBackgroundResource(android.R.color.transparent);
            Picasso.get().load(imgUri5).fit().into(exImgView2);
        }else if (requestCode == 6 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri6 = data.getData();
            exImgView3.setBackgroundResource(android.R.color.transparent);
            Picasso.get().load(imgUri6).fit().into(exImgView3);
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

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uri));
    };

    private void saveFirstDate() {
        String amount = amount811.getText().toString();
        String percent = percent811.getText().toString();
        String date = date811.getText().toString();
        String currentTime = String.valueOf(System.currentTimeMillis());
        String imageUrl = "";

        if (amount.isEmpty() | percent.isEmpty() | date.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Insufficient Data", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
                collRef.document(id).collection("First Date").add(new Date(amount, percent, date, currentTime, imageUrl))
                        .addOnSuccessListener(this, new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                if (imgUri4 != null) {
                                    StorageReference fileRef = storageReference.child(name.getText().toString() + "/Expired Date/First Date/"
                                            +currentTime+"." + getFileExtension(imgUri4));
                                    fileRef.putFile(imgUri4).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                           fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                               @Override
                                               public void onSuccess(Uri uri) {
                                                   collRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                       @Override
                                                       public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                           StorageReference deleRef = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imgUrlOne"));
                                                           deleRef.delete();
                                                           collRef.document(id).update("imgUrlOne","");
                                                           collRef.document(id).collection("First Date").document(documentReference.getId())
                                                                   .update("imageUrl",uri.toString());

                                                           progressBar.setVisibility(View.GONE);
                                                           Toast.makeText(EditProfileActivity.this, "Saved pre-date", Toast.LENGTH_SHORT).show();
                                                           saveFirst.setText("Saved");
                                                           saveFirst.setClickable(false);
                                                       }
                                                   });
                                               }
                                           });
                                        }
                                    });
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(EditProfileActivity.this, "Saved pre-date without image", Toast.LENGTH_SHORT).show();
                                    saveFirst.setText("Saved");
                                    saveFirst.setClickable(false);
                                }
                            }
                        });
        }
    }

    private void saveSecondDate() {
        String amount = amount58.getText().toString();
        String percent = percent58.getText().toString();
        String date = date58.getText().toString();
        String currentTime = String.valueOf(System.currentTimeMillis());
        String imageUrl = "";

        if (amount.isEmpty() | percent.isEmpty() | date.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Insufficient Data", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            collRef.document(id).collection("Second Date").add(new Date(amount, percent, date, currentTime, imageUrl))
                    .addOnSuccessListener(this, new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            if (imgUri5 != null) {
                                StorageReference fileRef = FirebaseStorage.getInstance().getReference(name.getText().toString())
                                        .child("Expired Date/Second Date/" + date + "." + getFileExtension(imgUri5));
                                fileRef.putFile(imgUri5).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                collRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        StorageReference deleRef = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imgUrlTwo"));
                                                        deleRef.delete();

                                                        collRef.document(id).update("imgUrlTwo", "");
                                                        collRef.document(id).collection("Second Date").document(documentReference.getId())
                                                                .update("imageUrl", uri.toString());

                                                        progressBar.setVisibility(View.GONE);
                                                        Toast.makeText(EditProfileActivity.this, "Saved pre-date", Toast.LENGTH_SHORT).show();
                                                        saveSecond.setText("Saved");
                                                        saveSecond.setClickable(false);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(EditProfileActivity.this, "Saved pre-date without image", Toast.LENGTH_SHORT).show();
                                saveSecond.setText("Saved");
                                saveSecond.setClickable(false);
                            }
                        }
                    });
        }
    }

    private void saveFinalDate() {
        String amount = amount456.getText().toString();
        String percent = percent456.getText().toString();
        String date = date456.getText().toString();
        String currentTime = String.valueOf(System.currentTimeMillis());
        String imageUrl = "";

        if (amount.isEmpty() | percent.isEmpty() | date.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Insufficient Data", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            collRef.document(id).collection("Final Date").add(new Date(amount, percent, date, currentTime, imageUrl))
                    .addOnSuccessListener(this, new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            if (imgUri6 != null){
                                StorageReference fileRef = storageReference.child(name.getText().toString()+"/Expired Date/Final Date/"
                                +System.currentTimeMillis()+"."+getFileExtension(imgUri6));
                                fileRef.putFile(imgUri6).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                collRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        StorageReference deleRef = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imgUrlThree"));
                                                        deleRef.delete();

                                                        collRef.document(id).update("imgUrlThree","");
                                                        collRef.document(id).collection("Final Date").document(documentReference.getId())
                                                                .update("imageUrl",uri.toString());

                                                        progressBar.setVisibility(View.GONE);
                                                        Toast.makeText(EditProfileActivity.this, "Saved pre-date", Toast.LENGTH_SHORT).show();
                                                        saveFinal.setText("Saved");
                                                        saveFinal.setClickable(false);
                                                    }
                                                });

                                            }
                                        });
                                    }
                                });
                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(EditProfileActivity.this, "Saved pre-date without image", Toast.LENGTH_SHORT).show();
                                saveFinal.setText("Saved");
                                saveFinal.setClickable(false);
                            }
                        }
                    });
        }

    }

    private void UpdateData() {
        collRef.document(id).update("name", name.getText().toString());
        collRef.document(id).update("companyID", companyID.getText().toString());
        collRef.document(id).update("phone", phone.getText().toString());
        collRef.document(id).update("nrc", nrc.getText().toString());
        collRef.document(id).update("address", address.getText().toString());
        collRef.document(id).update("preProfit", preProfit.getText().toString());

        if (amount811.getText().equals("") && percent811.getText().equals("") && date811.getText().equals("")) {
            collRef.document(id).update("amount811", "Unavaliable");
            collRef.document(id).update("percent811", "Unavaliable");
            collRef.document(id).update("date811", "Unavaliable");
        } else {
            collRef.document(id).update("amount811", amount811.getText().toString());
            collRef.document(id).update("percent811", percent811.getText().toString());
            collRef.document(id).update("date811", date811.getText().toString());
        }

        if (cashAmount.getText().toString().isEmpty() && cashPercent.getText().toString().isEmpty() && cashProfit.getText().toString().isEmpty()) {
            collRef.document(id).update("cashAmount", "");
            collRef.document(id).update("cashPercent", "No cash bonus");
            collRef.document(id).update("cashProfit", "");
        } else {
            collRef.document(id).update("cashAmount", cashAmount.getText().toString());
            collRef.document(id).update("cashPercent", cashPercent.getText().toString());
            collRef.document(id).update("cashProfit", cashProfit.getText().toString());
        }

        if (amount58.getText().toString().isEmpty() && percent58.getText().toString().isEmpty() && date58.getText().toString().isEmpty()) {
            collRef.document(id).update("amount58", "Unavaliable");
            collRef.document(id).update("percent58", "Unavaliable");
            collRef.document(id).update("date58", "Unavaliable");
        } else {
            collRef.document(id).update("amount58", amount58.getText().toString());
            collRef.document(id).update("percent58", percent58.getText().toString());
            collRef.document(id).update("date58", date58.getText().toString());
        }

        if (amount456.getText().toString().isEmpty() && percent456.getText().toString().isEmpty() && date456.getText().toString().isEmpty()) {
            collRef.document(id).update("amount456", "Unavaliable");
            collRef.document(id).update("percent456", "Unavaliable");
            collRef.document(id).update("date456", "Unavaliable");
        } else {
            collRef.document(id).update("amount456", amount456.getText().toString());
            collRef.document(id).update("percent456", percent456.getText().toString());
            collRef.document(id).update("date456", date456.getText().toString());
        }

        if (imgUri1 != null){
            StorageReference fileRef = storageReference.child(name.getText().toString()+"/Ongoing Date/"+"First Date."+getFileExtension(imgUri1));
            fileRef.putFile(imgUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            collRef.document(id).update("imgUrlOne",uri.toString());
                        }
                    });
                }
            });
        }

        if (imgUri2 != null){
            StorageReference fileRef = storageReference.child(name.getText().toString()+"/Ongoing Date/"+"Second Date."+getFileExtension(imgUri2));
            fileRef.putFile(imgUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            collRef.document(id).update("imgUrlTwo",uri.toString());
                        }
                    });
                }
            });
        }

        if (imgUri3 != null){
            StorageReference fileRef = storageReference.child(name.getText().toString()+"/Ongoing Date/"+"Final Date."+getFileExtension(imgUri3));
            fileRef.putFile(imgUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            collRef.document(id).update("imgUrlThree",uri.toString());
                        }
                    });
                }
            });
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EditProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, 5000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RetrieveData();
    }

    private void RetrieveData() {
        collRef.document(intentPass).get()
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);

                        id = documentSnapshot.getId();

                        name.setText(documentSnapshot.getString("name"));
                        companyID.setText(documentSnapshot.getString("companyID"));
                        phone.setText(documentSnapshot.getString("phone"));
                        nrc.setText(documentSnapshot.getString("nrc"));
                        address.setText(documentSnapshot.getString("address"));
                        preProfit.setText(documentSnapshot.getString("preProfit"));

                        if (documentSnapshot.getString("amount811").equals("Unavaliable") &&
                                documentSnapshot.getString("percent811").equals("Unavaliable") &&
                                documentSnapshot.getString("date811").equals("Unavaliable")) {
                            amount58.setHint("Unsettle");
                            percent58.setHint("%");
                            date58.setHint("No date");
                        } else {
                            amount811.setText(documentSnapshot.getString("amount811"));
                            percent811.setText(documentSnapshot.getString("percent811"));
                            date811.setText(documentSnapshot.getString("date811"));
                        }

                        if (documentSnapshot.getString("amount58").equals("Unavaliable") &&
                                documentSnapshot.getString("percent58").equals("Unavaliable") &&
                                documentSnapshot.getString("date58").equals("Unavaliable")) {
                            amount58.setHint("Unsettle");
                            percent58.setHint("%");
                            date58.setHint("No date");
                        } else {
                            amount58.setText(documentSnapshot.getString("amount58"));
                            percent58.setText(documentSnapshot.getString("percent58"));
                            date58.setText(documentSnapshot.getString("date58"));
                        }

                        if (documentSnapshot.getString("amount456").equals("Unavaliable") &&
                                documentSnapshot.getString("percent456").equals("Unavaliable") &&
                                documentSnapshot.getString("date456").equals("Unavaliable")) {
                            amount456.setHint("Unavaliable");
                            percent456.setHint("%");
                            date456.setHint("No date");
                        } else {
                            amount456.setText(documentSnapshot.getString("amount456"));
                            percent456.setText(documentSnapshot.getString("percent456"));
                            date456.setText(documentSnapshot.getString("date456"));
                        }

                        if (documentSnapshot.getString("cashAmount").equals("") &&
                                documentSnapshot.getString("cashPercent").equals("No cash bonus") &&
                                documentSnapshot.getString("cashProfit").equals("")) {
                            cashAmount.setHint("Unsettle");
                            cashPercent.setHint("%");
                            cashProfit.setHint("Unsettle");
                        } else {
                            cashAmount.setText(documentSnapshot.getString("cashAmount"));
                            cashPercent.setText(documentSnapshot.getString("cashPercent"));
                            cashProfit.setText(documentSnapshot.getString("cashProfit"));
                        }

                        if (!documentSnapshot.getString("imgUrlOne").equals("")) {
                            imageView1.setBackgroundResource(android.R.color.transparent);
                            Picasso.get().load(documentSnapshot.getString("imgUrlOne")).fit().into(imageView1);
                        }

                        if (!documentSnapshot.getString("imgUrlTwo").equals("")) {
                            imageView2.setBackgroundResource(android.R.color.transparent);
                            Picasso.get().load(documentSnapshot.getString("imgUrlTwo")).fit().into(imageView2);
                        }

                        if (!documentSnapshot.getString("imgUrlThree").equals("")) {
                            imageView3.setBackgroundResource(android.R.color.transparent);
                            Picasso.get().load(documentSnapshot.getString("imgUrlThree")).fit().into(imageView3);
                        }
                    }
                });
    }
}