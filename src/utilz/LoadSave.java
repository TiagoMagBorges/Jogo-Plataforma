package utilz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;

public class LoadSave {
    public static BufferedImage GetSpriteAtlas(String fileName){
        BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
    }

	public static BufferedImage[] GetAllLevels(){
		URL url = LoadSave.class.getResource("/lvls");
		File file = null;

		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		File[] files = file.listFiles();
		File[] filesSorted = new File[files.length];

		for(int i = 0; i < filesSorted.length; i++){
			for(int j = 0; j < files.length; j++){
				if(files[j].getName().equals((i+1) + ".png")){
					filesSorted[i] = files[j];
				}
			}
		}

		BufferedImage[] imgs = new BufferedImage[files.length];
		for(int i = 0; i < imgs.length; i++){
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return imgs;
	}
}
