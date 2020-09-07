import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * space shutter game
 */
public class Game implements ActionListener, KeyListener {
    public static Game game;
    Image imageID;//375 x 666
    player player;//the spaceship
    Image spaceShip;//image of spaceship
    Image fireBall;//image of the bullet
    paint p;//painter
    ArrayList<BackGround> backGrounds;//background
    ArrayList<fireBall> fireBalls;//list of bullet
    public boolean fire = false;
    public ArrayList<Virus> listVirus;//list of obstacles
    Random rand;
    BufferedImage virus;
    BufferedImage virus1;
    BufferedImage virus2;
    public int numVirus;
    int score;
    boolean gameStart = false;
    public int speed = 10;
    public boolean gameOver = false;
    int tick = 0;
    public Game(){
        try{
            imageID = ImageIO.read(new File("1.jpg"));
            spaceShip = ImageIO.read(new File("catSpaceShip.png"));
            fireBall = ImageIO.read(new File("bubbleBall.png"));
            virus = ImageIO.read(new File("donut1.png"));
            virus1 = ImageIO.read(new File("donut2.png"));
            virus2 = ImageIO.read(new File("donut5.png"));
        } catch (IOException e){
            System.out.println("file error!");
        }
        JFrame frame = new JFrame();//initialize new frame
        frame.setSize(375,700);
        frame.setLayout(new LayoutManager() {//set layout for the frame
            @Override
            public void addLayoutComponent(String s, Component component) {

            }

            @Override
            public void removeLayoutComponent(Component component) {

            }

            @Override
            public Dimension preferredLayoutSize(Container container) {
                return null;
            }

            @Override
            public Dimension minimumLayoutSize(Container container) {
                return null;
            }

            @Override
            public void layoutContainer(Container container) {

            }
        });
        backGrounds = new ArrayList<>();//list backgrounds
        backGrounds.add(new BackGround(0,0));
        backGrounds.add(new BackGround(0,-666));
        fireBalls = new ArrayList<>();//initialize list of fireBalls
        rand = new Random();
        numVirus = random(2,5);//random for number of obstacles
        listVirus = new ArrayList<>();//Initializing array of obstacles
        for (int i = 0; i < numVirus; i++){
            makeVirus1();//create the obstacles
        }
        Timer t = new Timer(40,this);//initialize Timer
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        p = new paint();//initialize the painter of the frame
        p.setBounds(0,0,375,700);//set size for the JPanel
        player = new player(120,540);//set the initial coordinates for the player
        frame.add(p);//add JPanel to the JFrame
        frame.addKeyListener(this);//enable the frame to receive the input key from the user
        p.addKeyListener(this);
        p.repaint();
        t.start();



    }
    public int random(int min, int max){
        int num = rand.nextInt(max - min);
        return num + min;
    }
    public void repaint(Graphics g){
        for (int i = 0; i < backGrounds.size(); i++) {
            int xCoord = backGrounds.get(i).xCoord;
            int yCoord = backGrounds.get(i).yCoord;
            g.drawImage(imageID, xCoord, yCoord, null);
        }
        g.drawImage(spaceShip,player.xPos,player.yPos,null);
        for (int i = 0; i < listVirus.size(); i++){
            int x = listVirus.get(i).xCoord;
            int y = listVirus.get(i).yCoord;
            int type = listVirus.get(i).type;
            if (type == 0){
                g.drawImage(virus,x,y,null);
            }else if (type == 1){
                g.drawImage(virus1,x,y,null);
            }else if (type == 2){
                g.drawImage(virus2,x,y,null);
            }
        }
        for (int i = 0; i < fireBalls.size(); i++){
            g.drawImage(fireBall,fireBalls.get(i).xCoord,fireBalls.get(i).yCoord,null);
        }
        g.setColor(Color.white);
        g.setFont(new Font("Arial",1,20));
        String grade = "" + score;
        g.drawString(grade,150,30);
        if(!gameStart){
            g.setFont(new Font("Arial",1,25));
            String announce = "Press enter to start the game";
            g.drawString(announce,5,300);
        }
        if (gameOver){
            if (tick%5 == 0){
                g.setColor(Color.white);
                g.setFont(new Font("Arial",1,40));
                String announce = "Game over";
                g.drawString(announce,80,300);
            }
            g.setFont(new Font("Arial",1,25));
            String announce = "Hit Space to play again";
            g.drawString(announce,40,350);

        }

    }
    public void makeVirus(){
        int type = rand.nextInt(3);
        int xCoord = rand.nextInt(275);
        int yCoord = random(-700, -80);
        while (checkCollide(xCoord,yCoord) || checkCollide1(xCoord,yCoord)){
            xCoord = rand.nextInt(275);
            yCoord = rand.nextInt(620);
        }
        Virus v = new Virus(xCoord,yCoord,type);
        listVirus.add(v);

    }
    public void makeVirus1(){
        int type = rand.nextInt(3);
        int xCoord = rand.nextInt(275);
        int yCoord = random(0, 600);
        while (checkCollide(xCoord,yCoord) || checkCollide1(xCoord,yCoord)){
            xCoord = rand.nextInt(275);
            yCoord = rand.nextInt(620);
        }
        Virus v = new Virus(xCoord,yCoord,type);
        listVirus.add(v);

    }
    public boolean checkCollide(int x, int y){
        int rightBounder = x + 80;
        int downBounder = y + 80;
        if (108 <= rightBounder && rightBounder <= 368 && 460 <= downBounder && downBounder <= 720 ){
            return true;
        }
        return false;
    }
    public int hit(int xBall, int yBall){
        int downBound = yBall + 40;
        int rightBound = xBall + 40;
        for (int i = 0; i < listVirus.size(); i ++){
            int xCoord = listVirus.get(i).xCoord;
            int yCoord = listVirus.get(i).yCoord;
            if (xCoord <= rightBound && xBall <= xCoord + 40 && yCoord <= downBound && yBall <= yCoord + 40  ){
                return i;
            }
        }
        return -1;

    }
    public boolean checkCollide1(int x, int y){
        int downBound = y + 40;
        int rightBound = x + 40;
        for (int i = 0; i < listVirus.size(); i ++){
            int xCoord = listVirus.get(i).xCoord;
            int yCoord = listVirus.get(i).yCoord;
            if (xCoord <= rightBound && x <= xCoord + 40 && yCoord <= downBound && y <= yCoord + 40  ){
                return true;
            }
        }
        return false;
    }
    public boolean checkCollide2(int xDonut, int yDonut){
        int rightBound = player.xPos + 100;
        int downBound = player.yPos + 100;
        if (xDonut<=rightBound && rightBound-100<=xDonut+40 && yDonut<=downBound && downBound - 100<=yDonut+40){
            return true;
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!gameOver) {
            for (int i = 0; i < backGrounds.size(); i++) {
                backGrounds.get(i).yCoord = backGrounds.get(i).yCoord + speed;
            }
            for (int i = 0; i < listVirus.size(); i++) {
                if (listVirus.get(i).yCoord >= 700) {
                    listVirus.remove(i);
                    makeVirus();
                }
            }

            if (backGrounds.get(0).yCoord >= 700) {
                backGrounds.remove(0);
                int finalY = backGrounds.get(backGrounds.size() - 1).yCoord;
                backGrounds.add(new BackGround(0, finalY - 666));
            }
            if (fire) {
                ArrayList<Integer> Remove = new ArrayList<>();
                for (int i = 0; i < fireBalls.size(); i++) {
                    fireBalls.get(i).yCoord = fireBalls.get(i).yCoord - speed;
                    int x = fireBalls.get(i).xCoord;
                    int y = fireBalls.get(i).yCoord;
                    int hit = hit(x, y);
                    if (hit != -1) {
                        score = score + 1;
                        if (score >= 20) {
                            speed = 15;
                        }
                        if (score >= 30) {
                            speed = 25;
                        }
                        listVirus.remove(hit);
                        Remove.add(i);
                        makeVirus();
                    }
                    if (fireBalls.get(i).yCoord < -40) {
                        fireBalls.remove(i);
                    }
                }
                for (int i = 0; i < Remove.size(); i++) {
                    int index = Remove.get(i);
                    fireBalls.remove(index);
                }
            }
                for (int i = 0; i < listVirus.size(); i++) {
                    listVirus.get(i).yCoord = listVirus.get(i).yCoord + speed;
                    if (gameStart) {
                        if (checkCollide2(listVirus.get(i).xCoord, listVirus.get(i).yCoord)) {
                            gameOver = true;
                            break;
                        }
                    }
                }
        }else{
            tick = tick +  1;
        }
        p.repaint();

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (gameStart) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                player.xPos = player.xPos - 20;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                player.xPos = player.xPos + 20;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                if (!gameOver) {
                    fire = true;
                    int xHead = player.xPos;
                    int yHead = player.yPos;
                    fireBalls.add(new fireBall(xHead + 30, yHead - 45));
                }else{
                    gameOver = false;
                    speed = 10;
                    score = 0;
                    listVirus = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        makeVirus1();
                    }
                    p.repaint();
                }
            }
            p.repaint();
        } else {
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                gameStart = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (fireBalls.size() <= 0) {
            fire = false;
        }

    }
    public static void main(String[] args){
        game = new Game();
    }
}
