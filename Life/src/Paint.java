import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class Paint extends JPanel{
    private BufferedImage image;

    public Paint(BufferedImage img){
        this.image = img;
    }

    private int sign(int x) {
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;
    }

    public void drawBresenhamLine(int xstart, int ystart, int xend, int yend) {
        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

        dx = xend - xstart;
        dy = yend - ystart;

        incx = sign(dx);
        incy = sign(dy);

        if (dx < 0) dx = -dx;
        if (dy < 0) dy = -dy;

        if (dx > dy) {
            pdx = incx;
            pdy = 0;
            es = dy;
            el = dx;
        } else {
            pdx = 0;
            pdy = incy;
            es = dx;
            el = dy;
        }

        x = xstart;
        y = ystart;
        err = el / 2;
        image.setRGB(x,y, Color.BLACK.getRGB());

        for (int t = 0; t < el; t++) {
            err -= es;
            if (err < 0) {
                err += el;
                x += incx;
                y += incy;
            } else {
                x += pdx;
                y += pdy;
            }
            image.setRGB(x,y,Color.BLACK.getRGB());
        }
    }

    public void drawHexagon(int x, int y, int r) {
        int xArr[] = new int[6];
        int yArr[] = new int[6];

        xArr[0] = x;
        xArr[1] = x + r;
        xArr[2] = x + r;
        xArr[3] = x;
        xArr[4] = x - r;
        xArr[5] = x - r;

        yArr[0] = y - r;
        yArr[1] = y - r/2;
        yArr[2] = y + r/2;
        yArr[3] = y + r;
        yArr[4] = y + r/2;
        yArr[5] = y - r/2;

        for(int i = 0; i < 5; i++) {
            drawBresenhamLine(xArr[i], yArr[i], xArr[i+1], yArr[i+1]);
        }
        drawBresenhamLine(xArr[0], yArr[0], xArr[5], yArr[5]);
    }

    public BufferedImage drawField(int m, int n, int r){
        int mc = m;
//        int centrX = (int)Math.round(Math.sqrt(3)/2*r)+10;
//        int centrY = r;
//
//        int deltaX = (int)Math.round(Math.sqrt(3)*r);
//        int deltaX2 = (int)Math.round(Math.sqrt(3)/2*r);
//        int deltaY = (int)Math.round(3*r/2);

        int centrX = (int)Math.ceil(Math.sqrt(3)/2*r)+10;
        int centrY = r;

        int deltaX = 2*r;
        int deltaX2 = r;
        int deltaY = Math.round(3*r/2);

        for (int i=0;i<n;i++){
            if(i%2 == 0){
                mc = m;
            } else {
                mc = m-1;
            }

            for(int j=0;j<mc;j++){
                drawHexagon(centrX,centrY,r);
                if(i%2 == 0 && j != mc -1){
                    centrX += deltaX;
                }
                if(i%2 == 1 && j != mc-1){
                    centrX -= deltaX;
                }
            }
            centrY += deltaY;
            centrX -= deltaX2;
        }
        return image;
    }

    public class Span{
        private Point p1;
        private Point p2;

        public Span(int x1, int y1, int x2, int y2){
            p1 = new Point(x1,y1);
            p2 = new Point(x2,y2);
        }

        public Point getAPoint(){
            return p1;
        }

        public Point getBPoint(){
            return p2;
        }
    }

    public Span makeSpan(int x, int y){
        int x1,x2;
        int tmpX = x;

        while(image.getRGB(x,y) != Color.BLACK.getRGB()){
            x--;
        }
        x1 = x+1;
        x = tmpX;

        while(image.getRGB(x,y) != Color.BLACK.getRGB()){
            x++;
        }
        x2 = x-1;

        return new Span(x1,y,x2,y);
    }

    public BufferedImage fillHexagon(int x, int y, Color c){
        Boolean lockUp;
        Boolean lockDown;
        Stack<Span> stack = new Stack<>();
        Span span = makeSpan(x,y);
        stack.push(span);

        while(!stack.empty()){
            lockUp = false;
            lockDown = false;
            span = stack.pop();
            Point a = span.getAPoint();
            Point b = span.getBPoint();
            int j = (int)a.getY();
            for(int i = (int)a.getX(); i <= b.getX(); i++){
                image.setRGB(i,j, c.getRGB());
                if(image.getRGB(i,j-1) != Color.BLACK.getRGB() && image.getRGB(i,j-1) != c.getRGB()){
                    if(!lockUp){
                        lockUp = true;
                        stack.push(makeSpan(i,j-1));
                    }
                } else {
                    lockUp = false;
                }

                if(image.getRGB(i,(int)a.getY()+1) != Color.BLACK.getRGB() && image.getRGB(i,(int)a.getY()+1) != c.getRGB()){
                    if(!lockDown){
                        lockDown = true;
                        stack.push(makeSpan(i,(int)a.getY()+1));
                    }
                } else {
                    lockDown = false;
                }
            }
        }
        return image;
    }
}
