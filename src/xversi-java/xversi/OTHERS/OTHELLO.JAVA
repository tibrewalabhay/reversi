/*
 * @(#) Othello.java 	
 *
 * Copyright (c) 1995 Yukio Hirai. All Rights Reserved.
 *
 * Author: Yukio Hirai <t94369yh@sfc.keio.ac.jp> 
 *
 * On condition that you do not modify this copyright notice,
 * You can use, copy, modify, and distribute this program
 * I am not responsible for  ANY damage caused by this program.
 * Use this program at YOUR OWN RISK!
 * Feel free to e-mail me some comments.
 *
 * ���: ʿ�� ���� (�Ҥ餤 �椭��) <t94369yh@sfc.keio.ac.jp> 
 *
 * ����ʸ�ϼ��Ȥ��ѹ���������ʤ��Ȥ������ˤ����ơ����Υץ�
 * ����ͳ�˻��ѡ�ʣ������¤�����ۤ��Ʋ����äƹ����ޤ���
 * ���Υץ����ˤ�äƤ⤿�餵�줿�������ʤ�»���ˤĤ��Ƥ⡢
 * ��Ԥ���Ǥ���餤�ޤ��󡣸漫ʬ����Ǥ�ˤƸ���Ѳ������� 
 * ���������Ȥ�����ޤ����顢����ᥤ������겼������
 *
 */

import java.applet.*;
import java.lang.*;
import java.awt.*;
import java.net.*;

/**
 * This class is for the Othello game. This is the first applet I wrote.<br>
 * ������ñ��ʡ����������ࡣ����ϻ䤬��ä��ǽ�Υ��ץ�åȤǤ���
 * @author Yukio Hirai (ʿ�� ����) 
 * <a href="mailto:t94369yh@sfc.keio.ac.jp">(t94369yn@sfc.keio.ac.jp)</a><br>
 *  - URL <A HREF="http://www.sfc.keio.ac.jp/~t94369yh/">
 * http://www.sfc.keio.ac.jp/~t94369yh/</a>
 * @version 0.75  <i>Nov 1, 1995</i>
 */
public class Othello extends Applet implements Runnable {
  /**
   * The stete of a particular cell.<br>
   * �������פξ��֤�ɽ��
   */
  final int BLACK =  1;	
  final int WHITE = -1;
  final int EMPTY =  0;
  
  /**
   * Width of the board<br>
   * �������פ��礭��
   */
  final int WIDTH = 480;
  
  /**
   * Space at bottom right<br>
   * �����ζ������ڡ���
   */
  final int SPACE = 80;
  
  /**
   * Directions<br>
   * ������ɽ�����
   */
  private final int UPPER 	= 0;
  private final int LOWER 	= 1;
  private final int RIGHT 	= 2;
  private final int LEFT  	= 3;
  private final int UPPERLEFT 	= 4;
  private final int UPPERRIGHT	= 5;
  private final int LOWERRIGHT	= 6;
  private final int LOWERLEFT	= 7;
  boolean direction[] = 
    {false, false, false, false, false, false, false, false};
  
  /**
   * Turn.<br>
   * ����
   */
  public int turn;
  
  /**
   * The othello board. matrix of 8 * 8.<br>
   * �������ס�8 * 8�ޥ���
   */
  protected int stone[][];

  /**
   * Number of stones on the board<br>
   * �׾���Ф�����뤿����ѿ�
    */
  protected int counter_black = 0, counter_white = 0;

  /** 
   * column and row data for audio.<br>
   * �ɤ߾夲�Ѥ��ѿ����ѿ����Ϥ��������ޤꥹ�ޡ��ȤǤϤʤ�
   * �Τǲ��ɤ�;�Ϥ��ꡣ
   */
  int audioColumn, audioRow;

  /**
   * Computer player<br>
   * �������Υ���ԥ塼�������ϡ��Ф��֤���Ȥ����̵��٤�
   * �֤��Ƥ��������Υ��르�ꥺ��Ǥ��롣����Ū�ˤϡ����Υץ쥤���
   * �Ѿ����ơ���äȶ������르�ꥺ�������Ǥ���褦�ˤ�������
   */
  OthelloPlayer computer;

