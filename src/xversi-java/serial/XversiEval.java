public class XversiEval {
 
  public static int maxLevel = 5 ;
  private static int bMaxLevel = 5 ;
  public static int lastnm = 13 ;
  private static int side ;  

  public static int Evaluate(XversiBoard b , int c , int turn , int turnNum) {

    int score ;

    if(!XversiAgent.CheckSide(c))
      return -9999998 ;

    side = c ;

    if(turnNum > 64 - lastnm) 
      maxLevel = 64 - turnNum ;
    else 
      maxLevel = bMaxLevel ;

    score = Eval(b , turn , maxLevel , turnNum , -9999996) ;
    return score ; 
  }

  protected static int Eval(XversiBoard b , int turn , int level , int turnNum , int prevScore) {

    int i ;
    int numMove ;
    int move ;
    Spot s ;
    int score ;
    int bestScore ;
    XversiMoveList list ;
    XversiBoard bb ;

    if(level == 0) return ComputeEval(b,turn,turnNum) ;
    list = XversiAgent.GenerateMove(b , turn) ; 
    if(list == null) {
      return ComputeEval(b , turn,turnNum) ; 
    } else {
      bb = new XversiBoard() ;
      s = new Spot() ;
      numMove = list.GetNum() ;
      if(side == turn) {
        bestScore = -9999997 ; 
        for(i = 0 ; i <= numMove ; i++) {
           bb.Copy(b) ;
           move = list.GetMove(i) ; 
           XversiAgent.TranslatePosition(move , s) ;
           XversiAgent.MakeMove(bb , s.x , s.y , turn) ;
           score = Eval(bb , (turn+1) % 2 , level - 1 , turnNum+1 , bestScore) ;
           if(score >= prevScore)
             return score ;
           if(score > bestScore) {
             bestScore = score ;
           }
        }

      } else {
        bestScore = 9999997 ;
        for(i = 0 ; i <= numMove ; i++) {
           bb.Copy(b) ;
           move = list.GetMove(i) ;
           XversiAgent.TranslatePosition(move , s) ;
           XversiAgent.MakeMove(bb , s.x , s.y , turn) ;
           score = Eval(bb , (turn+1) % 2 , level - 1 , turnNum+1 , bestScore) ;
           if(score <= prevScore) {
             return score ;
           }
           if(score < bestScore) {
             bestScore = score ;
           }
        } 

      }

      return bestScore ;

    } 

  }

  protected static int ComputeEval(XversiBoard b , int turn , int turnNum) {

    int score ;

    score = PieceCount(b , turn , turnNum) ;
    if(64 - turnNum == maxLevel)
      return score ;   

    score+= Mobility(b , turn , turnNum) ;
    score+= CheckCorner(b , turn) ;
    return score ;
  }

  protected static int PieceCount(XversiBoard b , int turn , int turnNum) {

    int count , count2 ;
    int temp ;
    int i , j ; 
    int k ;
    int score ;

    if(side == 0) {
      count  = b.GetBC() ;
      count2 = b.GetWC() ;
    } else if(side == 1) {
      count  = b.GetWC() ;
      count2 = b.GetBC() ;
    } else {
      return -9999991 ;
    }

    if(count == 0) return -9999994 ;
    if(count2 ==0) return 9999994  ;

    if(XversiBoard.BOARD_SQUARE == turnNum) {
      if(count > count2) {
        return 9999994 ;
      } else if(count < count2) {
        return -9999994;
      } else {
        return 0 ; 
      }
    }


    score = count - count2 ;
    if(turnNum < 40) {
      if((count+count2) / count > count + count2 - 8) {
        score *= 2 ;
      } else {
        score *= -2 ;
      }
    } else {
      score *= 3 ;
    } 
    //System.out.println("piece score: " + score) ;
    return score ;
  }


  protected static int Mobility(XversiBoard b , int turn , int turnNum) {

    int count , count2 ;
    int temp ;
    int score ;

    count = XversiAgent.CountMove(b,side) ;
    if(turnNum < 40) {
      score = 2*count  ;
    }else {
      score = 3*count  ;
    }
    //System.out.println("Mobility: " + score) ;
    return score ;
  }

  protected static int CheckCorner(XversiBoard b , int turn) {
    
    int count  ;
    int i , j ;
    int c[] = new int[4] ;
    int d ;
    int ch1, ch2 ;
    int score ;

    score = 0 ;
    count = 0 ;
    for(i = 1 ; i <= 8 ; i+=7) {
      for(j = 1 ; j <= 8 ; j+=7) {
        if((c[count] = b.GetBoard(i,j)) == side) {
          score += 20 ;
        } else if(c[count] == (side+1) % 2) {
          score -= 25 ;
        } else {
          ch1 = XversiAgent.CheckMove(b , i , j , side) ;
          ch2 = XversiAgent.CheckMove(b , i , j , (side+1) % 2) ;
          if(ch1 > 0 && (turn == side || ch2 <= 0)) {
            score += 20 ;
          } else if(ch2 > 0 && (turn != side || ch1 <= 0)) {
            score -= 25 ;
          }
          c[count] = -1 ;
        } 
        count++ ;
      }
    } 

    if(c[0] == -1) {
      if((d = b.GetBoard(2,2)) == side) {
        score -= 25 ;
      } else if(d == (side + 1) % 2) {
        score += 20 ;
      }
    } 

    if(c[1] == -1) {
      if((d = b.GetBoard(2,7)) == side) {
        score -= 25 ;
      } else if(d == (side + 1) % 2) {
        score += 20 ;
      }
    }

    if(c[2] == -1) {
    if((d = b.GetBoard(7,2)) == side) {
        score -= 25 ;
      } else if(d == (side + 1) % 2) {
        score += 20 ;
      }
    }
  
    if(c[3] == -1) {
      if((d = b.GetBoard(7,7)) == side) {
        score -= 25 ;
      } else if(d == (side + 1) % 2) {
        score += 20 ;
      }

    }
    //System.out.println("Corner: " + score) ;
    return score ;
 
  }

}

