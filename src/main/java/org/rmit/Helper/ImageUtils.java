package org.rmit.Helper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageUtils {
    public static long MB = 1024 * 1024;
    public static long MAX_IMAGE_SIZE = 1 * MB;
    public static long MAX_WIDTH = 300;
    public static long MAX_HEIGHT = 300;
    public static String DEFAULT_IMAGE = "src/main/resources/org/rmit/demo/Image/DEFAULT_AVT.jpg";


    public static Image byteToImage(byte[] bytes) {
        if(bytes == null || bytes.length == 0) bytes = getByte(DEFAULT_IMAGE);
        return new Image(new ByteArrayInputStream(bytes));
    }

    public static ImageView getImageView(String path){
        Image img = byteToImage(getByte(path));
        if(img == null) return new ImageView();

        ImageView imageView = new ImageView();
        imageView.setImage(img);
        imageView.setFitWidth(Math.min(MAX_WIDTH, img.getWidth()));
        imageView.setFitHeight(Math.min(MAX_HEIGHT, img.getHeight()));
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public static byte[] getByte(String imagePath){
        File imageFile = new File(imagePath);
        if(!imageFile.exists()) return new byte[0];

        byte[] imageData = new byte[(int) imageFile.length()];
        try (FileInputStream fis = new FileInputStream(imageFile)) {
            fis.read(imageData);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return imageData;
    }

    public static Image imageFromPath(String imagePath){
        Image img =  byteToImage(getByte(imagePath));
        return img;
    }

    public static String openFileChooseDialog(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        fileChooser.setTitle("Choose an image");
        File file = fileChooser.showOpenDialog(stage);
        if(file == null) return DEFAULT_IMAGE;
        if(file.length() > MAX_IMAGE_SIZE) return DEFAULT_IMAGE;

        return file.getAbsolutePath();
    }
}