  /**
   * Initialize this applet.
   * Resizing and preparing AudioData.<br>
   * ���ץ�åȤ��������롣
   */
  public void init() {
    /* �ǽ��Applet�������������˸ƤФ�� */
    //System.out.println("Othello: init() --- �����");
    
    /* �������פ��� */
    stone = new int[8][8];
    
    /* �������פ����� */
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
	stone[i][j] = EMPTY;
      }
    }
    stone[3][3] = BLACK; stone[4][3] = WHITE;
    stone[3][4] = WHITE; stone[4][4] = BLACK;
    
    countStone();		/* �׾���Ф������ */

    turn = BLACK;		/* ������֤����� */
    
    /* Netscape2.0b1J�Ǥϡ�resize�����Թ礬����餷�� */
    /*resize(WIDTH*2 + SPACE, 1000 + WIDTH*2 + SPACE);  applet���礭�������� */
    
    /* ���ǡ����ν��� */
    for (int i = 1; i <= 8; i++) {
      getAudioClip(getCodeBase(), "audio/" + i + ".au");
    }
    getAudioClip(getCodeBase(), "audio/black.au");
    getAudioClip(getCodeBase(), "audio/white.au");

    computer = new OthelloPlayer(this);	/* �������Υ���ԥ塼������� */
  }
  

  /**
   * Paint this applet.<br>
   * ���ץ�åȤ����褹��
   */
  public void paint(Graphics g) {
    //System.out.println("Othello: paint() --- ����");

    drawBoard(g);	/* �������פ����� */
    /* �Ф����� */
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
	if (stone[i][j] != 0){
	  drawStone(i, j, g);
	}
      }
    }
    
    drawTurn(g); 	/* �ɤ�����֤����פβ��˼��� */
    drawCountStone(g);  /* �ơ����Фο������� */

    if (counter_black + counter_white == 64) {
      showWinner(g);
    }
  }
  

  /**
   * Draw the Othello board<br>
   * �������פ�����
   */
  public void drawBoard (Graphics g) {
    /* �Хå��ο����� */
    setBackground(Color.green);
    
    /* ���Ȥ����� */
    g.setColor(Color.black);
    g.drawLine(0,0, 0,WIDTH);		
    g.drawLine(WIDTH,0, WIDTH,WIDTH);	
    g.drawLine(0,0, WIDTH,0);		
    g.drawLine(0,WIDTH, WIDTH,WIDTH);	
    
    for(int i = 1; i < 8; i++){
      /* ��������� */
      g.drawLine(WIDTH*i/8,0, WIDTH*i/8,WIDTH);
      /* ��������� */
      g.drawLine(0,WIDTH*i/8, WIDTH,WIDTH*i/8);
    }
  }
  

  /**
   * Paint stones on the board.<br>
   * �Ф�����
   */
  public void drawStone(int column, int row, Graphics g) {
    /* �������� */
    if (stone[column][row] == BLACK) {
      g.setColor(Color.black);
    } else if (stone[column][row] == WHITE){
      g.setColor(Color.white);
    }
    g.fillOval(column * WIDTH / 8 + 10, row * WIDTH / 8 + 10, 
		  WIDTH / 12, WIDTH / 12);
  }
  

  /**
   * Display which trun.
   * ���֤�ɽ��
   */
  void drawTurn(Graphics g){
    String black = "Black";		
    String white = "White";		
    String comment = " player's turn";		
    
    g.setColor(Color.blue);
    if (turn == BLACK) {
      g.drawString(black + comment, WIDTH/2, WIDTH + 35);
      g.setColor(Color.black);
      showStatus("Your turn!");
    } else {
      g.drawString(white + comment, WIDTH/2, WIDTH + 35);
      g.setColor(Color.white);
      showStatus("Compurter's turn!");
    }
    /* ���򼨤��������� */
    g.fill3DRect(WIDTH/2 - 20, WIDTH+20, 20, 20, true);
  }
  

  /**
   * Show win or lose.<br>
   * ���Ԥ�ɽ������
   */
  void showWinner(Graphics g){
    /* �ե���Ȥ����� */
    //Font font = getFont("Courier", Font.BOLD, 96);
    //g.setFont(font);

    g.clearRect(0, 0, WIDTH+1, WIDTH+1);

    if (counter_black > counter_white) {
      for(int  i = 0; i < 255; i++){
	//g.setColor(getColor(i, i, i));
	g.fillRect(0, 0, WIDTH+1, WIDTH+1);
	//g.setColor(getColor(0, i, i));
	g.drawString("You", 150, 100);
	g.drawString("Won!", 130, 200);
      }
    } else if (counter_black < counter_white) {
      g.setColor(Color.black);
      g.fillRect(0, 0, WIDTH+1, WIDTH+1);
      for(int i = 255; i >=0; i--){
	//g.setColor(getColor(i, i, i));
	g.drawString("You", 150, 100);
	g.drawString("lost...", 75, 200);
      }
    } else {
      g.drawString("Draw", 100, 100);
      g.drawString("Game", 100, 200);
    }
  }

  /**
   * Count the number of stones.
   * �׾���Ф������
   */
  void countStone() {
    counter_black = 0;
    counter_white = 0;
    
    /* �ơ����Ф������ */
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
	if(stone[i][j] == BLACK) counter_black++;
	if(stone[i][j] == WHITE) counter_white++;
      }
    }

    /* �����ཪλȽ�� */
    if (counter_black + counter_white == 64) {
      //System.out.println("Othello: countStone(���餢�ꡪ)");
      endGame();
      init();
    }
  }

  /**
   * Display winner. This method is called when the game is finished.
   */
  public void endGame(){
    if (counter_black > counter_white) {
      //System.out.println("Othello: endGame(���ξ�����)");
    } else if (counter_black < counter_white) {
      //System.out.println("Othello: endGame(��ξ�����)");
    } else {
      //System.out.println("Othello: endGame(����ʬ��)");
    }
    repaint();
    try {
      Thread.sleep(500);
    } catch(Exception e){
    }
  }
  

  /**
   * Display the number of stones.<br>
   * �Фο�������
   */
  void drawCountStone(Graphics g){
    g.setColor(Color.white);
    g.fill3DRect(WIDTH+15, 130, 30,20, false); /* ������񤯥ܥå��� */
    g.fill3DRect(WIDTH+15, 190, 30,20, false); /* ������񤯥ܥå��� */
    g.fill3DRect(WIDTH+5, 160, 20,20, true); /* �򥢥����� */
    g.setColor(Color.black);
    g.fill3DRect(WIDTH+5, 100, 20,20, true); /* ���������� */
    g.drawString("Black", WIDTH+30, 115);		
    g.drawString("White", WIDTH+30, 175);
    g.drawString(Integer.toString(counter_black), WIDTH+20, 145); 
    g.drawString(Integer.toString(counter_white), WIDTH+20, 205);
  }
  

  /**
   * When mouse button is released.<br>
   * �ޥ����ܥ��������줿���ν���
   */
  public boolean mouseUp(Event e, int x, int y) {
    /* ����å���ɸ���饪�����פι�����Ѵ� */
    int column = (int)(x / (WIDTH / 8));
    int row	   = (int)(y / (WIDTH / 8));
    
    if (turn == BLACK) {
      /* �Ф��֤��������֤ˤ��� */
      if (checkStone(column, row, turn) == true){
	turnStone(column, row, turn); /* �Ф��֤���΢�֤� */
	playAudio(column, row);       /* �ɤ߾夲�� */

	turn = - turn;	/* ���֤���� */
	countStone();   /* �������׾�Ρ��ơ����Фο�������� */
	//repaint();      /* ������ */
	update(getGraphics());
	
	try {
	  Thread.sleep(500);	/* �֤������ */
	} catch (Exception excep){
	}
      }
    }

    if (turn == WHITE) {
      computer.decide();	/* ����ԥ塼���μ� */
      countStone(); /* �������׾�Ρ��ơ����Фο�������� */
      //repaint();    /* ������ */
      update(getGraphics());
    }
    return true;
  }

