package jp.example.owner.jp.phoneappli;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    EditText editText;

    private String[] number = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};//セットする値＋asterisk,sharp

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
                // Creates a new Intent to insert a contact
                //Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
// Sets the MIME type to match the Contacts Provider
                //intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                //intent.putExtra(ContactsContract.Intents.Insert.PHONETIC_NAME, editText.getText().toString());//電話帳に名前を登録する
//startActivity(intent);
                break;
            case R.id.action_SearchOnWeb:
                String url=String.format("%s",Uri.encode(editText.getText().toString()));
                i = new Intent(Intent.ACTION_WEB_SEARCH,Uri.parse(url));
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
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
                    startActivity(i);
                    //# ♯ # ＃　♯
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
            }
        });
        getActivity().findViewById(R.id.imv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[1]);
            }
        });
        getActivity().findViewById(R.id.imv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[2]);
            }
        });
        getActivity().findViewById(R.id.imv3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[3]);
            }
        });
        getActivity().findViewById(R.id.imv4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[4]);
            }
        });
        getActivity().findViewById(R.id.imv5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[5]);
            }
        });
        getActivity().findViewById(R.id.imv6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[6]);
            }
        });
        getActivity().findViewById(R.id.imv7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[7]);
            }
        });
        getActivity().findViewById(R.id.imv8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[8]);
            }
        });
        getActivity().findViewById(R.id.imv9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append(number[9]);
            }
        });
        getActivity().findViewById(R.id.imvAsterisk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String asterisk = "*";
                editText.append(asterisk);
            }
        });
        getActivity().findViewById(R.id.imvSharp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("#");

            }
        });
    }

}



