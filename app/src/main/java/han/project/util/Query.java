package han.project.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import han.project.toeic.R;

/**
 * Created by Han on 05/12/2015.
 */
public class Query {

    Activity activity;

    public Query(Activity activity) {
            this.activity=activity;
    }

     public EditText id(int id){
         return  (EditText)activity.findViewById(id);
     }

     public  void Longtoast(String st){
         LayoutInflater inflater = activity.getLayoutInflater();
         View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) activity.findViewById(R.id.toast_layout_root));
         TextView text = (TextView) layout.findViewById(R.id.txtToast);
         text.setText(st);
         Toast toast = new Toast(activity);
         toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 0);
         toast.setDuration(Toast.LENGTH_LONG);
         toast.setView(layout);
         toast.show();
     }
}