/**
 * Speak out the column and the row by activating audio thread.
 */
public void playAudio(int column, int row){
    //System.out.print("Othello: playAudio(" + (column+1) + ", ");
    //System.out.print((row+1) + ", ");
    if (turn == BLACK) {
      //System.out.println("��)");
    } else {
      //System.out.println("��)");
    }

    /* ��������method�˰������Ϥ� */
    audioColumn = column + 1;
    audioRow = row + 1;
    
    /* ���������Ѥ�Thread��fork���� */
    (new Thread(this)).start();
  }    

  /**
   * Play the sound on another Thread.<br>
   * �̤�Thread�ξ�ǡ��������Ĥ餹
   */
  public void run() {
    /* ����ɤ߾夲�� */
    play(getCodeBase(), "audio/" + audioColumn + ".au");
    try {
      Thread.sleep(500); 
    } catch (Exception e) {
    }
    /* �Ԥ��ɤ߾夲�� */
    play(getCodeBase(),"audio/" + audioRow + ".au");
    //Thread.sleep(500); 
    /* �ɤ���μ꤫���ɤ߾夲�� */
    /*if (turn == BLACK){
      play(getCodeBase(), "audio/white.au");
    } else if (turn == WHITE) {
      play(getCodeBase(), "audio/black.au");
    }*/
    /* Thread������Ū���˴����뤳�ȤϤ��ξ�硢ɬ�פʤ��ʤ餷���� */
  }

  /**
   * Check whether a stone can be put anywhere.
   * 
   * <jp �פΤɤ������Ф��֤��뤫�ɤ���Ĵ�٤롣
   * ��ĤǤ��֤����꤬����ʤ� true, �֤��ʤ��ʤ� false ���֤�>
   * 
   * @return boolean true if you can put a stone
   */
  public boolean checkAll(int turn) {
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
	if(checkStone(i, j, turn)){
	  return true;
	}
      }
    }
    return false;
  }

  /**
   * Check whether a stone can be put there.<br>
   * �Ф��֤��뤫�ɤ���Ĵ�٤롣�֤���ʤ� true, �֤��ʤ��ʤ� false ���֤�
   * 
   * @return boolean true if you can put a stone there
   */
  public boolean checkStone(int column, int row, int color){
    //System.out.println("Othello: checkStone("+column+","+row+")");
    
    int i, j;
    
    /* ���������å���������� */
    for (i = 0; i < 8; i++){
      direction[i] = false;
    }
    
    if(stone[column][row] != 0) {
      /* �⤷���������Ф����ä��顢false���֤� */
      //System.out.println("Othello: checkStone(���Ǥ��Ф����뤾)");
      //System.out.println("Othello: checkStone(�����ˤ��֤��ʤ���)");
      return false;
    } else { 
      /* �� */
      if (column > 1 && stone[column-1][row] == -color) {
	for (i = column-2; i > 0 && stone[i][row] == -color; i--);
	if (stone[i][row] == color) {
	  //System.out.println("Othello: checkStone(����΢�֤��뤾)");
	  direction[LEFT] = true;
	}
      }
      /* �� */
      if (column < 6 && stone[column+1][row] == -color) {
	for (i = column+2; i < 7 && stone[i][row] == -color; i++);
	if (stone[i][row] == color) {
	  //System.out.println("Othello: checkStone(����΢�֤��뤾)");
	  direction[RIGHT] = true;
	}
      } 
      /* �� */
      if (row > 1 && stone[column][row-1] == -color) {
	for (j = row-2; j > 0 && stone[column][j] == -color; j--);
	if (stone[column][j] == color) {
	  //System.out.println("Othello: checkStone(�夬΢�֤��뤾)");
	  direction[UPPER] = true;
	}
      }
      /* �� */
      if (row < 6 && stone[column][row+1] == -color) {
	for (j = row+2; j < 7 && stone[column][j] == -color; j++);
	if (stone[column][j] == color) {
	  //System.out.println("Othello: checkStone(����΢�֤��뤾)");
	  direction[LOWER] = true;
	}
      }
      /* ���� */
      if (column > 1 && row > 1 && stone[column-1][row-1] == -color) {
	for (i = column-2, j = row-2; i > 0 && j > 0 
	     && stone[i][j] == -color; i--, j--);
	if (stone[i][j] == color) {
	  //System.out.println("Othello: checkStone(���夬΢�֤��뤾)");
	  direction[UPPERLEFT] = true;
	}
      }
      /* ���� */
      if (column < 6 && row > 1 && stone[column+1][row-1] == -color) {
	for (i = column+2, j = row-2; i < 7 && j > 0 
	     && stone[i][j] == -color; i++, j--);
	if (stone[i][j] == color) {
	  //System.out.println("Othello: checkStone(���夬΢�֤��뤾)");
	  direction[UPPERRIGHT] = true;
	}
      }
      /* ���� */
      if (column < 6 && row < 6 && stone[column+1][row+1] == -color) {
	for (i = column+2, j = row+2; i < 7 && j < 7 
	     && stone[i][j] == -color; i++, j++);
	if (stone[i][j] == color) {
	  //System.out.println("Othello: checkStone(������΢�֤��뤾)");
	  direction[LOWERRIGHT] = true;
	}
      }
      /* ���� */
      if (column > 1 && row < 6 && stone[column-1][row+1] == -color) {
	for (i = column-2, j = row+2; i > 0 && j < 7 
	     && stone[i][j] == -color; i--, j++);
	if (stone[i][j] == color) {
	  //System.out.println("Othello: checkStone(������΢�֤��뤾)");
	  direction[LOWERLEFT] = true;
	}
      } 

      /* �ɤ�����������΢�֤���ʤ顢true���֤� */
      for (i = 0; i < 8; i++){
	if (direction[i] == true){
	  return true;
	}
      }

      //System.out.println("Othello: checkStone(�ɤ���΢�֤��ʤ���)");
      //System.out.println("Othello: checkStone(�����ˤ��֤��ʤ���)");
      return false;
    }
  }

  /**
   * Turn the stone over.
   * <jp �ºݤ��Ф�Ҥä����֤�>
   */
  public void turnStone(int column, int row, int color) {
    /* �ޤ����Ф��֤� */
    stone[column][row] = color;

    /* i �����j �ϹԤ�ɽ���Τ��Ѥ��� */
    int i,j;

    /* �� */
    if (direction[LEFT] == true){
      //System.out.println("Othello: turnStone(����΢�֤���)");
      for (i = column-1; stone[i][row] != color; i--){
	stone[i][row] = - stone[i][row];
      }
    }
    /* �� */
    if (direction[RIGHT] == true){
      //System.out.println("Othello: turnStone(����΢�֤���)");
      for (i = column + 1; stone[i][row] != color; i++){
	stone[i][row] = - stone[i][row];
      }
    }
    /* �� */
    if (direction[UPPER] == true){
      //System.out.println("Othello: turnStone(���΢�֤���)");
      for (j = row - 1; stone[column][j] != color; j--){
	stone[column][j] = - stone[column][j];
      }
    }
    /* �� */
    if (direction[LOWER] == true){
      //System.out.println("Othello: turnStone(����΢�֤���)");
      for (j = row + 1; stone[column][j] != color; j++){
	stone[column][j] = - stone[column][j];
      }
    }
    /* ���� */
    if (direction[UPPERLEFT] == true){
      //System.out.println("Othello: turnStone(�����΢�֤���)");
      for (i = column-1, j = row-1; stone[i][j] != color; i--, j--){
	stone[i][j] = - stone[i][j];
      }
    }
    /* ���� */
    if (direction[UPPERRIGHT] == true){
      //System.out.println("Othello: turnStone(�����΢�֤���)");
      for (i = column+1, j = row-1; stone[i][j] != color; i++, j--){
	stone[i][j] = - stone[i][j];
      }
    }
    /* ���� */
    if (direction[LOWERRIGHT] == true){
      //System.out.println("Othello: turnStone(������΢�֤���)");
      for (i = column+1, j = row+1; stone[i][j] != color; i++, j++){
	stone[i][j] = - stone[i][j];
      }
    }
    /* ���� */
    if (direction[LOWERLEFT] == true){
      //System.out.println("Othello: turnStone(������΢�֤���)");
      for (i = column-1, j = row+1; stone[i][j] != color; i--, j++){
	stone[i][j] = - stone[i][j];
      }
    }

    //System.out.println("Othello: turnStone(΢�֤��ϡ������ޤ�)");    
  }
}

