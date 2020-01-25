package core.game;

import core.game_engine.AI.RayCast;
import core.game_engine.AI.Tile;
import core.game_engine.Sprite;
import core.game_engine.data_management.Serializable;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class LevelEditor {

    private PApplet parent;
    private RayCast rayCast;
    private LevelManager levelManager;
    public int level;
    public boolean isPressed = false;
    private boolean missClick = true;

    public LevelEditor(PApplet p){
        level = 1;
        this.parent = p;
        rayCast = new RayCast(this.parent);
        levelManager = new LevelManager(p);
    }

    public void updatePlay(){
        levelManager.getGame_manager().update();
        AiUpdate();
    }

    public void updateEdit(){
        levelManager.getGame_manager().update();
    }

    public void mouseReleased(){
        levelManager.getPlayer().slingShot.Trigger(parent.mouseX, parent.mouseY);
    }

    public boolean levelFinish(){
        if(levelManager.getPlayer().finishedLevel()){
            return true;
        }
        return false;
    }

    public void debugRay(){
        if(parent.mouseX > 350 && parent.mouseX < 450 && parent.mouseY > 370 && parent.mouseY < 300){
            if(!isPressed){
                rayCast.setDebugRay(true);
                isPressed = true;
            }else if(isPressed){
                rayCast.setDebugRay(false);
                isPressed = false;
            }
        }
    }

    public RayCast getRayCast() {
        return rayCast;
    }

    public boolean isMissClick() {
        return missClick;
    }

    public void levelSelect(){
        if(parent.mouseX > 0 && parent.mouseX < 80 && parent.mouseY > 400 && parent.mouseY < 480){
            level = 1;
            missClick = false;
        }else if(parent.mouseX > 80 && parent.mouseX < 160 && parent.mouseY > 400 && parent.mouseY < 480){
            level = 2;
            missClick = false;
        }else if(parent.mouseX > 160 && parent.mouseX < 240 && parent.mouseY > 400 && parent.mouseY < 480){
            level = 3;
            missClick = false;
        }else if(parent.mouseX > 240 && parent.mouseX < 320 && parent.mouseY > 400 && parent.mouseY < 480){
            level = 4;
            missClick = false;
        }else if(parent.mouseX > 320 && parent.mouseX < 400 && parent.mouseY > 400 && parent.mouseY < 480){
            level = 5;
            missClick = false;
        }else if(parent.mouseX > 400 && parent.mouseX < 480 && parent.mouseY > 400 && parent.mouseY < 480){
            level = 6;
            missClick = false;
        }else if(parent.mouseX > 480 && parent.mouseX < 560 && parent.mouseY > 400 && parent.mouseY < 480){
            level = 7;
            missClick = false;
        }else if(parent.mouseX > 560 && parent.mouseX < 640 && parent.mouseY > 400 && parent.mouseY < 480){
            level = 8;
            missClick = false;
        }else if(parent.mouseX > 640 && parent.mouseX < 720 && parent.mouseY > 400 && parent.mouseY < 480){
            level = 9;
            missClick = false;
        }else if(parent.mouseX > 720 && parent.mouseX < 800 && parent.mouseY > 400 && parent.mouseY < 480){
            level = 10;
            missClick = false;
        }else {
            missClick = true;
        }
    }

    public void loadLevel(){
        levelManager = new LevelManager(parent);
        levelManager.load(level);
    }


    public void keyPressedEdit(char key){
        switch (key){
            case 'p' :
                levelManager.itemType = "Platform";
                levelManager.createObject(parent.mouseX, parent.mouseY);
                break;
            case 'g' :
                levelManager.itemType = "Goal";
                levelManager.createObject(parent.mouseX, parent.mouseY);
                break;
            case 's' :
                levelManager.save();
                break;
            case 'l' :
                levelManager.load(level);
                break;
            case 'a':
                levelManager.itemType = "AI";
                levelManager.createObject(parent.mouseX, parent.mouseY);
                break;
            case 'b':
                levelManager.itemType = "Player";
                levelManager.createObject(parent.mouseX, parent.mouseY);
                break;
            case 'd':
                levelManager.remove();
                break;
        }
    }

    private void AiUpdate(){
        AI enemy;
        for(Sprite sprite : levelManager.getGame_manager().getGame_objects()){
            if(sprite.getClass().getSimpleName().equals("AI")){
                enemy = ((AI) sprite);
                if(!ExternalRayHit(enemy, levelManager.addedSprites)){
                    if(enemy.slingShot.getLength() == 0){
                        enemy.slingShot.Trigger((int)levelManager.getPlayer().position.x, (int)levelManager.getPlayer().position.y);
                    }
                }
            }
        }
    }

    private boolean ExternalRayHit(AI ai, ArrayList<Sprite> platformCheck){
        Sprite platform;
        for (Sprite sprite : platformCheck){     //AI slingShot component
            if (sprite.getClass().getSimpleName().equals("Platform")){
                platform = sprite;
                rayCast.update(ai.position.x, ai.position.y, levelManager.getPlayer().position.x, levelManager.getPlayer().position.y, platform.position.x, platform.position.y, platform.size.x, platform.size.y);
                if (rayCast.isHit()) {
                    return true;
                }
            }
        }
        return false;
    }

}
