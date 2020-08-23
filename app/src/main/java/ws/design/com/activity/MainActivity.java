package ws.design.com.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;
import com.linheimx.app.library.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ws.design.com.R;
import ws.design.com.data.model.Graph;
import ws.design.com.data.model.Point;
import ws.design.com.data.model.SOAnswersResponse;
import ws.design.com.data.remote.ApiUtils;
import ws.design.com.data.remote.SOService;

public class MainActivity extends AppCompatActivity {

    //private LineChart mChart,mChart1;
    LineChart _lineChart, _lineChart1;
    //private int LINE_NUM=0;
    private Uri mImageCaptureUri;
    ImageView banar1;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;


    private List<Graph> mItems;
    private List<Point> mPoints;
    private SOService mService;


    private static ArrayList<Entry> xValue = new ArrayList<>();
    private static ArrayList<Entry> yValue = new ArrayList<>();
    private static ArrayList<Entry> zValue = new ArrayList<>();
//    ArrayList<String> xAXES = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] web = MainActivity.this.getResources().getStringArray(R.array.nav_drawer_labels);
        Integer[] imageId = {
                R.drawable.ic_home,
                R.drawable.ic_tv,
                R.drawable.ic_ticket,
                R.drawable.ic_offers,
                R.drawable.ic_earning,
                R.drawable.ic_usemoney,
                R.drawable.ic_ranking,
                R.drawable.ic_faq,
                R.drawable.ic_symbol21,
        };
        CustomList adapter1 = new CustomList(MainActivity.this, web, imageId);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter1);
        final String[] items = new String[]{"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { //pick from camera
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { //pick from file
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        });
        final AlertDialog dialog = builder.create();
        banar1 = (ImageView) findViewById(R.id.banar1);
        banar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);


//--------------------------------------------------------------------------------------start graph code -------------------------------------------------
//        mChart = (LineChart) findViewById(R.id.chart1);
//        mChart1 = (LineChart) findViewById(R.id.chart);
        _lineChart = (LineChart) findViewById(R.id.chart);
        _lineChart1 = (LineChart) findViewById(R.id.chart1);

        mService = ApiUtils.getSOService();
        progressDialog= new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading data...");
        loadAnswers();
        setChartData(_lineChart, 3);
//--------------------------------------------------------------------------------------end graph code------------------------------------------------
    }

    private void setChartData(LineChart lineChart, int lineCount) {
        HighLight highLight = lineChart.get_HighLight1();
        highLight.setEnable(true);
        highLight.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "X:" + value;
            }
        });
        highLight.setyValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "Y:" + Math.round(value);
            }
        });
        XAxis xAxis = lineChart.get_XAxis();
        xAxis.set_unit("v");
        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("m");
        Lines lines = new Lines();
        for (int i = 0; i < lineCount; i++) {
            int color = Color.argb(255,
                    (new Double(Math.random() * 256)).intValue(),
                    (new Double(Math.random() * 256)).intValue(),
                    (new Double(Math.random() * 256)).intValue());

            Line line = createLine(i, color);
            line.setFilled(true);
            lines.addLine(line);
        }
        lineChart.setLines(lines);
    }


    private Line createLine(int order, int color) {

        final Line line = new Line();
        List<Entry> list = new ArrayList<>();
//
//        Random random = new Random();
//        for (int i = 0; i < 10 + order; i++) {
//            double x = i;
//            double y = random.nextDouble() * 100;
//            list.add(new Entry(x, y));
//        }

        if (order == 0) list = xValue;
        if (order == 1) list = yValue;
        if (order == 2) list = zValue;


        line.setEntries(list);
        line.setDrawLegend(true);//设置启用绘制图例
        line.setLegendWidth((int) Utils.dp2px(60));//设置图例的宽
//        line.setLegendHeight((int)Utils.dp2px(60));//设置图例的高
//        line.setLegendTextSize((int)Utils.dp2px(19));//设置图例上的字体大小
        line.setName("_line:" + order);
        line.setLineColor(color);
        //line.setOnEntryClick(onEntryClick);
        return line;
    }

    public void loadAnswers() {
        if (xValue != null && !xValue.isEmpty()&&yValue != null && !yValue.isEmpty()&&zValue != null && !zValue.isEmpty()) {
            xValue.clear();yValue.clear();zValue.clear();
        }
        progressDialog.show();
        mService.getAnswers().enqueue(new Callback<SOAnswersResponse>() {
            @Override
            public void onResponse(Call<SOAnswersResponse> call, Response<SOAnswersResponse> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    mItems = response.body().getItems();
                    for (Graph s : mItems) {
                        mPoints = s.getPoints();
                        //LINE_NUM = s.getLines();
                        for (int i = 0; i < mPoints.size(); i++) {
                            Log.d("AnswersPresenter", "-----------" + "" + "----------" + mPoints.get(i).getAxis_value().getX() + "---------------------");
                            xValue.add(new Entry(i, mPoints.get(i).getAxis_value().getX()));
                            yValue.add(new Entry(i, mPoints.get(i).getAxis_value().getY()));
                            zValue.add(new Entry(i, mPoints.get(i).getAxis_value().getZ()));
                        }
                        break;
                    }

                } else {
                    int statusCode = response.code();
                    Log.d("AnswersPresenter", "----------------------handle request errors" + statusCode + "---------------------");
                }
            }

            @Override
            public void onFailure(Call<SOAnswersResponse> call, Throwable t) {
                //showErrorMessage();
                Log.d("AnswersPresenter", "error loading from API");

            }
        });
    }


    public void showErrorMessage() {
        Toast.makeText(this, "Error loading posts", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();

                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();

                doCrop();

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");

                    banar1.setImageBitmap(photo);
                }

                File f = new File(mImageCaptureUri.getPath());

                if (f.exists()) f.delete();

                break;

        }
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            getContentResolver().delete(mImageCaptureUri, null, null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }

    public class CustomList extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] web;
        private final Integer[] imageId;

        public CustomList(Activity context,
                          String[] web, Integer[] imageId) {
            super(context, R.layout.nav_drawer_row, web);
            this.context = context;
            this.web = web;
            this.imageId = imageId;

        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();


            View rowView = inflater.inflate(R.layout.nav_drawer_row, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);


            ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
            txtTitle.setText(web[position]);

            imageView.setImageResource(imageId[position]);
            return rowView;
        }
    }
    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }*/
}