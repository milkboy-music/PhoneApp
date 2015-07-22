package jp.example.owner.jp.phoneappli;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    EditText editText;

    private String[] number = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};//セットする値＋asterisk,sharp

    boolean flag = false;//ヴァイブOn/Offのフラグ

    //カスタムボタンのbitmap
    List<Bitmap> listBitmap = new ArrayList();
    SharedPreferences sharedPreferences;
    boolean check_preference;//プリファレンス

    //ディスプレイ明るさ
    LayoutInflater inflater;
    View layout;
    SeekBar seek;
    WindowManager.LayoutParams lp;
    static int seekValue=10;

    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //オプションメニューを有効にする
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main_activity, container, false);
    }

    //optionmenu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //インテントの宣言
        Intent i;
        int id = item.getItemId();


        switch (id) {
            case R.id.action_PhoneBook:
                i = new Intent("android.intent.action.VIEW", Uri.parse("content://contacts/people/"));//電話帳を開く
                startActivity(i);
                break;
            case R.id.action_ResisterBook:

                i = new Intent(ContactsContract.Intents.Insert.ACTION);//電話帳に番号を登録する
                i.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                i.putExtra(ContactsContract.Intents.Insert.PHONE, editText.getText().toString());
                startActivity(i);
                break;

            case R.id.action_SearchOnWeb://電話番号ネット検索
                String url = editText.getText().toString();
                i = new Intent(Intent.ACTION_WEB_SEARCH);
                i.putExtra(SearchManager.QUERY, url);
                startActivity(i);
                break;
            //バイブレーション
            case R.id.action_vibration:

                if (item.isChecked() == false) {

                    item.setChecked(true);
                    flag = true;
                    startVib();
                    Toast.makeText(getActivity(), "バイブレーションのON", Toast.LENGTH_SHORT).show();
                } else if (item.isChecked() == true) {
                    item.setChecked(false);
                    flag = false;
                    stopVib();
                    Toast.makeText(getActivity(), "バイブレーションをOFF", Toast.LENGTH_SHORT).show();

                }

                break;
            //画面の明るさ調整
            case R.id.action_Display:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("yuyuyuyu").setView(layout);
                builder.setNeutralButton("OK!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    }
                });
                builder.show();
                break;
            //オリジナルボタンの実装
            case R.id.action_CustomButton:
                i = new Intent(getActivity(), PreferenceActivity.class);

                startActivityForResult(i, 0);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startVib() {//バイブスタート
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(80L);

    }

    private void stopVib() {//バイブストップ
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();
    }

    public void setVib() {//バイブレーションの作動条件
        if (flag == true) {
            startVib();
        } else if (flag) {
            stopVib();

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //1 count=文字数　2 editText=数字入力 3 デリートボタン
        final TextView textView_count = (TextView) getActivity().findViewById(R.id.textView);
        editText = (EditText) getActivity().findViewById(R.id.et_phone);
        final ImageButton btn_delete = (ImageButton) getActivity().findViewById(R.id.btn_delete);
        View img_phone = getActivity().findViewById(R.id.img_phone);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int txtLength = s.length();
                String co = getString(R.string.text_mojicount);
                textView_count.setText(String.valueOf(txtLength) + co);
                btn_delete.setVisibility(View.VISIBLE);
                setVib();//バイブレーション


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //電話ボタン　インテント
        img_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")) {
                    //sharp=Uri.encode("#");
                    Uri numbers = Uri.parse(String.format("tel:%s", Uri.encode(editText.getText().toString())));//Uri.encodeで#の記号を変換!!
                    Intent i = new Intent(Intent.ACTION_DIAL, numbers);
                    setVib();//バイブレーション
                    startActivity(i);
                    //# ♯ # ＃　♯
                } else {

                    Toast toast = Toast.makeText(getActivity(), "1つ以上入力して下さい", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                    toast.show();


                }

            }
        });

        //デリートボタン(1文字削除)
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = editText.getText().toString();
                if (str.length() > 0) {
                    editText.setText(str.substring(0, str.length() - 1));//(start,end)
                    editText.setSelection(str.length() - 1);


                } else {
                    btn_delete.setVisibility(View.INVISIBLE);

                }


            }
        });
        btn_delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (editText.getText().length() != 0) {
                    editText.getText().clear();
                    Toast.makeText(getActivity(), R.string.text_message_ClearDone, Toast.LENGTH_SHORT).show();

                }
                return false;


            }


        });

        //数字ボタンを押したらエディットテキストに数字が入る実装(0~9とアスタリスクとシャープ)
        getActivity().findViewById(R.id.imv0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[0]);
                setVib();

            }
        });
        getActivity().findViewById(R.id.imv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[1]);
                setVib();

            }
        });
        getActivity().findViewById(R.id.imv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[2]);
                setVib();

            }
        });
        getActivity().findViewById(R.id.imv3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[3]);
                setVib();

            }
        });
        getActivity().findViewById(R.id.imv4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[4]);
                setVib();

            }
        });
        getActivity().findViewById(R.id.imv5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[5]);
                setVib();

            }
        });
        getActivity().findViewById(R.id.imv6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[6]);
                setVib();

            }
        });
        getActivity().findViewById(R.id.imv7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[7]);
                setVib();

            }
        });
        getActivity().findViewById(R.id.imv8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[8]);
                setVib();

            }
        });
        getActivity().findViewById(R.id.imv9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[9]);
                setVib();

            }
        });
        getActivity().findViewById(R.id.imvAsterisk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String asterisk = "*";
                editText.append(asterisk);
                setVib();

            }
        });
        getActivity().findViewById(R.id.imvSharp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("#");
                setVib();


            }
        });

        //ディスプレイの明るさ
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.layout_display, (ViewGroup) getActivity().findViewById(R.id.rl));
        //シークバー
