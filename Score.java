public class Score {

    private static int INCREMENT = 15;
    private static int DECREMENT = 10;
    private int points;
    private int lives;
    private GameWindow window;



    public Score(GameWindow window){
        this.window = window;
        points = 0;
        lives = 3;
    }


    public void initialise(){
        setPoints(0);
        setLives(3);
    }


    public void resetScore(){
        points = 10;
        lives = 3;
        setPoints(0);
        setLives(3);
    }

    public void resetLives(){
        lives = 3;
        setLives(3);
    }


    public void increasePoints(){
        points += INCREMENT;
        updatePoints();
    }


    public boolean decreasePoints(){
        points -= DECREMENT;
        if(points < 0)      //don't show negative points
            points = 0;
        return updatePoints();
    }


    public void increaseLives(){
        if(lives == 5)      //maximum 5 lives
            return;
        lives ++;
        updateLives();
    }
    
    
    public boolean decreaseLives(){
        lives --;
        return updateLives();
    }


    public boolean updatePoints(){
        if (points <= 0)
            return false;
        return true;
    }


    private void setPoints(int p){
        points = p;
    }


    public boolean updateLives(){
        if (lives <= 0)          
            return false;        
        return true;
    }

    
    private void setLives(int l){
        lives = l;
    }


    public int getPoints(){
        return points;
    }


    public int getLives(){
        return lives;
    }
}
