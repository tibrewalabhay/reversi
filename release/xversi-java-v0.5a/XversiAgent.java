public class XversiAgent {

public void Init() {
}

 public static XversiMoveList GenerateMove(XversiBoard b , int side) {


    int x , y ;
    int numMove ;
    XversiMoveList list ;

    numMove = CountMove(b , side) ;
    System.out.println("num move " + numMove) ;
    if(numMove == 0) return null ;

    list = new XversiMoveList(numMove) ; 

    for(x = 1 ; x <= XversiBoard.BOARD_SIZE ; x++) {
      for(y = 1 ; y <= XversiBoard.BOARD_SIZE ; y++) {
        if(XversiAgent.CheckMove(b , x , y , side) > 0) {
          list.InsertMove(XversiAgent.TranslateXY(x,y)) ;
        }
      }
    }
    for(x = 0 ; x < numMove ; x++ ){
      System.out.println(list.GetMove(x) + " ") ;
    }

    return list ;
  }

public static boolean CheckXY(int x , int y) {

  if(x <= 0 || x > XversiBoard.BOARD_SIZE || y <= 0 || y > XversiBoard.BOARD_SIZE)
    return false ;

  return true ;

}

public static boolean CheckPosition(int position) {

  if(position <= 0 || position > XversiBoard.BOARD_SQUARE)
    return false ;

  return true ;

}


public static boolean CheckSide(int c) {

  if(c == 1 || c == 0)
    return true ;

  return false ;

}


public static int CheckMove(XversiBoard b, int x , int y , int c) {

  int i , j ;
  int side ;
  int nextSide ;
  int count ;

  int result = 0 ;

  if(b.GetBoard(x , y) != -1)
    return 0 ;

  if(!CheckXY(x,y))
    return 0 ;
  if(!CheckSide(c))
    return 0 ; 
 
  nextSide = (c+1) % 2 ;
 
  /* right */ 
  count = 0 ;
  for(i = x + 1 , j = y ; CheckXY(i,j) == true ; i++) {
    side = b.GetBoard(i , j) ;
    if(side == c) { 
      if(count > 0) 
        result |= 1 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  } 

  /* lower right */
  count = 0 ;
  for(i = x + 1 , j = y - 1 ; CheckXY(i,j) == true ; i++ , j--) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 2 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }


  /* down */
  count = 0 ;
  for(i = x , j = y - 1 ; CheckXY(i,j) == true ; j--) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 4 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }


  /* lower left */
  count = 0 ;
  for(i = x - 1 , j = y - 1 ; CheckXY(i,j) == true ; i-- , j--) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 8 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }


  /* left */
  count = 0 ;
  for(i = x - 1 , j = y ; CheckXY(i,j) == true ; i--) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 16 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }

  /* upper left */
  count = 0 ;
  for(i = x - 1 , j = y + 1 ; CheckXY(i,j) == true ; i-- , j++) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 32 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }

  /* up */
  count = 0 ;
  for(i = x , j = y + 1 ; CheckXY(i,j) == true ; j++) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 64 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  } 


  /* upper right */
  count = 0 ;
  for(i = x + 1 , j = y + 1 ; CheckXY(i,j) == true ; i++ , j++) {
    side = b.GetBoard(i , j) ;
    if(side == c) {
      if(count > 0) 
        result |= 128 ;
      break ;
    } else if(side == nextSide) {
      count ++ ; 
    } else
      break ;
  }

  return result ;

}

public static boolean MakeMove(XversiBoard b, int x , int y , int c) {

  int allowMove ;
  int i , j ;
  int nextSide ;

  if(!CheckXY(x,y) || !CheckSide(c))
    return false ;

  nextSide = (c + 1) % 2 ;

  allowMove = CheckMove(b , x , y , c) ;
  b.SetBoard(x , y , c) ;

  /* right */
  i = x + 1 ; 
  j = y ;
  if((allowMove & 1) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i++ ;  
    }
  }


  /* lower right */
  i = x + 1 ;
  j = y - 1 ;
  if((allowMove & 2) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i++ ; 
      j-- ;
    }
  }
  /* down */
  i = x ;
  j = y - 1 ;
  if((allowMove & 4) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      j-- ; 
    }
  }
  /* lower left */
  i = x - 1 ;
  j = y - 1 ;
  if((allowMove & 8) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i-- ;
      j-- ; 
    }
  }
  /* left */
  i = x - 1 ;
  j = y ;
  if((allowMove & 16) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i-- ;
    }
  }
  /* upper left */
  i = x - 1 ;
  j = y + 1 ;
  if((allowMove & 32) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i-- ;
      j++ ; 
    }
  }
  /* up */
  i = x ;
  j = y + 1 ;
  if((allowMove & 64) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      j++ ; 
    }
  }
  /* upper right */
  i = x + 1 ;
  j = y + 1 ;
  if((allowMove & 128) != 0) {
    while(b.GetBoard(i,j) == nextSide && CheckXY(x,y) == true) {
      b.SetBoard(i , j , c) ; 
      i++ ; 
      j++ ;
    }
  }

  return true ;

}


public static int TranslateXY(int x , int y) {

  int position ;

  if(!CheckXY(x,y))
    return 0 ;

  position = (y-1)*XversiBoard.BOARD_SIZE ;
  position += XversiBoard.BOARD_SIZE - x + 1 ;
  return position ;


}

public static boolean TranslatePosition(int position , Spot s) {

  if(!CheckPosition(position))
    return false ;

  s.x = XversiBoard.BOARD_SIZE - ((position-1) % XversiBoard.BOARD_SIZE)  ;
  s.y = (position-1) / XversiBoard.BOARD_SIZE + 1 ;
  return true ;

}


public static int CountMove(XversiBoard b , int side) {
  int count = 0 ;
  int x , y ;

  for(x = 1 ; x <= XversiBoard.BOARD_SIZE ; x++) {
    for(y = 1 ; y <= XversiBoard.BOARD_SIZE ; y++) {
      if(CheckMove(b , x , y , side) > 0) {
        count++ ;
      }
    }
  }

  return count ;
}


}
