package com.app.outlook.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.app.outlook.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Util {
    public static final String LANGUAGE_CODE = Locale.getDefault().getLanguage().toLowerCase();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private Util() {
    }

    public static void showKeyboard(Context cntxt, EditText text, boolean show) {
        InputMethodManager imm = (InputMethodManager) cntxt.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show)
            imm.showSoftInput(text.getRootView(), 0);
        else
            imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
    }

    /**
     * Change date format yyyy-MM-dd to dd-MMM-yyyy
     */
    public static String changeFormatYMDtoDMY(String selected_date) {
        SimpleDateFormat dfDate = new SimpleDateFormat("ddMMMyy");
        String str_date = null;
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date varDate = dateformat.parse(selected_date);
            str_date = dfDate.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_date;

    }

    public static boolean checkFiledownLoaded(String root,String magazineID,String issueID){

        String mPath =  root + File.separator + "Outlook/Magazines/"+magazineID+"-magazine-" + issueID + ".json";
        File file = new File(mPath);
        if (file.exists()) {
            return true;
        }else {
            return false;
        }
    }

    public static String unixTimetoDate(String unix_time) {
        long unix_seconds = Long.parseLong(unix_time, 10);
        SimpleDateFormat dfDate = new SimpleDateFormat("ddMMMyy-HHmm");
        Date date = new Date(unix_seconds);
        String date_time = null;
        dfDate.setTimeZone(TimeZone.getDefault());
        date_time = dfDate.format(date);
        return date_time;
    }

    public static String unixTimePushMsg(long unix_time) {
        String time = "";
        //long unix_seconds=Long.parseLong(unix_time, 10);
        SimpleDateFormat dfDate = new SimpleDateFormat("ddMMMyy-HHmm");
        Date date = new Date(unix_time);

        String date_time = null;
        dfDate.setTimeZone(TimeZone.getDefault());
        date_time = dfDate.format(date);
        if (date_time.contains("-")) {
            String[] dm_date_time = date_time.split("-");
            String date1 = dm_date_time[0];
            time = dm_date_time[1];
        }
        return time;
    }

    public static String md5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(s.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
            // Create MD5 Hash
           /* MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();*/

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * get foreground activity
     */
    public static String currentActivity(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        // get the info from the currently running task
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        String topActivity = taskInfo.get(0).topActivity.getClassName();
        Log.d("topActivity", "::"
                + taskInfo.get(0).topActivity.getClassName());

        return topActivity;

    }

    public static Bitmap getBitmapFromURL(String strURL, Context context) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            //e.printStackTrace();
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_back);
        }
    }

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public static Bitmap getRoundedBitmap(Bitmap bitmap, int radius, int margin) {

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawRoundRect(new RectF(margin, margin, bitmap.getWidth() - margin, bitmap.getHeight() - margin), radius, radius, paint);

        if (bitmap != output) {
            bitmap.recycle();
        }

        return output;
    }

    public static String packageName(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        // get the info from the currently running task
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        String packageName = taskInfo.get(0).topActivity.getPackageName();
        Log.d("package", "::"
                + taskInfo.get(0).topActivity.getPackageName());

        return packageName;

    }

    public static String getDeviceImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static String getRealPathFromURI(Activity context, Uri contentUri) {

        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static boolean isEmptyString(String user) {
        if (user == null) return true;
        if (user.trim().length() == 0) return true;
        return false;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static int dipToPixels(Context context, int dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static boolean hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View f = activity.getCurrentFocus();
        if (null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom(f.getClass())) {
            imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
            return true;
        } else {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            return false;
        }
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * current time
     */
    public static String currentTime() {
        Calendar cal;
        int mHour;
        int mMinute;
        cal = Calendar.getInstance();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        // String currentTime = Utils.updateTime(mHour, mMinute);
        return mHour + mMinute + "";

    }

    /**
     * current date
     */
    public static String currentDate() {
        Calendar cal;
        int day;
        int month;
        int year;
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        String currentDate = checkDigit(day) + "-"
                + checkDigit(month + 1) + "-" + year;
        return currentDate;

    }

    /**
     * Change date format dd-MM-yyyy to dd-MMM-yyyy
     */
    public static String changeFormatDMY(String selected_date) {
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy");
        String str_date = null;
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
            Date varDate = dateformat.parse(selected_date);
            str_date = dfDate.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_date;

    }

    public static String getTime() {
        Calendar cal;
        cal = Calendar.getInstance();
        String timemili = cal.getTimeInMillis() + "";
        String time = cal.getTime().toString();
        Log.i("time", time + "::mili::" + timemili);
        return timemili;
    }

    /**
     * Adding '0' if single digit
     */
    public static String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public static boolean isNetworkOnline(Context context) {
        boolean status = false;
        try {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void ExportDatabaseToSD(final Context ctx) {

        class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
            private final ProgressDialog dialog = new ProgressDialog(ctx);

            // can use UI thread here
            protected void onPreExecute() {
                this.dialog.setMessage("Exporting database...");
                this.dialog.show();
            }

            // automatically done on worker thread (separate from UI thread)
            protected Boolean doInBackground(final String... args) {

                File dbFile =
                        new File(Environment.getDataDirectory() + "/data/com.chatapp/databases/socialapp.db");

                File exportDir = new File(Environment.getExternalStorageDirectory(), "");
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }
                File file = new File(exportDir, dbFile.getName());

                try {
                    file.createNewFile();
                    this.copyFile(dbFile, file);
                    return true;
                } catch (IOException e) {
                    Log.e("mypck", e.getMessage(), e);
                    return false;
                }
            }

            // can use UI thread here
            protected void onPostExecute(final Boolean success) {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
                if (success) {
                    Toast.makeText(ctx, "Export successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ctx, "Export failed", Toast.LENGTH_SHORT).show();
                }
            }

            void copyFile(File src, File dst) throws IOException {
                FileChannel inChannel = new FileInputStream(src).getChannel();
                FileChannel outChannel = new FileOutputStream(dst).getChannel();
                try {
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                } finally {
                    if (inChannel != null)
                        inChannel.close();
                    if (outChannel != null)
                        outChannel.close();
                }
            }

        }

        new ExportDatabaseFileTask().execute();
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static String getTimefromMilli(long millis) {

        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    //Params:
    // jsonResponse: pass directly new Gson().fromJson here.
    //path: Folder path to store json
    public static void saveJsonToSDCard(final String jsonResponse, final String path) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(path);
                    if (file.exists())
                        file.delete();

                    file.createNewFile();
                    file.setReadable(true);
                    file.setExecutable(true);
                    file.setWritable(true);

                    FileOutputStream fOut = new FileOutputStream(file);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    myOutWriter.append(jsonResponse);
                    myOutWriter.close();
                    fOut.close();
                } catch (Exception e) {
                }
            }
        });
        thread.start();
    }

    //Params:
    // path: Path  to the folder without "/"
    //fileName: Name of the File
    public static String readJsonFromSDCard(String path) {

        try {

            File jsonPath = new File(path);

            if (jsonPath.exists()) {
                FileInputStream fIn = new FileInputStream(jsonPath);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));

                String aDataRow = "";
                StringBuffer aBuffer = new StringBuffer();
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer.append(new String(aDataRow + "\n"));

                }
                Log.d("BRAND", "Buffer::" + aBuffer);

                return aBuffer.toString();

            } else {
                Log.d("Utils", "File Not Found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    //Params:
    // thumbnailsPath: Path  to the folder without "/"
    //thumbnailsImage: Name of the image
    private void downloadImagesToSdCard(final File thumbnailsPath, final String thumbnailImage, Context context) {


        Target mTarget = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                // Perform simple file operation to store this bitmap to your sd card
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File file = new File(thumbnailsPath + "/" + thumbnailImage.replace("model_thumbnails/", ""));
                        Log.d("Util", "File::" + file);
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("Util", "onBitmapFailed::" + errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d("Util", "onPrepareLoad::");
            }
        };

        Picasso.with(context).load(Uri.parse("MEDIA_URL" + thumbnailImage)).into(mTarget);
    }

    public static final class Image {

        public static final int DEFAULT_BUFFER_SIZE = 8192;
        public static int IMAGE_MAX_SIZE = 0;

        public static String getMediaStorePath(ContentResolver cr, Uri uri) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            String path = "";
            try {
                cursor = cr.query(uri, projection, null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    path = cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return path;
        }

        public static Bitmap decodeFile(File f, int maxSize) {
            Bitmap b = null;
            try {
                // Decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;

                FileInputStream fis = new FileInputStream(f);
                BitmapFactory.decodeStream(fis, null, o);
                fis.close();

                int scale = 1;
                if (o.outHeight > maxSize || o.outWidth > maxSize) {
                    scale = (int) Math.pow(
                            2,
                            (int) Math.round(Math.log(maxSize
                                    / (double) Math
                                    .max(o.outHeight, o.outWidth))
                                    / Math.log(0.5)));
                }

                // Decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                fis = new FileInputStream(f);
                b = BitmapFactory.decodeStream(fis, null, o2);
                fis.close();
            } catch (IOException e) {
                Log.e("Util", e.toString());
            }
            return b;
        }

        public static String storeBitmap(Context context, Uri url)
                throws Exception {
            File f = getTempFile(context);
            OutputStream os = null;

            try {
                InputStream is = null;
                if (url.toString().startsWith(
                        "content://com.google.android.gallery3d")
                        || url.toString().startsWith(
                        "content://com.android.gallery3d")) {
                    is = context.getContentResolver().openInputStream(url);
                } else {
                    is = new URL(url.toString()).openStream();
                }

                os = new FileOutputStream(f);
                copy(is, os);

                os.close();
            } finally {
                if (os != null)
                    os.close();
            }
            return f.getAbsolutePath();
        }

        public static int copy(InputStream input, OutputStream output)
                throws IOException {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int count = 0;
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
            return count;
        }

        public static Bitmap getBitmap(Context context, Uri imageUri) {
            context.getContentResolver().notifyChange(imageUri, null);
            ContentResolver cr = context.getContentResolver();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(cr,
                        imageUri);
            } catch (Exception e) {
            }
            return bitmap;
        }


        // decodes image and scales it to reduce memory consumption
        public static Bitmap decodeFile(String imageFilePath) {

            Uri uri1 = Uri.parse("file://" + imageFilePath);
            File f = new File(imageFilePath);

            Bitmap b = null;
            try {
                // Decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;

                FileInputStream fis = new FileInputStream(f);
                BitmapFactory.decodeStream(fis, null, o);
                fis.close();

                int scale = 1;
                if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                    scale = (int) Math.pow(
                            2,
                            (int) Math.round(Math.log(IMAGE_MAX_SIZE
                                    / (double) Math
                                    .max(o.outHeight, o.outWidth))
                                    / Math.log(0.5)));
                }

                // Decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                fis = new FileInputStream(f);
                b = BitmapFactory.decodeStream(fis, null, o2);
                fis.close();
            } catch (IOException e) {
            }
            return b;
        }

        public static String fileExtToMimeType(String path) {
            // File extension
            int pos = path.lastIndexOf(".");
            String ext = path.substring(pos + 1, path.length());

            // Mime tpe form extension
            MimeTypeMap map = MimeTypeMap.getSingleton();
            String mimeType = map.getMimeTypeFromExtension(ext);

            return mimeType;
        }


        public static String getRealPathFromURI(Context context, Uri contentUri) {

            // request only the image ID to be returned
            String[] proj = {MediaStore.Images.Media.DATA};
            // Create the cursor pointing to the SDCard
            Cursor cursor = context.getContentResolver().query(contentUri,
                    proj,
                    null,
                    null,
                    null);

            //String[] proj = { (fileType == Consts.CLIP_FILETYPE_IMAGE ) ? MediaStore.Images.Media.DATA : MediaStore.Audio.Media.DATA };
            //Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null,null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;

        }


        public static File getTempFile(Context context) throws Exception {
            File cacheDir;

            // if the device has an SD card
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                cacheDir = new File(
                        Environment.getExternalStorageDirectory()
                                + "/" + context.getString(R.string.app_name), "/temp/");
            } else {
                // it does not have an SD card
                cacheDir = context.getCacheDir();
            }
            if (!cacheDir.exists()) {
                boolean success = cacheDir.mkdirs();
                if (!success) {
                    throw new Exception("Not create cache dir");
                }
            }

            File f = new File(cacheDir, "temp.jpg");
            return f;
        }

        public static File getTempImageFile(Context context, String fileName) throws Exception {
            File cacheDir;

            // if the device has an SD card
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                cacheDir = new File(
                        Environment.getExternalStorageDirectory() +
                                "/" + context.getString(R.string.app_name), "/temp/");
            } else {
                // it does not have an SD card
                cacheDir = context.getCacheDir();
            }
            if (!cacheDir.exists()) {
                boolean success = cacheDir.mkdirs();
                if (!success) {
                    throw new Exception("Not create cache dir");
                }
            }

            File f = new File(cacheDir, fileName);//"tempimage.jpg");
            return f;
        }

        public static void saveBitmap(File file, Bitmap bmp) {
            try {
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    public static boolean checkPlayServices(Activity context) {
        GoogleApiAvailability
                apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                //Log.i(TAG, "This device is not supported.");
                //finish();
            }
            return false;
        }
        return true;
    }

}
