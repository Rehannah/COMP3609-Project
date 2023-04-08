public class Score {

    private static int INCREMENT = 15;
    private static int DECREMENT = 10;
    private int points;
    private int lives;
    private GameWindow window;



    public Score(GameWindow window){
        this.window = window;
        points = 10;
        lives = 3;
    }


    public void resetScore(){
        points = 10;
        lives = 3;
        setPoints();
        setLives();
    }


    //display initial points
    public void initialise(){
        setPoints();
        setLives();
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
        setPoints();
        if (points <= 0)
            return false;
        return true;
    }


    public void setPoints(){
        window.setPoints(points);
    }


    public boolean updateLives(){
        setLives();
        if (lives <= 0)          
            return false;        
        return true;
    }

    
    public void setLives(){
        window.setLives(lives);
    }


    public int getPoints(){
        return points;
    }
}