onSeek();



        //preferenceManagerの使い方
        onC();


    }

    private void onSeek() {//getwindow
        SeekBar sb= (SeekBar) getActivity().findViewById(R.id.seekbar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lp.screenBrightness=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void onC() {
        boolean flag;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        flag = sharedPreferences.getBoolean("preference_eto", true);//android:key="preference_color_num"


        ImageView img0 = (ImageView) getActivity().findViewById(R.id.imv0);
        ImageView img1 = (ImageView) getActivity().findViewById(R.id.imv1);
        ImageView img2 = (ImageView) getActivity().findViewById(R.id.imv2);
        ImageView img3 = (ImageView) getActivity().findViewById(R.id.imv3);
        ImageView img4 = (ImageView) getActivity().findViewById(R.id.imv4);
        ImageView img5 = (ImageView) getActivity().findViewById(R.id.imv5);
        ImageView img6 = (ImageView) getActivity().findViewById(R.id.imv6);
        ImageView img7 = (ImageView) getActivity().findViewById(R.id.imv7);
        ImageView img8 = (ImageView) getActivity().findViewById(R.id.imv8);
        ImageView img9 = (ImageView) getActivity().findViewById(R.id.imv9);
        ImageView imgAsterisk = (ImageView) getActivity().findViewById(R.id.imvAsterisk);
        ImageView imgSharp = (ImageView) getActivity().findViewById(R.id.imvSharp);

        listBitmap.add(0, BitmapFactory.decodeResource(getResources(), R.mipmap.dog));
        listBitmap.add(1, BitmapFactory.decodeResource(getResources(), R.mipmap.mouse));
        listBitmap.add(2, BitmapFactory.decodeResource(getResources(), R.mipmap.cow));
        listBitmap.add(3, BitmapFactory.decodeResource(getResources(), R.mipmap.tiger));
        listBitmap.add(4, BitmapFactory.decodeResource(getResources(), R.mipmap.bunny));
        listBitmap.add(5, BitmapFactory.decodeResource(getResources(), R.mipmap.dragon));
        listBitmap.add(6, BitmapFactory.decodeResource(getResources(), R.mipmap.snake));
        listBitmap.add(7, BitmapFactory.decodeResource(getResources(), R.mipmap.horse));
        listBitmap.add(8, BitmapFactory.decodeResource(getResources(), R.mipmap.sheep));
        listBitmap.add(9, BitmapFactory.decodeResource(getResources(), R.mipmap.monkey));
        listBitmap.add(10, BitmapFactory.decodeResource(getResources(), R.mipmap.bird));
        listBitmap.add(11, BitmapFactory.decodeResource(getResources(), R.mipmap.wildboar));


        if (flag) {
            img0.setImageBitmap(listBitmap.get(0));
            img1.setImageBitmap(listBitmap.get(1));
            img2.setImageBitmap(listBitmap.get(2));
            img3.setImageBitmap(listBitmap.get(3));
            img4.setImageBitmap(listBitmap.get(4));
            img5.setImageBitmap(listBitmap.get(5));
            img6.setImageBitmap(listBitmap.get(6));
            img7.setImageBitmap(listBitmap.get(7));
            img8.setImageBitmap(listBitmap.get(8));
            img9.setImageBitmap(listBitmap.get(9));
            imgAsterisk.setImageBitmap(listBitmap.get(10));
            imgSharp.setImageBitmap(listBitmap.get(11));


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                onC();
        }
    }
}



