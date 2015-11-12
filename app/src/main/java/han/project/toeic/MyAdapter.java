package han.project.toeic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	List<Representative> list;
	Activity ac;
	public MyAdapter(Activity ac,List<Representative> list) {
		// TODO Auto-generated constructor stub
		this.ac = ac;
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class lesson_item {
		ImageView img;
		TextView meaning, title;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2){
		LayoutInflater inflater = ac.getLayoutInflater();
		lesson_item i;
		if (view == null) {
			i = new lesson_item();
			view = inflater.inflate(R.layout.view_mot_o, null);
			i.img = (ImageView) view.findViewById(R.id.imageView1);
			i.meaning = (TextView) view.findViewById(R.id.textView2);
			i.title = (TextView) view.findViewById(R.id.textView1);

			view.setTag(i);
		} else {
			i = (lesson_item) view.getTag();
		}
		Representative re = list.get(position);
		i.meaning.setText(re.getMeaning() + "");
		i.title.setText(re.getTitle());
		String uri_icon = "mipmap/"+re.getImage();
		int ImageResource = view.getContext().getResources().getIdentifier(uri_icon, null, view.getContext().getApplicationContext().getPackageName());
		Drawable image = view.getContext().getResources().getDrawable(ImageResource);
		i.img.setImageDrawable(image);
		return view;
	}

}
