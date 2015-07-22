package jp.example.owner.jp.phoneappli;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    Resources r;
    Bitmap bitmap1;
    //テストプリファレンス
    ImageView img0;

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
                    Toast.makeText(getActivity(), "アプリ再起動時には再設定が必要です", Toast.LENGTH_SHORT).show();
                } else if (item.isChecked() == true) {
                    item.setChecked(false);
                    flag = false;
                    stopVib();
                }

                break;
            //画面の明るさ調整
            case R.id.action_Display:

                break;
            //オリジナルボタンの実装
            case R.id.action_CustomButton:
                i = new Intent(getActivity(), PreferenceActivity.class);

                startActivityForResult(i,0);

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


        //preferenceManagerの使い方
        onC();


    }




    public void onC() {
SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
       boolean flag=sharedPreferences.getBoolean("preference_color_num",true);//android:key="preference_color_num"
        img0 = (ImageView) getActivity().findViewById(R.id.imv0);
        r = getResources();
        bitmap1 = BitmapFactory.decodeResource(r, R.mipmap.bird);
        listBitmap.add(bitmap1);
        if(flag){
            img0.setImageBitmap(bitmap1);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                onC();
        }
    }
}



