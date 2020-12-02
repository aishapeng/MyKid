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
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
    private ImageView imageView;
    private Button addDateBtn,addTimeBtn,locationBtn,completeBtn,removeImgBtn;
    private ImageButton imageBtn, clearLocationBtn;

    private String activityName,location,date,time,reporter, selectedLocation,newActivityName,newLocation,newDate,newTime,newReporter,reportImage,uriStr;
    private int reportID;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient mFusedLocationClient;

    private UUID id;
    private File photoFile;
    private Intent captureImageIntent;
    private static final int REQUEST_PHOTO = 1;
    private Uri uri;
    ReportViewModel reportViewModel;

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
        addDateBtn = view.findViewById(R.id.dateBtn);
        addTimeBtn = view.findViewById(R.id.timeBtn);
        locationBtn = view.findViewById(R.id.locationBtn);
        completeBtn=view.findViewById(R.id.completeBtn);
        imageBtn = view.findViewById(R.id.imageBtn);
        removeImgBtn=view.findViewById(R.id.removeImgBtn);
        imageView = view.findViewById(R.id.imageView);
        clearLocationBtn = view.findViewById(R.id.clearLocationBtn);
        reportViewModel= new ViewModelProvider(this).get(ReportViewModel.class);

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

        //create the instance of file object
        photoFile = getPhotoFile();
        PackageManager pm = getContext().getPackageManager();

        //create the camera services
        captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = photoFile != null && captureImageIntent.resolveActivity(pm) != null;

        imageBtn.setEnabled(canTakePhoto);

        if(reportImage == null) {
            removeImgBtn.setVisibility(View.GONE);
        }else{
            Picasso.get().load(reportImage).into(imageView);
            uri = Uri.parse(reportImage);
        }

        if(savedInstanceState != null){
            date=savedInstanceState.getString("date");
            time=savedInstanceState.getString("time");
            location=savedInstanceState.getString("location");
            if(savedInstanceState.getString("Uri")!=null){
                uri= Uri.parse(savedInstanceState.getString("Uri"));
                Picasso.get().load(uri).into(imageView);
                removeImgBtn.setVisibility(View.VISIBLE);
            }
            else{
                imageView.setImageBitmap(null);
                uri=null;
                removeImgBtn.setVisibility(View.GONE);
            }

            dateInputTxtView.setText(date);
            timeInputTxtView.setText(time);
            locationInputTxtView.setText(location);
        }

        addDateBtn.setOnClickListener(this);
        addTimeBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        completeBtn.setOnClickListener(this);
        removeImgBtn.setOnClickListener(this);
        clearLocationBtn.setOnClickListener(this);
        imageBtn.setOnClickListener(this);

        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getParentFragmentManager().setFragmentResultListener("location", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                selectedLocation = bundle.getString("location");
                locationInputTxtView.setText(selectedLocation);
            }
        });



        return view;
    }

    @Override
    public void onClick(View view) {
        DialogFragment newFragment;
        switch (view.getId()){
            case R.id.dateBtn:
                newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(), "datePickerEditFrag");
                break;

            case R.id.timeBtn:
                newFragment = new TimePickerFragment();
                newFragment.show(getChildFragmentManager(),"timePickerEditFrag");
                break;

            case R.id.locationBtn:
                if (location == null){
                    getLocation(); //get user current location
                }else{
                        ((MainActivity)getActivity()).openMap(null, location, "true");
                }
                break;

            case R.id.clearLocationBtn:
                locationInputTxtView.setText("");
                break;

            case R.id.imageBtn:
                uri = FileProvider.getUriForFile(getActivity(),
                        "com.example.mykid.fileprovider",
                        photoFile);

                //start launch the camera service with file path
                captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                //permission
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
                break;

            case R.id.completeBtn:
                newActivityName=actNameEditTxt.getText().toString();
                newLocation=locationInputTxtView.getText().toString();
                newDate=dateInputTxtView.getText().toString();
                newTime=timeInputTxtView.getText().toString();
                newReporter=reporterNameEditTxt.getText().toString();
                if(newActivityName.isEmpty()){
                    actErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    actErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newDate.isEmpty()){
                    dateErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    dateErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newTime.isEmpty()){
                    timeErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    timeErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newReporter.isEmpty()){
                    reporterErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    reporterErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newLocation.isEmpty()){
                    newLocation=null;
                }
                String attached;
                if(uri==null || imageView.getDrawable() == null){
                    uriStr=null;
                    attached = "No image taken";
                }else{
                    uriStr=uri.toString();
                    attached = "Attached";
                }
                if(!newActivityName.isEmpty() && !newDate.isEmpty() &&!newTime.isEmpty() && !newReporter.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
                    builder.setTitle("Confirmation")
                            .setMessage("Are you sure you want to edit an report with these details?\n" +
                                    "\n" +
                                    "Activity Name : " + newActivityName + "\n" +
                                    "\n" +
                                    "Location : " + newLocation + "\n" +
                                    "\n" +
                                    "Date : " + newDate + "\n" +
                                    "\n" +
                                    "Time : " + newTime + "\n" +
                                    "\n" +
                                    "Reporter : " + newReporter + "\n" +
                                    "\n" +
                                    "Image : " + attached)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Report report= new Report();
                                    report.setReportId(reportID);
                                    report.setReportName(newActivityName);
                                    report.setReportLocation(newLocation);
                                    report.setReportDate(newDate);
                                    report.setReportTime(newTime);
                                    report.setReporterName(newReporter);
                                    report.setReportImage(uriStr);
                                    reportViewModel.update(report);

                                    getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

    public void getLocation() { //check for the ACCESS_FINE_LOCATION permission.
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
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

    @Override
    //request permission, then now chk permission result with this function
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,otherwise, show a Toast
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
        ((MainActivity)getActivity()).openMap(result, null,"true"); //open google map
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
            imageView.setImageDrawable(null);
        else
        {
            Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(),
                    getActivity());
            imageView.setImageBitmap(bitmap);
            removeImgBtn.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("date",dateInputTxtView.getText().toString());
        outState.putString("time",timeInputTxtView.getText().toString());
        outState.putString("location",locationInputTxtView.getText().toString());
        if(uri!=null){
            outState.putString("Uri",uri.toString());
        }
    }
}