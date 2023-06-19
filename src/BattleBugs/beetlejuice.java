
package BattleBugs;
import java.awt.Color;
import java.util.ArrayList;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Location;

public class beetlejuice extends BattleBug2012
{
    private int actCount;
    //I hope we are allowed to do this
    private ArrayList<Location> puLocs;
    private ArrayList<BattleBug> enemies = new ArrayList<>();
    public beetlejuice(int str, int def, int spd, String name, Color col)
    {
        super(str, def, spd, name, col);
        actCount = 0;
    }
    public void act()
    {
        String prio = "power";
        Location goTo;

        //Call the getPowerUpLocs() method and store the result in a variable named puLocs.
        puLocs = getPowerUpLocs();
        
        //Creates a list that contains all the enemies *NEAR THE BUG!!! ! ! ! ! ! !
        ArrayList<Actor> actors = getActors();
        for(Actor a : actors)
        {
            if(a instanceof BattleBug)
            {
                enemies.add((BattleBug)a);
            }
        }
        if(enemies.size() > 0) {
            Location temp = enemies.get(0).getLocation();
            double dist = 6;
            try {
                dist = distance(getLocation(), temp);
            }
            catch (Exception e) {}
            finally {
                if(dist < 5)
                    prio = "attack";
            }
        }
        if(actCount % 40 > 35) //if getting close to rocks dropping
            prio = "survive";
        
        // THUMBS DOWN EMOJI !!!!!!!!!!!!!!!!!!!!!!
        goTo = goFor(prio);
           
        //Call the getDirectionToward() method and store the result in a variable named dir.
        int dir = getDirectionToward(goTo);

        //Using the getDirection() method check to see if your bug is facing the desired direction dir
        //If so then call the move() method
        //if not then call turnTo() method towards the desired direction dir.
        if(getDirection()==dir)
           move2();
        else
            turnTo(dir);
        actCount++;
    }
    private Location goFor(String priority)
    {
        Location output = new Location(5,5); //initialized bc cant return if not
        BattleBug nearestEnemy;
        double dtne;
        switch (priority) //checks prio for what the bug should do
        {
            case "power": //if prio is power, go for PowerUp
                //CHECK TO SEE if there exists a PowerUp Location, if so then store the first location from the List into goTo
                double near = 210120;
                int nearest = -1;
                if(puLocs.size() > 0) {
                    for(int i = 0; i < puLocs.size(); i++) {
                        double distance = distance(getLocation(), puLocs.get(i));
                        if (distance < near) {
                            near = distance;
                            nearest = i;
                        }
                    }
                }    
                output = puLocs.get(nearest);
                break;
            case "survive": //if prio is survive, go to the middle (to avoid rocks)
                output = new Location(27/2, 27/2);
                break;
            case "attack": //if prio is attack, go to the nearest bug and attack
                nearestEnemy = enemies.get(0);
                dtne = distance(getLocation(), nearestEnemy.getLocation());
                
                if(nearestEnemy.getDefense() < getStrength() - 3 && getStrength() >= 5 && dtne < 6) {
                    output = enemies.get(0).getLocation();
                    if(this.getAmmo() > 0 && getDirection() == getDirectionToward(output))
                        attack();
                }
                else if(dtne < 6 && nearestEnemy.getStrength() - 3 >= getDefense())
                {
                    output = goFor("run"); //PLACEHOLDER
                }
                else
                    output = goFor("power");
                break;
            case "run":
                nearestEnemy = enemies.get(0);
                
                int awayDir = convertAngleToPositive(measurementOfAngleBetween(0, getDirectionToward(nearestEnemy.getLocation())) + 180) % 360;
                int newRow = this.getLocation().getRow();
                int newCol = this.getLocation().getCol();
                newRow = awayDir >= 135 && awayDir <= 225 ? newRow + 1 :
                        awayDir >= 315 || awayDir <= 45 ? newRow - 1 : 
                        newRow;
                newCol = awayDir >= 225 && awayDir <= 315 ? newCol + 1 :
                        awayDir >= 45 && awayDir <= 135 ? newCol - 1 : 
                        newCol;
                 //forgive me for my sins ms lee
                output = new Location(newRow, newCol);
                break;
        }
        
        return output;
    }
    private int convertAngleToPositive(int angle)
    {
        if(angle >= 0)
            return angle; //angle is already positive
        return 360 + angle;
    }
    private static double distance(Location p1, Location p2)
    {
        double dy = Math.pow((p2.getRow() - p1.getRow()), 2);
        double dx = Math.pow((p2.getCol() - p1.getCol()), 2);
        return Math.sqrt(dx + dy);
}
}
