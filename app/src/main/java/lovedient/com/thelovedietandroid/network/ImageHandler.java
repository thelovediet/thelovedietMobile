package lovedient.com.thelovedietandroid.network;

import android.content.Context;
import android.media.Image;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by MME on 6/23/2018.
 */

public class ImageHandler {
    private Context context;
    private static ImageHandler mInstance;
    private ImageLoader imageLoader;
    private ImageHandler(Context context){
        this.context = context;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.build();
        ImageLoader.getInstance().init(config);
    }
    public static ImageHandler getInstance(Context context){
        if(mInstance==null){
            mInstance = new ImageHandler(context);
        }
        return  mInstance;
    }
    public ImageLoader getImageLoader(){
        if(imageLoader==null) {
            imageLoader = ImageLoader.getInstance();
        }
        return imageLoader;
    }
}
