package ventasapp.com.ec.ventasapp.utilidades;


import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtils {

    private int height;
    private int width;

    public int getHeight(WindowManager windowManager) {
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        height = point.y/100;
        return height;
    }

    public int getWidth(WindowManager windowManager) {
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        width = point.x/100;
        return width;
    }

    public int getFontSize(double denstity){
        if(denstity >= 7.0){
            return 10;
        }

        else if(denstity >= 6.5 && denstity < 7.0){
            return 11;

        }

        else if(denstity >= 6.0 && denstity < 6.5){
            return 12;

        }

        else if(denstity >= 5.5 && denstity < 6.0){
            return 13;
        }

        else if(denstity >= 5.0 && denstity < 5.5){
            return 14;
        }

        else if(denstity >= 4.5 && denstity < 5.0){
            return 15;
        }

        else if(denstity >= 4.0 && denstity < 4.5){
            return 16;
        }

        else if(denstity >= 3.5 && denstity < 4.0){
            return 17;

        }else if(denstity >= 3.0 && denstity < 3.5){
            return 18;
        }

        else if(denstity >= 2.5 && denstity < 3.0){
            return 19;
        }

        else if(denstity >= 2.0 && denstity < 2.5){
            return 20;
        }

        else if(denstity >= 1.5 && denstity < 2.0){
            return 21;
        }

        else if(denstity >= 1.0 && denstity < 1.5){
            return 22;
        }

        else if(denstity >= 0.5 && denstity < 1.0){
            return 23;
        }

        else if(denstity >= 0.0 && denstity < 0.5){
            return 24;
        }

        return 25;
    }
}
