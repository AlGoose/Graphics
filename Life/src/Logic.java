import java.awt.*;
import java.awt.image.BufferedImage;

public class Logic {
    private Integer width;
    private Integer height;
    private Integer radius;
    private Integer fat;
    private Double LIVE_BEGIN;
    private Double BIRTH_BEGIN;
    private Double BIRTH_END;
    private Double LIVE_END;
    private Double FST_IMPACT;
    private Double SND_IMPACT;
    private Hex[][] field;

    private double[][] impactTable;
    private double[][] prevImpactTable;
    private boolean parity;
    private int fst_count;
    private int snd_count;
    private int mc;

    public class Hex{
        private Integer centrX;
        private Integer centrY;
        private Boolean alive = false;

        private Hex(int x, int y){
            centrX = x;
            centrY = y;
        }

        private void setAlive(Boolean status){
            alive = status;
        }

        private Boolean getAlive(){
            return alive;
        }
    }

    public Logic(int width, int height, int radius, int fat){
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.fat = fat;
        field = new Hex[height][width];
        impactTable = new double[height][width];
        field = createField(field);
    }

    public void setOptions(double LIVE_BEGIN, double BIRTH_BEGIN, double BIRTH_END, double LIVE_END, double FST_IMPACT, double SND_IMPACT){
        this.LIVE_BEGIN = LIVE_BEGIN;
        this.BIRTH_BEGIN = BIRTH_BEGIN;
        this.BIRTH_END = BIRTH_END;
        this.LIVE_END = LIVE_END;
        this.FST_IMPACT = FST_IMPACT;
        this.SND_IMPACT = SND_IMPACT;
    }

