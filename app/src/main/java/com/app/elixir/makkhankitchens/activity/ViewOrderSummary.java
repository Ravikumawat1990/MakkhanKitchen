package com.app.elixir.makkhankitchens.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.elixir.makkhankitchens.Model.CouponCodeModel;
import com.app.elixir.makkhankitchens.Model.CouponCodeModelArray;
import com.app.elixir.makkhankitchens.Model.OrderSummeryModel;
import com.app.elixir.makkhankitchens.Model.OrderSummeryModelArray;
import com.app.elixir.makkhankitchens.Model.OrderSummeryModelArraysSub;
import com.app.elixir.makkhankitchens.Model.TimeModel;
import com.app.elixir.makkhankitchens.R;
import com.app.elixir.makkhankitchens.adapter.CustomSpinnerAdapter;
import com.app.elixir.makkhankitchens.adapter.ListViewAdapter;
import com.app.elixir.makkhankitchens.adapter.adptordersummery;
import com.app.elixir.makkhankitchens.interfac.OnItemClickListenerOrderSuumnery;
import com.app.elixir.makkhankitchens.mtplview.MtplButton;
import com.app.elixir.makkhankitchens.mtplview.MtplLog;
import com.app.elixir.makkhankitchens.mtplview.MtplTextView;
import com.app.elixir.makkhankitchens.pojo.OrderSummeryPojo;
import com.app.elixir.makkhankitchens.pojo.OrderSummeryPojoArray;
import com.app.elixir.makkhankitchens.pojo.OrderSummeryPojoArraySub;
import com.app.elixir.makkhankitchens.pojo.OrderSummeryPojoArraySubSub;
import com.app.elixir.makkhankitchens.utils.CM;
import com.app.elixir.makkhankitchens.volly.OnVolleyHandler;
import com.app.elixir.makkhankitchens.volly.VolleyIntialization;
import com.app.elixir.makkhankitchens.volly.WebService;
import com.app.elixir.makkhankitchens.volly.WebServiceTag;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class ViewOrderSummary extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, DatePickerDialog.OnDateSetListener, Spinner.OnItemSelectedListener {
    private static final String TAG = "ViewOrderSummary";


    private RecyclerView recycleView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private adptordersummery mAdapter;
    private MtplTextView textViewTotalItems;
    private MtplTextView textViewItemTotal;
    private MtplTextView textViewSubTotal;
    private MtplTextView textViewVat;
    private MtplTextView textViewDeliveryType;
    private MtplTextView textViewtotalPayable;
    private MtplTextView textViewApplycoupon;
    private LinearLayout linearLayoutCouponApply;
    private ImageButton imageButtonRemove;
    private Button btnSubmit;
    private ArrayList<OrderSummeryModelArraysSub> orderSummeryModelArrays;
    public Spinner spinner;
    private Intent intent;
    private ArrayList<String> times;
    private CustomSpinnerAdapter mySpinnerArrayAdapter;
    private AppCompatEditText spinnerDate;
    private String serverReturnTime;
    private ArrayList<OrderSummeryPojoArray> orderSummeryPojoArrays;
    private EditText editTextDate;
    private AlertDialog helpDialog;
    private Spinner editTextTime;
    private ArrayList<CouponCodeModelArray> couponCodeModelArrays;
    private Dialog list_dialog;
    private ListView mRecyclerView;
    public static EditText editText;
    Button btnApplyCoupon;
    private MtplTextView textViewCouponDisCount;
    private LinearLayout linearLayoutTaxLayout;
    private MtplTextView mtplTextViewTax;
    private MtplTextView txtdeliveryCharge;
    private LinearLayout couponLayout;
    private LinearLayout deliveryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewordersummary);
        setTitle(getString(R.string.check_out));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.titleColor));
        toolbar.setTitle("Order Summary");
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewOrderSummary.this);
            }
        });

        TextView titleTextView = null;
        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
            Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), getString(R.string.fontface_robotolight_0));
            titleTextView.setTypeface(font);
            titleTextView.setTextSize(20);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }


        //  intent = getIntent();
        intView();


    }

    public void intView() {
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recycleView.setLayoutManager(mStaggeredLayoutManager);
        textViewTotalItems = (MtplTextView) findViewById(R.id.textViewToalItems);
        textViewItemTotal = (MtplTextView) findViewById(R.id.textViewItemtTotal);
        textViewCouponDisCount = (MtplTextView) findViewById(R.id.txtCouponDiscount);


        textViewSubTotal = (MtplTextView) findViewById(R.id.textViewItemSubTotal);
        textViewVat = (MtplTextView) findViewById(R.id.textViewVat);
        textViewDeliveryType = (MtplTextView) findViewById(R.id.textViewDeliveryType);
        textViewtotalPayable = (MtplTextView) findViewById(R.id.totalPayable);
        textViewApplycoupon = (MtplTextView) findViewById(R.id.textviewapplycoupon);
        imageButtonRemove = (ImageButton) findViewById(R.id.removeCouponbtn);
        linearLayoutCouponApply = (LinearLayout) findViewById(R.id.layoutCouponApply);
        btnApplyCoupon = (MtplButton) findViewById(R.id.btnApplyCoupon);
        linearLayoutTaxLayout = (LinearLayout) findViewById(R.id.taxLayout);
        couponLayout = (LinearLayout) findViewById(R.id.couponLayout);
        deliveryLayout = (LinearLayout) findViewById(R.id.deliveryLayout);


        mtplTextViewTax = (MtplTextView) findViewById(R.id.txtTaxAmt);
        txtdeliveryCharge = (MtplTextView) findViewById(R.id.txtdeliveryCharge);


        btnSubmit = (Button) findViewById(R.id.btncontinuPayment);
        spinnerDate = (AppCompatEditText) findViewById(R.id.edtDate);
        spinnerDate.setOnTouchListener(this);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        btnSubmit.setOnClickListener(this);
        imageButtonRemove.setOnClickListener(this);
        textViewApplycoupon.setOnClickListener(this);
        btnApplyCoupon.setOnClickListener(this);
        times = new ArrayList<String>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CM.isInternetAvailable(ViewOrderSummary.this)) {
            webCallOrderSummery(CM.getSp(ViewOrderSummary.this, "customerId", "").toString(), CM.getSp(ViewOrderSummary.this, "cartId", "").toString());
        } else {
            CM.showToast(ViewOrderSummary.this, getString(R.string.internetMsg));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CM.finishActivity(ViewOrderSummary.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btncontinuPayment:
                if (CM.isInternetAvailable(ViewOrderSummary.this)) {

                    Double maxOrder = 0.00;
                    if (CM.getSp(this, "maxOrderWar", "").toString().equals("null") && CM.getSp(this, "maxOrderWar", "").toString().equals("")) {
                        maxOrder = 0.00;
                    } else {
                        maxOrder = Double.parseDouble(CM.getSp(this, "maxOrderWar", "").toString());
                    }


                    Double totAmount = 0.00;
                    if (CM.getSp(this, "totAmount", "").toString().equals("null") && CM.getSp(this, "totAmount", "").toString().equals("")) {
                        totAmount = 0.00;
                    } else {
                        totAmount = Double.parseDouble(CM.getSp(this, "totAmount", "").toString());
                    }
                    Double GrandTot = 0.00;
                    if (CM.getSp(this, "totPay", "").toString().equals("null") && CM.getSp(this, "totPay", "").toString().equals("")) {
                        GrandTot = 0.00;
                    } else {
                        GrandTot = Double.parseDouble(CM.getSp(this, "totPay", "").toString());
                    }

                    Double minAmount = 0.00;
                    if (CM.getSp(ViewOrderSummary.this, "minOrder", "").toString().equals("null") && CM.getSp(ViewOrderSummary.this, "minOrder", "").toString().equals("")) {
                        minAmount = 0.00;
                    } else {
                        minAmount = Double.parseDouble(CM.getSp(ViewOrderSummary.this, "minOrder", "").toString());
                    }
                    Double maxAmount = 0.00;
                    if (CM.getSp(ViewOrderSummary.this, "maxOrder", "").toString().equals("null") && CM.getSp(ViewOrderSummary.this, "maxOrder", "").toString().equals("")) {
                        maxAmount = 0.00;
                    } else {
                        maxAmount = Double.parseDouble(CM.getSp(ViewOrderSummary.this, "maxOrder", "").toString());
                    }


                    if (maxAmount > 0.0) {
                        if (totAmount >= minAmount && totAmount <= maxAmount) {
                            if (GrandTot > 0.0) {
                                Date date = Calendar.getInstance().getTime();
                                String orderDate = CM.converDateFormate("dd-MM-yyyy", "dd-MM-yyyy", CM.getSp(ViewOrderSummary.this, "serverDate", "").toString());
                                String currentDate = CM.converDateFormate("EEE MMM dd HH:mm:ss Z yyyy", "dd-MM-yyyy", date.toString());
                                /**
                                 *  Order Date is Not less then Current Date
                                 */
                                Date odate = null, cdate = null;
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    odate = format.parse(orderDate);
                                    cdate = format.parse(currentDate);
                                    System.out.println(date);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                if (odate.compareTo(cdate) > 0) {
                                    Date orderTime = null, clossingTime;
                                    try {
                                        DateFormat readFormat = new SimpleDateFormat("hh:mm aaa");
                                        DateFormat readFormat1 = new SimpleDateFormat("hh:mm aaa");
                                        DateFormat dateFormat = new SimpleDateFormat("hh:mm aaa");
                                        // orderTime = readFormat.parse(CM.getSp(ViewOrderSummary.this, "serverTime", "").toString());
                                        // clossingTime = readFormat1.parse(CM.getSp(ViewOrderSummary.this, "closingTime", "").toString());
                                        String serverTime = CM.getSp(ViewOrderSummary.this, "serverTime", "").toString();
                                        if (CM.getCurentDateAndTime().contains("p.m.") || CM.getCurentDateAndTime().contains("a.m.")) {
                                            serverTime = serverTime.replace("PM", "p.m.");
                                            serverTime = serverTime.replace("AM", "a.m.");
                                        }
                                        orderTime = readFormat.parse(serverTime); //02:05 PM
                                        String clossingTimeFormat = CM.getSp(ViewOrderSummary.this, "closingTime", "").toString();
                                        if (CM.getCurentDateAndTime().contains("p.m.") || CM.getCurentDateAndTime().contains("a.m.")) {
                                            clossingTimeFormat = clossingTimeFormat.replace("PM", "p.m.");
                                            clossingTimeFormat = clossingTimeFormat.replace("AM", "a.m.");
                                        }
                                        clossingTime = readFormat.parse(clossingTimeFormat); //11:59 PM


                                        if (clossingTime.after(orderTime)) {
                                            CM.setSp(ViewOrderSummary.this, "serverTime", orderTime);
                                            CM.setSp(ViewOrderSummary.this, "serverDate", orderDate);
                                            CM.startActivity(ViewOrderSummary.this, ViewCustomerDetail.class);

                                        } else {
                                            CM.showToast(this, getString(R.string.validTimeDate));
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    /**
                                     *  Order Date is Equal Current Date
                                     */
                                } else if (odate.compareTo(cdate) == 0) {
                                    Date orderTime = null, clossiingTime;
                                    Date currentTime = null;
                                    try {
                                        DateFormat readFormat = new SimpleDateFormat("hh:mm aaa");
                                        DateFormat readFormat1 = new SimpleDateFormat("hh:mm aaa");
                                        DateFormat dateFormat = new SimpleDateFormat("hh:mm aaa");
                                        Date time = new Date();
                                        String serverTime = CM.getSp(ViewOrderSummary.this, "serverTime", "").toString();
                                        if (CM.getCurentDateAndTime().contains("p.m.") || CM.getCurentDateAndTime().contains("a.m.")) {
                                            serverTime = serverTime.replace("PM", "p.m.");
                                            serverTime = serverTime.replace("AM", "a.m.");
                                        }
                                        orderTime = readFormat.parse(serverTime); //02:05 PM

                                        String currentTimeFormat = readFormat.format(time);

                                        if (CM.getCurentDateAndTime().contains("p.m.") || CM.getCurentDateAndTime().contains("a.m.")) {
                                            currentTimeFormat = currentTimeFormat.replace("PM", "p.m.");
                                            currentTimeFormat = currentTimeFormat.replace("AM", "a.m.");
                                        }
                                        currentTime = readFormat.parse(currentTimeFormat); //11:26 PM

                                        String clossingTimeFormat = CM.getSp(ViewOrderSummary.this, "closingTime", "").toString();
                                        if (CM.getCurentDateAndTime().contains("p.m.") || CM.getCurentDateAndTime().contains("a.m.")) {
                                            clossingTimeFormat = clossingTimeFormat.replace("PM", "p.m.");
                                            clossingTimeFormat = clossingTimeFormat.replace("AM", "a.m.");
                                        }
                                        clossiingTime = readFormat.parse(clossingTimeFormat); //11:59 PM
                                        if (currentTime.before(orderTime) && clossiingTime.after(orderTime)) {
                                            CM.setSp(ViewOrderSummary.this, "serverTime", orderTime);
                                            CM.setSp(ViewOrderSummary.this, "serverDate", orderDate);
                                            CM.startActivity(ViewOrderSummary.this, ViewCustomerDetail.class);

                                        } else if (currentTime.equals(orderTime)) {
                                            if (clossiingTime.after(orderTime)) {
                                                CM.setSp(ViewOrderSummary.this, "serverTime", orderTime);
                                                CM.setSp(ViewOrderSummary.this, "serverDate", orderDate);
                                                CM.startActivity(ViewOrderSummary.this, ViewCustomerDetail.class);

                                            } else {
                                                CM.showToast(this, getString(R.string.validTimeDate));
                                            }
                                        } else {
                                            CM.showToast(this, getString(R.string.validTimeDate));
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    CM.showToast(this, getString(R.string.validTimeDate));
                                }
                            } else {
                                CM.showToast(this, "Order Must me unto Greater then 0");
                            }

                        } else {

                            if (totAmount > minAmount) {

                            } else {
                                CM.showToast(ViewOrderSummary.this, "Minimum order amount should be of " + CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "" + CM.getSp(ViewOrderSummary.this, "minOrder", "").toString());
                            }

                            if (totAmount < maxAmount) {

                            } else {
                                CM.showToast(ViewOrderSummary.this, "Maximum order amount should be of " + CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "" + CM.getSp(ViewOrderSummary.this, "maxOrder", "").toString());
                            }


                        }


                    } else if (totAmount >= minAmount) {


                        if (GrandTot > 0.0) {
                            Date date = Calendar.getInstance().getTime();
                            String orderDate = CM.converDateFormate("dd-MM-yyyy", "dd-MM-yyyy", CM.getSp(ViewOrderSummary.this, "serverDate", "").toString());
                            String currentDate = CM.converDateFormate("EEE MMM dd HH:mm:ss Z yyyy", "dd-MM-yyyy", date.toString());
                            /**
                             *  Order Date is Not less then Current Date
                             */
                            Date odate = null, cdate = null;
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                odate = format.parse(orderDate);
                                cdate = format.parse(currentDate);
                                System.out.println(date);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            if (odate.compareTo(cdate) > 0) {
                                Date orderTime = null, clossingTime;
                                try {
                                    DateFormat readFormat = new SimpleDateFormat("hh:mm aaa");
                                    DateFormat readFormat1 = new SimpleDateFormat("hh:mm aaa");
                                    DateFormat dateFormat = new SimpleDateFormat("hh:mm aaa");
                                    String serverTime = CM.getSp(ViewOrderSummary.this, "serverTime", "").toString();
                                    if (CM.getCurentDateAndTime().contains("p.m.") || CM.getCurentDateAndTime().contains("a.m.")) {
                                        serverTime = serverTime.replace("PM", "p.m.");
                                        serverTime = serverTime.replace("AM", "a.m.");
                                    }
                                    orderTime = readFormat.parse(serverTime); //02:05 PM

                                    //clossingTime = readFormat1.parse(CM.getSp(ViewOrderSummary.this, "closingTime", "").toString());

                                    String clossingTimeFormat = CM.getSp(ViewOrderSummary.this, "closingTime", "").toString();
                                    if (CM.getCurentDateAndTime().contains("p.m.") || CM.getCurentDateAndTime().contains("a.m.")) {
                                        clossingTimeFormat = clossingTimeFormat.replace("PM", "p.m.");
                                        clossingTimeFormat = clossingTimeFormat.replace("AM", "a.m.");
                                    }
                                    clossingTime = readFormat.parse(clossingTimeFormat); //11:59 PM


                                    if (clossingTime.after(orderTime)) {

                                        if (maxOrder <= totAmount) {
                                            showDialog(CM.getSp(ViewOrderSummary.this, "maxOrderWarMsg", "").toString(), orderTime, orderDate, maxOrder);
                                        } else {
                                            CM.setSp(ViewOrderSummary.this, "serverTime", orderTime);
                                            CM.setSp(ViewOrderSummary.this, "serverDate", orderDate);
                                            CM.startActivity(ViewOrderSummary.this, ViewCustomerDetail.class);
                                        }


                                    } else {
                                        CM.showToast(this, getString(R.string.validTimeDate));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                /**
                                 *  Order Date is Equal Current Date
                                 */
                            } else if (odate.compareTo(cdate) == 0) {
                                Date orderTime = null, clossiingTime;
                                Date currentTime = null;
                                try {
                                    DateFormat readFormat = new SimpleDateFormat("hh:mm aaa");
                                    DateFormat readFormat1 = new SimpleDateFormat("hh:mm aaa");
                                    DateFormat dateFormat = new SimpleDateFormat("hh:mm aaa");
                                    Date time = new Date();
                                    String serverTime = CM.getSp(ViewOrderSummary.this, "serverTime", "").toString();
                                    if (CM.getCurentDateAndTime().contains("p.m.") || CM.getCurentDateAndTime().contains("a.m.")) {
                                        serverTime = serverTime.replace("PM", "p.m.");
                                        serverTime = serverTime.replace("AM", "a.m.");
                                    }
                                    orderTime = readFormat.parse(serverTime); //02:05 PM

                                    String currentTimeFormat = readFormat.format(time);

                                    if (CM.getCurentDateAndTime().contains("p.m.") || CM.getCurentDateAndTime().contains("a.m.")) {
                                        currentTimeFormat = currentTimeFormat.replace("PM", "p.m.");
                                        currentTimeFormat = currentTimeFormat.replace("AM", "a.m.");
                                    }
                                    currentTime = readFormat.parse(currentTimeFormat); //11:26 PM


                                    String clossingTimeFormat = CM.getSp(ViewOrderSummary.this, "closingTime", "").toString();
                                    if (CM.getCurentDateAndTime().contains("p.m.") || CM.getCurentDateAndTime().contains("a.m.")) {
                                        clossingTimeFormat = clossingTimeFormat.replace("PM", "p.m.");
                                        clossingTimeFormat = clossingTimeFormat.replace("AM", "a.m.");
                                    }
                                    clossiingTime = readFormat.parse(clossingTimeFormat); //11:59 PM

                                    if (currentTime.before(orderTime) && clossiingTime.after(orderTime)) {
//                                        CM.setSp(ViewOrderSummary.this, "serverTime", orderTime);
//                                        CM.setSp(ViewOrderSummary.this, "serverDate", orderDate);
//                                        CM.startActivity(ViewOrderSummary.this, ViewCustomerDetail.class);

                                        if (maxOrder <= totAmount) {
                                            showDialog(CM.getSp(ViewOrderSummary.this, "maxOrderWarMsg", "").toString(), orderTime, orderDate, maxOrder);
                                        } else {
                                            CM.setSp(ViewOrderSummary.this, "serverTime", orderTime);
                                            CM.setSp(ViewOrderSummary.this, "serverDate", orderDate);
                                            CM.startActivity(ViewOrderSummary.this, ViewCustomerDetail.class);
                                        }


                                    } else if (currentTime.equals(orderTime)) {
                                        if (clossiingTime.after(orderTime)) {
//                                            CM.setSp(ViewOrderSummary.this, "serverTime", orderTime);
//                                            CM.setSp(ViewOrderSummary.this, "serverDate", orderDate);
//                                            CM.startActivity(ViewOrderSummary.this, ViewCustomerDetail.class);
                                            if (maxOrder <= totAmount) {
                                                showDialog(CM.getSp(ViewOrderSummary.this, "maxOrderWarMsg", "").toString(), orderTime, orderDate, maxOrder);
                                            } else {
                                                CM.setSp(ViewOrderSummary.this, "serverTime", orderTime);
                                                CM.setSp(ViewOrderSummary.this, "serverDate", orderDate);
                                                CM.startActivity(ViewOrderSummary.this, ViewCustomerDetail.class);
                                            }
                                        } else {
                                            CM.showToast(this, getString(R.string.validTimeDate));
                                        }
                                    } else {
                                        CM.showToast(this, getString(R.string.validTimeDate));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                CM.showToast(this, getString(R.string.validTimeDate));
                            }

                        } else {
                            CM.showToast(this, "Order Must me unto Greater then 0");

                        }
                    } else {
                        CM.showToast(this, "Order Must me upto " + CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "" + CM.getSp(ViewOrderSummary.this, "minOrder", "").toString());
                    }


                } else {
                    CM.showToast(ViewOrderSummary.this, getString(R.string.internetMsg));
                }
                break;
            case R.id.textviewapplycoupon:
                showPopUp();
                break;
            case R.id.removeCouponbtn:
                textViewApplycoupon.setText("Apply Coupon");
                linearLayoutCouponApply.setVisibility(View.GONE);
                break;
            case R.id.btnApplyCoupon:
                if (btnApplyCoupon.getText().toString().equals("Apply Coupon")) {
                    webCallCouponCode();
                } else {
                    webCallRemoveCouponCode(CM.getSp(ViewOrderSummary.this, "cartId", "").toString());
                }
                break;
        }


    }


    private void showPopUp() {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(ViewOrderSummary.this);
        View child = getLayoutInflater().inflate(R.layout.applycouponcodepopoup, null);

        final EditText editTextCoupon = (EditText) child.findViewById(R.id.popupCoupon);
        helpBuilder.setView(child);
        helpBuilder.setCancelable(false);

        helpBuilder.setPositiveButton("APPLY COUPON",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String _edtValue = editTextCoupon.getText().toString().trim();
                        if (_edtValue.length() == 0 && _edtValue == null && _edtValue.equalsIgnoreCase("")) {
                            //do nothing.
                        } else {
                            hideKeyboardFrom(ViewOrderSummary.this, editTextCoupon);
                            textViewApplycoupon.setText("Coupon Applied");
                            linearLayoutCouponApply.setVisibility(View.VISIBLE);
                        }
                    }
                }

        );

        helpBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        hideKeyboardFrom(ViewOrderSummary.this, editTextCoupon);
                    }
                }

        );

        final AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();


        helpDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextCoupon.getText().toString().equals("")) {
                    helpDialog.dismiss();
                    hideKeyboardFrom(ViewOrderSummary.this, editTextCoupon);
                    textViewApplycoupon.setText("Coupon Applied");
                    linearLayoutCouponApply.setVisibility(View.VISIBLE);

                } else {
                    CM.showToast(ViewOrderSummary.this, getString(R.string.validateCoupon));
                }
            }
        });

    }


    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void webCallOrderSummery(String id, String cartId) {
        try {
            VolleyIntialization v = new VolleyIntialization(ViewOrderSummary.this, true, true);

            WebService.getResponseForOrderSummer(v, id, cartId, new OnVolleyHandler() {
                @Override
                public void onVollySuccess(String response) {
                    if (isFinishing()) {
                        return;
                    }
                    MtplLog.i("WebCalls", response);
                    Log.e(TAG, response);
                    getResponseForOrderSummery(response);

                }

                @Override
                public void onVollyError(String error) {
                    MtplLog.i("WebCalls", error);
                    if (CM.isInternetAvailable(ViewOrderSummary.this)) {
                        CM.showPopupCommonValidation(ViewOrderSummary.this, error, false);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getResponseForOrderSummery(String response) {
        String strResponseStatus = CM.getValueFromJson(WebServiceTag.WEB_STATUS, response);
        if (strResponseStatus.equalsIgnoreCase(WebServiceTag.WEB_STATUSFAIL)) {
            CM.showPopupCommonValidation(ViewOrderSummary.this, CM.getValueFromJson(WebServiceTag.WEB_STATUS_ERRORTEXT, response), false);
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("ResponseCode") != null && jsonObject.getString("ResponseCode").equals("202")) {

                OrderSummeryModel model_main = CM.JsonParse(new OrderSummeryModel(), jsonObject.getString("ResponseObject"));
                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("ResponseObject"));
                JSONArray jsonArray = new JSONArray(jsonObject1.optString("data"));
                ArrayList<OrderSummeryPojo> orderSummeryPojos = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    OrderSummeryPojo orderSummeryPojo = new OrderSummeryPojo();
                    orderSummeryPojo.setdOrderDate(jsonArray.getJSONObject(i).optString("dOrderDate"));
                    orderSummeryPojo.setdOrderTime(jsonArray.getJSONObject(i).optString("dOrderTime"));
                    orderSummeryPojo.setnCartItemCount(jsonArray.getJSONObject(i).optString("nCartItemCount"));
                    orderSummeryPojo.setSubTotalAmount(jsonArray.getJSONObject(i).optString("SubTotalAmount"));
                    orderSummeryPojo.setTotalAmount(jsonArray.getJSONObject(i).optString("TotalAmount"));
                    orderSummeryPojo.setnDeliveryCharge(jsonArray.getJSONObject(i).optString("nDeliveryCharge"));
                    orderSummeryPojo.setbIsDiscountApplied(jsonArray.getJSONObject(i).optString("bIsDiscountApplied"));
                    orderSummeryPojo.setTax(jsonArray.getJSONObject(i).optString("Tax"));
                    JSONArray cartItemArray = new JSONArray(jsonArray.getJSONObject(i).optString("cartItems"));
                    orderSummeryPojoArrays = new ArrayList<>();
                    for (int j = 0; j < cartItemArray.length(); j++) {

                        OrderSummeryPojoArray orderSummeryPojoArray = new OrderSummeryPojoArray();
                        orderSummeryPojoArray.setQty(cartItemArray.getJSONObject(j).optString("Qty"));
                        orderSummeryPojoArray.setProductTaxAmt(cartItemArray.getJSONObject(j).optString("ProductTotalIncTax"));
                        orderSummeryPojoArray.setcProductImagePath(cartItemArray.getJSONObject(j).optString("cProductImagePath"));
                        orderSummeryPojoArray.setnCompanyProductID(cartItemArray.getJSONObject(j).optString("nCompanyProductID"));
                        orderSummeryPojoArray.setnAddonTotalAmt(cartItemArray.getJSONObject(j).optString("nAddonTotalAmt"));
                        orderSummeryPojoArray.setProductTaxAmt(cartItemArray.getJSONObject(j).optString("ProductTaxAmt"));

                        orderSummeryPojoArray.setnRate(cartItemArray.getJSONObject(j).optString("nRate"));
                        orderSummeryPojoArray.setProductTotalAmount(cartItemArray.getJSONObject(j).optString("ProductTotalAmount"));
                        orderSummeryPojoArray.setnCartID(cartItemArray.getJSONObject(j).optString("nCartID"));
                        orderSummeryPojoArray.setcProductName(cartItemArray.getJSONObject(j).optString("cProductName"));
                        orderSummeryPojoArray.setProductCatName(cartItemArray.getJSONObject(j).optString("cCategoryName"));
                        orderSummeryPojoArray.setnCartDetailID(cartItemArray.getJSONObject(j).optString("nCartDetailID"));
                        orderSummeryPojoArray.setnDiscountAmt(cartItemArray.getJSONObject(j).optString("nDiscountAmt"));
                        JSONArray jsonArray1 = new JSONArray(cartItemArray.getJSONObject(j).optString("ProductAssignedAddonsJSON"));

                        ArrayList<OrderSummeryPojoArraySub> orderSummeryPojoArraySubs = new ArrayList<>();
                        for (int k = 0; k < jsonArray1.length(); k++) {
                            OrderSummeryPojoArraySub orderSummeryPojoArraySub = new OrderSummeryPojoArraySub();
                            orderSummeryPojoArraySub.setnMapperID(jsonArray1.getJSONObject(k).optString("nMapperID"));
                            orderSummeryPojoArraySub.setnAttributeID(jsonArray1.getJSONObject(k).optString("nAttributeID"));
                            orderSummeryPojoArraySub.setcAttributeLabel(jsonArray1.getJSONObject(k).optString("cAttributeLabel"));
                            orderSummeryPojoArraySub.setIsMultiple(jsonArray1.getJSONObject(k).optString("isMultiple"));
                            orderSummeryPojoArraySub.setIsRequired(jsonArray1.getJSONObject(k).optString("isRequired"));

                            ArrayList<OrderSummeryPojoArraySubSub> orderSummeryPojoArraySubSubs = new ArrayList<>();
                            JSONArray jsonArray2 = new JSONArray(jsonArray1.getJSONObject(k).optString("ListProductAttributeMapperDetail"));
                            for (int l = 0; l < jsonArray2.length(); l++) {
                                OrderSummeryPojoArraySubSub orderSummeryPojoArraySubSub = new OrderSummeryPojoArraySubSub();
                                orderSummeryPojoArraySubSub.setcAttributeLabel(jsonArray1.getJSONObject(k).optString("cAttributeLabel"));
                                orderSummeryPojoArraySubSub.setnMapperID(jsonArray2.getJSONObject(l).optString("nMapperID"));
                                orderSummeryPojoArraySubSub.setcAttributeValueLabel(jsonArray2.getJSONObject(l).optString("cAttributeValueLabel"));
                                orderSummeryPojoArraySubSub.setnAttributeID(jsonArray2.getJSONObject(l).optString("nAttributeID"));
                                orderSummeryPojoArraySubSub.setnAttributeValueMasterID(jsonArray2.getJSONObject(l).optString("nAttributeValueMasterID"));
                                orderSummeryPojoArraySubSub.setnMapperDetailID(jsonArray2.getJSONObject(l).optString("nMapperDetailID"));
                                orderSummeryPojoArraySubSub.setnPrice(jsonArray2.getJSONObject(l).optString("nPrice"));
                                orderSummeryPojoArraySubSubs.add(orderSummeryPojoArraySubSub);
                            }
                            orderSummeryPojoArraySub.setOrderSummeryPojoArraySubSubs(orderSummeryPojoArraySubSubs);
                            orderSummeryPojoArraySubs.add(orderSummeryPojoArraySub);
                        }

                        orderSummeryPojoArray.setOrderSummeryPojoArraySubs(orderSummeryPojoArraySubs);
                        orderSummeryPojoArrays.add(orderSummeryPojoArray);
                    }
                    orderSummeryPojoArrays.size();
                }

                if (orderSummeryModelArrays != null) {
                    orderSummeryModelArrays.clear();
                }
                ArrayList<OrderSummeryModelArray> orderSummery = model_main.orderSummeryModelArrays;
                orderSummery.size();
                String orderDate = "";
                if (orderSummery.get(0).dOrderDate != null) {
                    orderDate = CM.converDateFormate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", orderSummery.get(0).dOrderDate);
                } else {
                    Date date = Calendar.getInstance().getTime();
                    orderDate = CM.converDateFormate("EEE MMM dd HH:mm:ss Z yyyy", "dd-MM-yyyy", date.toString());
                }
                if (!CM.getSp(this, "serverDate", "").toString().equals("")) {
                    spinnerDate.setText(CM.getSp(this, "serverDate", "").toString());
                } else {
                    Date date = Calendar.getInstance().getTime();
                    String currentDate = CM.converDateFormate("EEE MMM dd HH:mm:ss Z yyyy", "dd-MM-yyyy", date.toString());
                    CM.setSp(this, "serverDate", currentDate);
                    spinnerDate.setText(currentDate);
                }
                try {
                    DateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    Date dt = new Date(String.valueOf(inputFormat.parse(orderSummery.get(0).dOrderTime.toString())));
                    serverReturnTime = sdf.format(dt);
                    //   CM.setSp(this, "serverTime", serverReturnTime);
                } catch (Exception e) {
                    e.getMessage();
                }
                mAdapter = new adptordersummery(ViewOrderSummary.this, orderSummeryPojoArrays);
                recycleView.setAdapter(mAdapter);
                if (orderSummery.get(0).SubTotalAmount != null) {
                    textViewItemTotal.setText("Total " + CM.getSp(ViewOrderSummary.this, "currency", "").toString() + orderSummery.get(0).TotalAmount);
                } else {
                    textViewItemTotal.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "0.00");
                }

                if (orderSummery.get(0).SubTotalAmount != null) {
                    textViewSubTotal.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + orderSummery.get(0).SubTotalAmount);
                } else {
                    textViewSubTotal.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "0.00");
                }
                if (orderSummery.get(0).nCartItemCount != null) {
                    textViewTotalItems.setText("ITEMS" + "( " + orderSummery.get(0).nCartItemCount + " )");
                } else {
                    textViewTotalItems.setText("0");
                }
                if (orderSummery.get(0).Tax != null) {
                    if (orderSummery.get(0).Tax.toString().equals("0.00")) {
                        linearLayoutTaxLayout.setVisibility(View.GONE);
                    } else {
                        linearLayoutTaxLayout.setVisibility(View.VISIBLE);
                        mtplTextViewTax.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "" + orderSummery.get(0).Tax);

                    }
                } else {

                    linearLayoutTaxLayout.setVisibility(View.GONE);
                }


                if (orderSummery.get(0).nDeliveryCharge != null) {
                    if (!orderSummery.get(0).nDeliveryCharge.toString().equals("0.00") && !orderSummery.get(0).nDeliveryCharge.toString().equals("null")) {
                        deliveryLayout.setVisibility(View.VISIBLE);
                        txtdeliveryCharge.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + orderSummery.get(0).nDeliveryCharge);
                    } else {
                        txtdeliveryCharge.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "0.00");
                        deliveryLayout.setVisibility(View.GONE);
                    }

                } else {
                    txtdeliveryCharge.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "0.00");
                    deliveryLayout.setVisibility(View.GONE);
                }


                if (orderSummery.get(0).nDiscountAmt != null && !orderSummery.get(0).nDiscountAmt.toString().equals("0.00")) {
                    //btnApplyCoupon.setEnabled(false);
                    CM.showToast(ViewOrderSummary.this, "Coupon Applied");
                    btnApplyCoupon.setBackgroundResource(R.drawable.roundedbuttonunselect);
                    btnApplyCoupon.setText("Remove Coupon");
                    textViewCouponDisCount.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + orderSummery.get(0).nDiscountAmt);
                } else {

                    if (orderSummery.get(0).bIsDiscountApplied != null) {
                        if (Boolean.parseBoolean(orderSummery.get(0).bIsDiscountApplied)) {
                            CM.showToast(ViewOrderSummary.this, "Coupon Applied");
                            btnApplyCoupon.setBackgroundResource(R.drawable.roundedbuttonunselect);
                            btnApplyCoupon.setText("Remove Coupon");
                            if (orderSummery.get(0).nDiscountAmt != null && !orderSummery.get(0).nDiscountAmt.toString().equals("0.00")) {
                                textViewCouponDisCount.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + orderSummery.get(0).nDiscountAmt);
                            } else {
                                textViewCouponDisCount.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "0.00");
                                couponLayout.setVisibility(View.GONE);
                            }
                        } else {
                            textViewCouponDisCount.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "0.00");
                            couponLayout.setVisibility(View.GONE);
                            btnApplyCoupon.setText("Apply Coupon");
                            btnApplyCoupon.setBackgroundResource(R.drawable.roundedbutton);
                        }
                    } else {
                        textViewCouponDisCount.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "0.00");
                        couponLayout.setVisibility(View.GONE);
                        btnApplyCoupon.setText("Apply Coupon");
                        btnApplyCoupon.setBackgroundResource(R.drawable.roundedbutton);
                    }
                    //btnApplyCoupon.setBackground(R.drawable.roundedbutton);
                }


                if (orderSummery.get(0).TotalAmount != null) {
                    textViewtotalPayable.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + orderSummery.get(0).TotalAmount);
                } else {
                    textViewtotalPayable.setText(CM.getSp(ViewOrderSummary.this, "currency", "").toString() + "0.00");
                }


                try {
                    CM.setSp(ViewOrderSummary.this, "totAmount", orderSummery.get(0).SubTotalAmount.toString());
                    CM.setSp(ViewOrderSummary.this, "totPay", orderSummery.get(0).TotalAmount.toString());
                } catch (Exception e) {

                }
                mAdapter.SetOnItemClickListener(new OnItemClickListenerOrderSuumnery() {
                    @Override
                    public void onItemClick(String cartId, String cartIdDetail, String status, String compProdId) {
                        if (status.equals("edit")) {
                            if (CM.isInternetAvailable(ViewOrderSummary.this)) {
                                CM.startActivity(ViewOrderSummary.this, ViewFoodItemDetail.class);
                            } else {
                                CM.showToast(ViewOrderSummary.this, getString(R.string.internetMsg));
                            }
                        } else if (status.equals("remove")) {

                            if (CM.isInternetAvailable(ViewOrderSummary.this)) {
                                webCallRemoveCart(cartId, cartIdDetail, compProdId);
                            } else {
                                CM.showToast(ViewOrderSummary.this, getString(R.string.internetMsg));
                            }
                        }
                    }
                });

                setDateTime();

            } else if (jsonObject.getString("ResponseCode").equals("404")) {
                if (orderSummeryPojoArrays != null) {
                    orderSummeryPojoArrays.clear();
                }


                mAdapter = new adptordersummery(ViewOrderSummary.this, orderSummeryPojoArrays);
                recycleView.setAdapter(mAdapter);
                recycleView.invalidate();
                mAdapter.notifyDataSetChanged();
                textViewItemTotal.setText("Total Rs.");
                textViewSubTotal.setText("");
                textViewTotalItems.setText("ITEMS" + "( 0 )");
                textViewtotalPayable.setText("");
                spinnerDate.setText("");


                CM.showToast(ViewOrderSummary.this, jsonObject.getString("ResponseObject"));

            } else {
                if (orderSummeryModelArrays != null) {
                    orderSummeryModelArrays.clear();
                }
                CM.showToast(ViewOrderSummary.this, jsonObject.getString("ResponseObject"));
            }
        } catch (
                Exception e
                )

        {
            CM.showPopupCommonValidation(ViewOrderSummary.this, e.getMessage(), false);
        }

    }


    public void webCallRemoveCart(String cartId, String cartDetailId, String compProdId) {
        try {
            VolleyIntialization v = new VolleyIntialization(ViewOrderSummary.this, true, true);

            WebService.getResponseForRemoveCART(v, cartId, cartDetailId, compProdId, new OnVolleyHandler() {
                @Override
                public void onVollySuccess(String response) {
                    if (isFinishing()) {
                        return;
                    }
                    MtplLog.i("WebCalls", response);
                    Log.e(TAG, response);
                    getResponseForRemoveItem(response);

                }

                @Override
                public void onVollyError(String error) {
                    MtplLog.i("WebCalls", error);
                    if (CM.isInternetAvailable(ViewOrderSummary.this)) {
                        CM.showPopupCommonValidation(ViewOrderSummary.this, error, false);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getResponseForRemoveItem(String response) {
        String strResponseStatus = CM.getValueFromJson(WebServiceTag.WEB_STATUS, response);
        if (strResponseStatus.equalsIgnoreCase(WebServiceTag.WEB_STATUSFAIL)) {
            CM.showPopupCommonValidation(ViewOrderSummary.this, CM.getValueFromJson(WebServiceTag.WEB_STATUS_ERRORTEXT, response), false);
            return;
        }
        try {

            JSONObject jsonObject1 = new JSONObject(response);

            if (jsonObject1.getString("ResponseCode").equals("202")) {
                CM.showToast(ViewOrderSummary.this, "Item Removed");
                webCallOrderSummery(CM.getSp(ViewOrderSummary.this, "customerId", "").toString(), CM.getSp(ViewOrderSummary.this, "cartId", "").toString());
            } else {
                CM.showToast(ViewOrderSummary.this, jsonObject1.getString("ResponseObject"));
            }


        } catch (Exception e) {
            CM.showPopupCommonValidation(ViewOrderSummary.this, e.getMessage(), false);
        }
    }


    public void startEndTime(String startTime, String endTime, String tOpeningTime) {
        try {
            Date date1 = null;
            Date date2 = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
            try {
                date1 = dateFormat.parse(startTime);
                date2 = dateFormat.parse(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            String hour = tOpeningTime.substring(0, 2);
            String min = tOpeningTime.length() > 2 ? tOpeningTime.substring(tOpeningTime.length() - 2) : tOpeningTime;
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date1);
            while (calendar.getTime().before(date2)) {

                try {
                    if (hour != null && !hour.equals("00")) {
                        calendar.add(Calendar.HOUR, Integer.parseInt(hour));
                    }
                    if (min != null && !min.equals("00")) {
                        calendar.add(Calendar.MINUTE, Integer.parseInt(min));
                    }
                    times.add(sdf.format(calendar.getTime()));


                } catch (Exception e) {
                }
            }

            if (!CM.getSp(ViewOrderSummary.this, "time", "").toString().equals("")) {
                int selectionPosition = times.indexOf(Integer.parseInt(CM.getSp(ViewOrderSummary.this, "time", "").toString()));
                spinner.setSelection(selectionPosition);
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;
        switch (view.getId()) {
            case R.id.edtDate:

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (spinnerDate.getRight() - spinnerDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        Calendar now = Calendar.getInstance();

                        if (!CM.getSp(ViewOrderSummary.this, "serverDate", "").equals("")) {
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = format.parse(CM.getSp(ViewOrderSummary.this, "serverDate", "").toString());
                                now.setTime(date);
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                        DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH));


                        try {
                            Calendar now1 = Calendar.getInstance();
                            int day = now1.get(Calendar.DATE);
                            int days = now1.getActualMaximum(Calendar.DAY_OF_MONTH);
                            int month = now1.get(Calendar.MONTH) + 1;
                            String yearMonth = now1.get(Calendar.YEAR) + "/" + month + "/";
                            ArrayList<Calendar> calendars = new ArrayList<>();
                            for (int i = day + 3; i <= days; i++) {
                                calendars.add(DateToCalendar(new Date(yearMonth + i)));
                            }
                            Calendar tArray[] = calendars.toArray(new Calendar[calendars.size()]);
                            dpd.setDisabledDays(tArray);
                            dpd.setMinDate(DateToCalendar(new Date(System.currentTimeMillis() - 1000)));
                            dpd.show(getFragmentManager(), "Datepickerdialog");

                            LocalDate monthEnd = new LocalDate().plusMonths(1).withDayOfMonth(1).minusDays(1);
                            // 2016-12-31
                            if ((day == (monthEnd.getDayOfMonth() - 2)) || (day == (monthEnd.getDayOfMonth() - 1)) || (day == (monthEnd.getDayOfMonth()))) {

                            } else {
                                Calendar c = Calendar.getInstance();
                                c.set(monthEnd.getYear(), monthEnd.getMonthOfYear() - 1, monthEnd.getDayOfMonth());//Year,Mounth -1,Day
                                dpd.setMaxDate(c);
                            }

                        } catch (Exception e) {

                            e.getMessage();

                        }
                        return true;
                    }
                }
                break;
        }

        return false;
    }

    public static Calendar DateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;
        spinnerDate.setText(dayOfMonth + "-" + month + "-" + year);

        if (editTextDate != null) {
            editTextDate.setText(dayOfMonth + "/" + month + "/" + year);
        }
        String currentDate = "";
        Date cuurentTime;
        String formattedDate = "";
        DateFormat readFormat = new SimpleDateFormat("hh:mm aaa");
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aaa");
        DateFormat writeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        currentDate = CM.converDateFormate("EEE MMM dd HH:mm:ss Z yyyy", "yyyy-MM-dd", date.toString());
        Date time = new Date();
        try {
            cuurentTime = readFormat.parse(dateFormat.format(time));
            if (cuurentTime != null) {
                formattedDate = writeFormat.format(cuurentTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String orderDate = CM.converDateFormate("dd-MM-yyyy", "yyyy-MM-dd", dayOfMonth + "-" + month + "-" + year);
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        webCallCheckServerTime(orderDate + " " + timeStamp, currentDate + " " + formattedDate);


        CM.setSp(ViewOrderSummary.this, "serverDate", dayOfMonth + "-" + month + "-" + year);

    }


    public static boolean containsName(List<String> list, String id) {
        for (String object : list) {
            if (object.toString().equals(id)) {
                return true;
            }
        }
        return false;
    }


    public void showdate(String tOpeningTime, String tClosingTime, String intervel) {

        DateTimeFormatter FORMAT = DateTimeFormat.forPattern("hh:mm aa");
        String hour = intervel.substring(0, 2);
        String min = intervel.length() > 2 ? intervel.substring(intervel.length() - 2) : intervel;
        Duration duration = null;
        try {
            if (hour != null && !hour.equals("00")) {
                duration = Hours.hours(Integer.parseInt(hour)).toStandardDuration();
            } else if (min != null && !min.equals("00")) {
                duration = Minutes.minutes(Integer.parseInt(min)).toStandardDuration();
            } else {
                duration = Minutes.minutes(Integer.parseInt(min)).toStandardDuration();
            }
        } catch (Exception e) {

        }
        final Set<DateTime> set = new LinkedHashSet<DateTime>();
        DateTime d = new DateTime(CM.dateFormat(tOpeningTime));
        DateTime e = new DateTime(CM.dateFormat(tClosingTime));
        do {
            set.add(d);
            d = d.plus(duration);
        } while (d.compareTo(e) <= 0);
        for (final DateTime dt : set) {
            System.out.println(FORMAT.print(dt));
            //strings.add(FORMAT.print(dt));
            times.add(FORMAT.print(dt));
        }
        times.size();

    }


    public void setDateTime() {
        DateFormat readFormat = new SimpleDateFormat("hh:mm aaa");
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aaa");
        DateFormat writeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        Date time = new Date();
        Date orderTime = null, cuurentTime = null;
        String currentDate = "";
        String orderDate;
        String formattedDate = "";
        try {

            cuurentTime = readFormat.parse(dateFormat.format(time));
            if (cuurentTime != null) {
                formattedDate = writeFormat.format(cuurentTime);
            }
            currentDate = CM.converDateFormate("EEE MMM dd HH:mm:ss Z yyyy", "yyyy-MM-dd", date.toString());
            orderTime = readFormat.parse(CM.getSp(ViewOrderSummary.this, "serverTime", "").toString());
            orderDate = CM.converDateFormate("dd-MM-yyyy", "dd-MM-yyyy", CM.getSp(ViewOrderSummary.this, "serverDate", "").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //orderDate = CM.converDateFormate("yyyy-MM-dd", "yyyy-MM-dd", CM.getSp(ViewOrderSummary.this, "serverDate", "").toString());
        orderDate = CM.converDateFormate("dd-MM-yyyy", "yyyy-MM-dd", this.spinnerDate.getText().toString());
        //(String) spinner.getSelectedItem()
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        webCallCheckServerTime(orderDate + " " + timeStamp, currentDate + " " + formattedDate);


    }


    public void webCallCheckServerTime(String orderDate, String currentDate) {
        try {
            VolleyIntialization v = new VolleyIntialization(ViewOrderSummary.this, true, true);
            WebService.getResponseForSetTime(v, orderDate, currentDate, new OnVolleyHandler() {
                @Override
                public void onVollySuccess(String response) {
                    if (isFinishing()) {
                        return;
                    }
                    MtplLog.i("WebCalls", response);
                    Log.e(TAG, response);
                    getResponseForServerTime(response);

                }

                @Override
                public void onVollyError(String error) {
                    MtplLog.i("WebCalls", error);
                    if (CM.isInternetAvailable(ViewOrderSummary.this)) {
                        CM.showPopupCommonValidation(ViewOrderSummary.this, error, false);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getResponseForServerTime(String response) {
        String strResponseStatus = CM.getValueFromJson(WebServiceTag.WEB_STATUS, response);
        if (strResponseStatus.equalsIgnoreCase(WebServiceTag.WEB_STATUSFAIL)) {
            CM.showPopupCommonValidation(ViewOrderSummary.this, CM.getValueFromJson(WebServiceTag.WEB_STATUS_ERRORTEXT, response), false);
            return;
        }
        try {

            JSONObject jsonObject1 = new JSONObject(response);
            if (jsonObject1.getString("ResponseCode").equals("202")) {

                JSONObject jsonObject11 = new JSONObject(jsonObject1.getString("ResponseObject"));
                if (jsonObject11.has("lblStoreClosed")) {
                    if (helpDialog != null) {
                        helpDialog.dismiss();
                    }

                    showPopUp("", "", "");

                } else {
                    TimeModel model_main = CM.JsonParse(new TimeModel(), jsonObject1.getString("ResponseObject"));
                    CM.setSp(ViewOrderSummary.this, "openingTime", model_main.tOpeningTime);
                    CM.setSp(ViewOrderSummary.this, "closingTime", model_main.tClosingTime);
                    CM.setSp(ViewOrderSummary.this, "timeInterval", model_main.tDisplayHoursInterval);
                    if (model_main.isPopupOpen.equals("false")) {
                        times.clear();
                        showdate(model_main.tOpeningTime, model_main.tClosingTime, model_main.tDisplayHoursInterval);
                        if (times != null && times.size() > 0) {
//                            mySpinnerArrayAdapter = new CustomSpinnerAdapter(ViewOrderSummary.this, times);
//                            spinner.setAdapter(mySpinnerArrayAdapter);

                            if (editTextTime != null) {
                                CustomSpinnerAdapter mySpinnerArrayAdapter = new CustomSpinnerAdapter(ViewOrderSummary.this, times);
                                editTextTime.setAdapter(mySpinnerArrayAdapter);
                            }
                            mySpinnerArrayAdapter = new CustomSpinnerAdapter(ViewOrderSummary.this, times);
                            spinner.setAdapter(mySpinnerArrayAdapter);

//                            if (!CM.getSp(ViewOrderSummary.this, "serverTime", "").toString().equals("")) {
//                                boolean IsContain = times.contains(CM.getSp(ViewOrderSummary.this, "serverTime", "").toString());
//                                if (IsContain) {
//                                    int selectionPosition = times.indexOf(CM.getSp(ViewOrderSummary.this, "serverTime", "").toString());
//                                    spinner.setSelection(selectionPosition);
//                                } else {
//                                    times.add(CM.getSp(ViewOrderSummary.this, "serverTime", "").toString());
//
//                                    Collections.sort(times, new Comparator<String>() {
//                                        DateFormat f = new SimpleDateFormat("hh:mm aaa");
//
//                                        @Override
//                                        public int compare(String o1, String o2) {
//                                            try {
//                                                return f.parse(o1).compareTo(f.parse(o2));
//                                            } catch (ParseException e) {
//                                                throw new IllegalArgumentException(e);
//                                            }
//                                        }
//                                    });
//
//                                }
//                                times.size();
//
//                                try {
//                                    int selectionPosition = times.indexOf(CM.getSp(ViewOrderSummary.this, "serverTime", "").toString());
//                                    spinner.setSelection(selectionPosition);
//                                } catch (Exception e) {
//
//                                }
//                            }


                        }

                    } else if (model_main.isPopupOpen.equals("true")) {
                        if (helpDialog != null) {
                            helpDialog.dismiss();
                        }
                        showPopUp(model_main.tOpeningTime, model_main.tClosingTime, model_main.tDisplayHoursInterval);
                    } else {

                    }
                }

            } else {
                CM.showToast(ViewOrderSummary.this, jsonObject1.getString("ResponseObject"));
            }


        } catch (Exception e) {
            CM.showPopupCommonValidation(ViewOrderSummary.this, e.getMessage(), false);
        }
    }


    private void showPopUp(String tOpeningTime, String tClosingTime, String tDisplayHoursInterval) {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(ViewOrderSummary.this);
        helpBuilder.setTitle(getString(R.string.orderForTime));
        helpBuilder.setMessage(getString(R.string.content));
        View child = getLayoutInflater().inflate(R.layout.popuplout1, null);
        helpBuilder.setView(child);
        editTextDate = (EditText) child.findViewById(R.id.edtDate);
        editTextTime = (Spinner) child.findViewById(R.id.spinnerTime);
        editTextDate.setOnTouchListener(this);
        editTextDate.setText(CM.getSp(ViewOrderSummary.this, "serverDate", "").toString());
        helpBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });

        helpBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        times.clear();
        try {
            showdate(tOpeningTime, tClosingTime, tDisplayHoursInterval);
        } catch (Exception e) {

        }

        if (times != null && times.size() > 0) {
            CustomSpinnerAdapter mySpinnerArrayAdapter = new CustomSpinnerAdapter(ViewOrderSummary.this, times);
            editTextTime.setAdapter(mySpinnerArrayAdapter);
            mySpinnerArrayAdapter = new CustomSpinnerAdapter(ViewOrderSummary.this, times);
            spinner.setAdapter(mySpinnerArrayAdapter);

        }
        helpDialog = helpBuilder.create();
        helpDialog.show();

        helpDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextDate.getText().toString().equals("")) {

                    if (times != null && times.size() > 0) {
                        helpDialog.dismiss();
                        String strings = (String) editTextTime.getSelectedItem();
                        if (containsName(times, strings)) {
                            int retval = times.indexOf(strings);
                            spinner.setSelection(retval);
                        } else {
                            times.add(strings);
                            int retval = times.indexOf(strings);
                            spinner.setSelection(retval);
                        }
                    } else {
                        CM.showToast(ViewOrderSummary.this, getString(R.string.validateCate));
                    }
                } else {
                    CM.showToast(ViewOrderSummary.this, getString(R.string.validateCate));
                }
            }
        });
        editTextTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String strings = (String) adapterView.getSelectedItem();
                CM.setSp(ViewOrderSummary.this, "serverTime", strings);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String strings = (String) adapterView.getSelectedItem();
        CM.setSp(ViewOrderSummary.this, "serverTime", strings);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void webCallRemoveCouponCode(String cartId) {
        try {
            VolleyIntialization v = new VolleyIntialization(ViewOrderSummary.this, true, true);
            WebService.getResponseForRemoveCoupon(v, cartId, new OnVolleyHandler() {
                @Override
                public void onVollySuccess(String response) {
                    if (isFinishing()) {
                        return;
                    }
                    MtplLog.i("WebCalls", response);
                    Log.e(TAG, response);
                    getResponseForRemoveCouponCode(response);

                }

                @Override
                public void onVollyError(String error) {
                    MtplLog.i("WebCalls", error);
                    if (CM.isInternetAvailable(ViewOrderSummary.this)) {
                        CM.showPopupCommonValidation(ViewOrderSummary.this, error, false);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void webCallCouponCode() {
        try {
            VolleyIntialization v = new VolleyIntialization(ViewOrderSummary.this, true, true);
            WebService.getResponseForCouPonCode(v, new OnVolleyHandler() {
                @Override
                public void onVollySuccess(String response) {
                    if (isFinishing()) {
                        return;
                    }
                    MtplLog.i("WebCalls", response);
                    Log.e(TAG, response);
                    getResponseForCouponCode(response);

                }

                @Override
                public void onVollyError(String error) {
                    MtplLog.i("WebCalls", error);
                    if (CM.isInternetAvailable(ViewOrderSummary.this)) {
                        CM.showPopupCommonValidation(ViewOrderSummary.this, error, false);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getResponseForRemoveCouponCode(String response) {
        String strResponseStatus = CM.getValueFromJson(WebServiceTag.WEB_STATUS, response);
        if (strResponseStatus.equalsIgnoreCase(WebServiceTag.WEB_STATUSFAIL)) {
            CM.showPopupCommonValidation(ViewOrderSummary.this, CM.getValueFromJson(WebServiceTag.WEB_STATUS_ERRORTEXT, response), false);
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            //  JSONObject jsonObject1 = new JSONObject(jsonObject.getString("ResponseObject"));

            if (jsonObject.getString("ResponseCode").equals("202")) {


                if (CM.isInternetAvailable(ViewOrderSummary.this)) {
                    if (jsonObject.get("ResponseObject").toString().contains("Coupon successfully removed.")) {
                        CM.showToast(ViewOrderSummary.this, jsonObject.getString("ResponseObject"));
                        webCallOrderSummery(CM.getSp(ViewOrderSummary.this, "customerId", "").toString(), CM.getSp(ViewOrderSummary.this, "cartId", "").toString());
                    } else {
                        CM.showToast(ViewOrderSummary.this, jsonObject.getString("ResponseObject"));
                    }
                } else {
                    CM.showToast(ViewOrderSummary.this, getString(R.string.internetMsg));
                }


            } else {
                CM.showToast(ViewOrderSummary.this, jsonObject.getString("ResponseObject").toString());
            }
        } catch (Exception e) {
            CM.showPopupCommonValidation(ViewOrderSummary.this, e.getMessage(), false);
        }
    }


    private void getResponseForCouponCode(String response) {
        String strResponseStatus = CM.getValueFromJson(WebServiceTag.WEB_STATUS, response);
        if (strResponseStatus.equalsIgnoreCase(WebServiceTag.WEB_STATUSFAIL)) {
            CM.showPopupCommonValidation(ViewOrderSummary.this, CM.getValueFromJson(WebServiceTag.WEB_STATUS_ERRORTEXT, response), false);
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("ResponseObject"));

            if (jsonObject.getString("ResponseCode").equals("202")) {
                if (jsonObject1.has("lblNoDiscountCoupons")) {

                    try {
                        CM.showToast(ViewOrderSummary.this, "No Coupon's Available");
                    } catch (Exception e) {
                    }

                } else {
                    CouponCodeModel model_main = CM.JsonParse(new CouponCodeModel(), jsonObject.getString("ResponseObject"));
                    couponCodeModelArrays = model_main.couponCodeModelArrays;
                    couponCodeModelArrays.size();
                    showPopup();
                }
            } else {
                CM.showToast(ViewOrderSummary.this, jsonObject.getString("ResponseObject").toString());
            }
        } catch (Exception e) {
            CM.showPopupCommonValidation(ViewOrderSummary.this, e.getMessage(), false);
        }
    }

    void showPopup() {

        list_dialog = new Dialog(ViewOrderSummary.this);
        list_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        list_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        list_dialog.setContentView(R.layout.diloglayout);
        list_dialog.getWindow().getAttributes().height = (int) (CM.getDeviceMetrics(ViewOrderSummary.this).heightPixels * .8);
        list_dialog.setCanceledOnTouchOutside(false);
        mRecyclerView = (ListView) list_dialog.findViewById(R.id.component_list);
        editText = (EditText) list_dialog.findViewById(R.id.edtSecPhoneNo);
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.item_listview, couponCodeModelArrays);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.check);
                if (cb.isChecked()) {
                    CM.showToast(ViewOrderSummary.this, "Check");
                }

            }
        });

        Button positiveButton = (Button) list_dialog.findViewById(R.id.positive_button);
        Button brnApplyCoupon = (Button) list_dialog.findViewById(R.id.btnApplay);
        brnApplyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CM.getSp(ViewOrderSummary.this, "check", "").equals("1") || !editText.getText().toString().equals("")) {

                    if (!CM.getSp(ViewOrderSummary.this, "checkDate", "").toString().equals("")) {
                        webCallFoodApplayCoupon(CM.getSp(ViewOrderSummary.this, "checkDate", "").toString());
                        list_dialog.dismiss();
                        CM.setSp(ViewOrderSummary.this, "check", "0");
                        CM.setSp(ViewOrderSummary.this, "checkDate", "");
                    } else {
                        if (!editText.getText().toString().equals("")) {
                            webCallFoodApplayCoupon(editText.getText().toString().trim());
                            list_dialog.dismiss();
                            CM.setSp(ViewOrderSummary.this, "check", "0");
                            CM.setSp(ViewOrderSummary.this, "checkDate", "");
                        } else {
                            CM.showToast(ViewOrderSummary.this, "Enter Coupon");
                        }
                    }
                } else {
                    CM.showToast(ViewOrderSummary.this, "Enter Code or Choose code ");
                }
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                list_dialog.dismiss();


            }
        });

        list_dialog.show();
    }


    public void webCallFoodApplayCoupon(String coupon) {
        try {
            VolleyIntialization v = new VolleyIntialization(ViewOrderSummary.this, false, false);


            WebService.getResponseForApplayCoupon(v, CM.getSp(ViewOrderSummary.this, "customerId", "").toString(), CM.getSp(ViewOrderSummary.this, "cartId", "").toString(), coupon, new OnVolleyHandler() {
                @Override
                public void onVollySuccess(String response) {
                    if (isFinishing()) {
                        return;
                    }
                    MtplLog.i("WebCalls", response);
                    Log.e(TAG, response);
                    getResponseForApplyCoupon(response);

                }

                @Override
                public void onVollyError(String error) {
                    MtplLog.i("WebCalls", error);
                    if (CM.isInternetAvailable(ViewOrderSummary.this)) {
                        CM.showPopupCommonValidation(ViewOrderSummary.this, error, false);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getResponseForApplyCoupon(String response) {
        String strResponseStatus = CM.getValueFromJson(WebServiceTag.WEB_STATUS, response);
        if (strResponseStatus.equalsIgnoreCase(WebServiceTag.WEB_STATUSFAIL)) {
            CM.showPopupCommonValidation(ViewOrderSummary.this, CM.getValueFromJson(WebServiceTag.WEB_STATUS_ERRORTEXT, response), false);
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);


            if (jsonObject.get("ResponseCode").toString().equals("202")) {

                if (CM.isInternetAvailable(ViewOrderSummary.this)) {
                    webCallOrderSummery(CM.getSp(ViewOrderSummary.this, "customerId", "").toString(), CM.getSp(ViewOrderSummary.this, "cartId", "").toString());
                } else {
                    CM.showToast(ViewOrderSummary.this, getString(R.string.internetMsg));
                }
            } else if (jsonObject.get("ResponseCode").toString().equals("425")) {
                CM.showToast(ViewOrderSummary.this, jsonObject.get("ResponseObject").toString());
            }


        } catch (Exception e) {
            CM.showPopupCommonValidation(ViewOrderSummary.this, e.getMessage(), false);
        }
    }

    public void showDialog(String txt, final Date orderTime1, final String orderDate1, Double maxOrd) {

        txt = txt.replace("#nMaxOrderAmtForWarning", String.valueOf(maxOrd));


        new AlertDialog.Builder(ViewOrderSummary.this)
                .setTitle(getString(R.string.app_name))
                .setMessage(txt)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CM.setSp(ViewOrderSummary.this, "serverTime", orderTime1);
                        CM.setSp(ViewOrderSummary.this, "serverDate", orderDate1);
                        CM.startActivity(ViewOrderSummary.this, ViewCustomerDetail.class);
                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
    }
}

