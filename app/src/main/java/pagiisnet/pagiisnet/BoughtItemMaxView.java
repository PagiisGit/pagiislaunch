package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class BoughtItemMaxView extends AppCompatActivity

{
    private ImageView maxView,verifiedIWithStar,imageSwitcher,addSlipImage;
    private TextView modelName, brandName,verifiedText;


    private Button readyForDeliveryText;

    private LinearLayout deliveryInfoView;


    private String mImageUri;

    private ImageView editStatus;

    private Uri ImageUri;


    private FirebaseAuth mAuth;

    private ProgressBar mProgressBar;


    private ImageView handShake;

    private DatabaseReference mDataRef;

    private  String DeliveryLocation;

    private DatabaseReference mDataRef_x;
    private DatabaseReference mDataRef_y;

    private static final int PICK_IMAGE_REQUEST = 1;

    private androidx.appcompat.widget.Toolbar mToolbar;

    private DatabaseReference storeRef;
    private String ImageUrl;
    private String imageKey;
    private String imageExRater;
    private String StoreMainKey;
    private String StoreMainKeyUserId;

    private DatabaseReference getUserProfileDataRef;


    private Boolean isReceiptViewed = false;


    private String receiptUrlFinalValue ;

    private ImageView openShop;
    private ImageView openShopReceipt;
    private ImageView VeriFyPurchase;

    private ImageView verifyReceipt;
    private  TextView deliverLocation;


    private StorageTask mUploadTask;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_item_max_view);

        mAuth = FirebaseAuth.getInstance();

        final String user_id = mAuth.getCurrentUser().getUid();


        mToolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar);


        if(mAuth.getCurrentUser() == null)
        {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }




        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("@Sirocco_");

        deliveryInfoView = findViewById(R.id.deliveryInforView);


        maxView = findViewById(R.id.SirrocoMaxProfile);
        modelName = findViewById(R.id.profileNameTextModel);
        brandName = findViewById(R.id.profileEmailTextBrand);
        VeriFyPurchase = findViewById(R.id.profileProffessionTwo);
        imageSwitcher = findViewById(R.id.originalPic);
        verifyReceipt = findViewById(R.id.verifySlip);

        deliverLocation = findViewById(R.id.deliveryLocationTextview);

        verifyReceipt.setEnabled(false);
        verifyReceipt.setVisibility(View.INVISIBLE);
        deliveryInfoView.setEnabled(false);
        deliveryInfoView.setVisibility(View.INVISIBLE);
        modelName.setMovementMethod(LinkMovementMethod.getInstance());
        brandName.setMovementMethod(LinkMovementMethod.getInstance());
        mProgressBar = findViewById(R.id.SirrocoMaxProgressBar);

        openShop = findViewById(R.id.OnlineShop);
        openShopReceipt = findViewById(R.id.OnlineShopReceipt);
        imageSwitcher.setEnabled(false);
        imageSwitcher.setVisibility(View.INVISIBLE);

        addSlipImage = findViewById(R.id.ChooseSlip);

        openShop.setVisibility(View.INVISIBLE);

        getUserProfileDataRef = FirebaseDatabase.getInstance().getReference("Users");

        openShopReceipt.setVisibility(View.INVISIBLE);
        openShop.setEnabled(false);
        openShopReceipt.setEnabled(false);
        addSlipImage.setEnabled(false);
        addSlipImage.setVisibility(View.INVISIBLE);



        mProgressBar.setVisibility(View.VISIBLE);

        verifiedIWithStar = findViewById(R.id.sale);
        verifiedText = findViewById(R.id.siroccoMaxLikes);
        readyForDeliveryText = findViewById(R.id.readyForDelivery);

        handShake = findViewById(R.id.profileProffessionTwo);

        editStatus = findViewById(R.id.writeStatus);

        handShake.setEnabled(false);

        readyForDeliveryText.setVisibility(View.INVISIBLE);

        verifiedIWithStar.setVisibility(View.INVISIBLE);

        verifiedText.setText("Pending..");

        ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();

        imageKey = getIntent().getExtras().get("imageKeyMAx").toString();

        imageExRater = getIntent().getExtras().get("imageExRater").toString();

        StoreMainKey = getIntent().getExtras().get("imageStoreMainKey").toString();


        getSiroccoMaxView();

        checkIfItemVerified();

        //checkIfItemBought();

        checkifAdmindTrue();
        verifyLocation();


        editStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.writeStatus);

                openStatusEditText();
            }
        });



        verifyReceipt.setOnClickListener(new View.OnClickListener()
         {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.verifySlip);

                if(mImageUri != null)
                {
                    UploadReceit();
                }else
                {
                    Toast.makeText(BoughtItemMaxView.this, "Please always make sure to upload receipt for purchase. ", Toast.LENGTH_SHORT).show();
                    chooseReceipt();
                }

            }
        });

        addSlipImage.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.ChooseSlip);

                    chooseReceipt();

            }
        });




        openShop.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(BoughtItemMaxView.this, CustomerPurchase.class);
                intent.putExtra("imageKeyMAx", imageKey);
                intent.putExtra("imageUrlMax",ImageUrl);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });



        openShopReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.OnlineShopReceipt);

                RequestOptions options = new RequestOptions();

                if(receiptUrlFinalValue != null)

                {
                    isReceiptViewed = true;
                    verifyReceipt.setEnabled(true);
                    verifyReceipt.setVisibility(View.VISIBLE);
                    addSlipImage.setEnabled(true);
                    addSlipImage.setVisibility(View.VISIBLE);
                    Glide.with(BoughtItemMaxView.this).load(receiptUrlFinalValue).apply(options.centerCrop()).thumbnail(0.75f).into(maxView);
                }

            }
        });

        imageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                view.findViewById(R.id.billIcon);

                RequestOptions options = new RequestOptions();

                if(receiptUrlFinalValue != null)
                {
                    Glide.with(BoughtItemMaxView.this).load(ImageUrl).apply(options.centerCrop()).thumbnail(0.75f).into(maxView);
                }

            }
        });


        handShake.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                final String current_userId = mAuth.getCurrentUser().getUid();
                mDataRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoPurchasedItem").child(current_userId).child(imageKey);

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(BoughtItemMaxView.this);
                View mView = getLayoutInflater().inflate(R.layout.thank_you_sir,null);


                final ImageView exit = mView.findViewById(R.id.homeButotn);
                final ImageView saveStatus = mView.findViewById(R.id.verifyPurchase);


                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        view.findViewById(R.id.homeButotn);
                        Intent intent = new Intent(getApplicationContext(), BoughtItemMaxView.class);
                        startActivity(intent);

                    }
                });

                saveStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        view.findViewById(R.id.verifyPurchase);

                        mDataRef_x.child("name").setValue("sold").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @SuppressLint("SuspiciousIndentation")
                            @Override
                            public void onSuccess(Void aVoid)
                            {

                                mDataRef_y = FirebaseDatabase.getInstance().getReference().child("SiroccoPurchasesCount").child(StoreMainKey);

                                    mDataRef_y.push().setValue(imageKey).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)

                                        {
                                            deliveryInfoView.setEnabled(true);

                                            deliveryInfoView.setVisibility(View.VISIBLE);
                                        }
                                    });

                            }
                        });
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });
    }

    private void verifyLocation()

    {

        String current_userId = mAuth.getCurrentUser().getUid();


        mDataRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoPurchasedItem").child(current_userId).child(imageKey);

        mDataRef_x.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists() && dataSnapshot.child("postLocation").exists())
                {
                    DeliveryLocation =  dataSnapshot.child("postLocation").getValue().toString();

                    deliverLocation.setText(DeliveryLocation);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void UploadReceit()

    {
        if(receiptUrlFinalValue != null)

        {
            mProgressBar.setVisibility(View.VISIBLE);

            final String currentUserId = mAuth.getCurrentUser().getUid();

            mDataRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoPurchasedItem").child(currentUserId).child(imageKey);

            mStorageRef = FirebaseStorage.getInstance().getReference("PurchaseReceipts").child(currentUserId).child(imageKey);

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(ImageUri));


            if( mUploadTask != null && !mUploadTask.isInProgress())
            {
                Toast.makeText(BoughtItemMaxView.this, "Receipt upload in progress.", Toast.LENGTH_SHORT).show();
            }else
            {
                mUploadTask = fileReference.putFile(ImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()

                                {
                                    Uri finalImageUri;

                                    String finalImageUrl;

                                    @Override
                                    public void onSuccess(Uri uri)
                                    {
                                        finalImageUri = uri;
                                        mImageUri = String.valueOf(uri);

                                        finalImageUrl= String.valueOf(uri);

                                        mDataRef_x = FirebaseDatabase.getInstance().getReference("SiroccoPurchasedItem").child(currentUserId);

                                        mStorageRef= FirebaseStorage.getInstance().getReference("SiroccoPurchaseProof").child(currentUserId);

                                        mDataRef_x.child(imageKey).child("share").setValue(mImageUri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {

                                                mProgressBar.setVisibility(View.INVISIBLE);

                                                Toast.makeText(BoughtItemMaxView.this, "Receipt upload successfull", Toast.LENGTH_SHORT).show();

                                            }
                                        });


                                    }

                                });

                                Handler handler = new Handler();

                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(0);
                                    }
                                }, 1000);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Get the custom layout view.


                                View toastView = getLayoutInflater().inflate(R.layout.activity_toast_custom_view, null);

                                mProgressBar.setVisibility(View.INVISIBLE);

                                TextView messageGrid = findViewById(R.id.customToastText);
                                // Initiate the Toast instance.
                                Toast toast = new Toast(getApplicationContext());

                                messageGrid.setText("Pagiis failed to upload, please check Internet connections.");
                                // Set custom view in toast.
                                toast.setView(toastView);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0,0);
                                toast.show();
                                finish();

                                //Toast.makeText(ActivityUploadImage.this, "", Toast.LENGTH_LONG).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                mProgressBar.setProgress((int) progress);
                                mProgressBar.setVisibility(View.VISIBLE);
                            }
                        });
            }

        }



    }

    private void chooseReceipt()

    {
        Intent intent =  new  Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK   && data != null && data.getData() != null)
        {
            ImageUri = data.getData();

            mImageUri = String.valueOf(ImageUri);

            String user_id = mAuth.getCurrentUser().getUid();


            mDataRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoPurchasedItem").child(user_id).child(imageKey);


            RequestOptions options = new RequestOptions();

            openShop.setVisibility(View.INVISIBLE);
            openShopReceipt.setVisibility(View.VISIBLE);
            openShop.setEnabled(false);
            openShopReceipt.setEnabled(true);
            imageSwitcher.setVisibility(View.VISIBLE);
            imageSwitcher.setEnabled(true);
            verifyReceipt.setEnabled(true);
            verifyReceipt.setVisibility(View.VISIBLE);

            isReceiptViewed =false;

            Glide.with(getApplicationContext()).load(mImageUri).apply(options.centerCrop()).thumbnail(0.65f).into(openShopReceipt);
            Glide.with(getApplicationContext()).load(mImageUri).apply(options.centerCrop()).thumbnail(0.65f).into(maxView);

        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void openStatusEditText()
    {

        final String current_userId = mAuth.getCurrentUser().getUid();

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(BoughtItemMaxView.this);

        View mView = getLayoutInflater().inflate(R.layout.alert_status_edit,null);

        mDataRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoPurchasedItem").child(current_userId).child(imageKey);

        final EditText newStatus = mView.findViewById(R.id.statusEdit);
        final ImageView saveStatus = mView.findViewById(R.id.saveStatus);


        saveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.findViewById(R.id.saveStatus);
                if(!(newStatus.getText() == null))
                {
                    mDataRef_x.child("postLocation").setValue(newStatus.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            AlertDialog dialog = mBuilder.create();
                            dialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(),BoughtItemMaxView.class);
                            startActivity(intent);
                        }
                    });


                }else
                {
                    Toast.makeText(BoughtItemMaxView.this, "Please type in your status or discard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void checkifAdmindTrue()
    {

        final String userId = mAuth.getCurrentUser().getUid();
        mDataRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoPurchasedItem").child(userId).child(imageKey);


        mDataRef_x.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.hasChild("userId") && dataSnapshot.child("userId").getValue().toString().compareTo(userId)==0)
                {
                   handShake.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

                Toast.makeText(BoughtItemMaxView.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /*private void checkIfItemBought()

    {

        final String user_id = mAuth.getCurrentUser().getUid();
        mDataRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoPurchasedItem").child(user_id).child(imageKey);

        mDataRef_x.addValueEventListener(new ValueEventListener()

        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if(ds.getKey().compareTo(imageKey) == 0 && ds.hasChild("likes") & ds.child("likes").getValue().toString().compareTo(user_id)==0)
                        {
                            handShake.setEnabled(true);

                        }

                    }

                }else
                {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BoughtItemMaxView.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void checkIfItemVerified()
    {

        if(imageKey != null)
        {
            String user_id = mAuth.getCurrentUser().getUid();
            final String dummie = "sold";

            mDataRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoPurchasedItem").child(user_id);


            mDataRef_x.child(imageKey).addValueEventListener(new ValueEventListener()
            {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.exists())
                    {

                        if(dataSnapshot.child("name").exists())
                        {

                            String dummieTwo = dataSnapshot.child("name").getValue().toString();

                            StoreMainKeyUserId = dataSnapshot.child("postName").getValue().toString();

                            String user_id = mAuth.getCurrentUser().getUid();

                            if(dummie.compareTo(dummieTwo) ==0 )
                            {
                                readyForDeliveryText.setVisibility(View.VISIBLE);
                                verifiedIWithStar.setVisibility(View.VISIBLE);
                                verifiedText.setText("Verified.");

                                deliveryInfoView.setEnabled(true);

                                deliveryInfoView.setVisibility(View.VISIBLE);

                            }else
                            {
                                RequestOptions options = new RequestOptions();
                                handShake.setImageResource(R.drawable.star_rate);
                                verifiedText.setText("rated");

                                openShopReceipt.setVisibility(View.VISIBLE);
                                openShopReceipt.setEnabled(true);

                                String ReceiptUrl = dataSnapshot.child("share").getValue().toString();

                                receiptUrlFinalValue = ReceiptUrl;

                                if(ReceiptUrl.compareTo("true")!=0 && receiptUrlFinalValue != null)

                                {
                                    Glide.with(BoughtItemMaxView.this).load(receiptUrlFinalValue).apply(options.centerCrop()).thumbnail(0.75f).into(openShopReceipt);
                                    openShop.setVisibility(View.INVISIBLE);
                                    openShopReceipt.setVisibility(View.VISIBLE);
                                    openShop.setEnabled(false);
                                    openShopReceipt.setEnabled(true);
                                    imageSwitcher.setVisibility(View.VISIBLE);
                                    imageSwitcher.setEnabled(true);
                                }

                            }
                        }


                    }else
                    {
                        handShake.setImageResource(R.drawable.star_rate);
                        verifiedText.setText("rated");
                        openShop.setVisibility(View.VISIBLE);
                        openShop.setEnabled(true);
                        Toast.makeText(BoughtItemMaxView.this, "Sirocco Online shopping is now open ***", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else
        {
            Toast.makeText(this, "item not found.", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent(BoughtItemMaxView.this,SiroccoPage.class);
            startActivity(returnIntent);
        }


    }


    private void getSiroccoMaxView()

    {
        if(!imageKey.isEmpty() && !ImageUrl.isEmpty())
        {

            ImageUrl = getIntent().getExtras().get("imageUrlMax").toString();

            imageKey = getIntent().getExtras().get("imageKeyMAx").toString();

            imageExRater = getIntent().getExtras().get("imageExRater").toString();

            mDataRef = FirebaseDatabase.getInstance().getReference().child("SiroccoOnSaleItem").child(imageKey);


            mDataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                                RequestOptions options = new RequestOptions();

                                Glide.with(BoughtItemMaxView.this).load(ImageUrl).apply(options.centerCrop()).thumbnail(0.75f).into(maxView);
                                mProgressBar.setVisibility(View.INVISIBLE);

                                String BrandName = dataSnapshot.child("name").getValue().toString();

                                String receiptUrl = dataSnapshot.child("share").getValue().toString();

                                receiptUrlFinalValue = receiptUrl;

                                if(receiptUrlFinalValue != null)
                                {
                                    Glide.with(BoughtItemMaxView.this).load(receiptUrl).apply(options.centerCrop()).thumbnail(0.75f).into(openShopReceipt);
                                }

                                String ModelName =dataSnapshot.child("likes").getValue().toString();

                                brandName.setText(BrandName);
                                modelName.setText(ModelName);
                                openShopReceipt.setEnabled(true);
                                openShopReceipt.setVisibility(View.VISIBLE);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)

                {
                }
            });
        }else
        {
            Intent intent = new Intent(BoughtItemMaxView.this, SiroccoPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
