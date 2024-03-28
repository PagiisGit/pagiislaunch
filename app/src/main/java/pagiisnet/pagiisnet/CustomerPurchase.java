package pagiisnet.pagiisnet;

import static pagiisnet.pagiisnet.R.*;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class CustomerPurchase extends AppCompatActivity
{
    private static final int PICK_IMAGE_REQUEST = 1;

    private String mImageUri;

    private Uri ImageUri;

    private EditText shopName;
    private EditText shopAdmin;
    private EditText shopCell;

    private androidx.appcompat.widget.Toolbar mToolbar;
    private EditText shopBankDetails;
    private EditText shopEmail;


    private TextView bankValue;
    private TextView eWalletValue;

    private StorageReference userImageStorageDpRef;
    private DatabaseReference getUserProfileDataRef;


    private TextView catergoryShop;
    private ImageView createShopButton;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef_x;
    private DatabaseReference mDatabaseRef_y;
    private DatabaseReference mDataRef;

    private ImageView shopLogo;

    private  String receiptProof;

    private ImageView shopItemPurchase;

    private ImageView shopBillImage;

    private ImageView previosoriginalImage;

    private ImageView shopBankImage;


    private ImageView addToCartImage;

    private ImageView starItemRater;

    private  String VisitedStoreItem;

    private FirebaseAuth mAuth;
    private ImageView Art;
    private ImageView Busimess;
    private ImageView Music;
    private ImageView Fashion;


    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private ProgressBar mProgressBar;
    private String itemKey;
    private String itemImageUrl;
    private String itemName;
    private String shopType;
    private  String StoreAdminKey;
    private String StoreKey;


    private TextView itemBoughtTextView;


    private  String storeInfoBank;
    private  String storeInfoEwallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_customer_purchase);

        mToolbar = findViewById(id.appBarLayout);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("@sir_occo");

        shopName = findViewById(id.storeName);
        shopAdmin = findViewById(id.admin);
        shopCell = findViewById(id.cell);
        shopBankDetails = findViewById(id.bankDetails);
        shopEmail = findViewById(id.email);


        catergoryShop =  findViewById(id.category);
        createShopButton = findViewById(id.purchase);

        Art = findViewById(id.small);
        Busimess = findViewById(id.Medium);
        Music = findViewById(id.Large);
        Fashion = findViewById(id.xLarge);
        bankValue = findViewById(id.bankValue);
        eWalletValue = findViewById(id.eWalletValue);


        bankValue.setVisibility(View.INVISIBLE);
        eWalletValue.setVisibility(View.INVISIBLE);

        itemBoughtTextView = findViewById(id.item_bought_des);


        mAuth = FirebaseAuth.getInstance();


        shopLogo = findViewById(id.storeLogo);

        shopBillImage = findViewById(id.billIcon);

        addToCartImage = findViewById(id.addToCart);
        starItemRater = findViewById(id.extraPurchaseInfor);



        shopItemPurchase = findViewById(id.originalView);

        previosoriginalImage = findViewById(id.originalPic);

        shopBankImage = findViewById(id.viewPaymentinfor);


        mProgressBar = findViewById(id.progress_circle_upload);

        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoOnSaleItem");

        mDatabaseRef_y = FirebaseDatabase.getInstance().getReference().child("SiroccoOnSaleItem");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoPurchasedItem");

        mDataRef = FirebaseDatabase.getInstance().getReference("Users");


        mProgressBar.setVisibility(View.INVISIBLE);



        addToCartImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.findViewById(id.addToCart);
                addItemToCart();

            }
        });


        starItemRater.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view)
            {
                view.findViewById(id.extraPurchaseInfor);
                Toast.makeText(CustomerPurchase.this, "**Sirocco Star rated.**", Toast.LENGTH_SHORT).show();
            }
        });


        shopBankImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                view.findViewById(id.viewPaymentinfor);
                bankValue.setVisibility(View.VISIBLE);
                eWalletValue.setVisibility(View.VISIBLE);


            }
        });

        previosoriginalImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.findViewById(id.billIcon);

                RequestOptions options = new RequestOptions();

                Glide.with(getApplicationContext()).load(itemImageUrl).apply(options.centerCrop()).thumbnail(0.65f).into(shopItemPurchase);

            }
        });



        shopBillImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.findViewById(id.billIcon);
                openFileChooser();

            }
        });


        Art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(id.catArt);

                shopType = "S";

                catergoryShop.setText("S");

            }
        });

        
        Busimess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(id.catArt);

                shopType = "M";

                catergoryShop.setText("M");

            }
        });


        Music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(id.catArt);

                shopType = "L";
                catergoryShop.setText("L");

            }
        });


        Fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(id.catArt);

                shopType = "XL";
                catergoryShop.setText("XL");

            }
        });


        createShopButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                view.findViewById(id.purchase);
                opneOnlineShop();
            }
        });

        itemImageUrl = getIntent().getExtras().get("imageUrlMax").toString();

        itemKey = getIntent().getExtras().get("imageKeyMAx").toString();

        getItemData();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void addItemToCart()

    {
        if (itemImageUrl != null && shopType != null )
        {
            Toast.makeText(CustomerPurchase.this, "PAGiiS image uploading ...", Toast.LENGTH_SHORT).show();

            final String raterBarValueDefault = "userDefaultDp";

            if(mImageUri == null)
            {
                mImageUri = "link..";
            }

            final String currentUserId = mAuth.getCurrentUser().getUid();

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoPurchasedItem");

            mStorageRef = FirebaseStorage.getInstance().getReference("SiroccoPurchaseProof").child(currentUserId);

            ImageUploads upload = new ImageUploads(itemName,itemImageUrl,shopType,StoreAdminKey,"","",mImageUri,"",currentUserId,StoreKey);
            mDatabaseRef.child(itemKey).setValue(upload, new DatabaseReference.CompletionListener()
            {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
                {
                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                    Toast.makeText(CustomerPurchase.this, "Item add-to-cart successful !!", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(CustomerPurchase.this, ViewMyCart.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    // String uniqueKey = databaseReference.getKey();
                    //Create the function for Clearing/The ImageView Widget.
                }
            });


        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void openFileChooser()
    {
        Intent intent =  new  Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK   && data != null && data.getData() != null)
        {
            ImageUri = data.getData();

            mImageUri = String.valueOf(ImageUri);

            RequestOptions options = new RequestOptions();

            String user_id = mAuth.getCurrentUser().getUid();

            Glide.with(getApplicationContext()).load(ImageUri).apply(options.centerCrop()).thumbnail(0.65f).into(shopBillImage);
            Glide.with(getApplicationContext()).load(ImageUri).apply(options.centerCrop()).thumbnail(0.65f).into(shopItemPurchase);


            if(mUploadTask != null && mUploadTask.isInProgress())
            {
                Toast.makeText(CustomerPurchase.this, "Attaching  receipt in progress.. !!", Toast.LENGTH_SHORT).show();
            }else
            {
                mProgressBar.setVisibility(View.VISIBLE);
                Upload();
            }

        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void getItemData()
    {
        itemImageUrl = getIntent().getExtras().get("imageUrlMax").toString();

        itemKey = getIntent().getExtras().get("imageKeyMAx").toString();

        mDatabaseRef_x = FirebaseDatabase.getInstance().getReference().child("SiroccoOnSaleItem").child(itemKey);

        mDatabaseRef_x.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                StoreAdminKey =dataSnapshot.child("userId").getValue().toString();
                itemImageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                itemName = dataSnapshot.child("name").getValue().toString();
                StoreKey = dataSnapshot.child("views").getValue().toString();

                itemBoughtTextView.setText(itemName);

                RequestOptions options = new RequestOptions();

                Glide.with(getApplicationContext()).load(itemImageUrl).apply(options.centerCrop()).thumbnail(0.65f).into(shopItemPurchase);

                setDataView();

                getStoreBankDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void getStoreBankDetails()
    {
        if(StoreKey != null)
        {
            mDataRef = FirebaseDatabase.getInstance().getReference().child("Users");
            mDataRef.child(StoreKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.exists())
                    {
                        String eWallet = dataSnapshot.child("storeCellNumber").getValue().toString();
                        String bank = dataSnapshot.child("storeBankDetails").getValue().toString();

                        eWalletValue.setText("eWallet:"+eWallet);
                        bankValue.setText(bank);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    private void setDataView()

    {
        mDataRef = FirebaseDatabase.getInstance().getReference("SiroccoOnlineShop");

        mDataRef.child(StoreKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    VisitedStoreItem = dataSnapshot.child("storeLogo").getValue().toString();

                    RequestOptions options = new RequestOptions();

                    //Glide.with(getApplicationContext()).load(itemImageUrl).apply(options.centerCrop()).thumbnail(0.65f).into(shopItemPurchase);
                    Glide.with(getApplicationContext()).load(VisitedStoreItem).apply(options.centerCrop()).thumbnail(0.65f).into(shopLogo);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }


    private void Upload()
    {

        if (ImageUri != null) {

            String onlineUserId = mAuth.getCurrentUser().getUid();

            getUserProfileDataRef = FirebaseDatabase.getInstance().getReference().child("sirReceipts").child(onlineUserId);

            userImageStorageDpRef = FirebaseStorage.getInstance().getReference().child("SirReceipts");

            //final String imageUrl = String.valueOf(resultUri);

            final StorageReference storageReference = userImageStorageDpRef.child(ImageUri.getLastPathSegment());

            mUploadTask = storageReference.putFile(ImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                Uri finalImageUri;

                                String finalImageUrl;


                                @Override
                                public void onSuccess(Uri uri)
                                {

                                    finalImageUri = uri;
                                    finalImageUrl = String.valueOf( uri);


                                    receiptProof = String.valueOf( uri);


                                    getUserProfileDataRef.child(itemKey).setValue(finalImageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(CustomerPurchase.this, "Upload successful", Toast.LENGTH_LONG).show();
                                            Glide.with(getApplicationContext()).load(ImageUri).into(shopBillImage);
                                            mProgressBar.setVisibility(View.INVISIBLE);
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
                            }, 500);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CustomerPurchase.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            mProgressBar.setVisibility(View.VISIBLE);

                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void opneOnlineShop()
    {
        if ( ImageUri != null && itemImageUrl != null && shopType != null && receiptProof != null  )
        {
            Toast.makeText(CustomerPurchase.this, "Purchase in process...", Toast.LENGTH_SHORT).show();

            final String raterBarValueDefault = "userDefaultDp";

            final String currentUserId = mAuth.getCurrentUser().getUid();

            mDatabaseRef = FirebaseDatabase.getInstance().getReference("SiroccoPurchasedItem");

            mStorageRef = FirebaseStorage.getInstance().getReference("SiroccoPurchaseProof").child(currentUserId);

            ImageUploads upload = new ImageUploads(itemName,itemImageUrl,shopType,StoreAdminKey,StoreKey,currentUserId,receiptProof,"",currentUserId,"");
            mDatabaseRef.child(itemKey).setValue(upload, new DatabaseReference.CompletionListener()
            {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
                {
                    //mProgressCircle.setVisibility(View.INVISIBLE);  This function is used to hide the progress Bar after its function is done
                    Toast.makeText(CustomerPurchase.this, "Item purchase-order successful !!"+"\n"+"generating receipt", Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(CustomerPurchase.this, ViewMyCart.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    // String uniqueKey = databaseReference.getKey();
                    //Create the function for Clearing/The ImageView Widget.
                }
            });
        }else if(mUploadTask != null)
        {
            Toast.makeText(this, "Purchase in progress..R.$.R", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Purchase in progress..R.$.R", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this, "Please ensure to attach proof of payment or add item to cart", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


    @Override
    protected void onStart()
    {
        super.onStart();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        finish();
    }
}
