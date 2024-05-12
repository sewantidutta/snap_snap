import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Snap extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }  

    int board_width;
    int board_height;
    int tilesize = 25;
    
    Tile snap_head;
    ArrayList<Tile> snap_body;

    Tile food;
    Random rand;

    int velX;
    int velY;
    Timer gameLoop;

    boolean gameover = false;

    Snap(int board_width, int board_height) {
        this.board_width = board_width;
        this.board_height = board_height;
        setPreferredSize(new Dimension(this.board_width, this.board_height));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snap_head = new Tile(5, 5);
        snap_body = new ArrayList<Tile>();

        food = new Tile(10, 10);
        rand = new Random();
        place_food();

        velX = 1;
        velY = 0;
  
		gameLoop = new Timer(100, this); 
        gameLoop.start();
	}	
    
    public void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		draw(gr);
	}

	public void draw(Graphics gr) {

        gr.setColor(Color.red);
        gr.fill3DRect(food.x*tilesize, food.y*tilesize, tilesize, tilesize, true);

        gr.setColor(Color.green);
        gr.fill3DRect(snap_head.x*tilesize, snap_head.y*tilesize, tilesize, tilesize, true);
        
        for (int i = 0; i < snap_body.size(); i++) {
            Tile snake_part = snap_body.get(i);
            gr.fill3DRect(snake_part.x*tilesize, snake_part.y*tilesize, tilesize, tilesize, true);
		}

        gr.setFont(new Font("Calibri", Font.BOLD, 18));
        if (gameover) {
            gr.setColor(Color.red);
            gr.drawString("Game Over: " + String.valueOf(snap_body.size()), tilesize - 16, tilesize);
        }
        else {
            gr.setColor(Color.yellow);
            gr.drawString("Score: " + String.valueOf(snap_body.size()), tilesize - 16, tilesize);
        }
	}

    public void place_food(){
        food.x = rand.nextInt(board_width/tilesize);
		food.y = rand.nextInt(board_height/tilesize);
	}

    public void move() {
        if (collision(snap_head, food)) {
            snap_body.add(new Tile(food.x, food.y));
            place_food();
            velX += 0.5 * Math.signum(velX);
            velY += 0.5 * Math.signum(velY);
        }

        for (int i = snap_body.size()-1; i >= 0; i--) {
            Tile snake_part = snap_body.get(i);
            if (i == 0) { 
                snake_part.x = snap_head.x;
                snake_part.y = snap_head.y;
            }
            else {
                Tile prevsnake = snap_body.get(i-1);
                snake_part.x = prevsnake.x;
                snake_part.y = prevsnake.y;
            }
        }
    
        snap_head.x += velX;
        snap_head.y += velY;

        for (int i = 0; i < snap_body.size(); i++) {
            Tile snake_part = snap_body.get(i);

            if (collision(snap_head, snake_part)) {
                gameover = true;
            }
        }
        if (snap_head.x*tilesize < 0) snap_head.x=(board_width / tilesize) - 1;
        else if(snap_head.x*tilesize >board_width)  snap_head.x=0;
        else if(snap_head.y*tilesize < 0 )  snap_head.y=(board_height / tilesize) - 1;
        else if(snap_head.y*tilesize >board_height)  snap_head.y=0;
    }

    public boolean collision(Tile t1, Tile t2) {
        return t1.x == t2.x && t1.y == t2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (gameover) {
            gameLoop.stop();
        }
    }  

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP && velY != 1) {
            velX = 0;
            velY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velY != -1) {
            velX = 0;
            velY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velX != 1) {
            velX = -1;
            velY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velX != -1) {
            velX = 1;
            velY = 0;
        }
    }

    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
