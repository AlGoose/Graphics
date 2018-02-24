import java.awt.*;

public class Logic {
    private Integer width;
    private Integer height;
    private Integer radius;
    private Integer fat;
    private Hex[][] field;

    public class Hex{
        private Integer centrX;
        private Integer centrY;
        private Double impact;

        private Hex(int x, int y){
            centrX = x;
            centrY = y;
        }

        private int getX(){
            return centrX;
        }

        private int getY(){
            return centrY;
        }

        private void setImpact(double imp){
            impact = imp;
        }

        private double getImpact(){
            return impact;
        }
    }

    public Logic(int width, int height, int radius, int fat){
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.fat = fat;
        field = new Hex[width][height];
        createField();
    }

    private void createField(){
        radius+=fat/2;
        int deltaX = (int)Math.ceil(Math.sqrt(3)*radius);
        int deltaX2 = deltaX / 2;

        while(deltaX != deltaX2*2){
            radius++;
            deltaX = (int)Math.ceil(Math.sqrt(3)*radius);
            deltaX2 = deltaX/2;
        }

        int centrX = radius + fat + 5;
        int centrXX = centrX;
        int centrY = radius + fat + 5;
        int centrYY = centrY;
        int deltaY = (int)Math.ceil(3*radius/2);

        for (int i=0;i<height;i++){
            int mc = i%2 == 0 ? width : width-1;

            for(int j=0;j<mc;j++){
                field[i][j] = new Hex(centrX,centrY);
                centrX += deltaX;
            }
            centrY = centrYY + deltaY;
            centrYY = centrY;
            if(i%2 == 0){
                centrX = centrXX + deltaX2;
                centrXX = centrX;
            } else {
                centrX = centrXX - deltaX2;
                centrXX = centrX;
            }
        }
    }

    public double getImpact(int i, int j){
        return field[i][j].getImpact();
    }

    public void setImpact(int i, int j, double imp){
        field[i][j].setImpact(imp);
    }

    public Point whatHex(int click_X,int click_Y){
        for(int i=0; i<height;i++){
            for(int j=0; j<width; j++){
                if(field[i][j] != null){
                    if(check_attachment(field[i][j], click_X, click_Y)){
                        return new Point(i,j);
                    }
                }
            }
        }
        return new Point(-1,-1);
    }

    public boolean check_attachment(Hex hex, int click_X, int click_Y) {  //возвращает true, если точка лежит слева от каждой прямой
        int x = hex.centrX;
        int y = hex.centrY;
        int xArr[] = new int[6];
        int yArr[] = new int[6];

        xArr[0] = x;
        xArr[1] = x + (int) Math.round(Math.sqrt(3) * radius / 2);
        xArr[2] = x + (int) Math.round(Math.sqrt(3) * radius / 2);
        xArr[3] = x;
        xArr[4] = x - (int) Math.round(Math.sqrt(3) * radius / 2);
        xArr[5] = x - (int) Math.round(Math.sqrt(3) * radius / 2);

        yArr[0] = y - radius;
        yArr[1] = y - radius/2;
        yArr[2] = y + radius/2;
        yArr[3] = y + radius;
        yArr[4] = y + radius/2;
        yArr[5] = y - radius/2;

        for(int i = 0; i < 5; i++) {
            if(!vector_mult(new Point(xArr[i],yArr[i]), new Point(xArr[i+1],yArr[i+1]), click_X, click_Y)) {
                return false;
            }
        }
        return vector_mult(new Point(xArr[5], yArr[5]), new Point(xArr[0], yArr[0]), click_X, click_Y);
    }

    private boolean vector_mult(Point A, Point B, int click_X, int click_Y) {
        if(((B.getX()-A.getX())*(click_Y - A.getY()) - (B.getY()-A.getY())*(click_X - A.getX())) >= 0) return true;
        else return false;
    }
}
