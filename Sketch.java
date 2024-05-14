import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {
	
  // images
  PImage imgGoob;
  PImage imgGrass;
  PImage imgPlatform;

  // player related variables
  float fltGravity = 0;
  float fltGoobY = 250;
  float fltGoobX = 150;
  boolean boolLeft = false;
  boolean boolRight = false;
  boolean boolUp = false;
  int intDirection = 1;

  // platform related variables
  float fltPlatformY = -400;
  float fltPlatformX = -400;

  // grass variables
  float fltGrassY = 300;

  // array for background
  int[] arrBackg = {255, 255, 255};

  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
	// put your size call here
    size(400, 400);
  }

  /** 
   * Called once at the beginning of execution.  Add initial set up
   * values here i.e background, stroke, fill etc.
   */
  public void setup() {
    background(255, 255, 255);

    imgGoob = loadImage("Stand.png");
    imgGoob.resize(imgGoob.width * 2, imgGoob.height * 2);

    imgGrass = loadImage("Grass.png");
    imgGrass.resize(450, 150);

    imgPlatform = loadImage("Platform.png");
    imgPlatform.resize(imgPlatform.width * 2, imgPlatform.height * 2);

  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
    // background is made up of an array to make it easier to randomize
	  background(arrBackg[0], arrBackg[1], arrBackg[2]);
    // calling upon all sprites
    image(imgGrass, -25, fltGrassY);
    image(imgPlatform, fltPlatformX, fltPlatformY);
    goober(fltGoobX, fltGoobY);
  }

  /**
   * Player character, includes movement and gravity values and calls upon collision() and state()
   * 
   * @param fltX x-coordinate of player
   * @param fltY y-coordinate of player
   */
  private void goober(float fltX, float fltY) {

    // constant pull of gravity
    fltGoobY -= fltGravity;

    // movement related changes
    if (boolRight == true || boolLeft == true){
      fltGoobX += 5 * intDirection;
    }

    // gravity related changes
    if (isOnFloor() == true && boolUp == true){
      fltGravity = 11;
    } else if (isOnFloor() == true && boolUp == false) {
      fltGravity = 0;
    } else if (isOnFloor() == false){
      fltGravity -= 0.5;
    }

    // called after movement to avoid jank
    collision();
    // called last because purely cosmetic
    state();

    image(imgGoob, fltX, fltY);
  }

  /**
   * Checks if player is on the floor, and moves them slightly up if they are in the floor
   * 
   * @return returns true if player is on floor, and false if they are not
   */
  private boolean isOnFloor() {

    // checks if player is on grass
    if (fltGoobY >= fltGrassY - imgGoob.height)
    {
      if (fltGoobY > fltGrassY - imgGoob.height){
        fltGoobY -= 1;
      }
      return true;

    // checks if player is on platform
    } else if (fltGoobY >= fltPlatformY - imgGoob.height && fltGoobY <= fltPlatformY - imgGoob.height + 50 && fltGoobX + imgGoob.width - 5 >= fltPlatformX && fltGoobX + 5 <= fltPlatformX + imgPlatform.width) {
      if (fltGoobY > fltPlatformY - imgGoob.height){
        fltGoobY -= 1;
      }
      return true;

    // if player isn't on anything, return false
    } else {
      return false;
    }

  }

  /**
   * Calculates all collision, checking if the player is in a platform and preventing them from jumping through or walking through platforms.
   */
  private void collision() {
    // checks if player coords overlap with platform coords
    if(fltGoobX + imgGoob.width >= fltPlatformX && fltGoobX <= fltPlatformX + imgPlatform.width && fltGoobY + imgGoob.height > fltPlatformY && fltGoobY <= fltPlatformY + imgPlatform.height){
      if (boolRight == true || boolLeft == true){
        fltGoobX -= 5 * intDirection;
      }
       
      if (fltGravity > 0 && fltGoobY <= fltPlatformY + imgPlatform.height && isOnFloor() == false && fltGoobX + imgGoob.width > fltPlatformX && fltGoobX < fltPlatformX + imgPlatform.width){
        fltGravity = 0;
        fltGoobY += 5;
      }
    }
  }

  /**
   * Changes the player's sprite depending on their current state
   */
  private void state() {
    // floor sprites
    if(isOnFloor() == true){
      if(intDirection == 1){
        imgGoob = loadImage("Stand.png");
        imgGoob.resize(imgGoob.width * 2, imgGoob.height * 2);
      } else {
        imgGoob = loadImage("Stand2.png");
        imgGoob.resize(imgGoob.width * 2, imgGoob.height * 2);
      }
    // air sprites
    } else {
      if(intDirection == 1){
        imgGoob = loadImage("Jump.png");
        imgGoob.resize(imgGoob.width * 2, imgGoob.height * 2);
      } else {
        imgGoob = loadImage("Jump2.png");
        imgGoob.resize(imgGoob.width * 2, imgGoob.height * 2);
      }
    }
  }

  public void keyPressed(){
  // when keys are pressed, bools are true
    if(keyPressed == true){
      if (keyCode == RIGHT || key == 'd'){
        boolRight = true;
        intDirection = 1;
      }
      if (keyCode == LEFT || key == 'a'){
        boolLeft = true;
        intDirection = -1;
      } 
      if (keyCode == UP || key == 'w'){
        boolUp = true;
      }
  
      // change background to random combination
      if (keyCode == DOWN || key == 's'){
        arrBackg[0] = (int)random(255);
        arrBackg[1] = (int)random(255);
        arrBackg[2] = (int)random(255);
      }
    }
  }

  public void keyReleased(){
  // when keys are released, bools are false
    if (keyCode == RIGHT || keyCode == 68){
      boolRight = false;
    }
    if (keyCode == LEFT || keyCode == 65){
      boolLeft = false;
    } 
    if (keyCode == UP || keyCode == 87){
      boolUp = false;
    }

    // allows for short-hops
    if (fltGravity > 4 && boolUp == false){
      fltGravity -= 5;
    }

  }
  
  public void mousePressed(){
    if(mousePressed){
    // allows platform to be spawned in with a click
    if(fltPlatformX == -400 && fltPlatformY == -400){
      if(mouseY <= fltGrassY - imgPlatform.height / 2 && mouseY >= 0 + imgPlatform.height / 2 && mouseX <= width - imgPlatform.width / 2 && mouseX >= 0 + imgPlatform.width / 2){
        fltPlatformX = mouseX - imgPlatform.width / 2;
        fltPlatformY = mouseY - imgPlatform.height / 2;
      }
    }
    }
  }

  public void mouseDragged(){
    // if mouse is over platform, can drag it
    if(mouseX >= fltPlatformX && mouseX <= fltPlatformX + imgPlatform.width && mouseY >= fltPlatformY && mouseY <= fltPlatformY + imgPlatform.height){
      if(fltPlatformX + imgPlatform.width >= fltGoobX && fltPlatformX <= fltGoobX + imgGoob.width && fltPlatformY + imgPlatform.height >= fltGoobY && fltPlatformY <= fltGoobY + imgGoob.height){
        fltPlatformX -= mouseX - pmouseX;
        fltPlatformY -= mouseY - pmouseY;
      // if platform runs into player, moves away to prevent clipping
      } else {
        fltPlatformX += mouseX - pmouseX;
        fltPlatformY += mouseY - pmouseY;
      }
    }
  }
}