    private Hex[][] createField(Hex[][] hex){
        radius+=fat/2;
        int deltaX = (int)Math.ceil(Math.sqrt(3)*radius);
        int deltaX2 = deltaX / 2;

        while(deltaX != deltaX2*2){
            radius++;
            deltaX = (int)Math.ceil(Math.sqrt(3)*radius);
            deltaX2 = deltaX/2;
        }

        int centrX = radius + fat + 15;
        int centrXX = centrX;
        int centrY = radius + fat + 20;
        int centrYY = centrY;
        int deltaY = (int)Math.ceil(3*radius/2);

        for (int i=0;i<height;i++){
            int mc = i%2 == 0 ? width : width-1;

            for(int j=0;j<mc;j++){
                hex[i][j] = new Hex(centrX,centrY);
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
        return hex;
    }

    public double getImpact(int i, int j){
        return impactTable[i][j];
    }

    public boolean getAlive(int i, int j){
        return field[i][j].getAlive();
    }

    public Point getCentre(int i, int j){ return new Point(field[i][j].centrX, field[i][j].centrY); }

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

    private boolean check_attachment(Hex hex, int click_X, int click_Y) {  //возвращает true, если точка лежит слева от каждой прямой
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

    public void newLogic(int width, int height, int radius, int fat){
        int t1 = this.width;
        int t2 = this.height;
        int t3 = this.radius;
        int t4 = this.fat;

        prevImpactTable = impactTable;
        Hex[][] tmp = new Hex[height][width];
        double[][] tmp2 = new double[height][width];
        impactTable = tmp2;

        this.width = width;
        this.height = height;
        this.radius = radius;
        this.fat = fat;

        tmp = createField(tmp);

        this.width = t1;
        this.height = t2;
        this.radius = t3;
        this.fat = t4;

        tmp2 = resurrectImpacts(tmp2,width,height);
        impactTable = tmp2;

        tmp = resurrectHex(tmp,width,height);
        field = tmp;

        this.width = width;
        this.height = height;
        this.radius = radius;
        this.fat = fat;
    }

    public void setAlive(int i, int j, boolean status){
        field[i][j].setAlive(status);
    }

    public BufferedImage nextStep(BufferedImage image){
        Paint paint = new Paint(image);

        countImpacts();

        for (int i = 0; i < height; i++) {
            if (i % 2 == 0) {
                mc = width;
                parity = true;
            } else {
                mc = width -1;
                parity = false;
            }

            for (int j = 0; j < mc; j++) {
                if (!field[i][j].getAlive() && goingToBorn(i,j))
                    field[i][j].setAlive(true);

                if (field[i][j].getAlive() && isAlive(i,j))
                    field[i][j].setAlive(true);
                else
                    field[i][j].setAlive(false);
            }
        }
        countImpacts();

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(field[i][j] != null){
                    if(field[i][j].getAlive()){
                        image = paint.fillHexagon(field[i][j].centrX, field[i][j].centrY, Color.RED);
                    } else {
                        image = paint.fillHexagon(field[i][j].centrX, field[i][j].centrY, Color.WHITE);
                    }
                }
            }
        }
        return image;
    }

    public void countImpacts(){
        prevImpactTable = impactTable;
        double[][] tmp = prevImpactTable;
        mc = width;

        for(int i=0; i<height; i++){
            if(i%2 == 0){
                mc = width;
                parity = true;
            } else {
                mc = width-1;
                parity = false;
            }

            for(int j=0; j<mc; j++){
                fst_count = 0;
                snd_count = 0;

                countFirstImpact(i,j);
                countSecondImpact(i,j);

                tmp[i][j] = fst_count*FST_IMPACT + snd_count* SND_IMPACT;
            }
        }
        impactTable = tmp;
    }

    private void countFirstImpact(int i, int j){
        boolean top = i > 0;
        boolean down = i < impactTable.length - 1;
        boolean left = j > 0;
        boolean right = j < mc - 1;

        if(right && field[i][j + 1].getAlive()){
            fst_count++;
        }
        if(left && field[i][j - 1].getAlive()){
            fst_count++;
        }

        if(top){
            if(parity){
                if(left && field[i - 1][j - 1].getAlive()){
                    fst_count++;
                }
                if(right && field[i - 1][j].getAlive()){
                    fst_count++;
                }
            } else {
                if(field[i - 1][j + 1].getAlive()){
                    fst_count++;
                }
                if(field[i - 1][j].getAlive()){
                    fst_count++;
                }
            }
        }

        if(down){
            if(parity){
                if(left && field[i + 1][j - 1].getAlive()){
                    fst_count++;
                }
                if(right && field[i + 1][j].getAlive()){
                    fst_count++;
                }
            } else {
                if(field[i+1][j].getAlive()){
                    fst_count++;
                }
                if(field[i+1][j+1].getAlive()){
                    fst_count++;
                }
            }
        }
    }

    private void countSecondImpact(int i, int j){
        boolean top = i > 1;
        boolean down = i < impactTable.length - 2;
        boolean lineUp = i > 0;
        boolean lineDown = i < impactTable.length - 1;

        if (parity) {
            boolean left = j > 1;
            boolean right = j < mc - 2;

            if (lineUp) {
                if (left && field[i-1][j-2].getAlive()) {
                    snd_count++;
                }
                if (right && field[i-1][j+1].getAlive()) {
                    snd_count++;
                }
            }

            if (lineDown) {
                if (left && field[i+1][j-2].getAlive()) {
                    snd_count++;
                }
                if (right && field[i+1][j+1].getAlive()) {
                    snd_count++;
                }
            }
        } else {
            boolean left = j > 0;
            boolean right = j < mc - 1;

            if (lineUp) {
                if (left && field[i-1][j-1].getAlive()) {
                    snd_count++;
                }
                if (right && field[i-1][j+2].getAlive()) {
                    snd_count++;
                }
            }

            if (lineDown) {
                if (left && field[i+1][j-1].getAlive()) {
                    snd_count++;
                }
                if (right && field[i+1][j+2].getAlive()) {
                    snd_count++;
                }
            }
        }

        if (down && field[i+2][j].getAlive()) {
            snd_count++;
        }
        if (top && field[i-2][j].getAlive()) {
            snd_count++;
        }
    }

    private boolean isAlive(int x, int y){
        return prevImpactTable[x][y] >= LIVE_BEGIN && prevImpactTable[x][y] <= LIVE_END;
    }

    private boolean goingToBorn(int x, int y){
        return prevImpactTable[x][y] >= BIRTH_BEGIN && prevImpactTable[x][y] <= BIRTH_END;
    }

    public boolean isLive(){
        int length = width - 1;

        for (int i = 0; i < height; i++) {
            if (i % 2 == 0) {
                length++;
            } else {
                length--;
            }
            for (int j = 0; j < length; j++) {
                if(field[i][j].getAlive())
                    return true;
            }
        }
        return false;
    }

    private double[][] resurrectImpacts(double[][] tmp, int width, int height){
        if(width < this.width){
            if(height < this.height){
                for(int i=0; i<height; i++){
                    for(int j=0; j<width; j++){
                        tmp[i][j] = prevImpactTable[i][j];
                    }
                }
            } else {
                for(int i=0; i<this.height; i++){
                    for(int j=0; j<width; j++){
                        tmp[i][j] = prevImpactTable[i][j];
                    }
                }
            }
        } else {
            if(height < this.height){
                for(int i=0; i<height; i++){
                    for(int j=0; j<this.width; j++){
                        tmp[i][j] = prevImpactTable[i][j];
                    }
                }
            } else {
                for(int i=0; i<this.height; i++){
                    for(int j=0; j<this.width; j++){
                        tmp[i][j] = prevImpactTable[i][j];
                    }
                }
            }
        }
        return tmp;
    }

    private Hex[][] resurrectHex(Hex[][] tmp, int width, int height){
        if(width < this.width){
            if(height < this.height){
                for(int i=0; i<height; i++){
                    for(int j=0; j<width; j++){
                        if(field[i][j]!=null && tmp[i][j]!=null){
                            tmp[i][j].setAlive(field[i][j].alive);
                        }
                    }
                }
            } else {
                for(int i=0; i<this.height; i++){
                    for(int j=0; j<width; j++){
                        if(field[i][j]!=null && tmp[i][j]!=null){
                            tmp[i][j].setAlive(field[i][j].alive);
                        }
                    }
                }
            }
        } else {
            if(height < this.height){
                for(int i=0; i<height; i++){
                    for(int j=0; j<this.width; j++){
                        if(field[i][j]!=null && tmp[i][j]!=null){
                            tmp[i][j].setAlive(field[i][j].alive);
                        }
                    }
                }
            } else {
                for(int i=0; i<this.height; i++){
                    for(int j=0; j<this.width; j++){
                        if(field[i][j]!=null && tmp[i][j]!=null){
                            tmp[i][j].setAlive(field[i][j].alive);
                        }
                    }
                }
            }
        }
        return tmp;
    }

    public BufferedImage resurrectImage(BufferedImage image){
        Paint paint = new Paint(image);
        int tmp = width;
        for(int i=0; i<height; i++){
            tmp = i%2==0 ? width : width-1;
            for(int j=0; j<tmp; j++){
                if(field[i][j].getAlive()){
                    paint.fillHexagon(field[i][j].centrX, field[i][j].centrY, Color.RED);
                }
            }
        }
        return image;
    }

    public void clearField(){
        double[][] tmp = new double[height][width];
        impactTable = tmp;

        for(int i=0; i<height; i++){
            int mc = i%2==0 ? width : width-1;
            for(int j=0; j<mc; j++){
                field[i][j].setAlive(false);
            }
        }
    }

    public int liveNumber(){
        int res = 0;
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(field[i][j]!=null && field[i][j].getAlive()) res++;
            }
        }
        return res;
    }
}
