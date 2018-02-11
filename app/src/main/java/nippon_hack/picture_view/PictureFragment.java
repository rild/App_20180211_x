package nippon_hack.picture_view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PictureFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Bundle args = getArguments();
        if (args != null) {
            imageView.setImageResource(args.getInt("image_id"));
        }
        return view;
    }
}
