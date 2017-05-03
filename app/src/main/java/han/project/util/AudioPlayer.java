package han.project.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Han on 18/11/2015.
 */
public class AudioPlayer {
    String fileName;
    Context contex;
    MediaPlayer mp;
    private final static int MAX_VOLUME = 100;

    //Constructor
    public AudioPlayer(String name, Context context) {
        fileName = name;
        contex = context;
        playAudio();
    }

    //Play Audio
    public void playAudio() {
        mp = new MediaPlayer();
        final float volume = (float) (1 - (Math.log(MAX_VOLUME - 50) / Math.log(MAX_VOLUME)));
        mp.setVolume(volume, volume);
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = new MediaPlayer();
            }
            AssetFileDescriptor descriptor = contex.getAssets()
                    .openFd("audios/" + fileName);
            mp.setDataSource(descriptor.getFileDescriptor(),
                    descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mp.prepare();
            mp.setVolume(1.0f, 1.0f);
            mp.start();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Stop Audio
    public void stop() {
        mp.stop();
    }
}
