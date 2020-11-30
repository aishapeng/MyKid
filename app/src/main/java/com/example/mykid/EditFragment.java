package com.example.mykid;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class EditFragment extends Fragment implements FetchAddressTask.OnTaskCompleted, View.OnClickListener{

    private TextView dateInputTxtView,timeInputTxtView,locationInputTxtView,actErrorMsg,dateErrorMsg,timeErrorMsg,reporterErrorMsg,imageTxtView;
    private EditText actNameEditTxt,reporterNameEditTxt;
    ReportViewModel reportViewModel;
    private String activityName,location,date,time,reporter, selectedlocation;
    private int reportID;
    private String newactivityName,newlocation,newdate,newtime,newreporter,reportImage;
    private ImageView imageView;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient mFusedLocationClient;

    private UUID id;
    private ImageButton imageBtn, clearLocationBtn;
    private File photoFile;
    private Intent captureImageIntent;
    private static final int REQUEST_PHOTO = 1;
    private Uri uri;
    String uriStr;
    Button removeImgBtn;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_edit, container, false);
        dateInputTxtView = view.findViewById(R.id.dateInputTxtView);
        timeInputTxtView = view.findViewById(R.id.timeInputTxtView);
        actNameEditTxt= view.findViewById(R.id.actNameEditTxt);
        imageTxtView = view.findViewById(R.id.imageTxtView);
        reporterNameEditTxt=view.findViewById(R.id.reporterNameEditTxt);
        locationInputTxtView=view.findViewById(R.id.locationInputTxtView);
        actErrorMsg=view.findViewById(R.id.actErrorMsg);
        dateErrorMsg=view.findViewById(R.id.dateErrorMsg);
        timeErrorMsg=view.findViewById(R.id.timeErrorMsg);
        reporterErrorMsg=view.findViewById(R.id.reporterErrorMsg);
        reportViewModel= new ViewModelProvider(this).get(ReportViewModel.class);
        Button addDateBtn = view.findViewById(R.id.dateBtn);
        Button addTimeBtn = view.findViewById(R.id.timeBtn);
        Button locationBtn = view.findViewById(R.id.locationBtn);
        Button completeBtn=view.findViewById(R.id.completeBtn);
        imageBtn = view.findViewById(R.id.imageBtn);
        removeImgBtn=view.findViewById(R.id.removeImgBtn);
        imageView = view.findViewById(R.id.imageView);
        clearLocationBtn = view.findViewById(R.id.clearLocationBtn);

        Bundle bundle = this.getArguments();
        reportID = bundle.getInt("ReportID");
        activityName= bundle.getString("activityName");
        location=bundle.getString("location");
        date=bundle.getString("date");
        time=bundle.getString("time");
        reporter=bundle.getString("reporter");
        reportImage = bundle.getString("image");

        actNameEditTxt.setText(activityName);
        locationInputTxtView.setText(location);
        dateInputTxtView.setText(date);
        timeInputTxtView.setText(time);
        reporterNameEditTxt.setText(reporter);

        addDateBtn.setOnClickListener(this);
        addTimeBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        completeBtn.setOnClickListener(this);
        removeImgBtn.setOnClickListener(this);
        clearLocationBtn.setOnClickListener(this);

        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getParentFragmentManager().setFragmentResultListener("location", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                selectedlocation = bundle.getString("location");
                locationInputTxtView.setText(selectedlocation);
                // Do something with the result
            }
        });

        //create the instance of file object
        photoFile = getPhotoFile();
        PackageManager pm = getContext().getPackageManager();

        //create the camera services
        captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = photoFile != null &&
                captureImageIntent.resolveActivity(pm) != null;

        imageBtn.setEnabled(canTakePhoto);
        imageBtn.setOnClickListener(this);

        if(reportImage == null) {
            removeImgBtn.setVisibility(View.GONE);
        }else{
            Picasso.get().load(reportImage).into(imageView);
            uri = Uri.parse(reportImage);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        DialogFragment newFragment;
        switch (view.getId()){
            case R.id.dateBtn:
                newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(), "datePickerEditFrag"); //getsupportmanager to show, tag is identifier
                break;

            case R.id.timeBtn:
                newFragment = new TimePickerFragment();
                newFragment.show(getChildFragmentManager(),"timePickerEditFrag");
                break;

            case R.id.locationBtn:
                if (location == null){
                    getLocation(); //get user current location
                }else{
                    ((SecondActivity)getActivity()).openMap(null, location, "true");
//                    Fragment frag = new GoogleMapFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("currentLocation", location);
//                    bundle.putString("selectedLocation", null);
//                    bundle.putString("editable", "false");
//                    frag.setArguments(bundle);
//                    getParentFragmentManager().beginTransaction().add(R.id.fragment_sec, frag).addToBackStack(null).commit();
//                    //transaction.addToBackStack(null);
                }
                break;

            case R.id.clearLocationBtn:
                locationInputTxtView.setText("");
                Log.d("ClrBtnClick: ", "Clicked");
                break;

            case R.id.imageBtn:
                uri = FileProvider.getUriForFile(getActivity(),
                        "com.example.mykid.fileprovider",
                        photoFile);

                //check the file location
                Log.d("FILE-URI", uri.toString());

                //start launch the camera service with file path
                captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                //We have to settle the permission problem
                //Check the return value from captureImageIntent

                List<ResolveInfo> cameraActivities =
                        getActivity().getPackageManager().queryIntentActivities(captureImageIntent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                //solve each activity
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                //start the camera services
                startActivityForResult(captureImageIntent, REQUEST_PHOTO);
                break;

            case R.id.removeImgBtn:
                imageView.setImageBitmap(null);
                uri=null;
                removeImgBtn.setVisibility(View.GONE);
                Log.d("removeImage: ", ""+ uri);
                break;


            case R.id.completeBtn:
                newactivityName=actNameEditTxt.getText().toString();
                newlocation=locationInputTxtView.getText().toString();
                newdate=dateInputTxtView.getText().toString();
                newtime=timeInputTxtView.getText().toString();
                newreporter=reporterNameEditTxt.getText().toString();
                if(newactivityName.isEmpty()){
                    actErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    actErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newdate.isEmpty()){
                    dateErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    dateErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newtime.isEmpty()){
                    timeErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    timeErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newreporter.isEmpty()){
                    reporterErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    reporterErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newlocation.isEmpty()){
                    newlocation=null;
                }
                String attached;
                if(uri==null){
                    uriStr=null;
                    attached = "No image taken";
                }else{
                    uriStr=uri.toString();
                    attached = "Attached";
                }
                Log.d("removeImage: uristr ", ""+ uriStr);
                if(!newactivityName.isEmpty() && !newdate.isEmpty() &&!newtime.isEmpty() && !newreporter.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
                    builder.setTitle("Confirmation")
                            .setMessage("Are you sure you want to edit an activity with these details?\n" +
                                    "\n" +
                                    "Activity Name : " + newactivityName + "\n" +
                                    "\n" +
                                    "Location : " + newlocation + "\n" +
                                    "\n" +
                                    "Date : " + newdate + "\n" +
                                    "\n" +
                                    "Time : " + newtime + "\n" +
                                    "\n" +
                                    "Reporter : " + newreporter + "\n" +
                                    "\n" +
                                    "Image : " + attached)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Report report= new Report();
                                    report.setReportId(reportID);
                                    report.setReportName(newactivityName);
                                    report.setReportLocation(newlocation);
                                    report.setReportDate(newdate);
                                    report.setReportTime(newtime);
                                    report.setReporterName(newreporter);
                                    report.setReportImage(uriStr);
                                    reportViewModel.update(report);

//                                    AppCompatActivity activity= (AppCompatActivity)getContext();
//                                    DetailFragment detailFragment= new DetailFragment();
//                                    Bundle arguments = new Bundle();
//                                    arguments.putInt("ReportID", report.getReportId());
//                                    arguments.putString("activityName",report.getReportName());
//                                    arguments.putString("location",report.getReportLocation());
//                                    arguments.putString("date",report.getReportDate());
//                                    arguments.putString("time",report.getReportTime());
//                                    arguments.putString("reporter",report.getReporterName());
//                                    detailFragment.setArguments(arguments);
//                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,detailFragment).addToBackStack(null).commit();

//                                    Intent intent = new Intent (getActivity(), MainActivity.class);
//                                    startActivity (intent);

                                    //getActivity().startActivity(intent);
                                    if(MainActivity.DUAL_FRAME){
                                        Intent intent = new Intent (getActivity(), MainActivity.class);
                                        startActivity (intent);
                                        //getActivity().onBackPressed();
                                    }
                                    else{
                                        getActivity().onBackPressed();
                                    }


                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .show();
                }
                break;

            default:
                break;
        }
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1); // bc start from 0
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String date = ( day_string+"/"  +month_string+ "/" + year_string);

        dateInputTxtView.setText(date);
    }

    public void processTimePickerResult(int hourOfDay, int minute) {
        String timeMessage = (String.format("%02d:%02d",hourOfDay , minute));

        timeInputTxtView.setText(timeMessage);
    }


    //standard code
    public void getLocation() { //check for the ACCESS_FINE_LOCATION permission.
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] // [] can pass more than one permission at the same time//??
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                new FetchAddressTask(getActivity(), EditFragment.this).execute((location));
                            }
                        }
                    });
        }
    }

    //standard code
    @Override
    //request permission, then now chk permission result with this function
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation(); // permit le can get location le
                } else {
                    Toast.makeText(getActivity(),
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        ((SecondActivity)getActivity()).openMap(result, null,"true"); //open google map
    }

    //setup methods to get file name and file location
    public String getPhotoFileName() {
        String fileName="";
        id = UUID.randomUUID();

        fileName = "IMG_" + id.toString() + ".jpg";
        Log.d("FILE", fileName);
        return fileName;
    }

    //get the file path
    public File getPhotoFile() {
        //construct the file object
        File fileDir = getActivity().getFilesDir();
        return new File(fileDir, getPhotoFileName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        //other implementation

        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_PHOTO) {
            //retrieve back the file from file system
            uri = FileProvider.getUriForFile(getContext(),
                    "com.example.mykid.fileprovider", photoFile);

            getContext().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    private void updatePhotoView() {
        if (photoFile == null || !photoFile.exists())
            imageBtn.setImageDrawable(null);
        else
        {
            Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(),
                    getActivity());
            imageView.setImageBitmap(bitmap);

            //Picasso.get().load(uri).into(imageView);
            removeImgBtn.setVisibility(View.VISIBLE);
        }

    }


}