/**
 * This is a class which define the strategy of computer othello player.<br>
 * ���ϡ��Ф��֤���ޥ���̵��٤�����ǡ��Ф��֤������Υ��르�ꥺ�ࡣ
 * ����Ū�ˤϡ����Υ��饹���ĥ���뤳�Ȥˤ�äƼ�ʬ�Υץ쥤�䡼��
 * �Ĥ��äơ����򳫤��Ƥߤ�����
 *
 * @author Yukio Hirai (ʿ�� ����)
 * <a href="mailto:t94369yh@sfc.keio.ac.jp">(t94369yn@sfc.keio.ac.jp)</a>
 *  - URL <A HREF="http://www.sfc.keio.ac.jp/~t94369yh/">
 * http://www.sfc.keio.ac.jp/~t94369yh/</a>
 * @version 0.1  <i>Sep 3, 1995</i>
 */
class OthelloPlayer {
  Othello parent;

  int i,j;

  OthelloPlayer(Othello parent){
    this.parent = parent;
  }

  public void decide(){
    if (parent.checkAll(parent.turn) == true) {
      do {
	//Math.srandom(System.currentTimeMillis());
	i = (int)(Math.random() * 8);
	//Math.srandom(System.currentTimeMillis() * System.currentTimeMillis());
	j = (int)(Math.random() * 8);
      } 
      while (! parent.checkStone(i, j, parent.turn));
      
      parent.turnStone(i, j, parent.turn); /* �Ф��֤���΢�֤� */
      //parent.playAudio(i, j);              /* �ɤ߾夲�� */
    }
    parent.turn = - parent.turn;
  }
}
