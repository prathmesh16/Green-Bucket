package com.projects.greenbucketui.PaymentModule;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.projects.greenbucketui.API.Api_Constants;
import com.projects.greenbucketui.AddressActivity;
import com.projects.greenbucketui.CartItem;
import com.projects.greenbucketui.DBHelper;
import com.projects.greenbucketui.MainActivity;
import com.projects.greenbucketui.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PaymentScreen extends AppCompatActivity {

    RadioGroup radioGroup,radioGroup1,radioGroup2,radioGroup3;
    RadioButton rb1,rb2,rb3,rb4,rb5,rb6;
    ImageView backpage;

    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    final int UPI_PAYMENT=0;
    AppAdapter adapter=null;
    LinearLayout cardsAndNetBanking;
    private Toolbar toolbar;
    TextView totalAmount;

    TextView cards,upi;
    RelativeLayout cardsLayout;
    ListView upiMethods;
    String MID="",MKEY="",UPI_ID="",ordID="";

    DBHelper mydb;
    ArrayList<CartItem> data;

    private SharedPreferences adr_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);

        //Change the colour of status bar/ notification bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.green));

        cardsAndNetBanking=findViewById(R.id.cardNetBanking);
        toolbar=findViewById(R.id.my_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        totalAmount=findViewById(R.id.totalAmount);
        Intent i=this.getIntent();
        final int amount=i.getIntExtra("Amount",10);
        totalAmount.setText("Item total ₹"+amount);
        cards=findViewById(R.id.cards);
        cardsLayout=findViewById(R.id.cardLayout);
        upi=findViewById(R.id.upi);
        upiMethods=findViewById(R.id.PaymentAppsList);
        ordID=getIntent().getStringExtra("ORDERID");
        MID="lpsvEw59219385907682";//Merchant ID
        MKEY="S@IoR&bs#nOCsY2G";//Merchant key
        mydb = new DBHelper(this);
        data=mydb.getAllData();
        adr_save=getSharedPreferences("profile",MODE_PRIVATE);


        String adr = adr_save.getString("adr","");
        String sco = adr_save.getString("society","");
        String city = adr_save.getString("city","");

        cardsAndNetBanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PaymentScreen.this, checksum.class);
                intent.putExtra("name","Green Bucket");
                intent.putExtra("seller","Green Bucket");
                intent.putExtra("image","abc");
                intent.putExtra("catagory","cat");
                intent.putExtra("MID",MID);
                intent.putExtra("MKEY",MKEY);
                intent.putExtra("ORDERID",ordID);
                intent.putExtra("price",amount);
                startActivity(intent);
            }
        });

         UPI_ID="prajadhav1243@okaxis";//UPI ID
        final ListView lv=findViewById(R.id.PaymentAppsList);
        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", UPI_ID)
                        .appendQueryParameter("pn", "Green Bucket")
                        .appendQueryParameter("tn", "Green Bucket Payment")
                        .appendQueryParameter("am", ""+amount)
                        .appendQueryParameter("cu", "INR")
                        .build();
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        Intent chooser = Intent.createChooser(intent,"Pay With");
        PackageManager pm=getPackageManager();
        List<ResolveInfo> launchables=pm.queryIntentActivities(intent, 0);
        Collections.sort(launchables,new ResolveInfo.DisplayNameComparator(pm));
        adapter=new AppAdapter(pm, launchables);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                ResolveInfo launchable=adapter.getItem(position);
                ActivityInfo activity=launchable.activityInfo;
                ComponentName name=new ComponentName(activity.applicationInfo.packageName,
                        activity.name);
                intent.setPackage(activity.applicationInfo.packageName);
                startActivityForResult(intent,UPI_PAYMENT);
            }

        });
    }
    public String genOrderId() {
        Random r = new Random(System.currentTimeMillis());
        return "ORDER" + (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(PaymentScreen.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                submitRequest(ordID);
                Toast.makeText(PaymentScreen.this, "Transaction successful. Order Placed!", Toast.LENGTH_SHORT).show();
//                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String date = df.format(Calendar.getInstance().getTime());
//                Constants.order.setTIME(date);
//                //order placement
//                db.collection("customer/"+Constants.order.getTO()+"/my-orders").document(ordID).set(Constants.order);
//                db.collection("vendors/"+Constants.order.getFROM()+"/orders").document(ordID).set(Constants.order);
//                HashMap<String,String> loc = new HashMap();
//                loc.put("lat",Constants.order.getBUSI_LOC().get(0));
//                loc.put("long",Constants.order.getBUSI_LOC().get(1));
//                db.collection("customer/"+Constants.order.getTO()+"/my-orders/"+ordID+"/order-loc").document("loc").set(loc);
//                db.collection("vendors/"+Constants.order.getFROM()+"/orders/"+ordID+"/order-loc").document("loc").set(loc);
//                HashMap<String ,String > stages = new HashMap<>();
//                stages.put("status","IA");
//                db.collection("customer/"+Constants.order.getTO()+"/my-orders/"+ordID+"/order-loc").document("stage1").set(stages);
//                db.collection("vendors/"+Constants.order.getFROM()+"/orders/"+ordID+"/order-loc").document("stage1").set(stages);
//                db.collection("customer/"+Constants.order.getTO()+"/my-orders/"+ordID+"/order-loc").document("stage2").set(stages);
//                db.collection("vendors/"+Constants.order.getFROM()+"/orders/"+ordID+"/order-loc").document("stage2").set(stages);
//                db.collection("customer/"+Constants.order.getTO()+"/my-orders/"+ordID+"/order-loc").document("stage3").set(stages);
//                db.collection("vendors/"+Constants.order.getFROM()+"/orders/"+ordID+"/order-loc").document("stage3").set(stages);
//                db.collection("customer/"+Constants.order.getTO()+"/cart")
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        Details item = new Details();
//                                        item.setITEM_CAT(document.getString("type"));
//                                        item.setITEM_COST(document.getLong("price"));
//                                        item.setITEM_NAME(document.getString("name"));
//                                        item.setN(document.getLong("quantity"));
//                                        item.setSUB_TOT(document.getLong("price")*document.getLong("quantity"));
//                                        item.setITEM_ID(document.getId());
//                                        db.collection("customer/"+Constants.order.getTO()+"/my-orders/"+ordID+"/details").document(document.getId()).set(item);
//                                        db.collection("vendors/"+Constants.order.getFROM()+"/orders/"+ordID+"/details").document(document.getId()).set(item);
//                                        db.collection("customer/"+Constants.order.getTO()+"/cart").document(document.getId()).delete();
//                                    }
//                                   Constants.order=new SingleEntityOfOrders();
//                                } else {
//
//                                }
//                            }
//                        });


                Intent i = new Intent(PaymentScreen.this, MainActivity.class);
                startActivity(i);
                Log.e("UPI", "payment successfull: "+approvalRefNo);

            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PaymentScreen.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                Toast.makeText(PaymentScreen.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(PaymentScreen.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isConnectionAvailable(PaymentScreen context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
    class AppAdapter extends ArrayAdapter<ResolveInfo> {
        private PackageManager pm=null;

        AppAdapter(PackageManager pm, List<ResolveInfo> apps) {
            super(PaymentScreen.this, R.layout.row, apps);
            this.pm=pm;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            if (convertView==null) {
                convertView=newView(parent);
            }

            bindView(position, convertView);

            return(convertView);
        }

        private View newView(ViewGroup parent) {
            return(getLayoutInflater().inflate(R.layout.row, parent, false));
        }

        private void bindView(int position, View row) {
            TextView label=(TextView)row.findViewById(R.id.label);

            label.setText(getItem(position).loadLabel(pm));

            ImageView icon=(ImageView)row.findViewById(R.id.icon);

            icon.setImageDrawable(getItem(position).loadIcon(pm));
        }
    }
    private void submitRequest(String orderid) {


        ArrayList<CartItem> data = mydb.getAllData();
        for(CartItem a:data)
        {
            Log.e("qua",a.getQuantity()+"");
            Log.e("nom",a.getName()+"");

            JSONObject request = new JSONObject();
            try {

                request.put("MethodName", "orderlist");
                request.put("memberid", adr_save.getString("uid", ""));  //
                request.put("orderid", orderid);
                request.put("product_id", a.getId() );
                request.put("p_name", a.getName());
                request.put("qty", a.getQuantity());
                request.put("rate", a.getPrice());
                request.put("amount", a.getPrice());
                request.put("pdelete", "0");

                mydb.deleteData(a.getId());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking.post(Api_Constants.mainApi)
                    .addUrlEncodeFormBodyParameter("Request",request.toString())
                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("res",response.toString());

                            //TODO : fetch api data
                            try {
                                JSONArray data = response.getJSONArray("data");
                                //Log.e("data",data.toString());
                                //JSONObject  WHA1=data.getJSONObject(0);

                                JSONObject msg = data.getJSONObject(0);
                                Log.e("data msg",msg.getString("Column1"));

                                if(msg.getString("Column1").equalsIgnoreCase("Record Save Successfully"))
                                {
                                    Toast.makeText(PaymentScreen.this,"Record Save Successfully",Toast.LENGTH_LONG).show();

                                    /// Clear Cart Data Here



                                }
                                else
                                {
                                    Toast.makeText(PaymentScreen.this,"Something went wrong",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                            Log.e("ERROR", anError.getMessage());
                        }
                    });
        } // for loop end here



    }

}

