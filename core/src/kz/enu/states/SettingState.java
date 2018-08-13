package kz.enu.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import kz.enu.ResID;
import kz.enu.TheTogyzQumalaq;

import static kz.enu.ResID.skinsList;
import static kz.enu.TheTogyzQumalaq.botLevel;
import static kz.enu.TheTogyzQumalaq.getBitmapFont;
import static kz.enu.TheTogyzQumalaq.sound;

/**
 * Created by SLUX on 02.06.2017.
 */

public class SettingState extends State implements InputProcessor {
    private float alpha;
    private Texture bg;
    private PrintWriter pw;
    public static FileHandle fileHandle;
    private static int selectedLanguage;
    private static int oldSelectedLanguage;
    private Texture homeTexture;
    private Texture soundTexture;
    private Texture soundOnTexture;
    private Texture soundOffTexture;
    private Texture wrapperTexture;

    private static final float DELAY = 0.5f;
    private static float currentTime;

    private static boolean isAnyThinkChanged;

    private static Sprite blackSprite;
    private static Texture blackBackground;

    private Rectangle homeRectangle;
    private Rectangle soundRectangle;
    private Rectangle acceptRectangle;
    private Rectangle designRectangle;
    private Rectangle languageLeftArrowRectangle;
    private Rectangle languageRightArrowRectangle;
    private Rectangle difficultyLeftArrowRectangle;
    private Rectangle difficultyRightArrowRectangle;
    private Rectangle volumeSlider;
    private static String volumeSliderBar;
    float wOK,wDesign,wLanguage,wLevel,wDifficulty,wProgress,wMusic;

    public SettingState(GameStateManager gsm) {
        super(gsm);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        isAnyThinkChanged = false;
        bg = new Texture(ResID.BACKGROUND + TheTogyzQumalaq.POSTFIX+".png");
        fileHandle = Gdx.files.local("profile.txt");
        try {
            pw = new PrintWriter(fileHandle.file());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        selectedLanguage = TheTogyzQumalaq.getIndexOfLanguage();
        oldSelectedLanguage = selectedLanguage;
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(TheTogyzQumalaq.getBitmapFont(),"OK");
        wOK = glyphLayout.width;
        glyphLayout.reset();
        glyphLayout.setText(TheTogyzQumalaq.getBitmapFont(),TheTogyzQumalaq.WORDS[15]);
        wDesign = glyphLayout.width;
        glyphLayout.reset();
        glyphLayout.setText(TheTogyzQumalaq.getBitmapFont(),TheTogyzQumalaq.WORDS[6]);
        wLanguage = glyphLayout.width;
        glyphLayout.reset();
        glyphLayout.setText(TheTogyzQumalaq.getBitmapFont(),TheTogyzQumalaq.WORDS[8]);
        wLevel = glyphLayout.width;
        glyphLayout.reset();
        glyphLayout.setText(TheTogyzQumalaq.getBitmapFont(),TheTogyzQumalaq.WORDS[7]);
        wDifficulty = glyphLayout.width;
        glyphLayout.reset();
        glyphLayout.setText(TheTogyzQumalaq.getBitmapFont(),"||||||||||||||||||||||||||||||");
        wProgress = glyphLayout.width;
        glyphLayout.reset();
        glyphLayout.setText(TheTogyzQumalaq.getBitmapFont(),TheTogyzQumalaq.WORDS[16]);
        wMusic = glyphLayout.width;

        //System.out.println(wProgress);
        homeTexture = new Texture(ResID.HOME + TheTogyzQumalaq.POSTFIX+".png");
        soundOnTexture = new Texture(ResID.SOUND + TheTogyzQumalaq.POSTFIX+".png");
        soundOffTexture = new Texture(ResID.SOUND_OFF + TheTogyzQumalaq.POSTFIX+".png");
        wrapperTexture = new Texture(ResID.WRAPPER + TheTogyzQumalaq.POSTFIX+".png");
        soundTexture = TheTogyzQumalaq.sound?soundOnTexture:soundOffTexture;
        homeRectangle = new Rectangle(812f,465f,homeTexture.getWidth()+20,homeTexture.getHeight()+20);
        soundRectangle = new Rectangle(812f,400f,soundTexture.getWidth()+20,soundTexture.getHeight()+20);
        acceptRectangle = new Rectangle(754f,30f,wOK+100f,70f);
        designRectangle = new Rectangle(120f,440f,wrapperTexture.getWidth(),80f);

        blackBackground = new Texture(ResID.BLACK_BG);
        blackSprite = new Sprite(blackBackground);
        alpha = 1f;
        blackSprite.setAlpha(alpha);
        blackSprite.setPosition(0,0);

        languageLeftArrowRectangle = new Rectangle(420f,250f,43f,50f);
        languageRightArrowRectangle = new Rectangle(420f+43f+getBitmapFont().getSpaceWidth()*2+ResID.LANGUAGE_WIDTH[2],250f,43f,50f);
        difficultyLeftArrowRectangle = new Rectangle(420f,350f,43f,50f);
        difficultyRightArrowRectangle = new Rectangle(420f+43f+getBitmapFont().getSpaceWidth()*2+ResID.LANGUAGE_WIDTH[2],350f,43f,50f);
        initSlider();
        camera.setToOrtho(false, TheTogyzQumalaq.WIDTH,TheTogyzQumalaq.HEIGHT);
        //System.out.println((420f+43f+getBitmapFont().getSpaceWidth()*2+ResID.LANGUAGE_WIDTH[2]));
        volumeSlider = new Rectangle(420f,140f,wProgress,60f);
    }

    private static void initSlider(){
        volumeSliderBar = "";
        int tmp = 0;
        for(int i = 0;i<(int)(TheTogyzQumalaq.getMusic().getVolume()*30);i++){
            tmp++;
            volumeSliderBar+="|";
            if(tmp>30){
                break;
            }
        }
    }
    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            Vector3 tmp = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(tmp);

            if(acceptRectangle.contains(tmp.x,tmp.y)){
                accept();
            }else if(homeRectangle.contains(tmp.x,tmp.y)){
                if(TheTogyzQumalaq.sound)TheTogyzQumalaq.getButtonSound().play();
                gsm.set(new MenuState(gsm,TheTogyzQumalaq.POSTFIX));
            }else if(designRectangle.contains(tmp.x,tmp.y)){
                if(TheTogyzQumalaq.sound)TheTogyzQumalaq.getButtonSound().play();
                gsm.set(new DesignState(gsm, selectedLanguage));
            }else if(soundRectangle.contains(tmp.x,tmp.y)){
                if(sound){
                    sound = false;
                    TheTogyzQumalaq.getMusic().pause();
                    soundTexture = soundOffTexture;
                }else {
                    sound = true;
                    TheTogyzQumalaq.getMusic().play();
                    soundTexture = soundOnTexture;
                }
            }else if(languageLeftArrowRectangle.contains(tmp.x,tmp.y)){
                if(TheTogyzQumalaq.sound)TheTogyzQumalaq.getButtonSound().play();
                selectedLanguage--;
                if(selectedLanguage == -1) selectedLanguage = 2;
            }else if(languageRightArrowRectangle.contains(tmp.x,tmp.y)){
                if(TheTogyzQumalaq.sound)TheTogyzQumalaq.getButtonSound().play();
                selectedLanguage =(selectedLanguage +1)%3;
            }else if(difficultyLeftArrowRectangle.contains(tmp.x,tmp.y)){
                if(TheTogyzQumalaq.sound)TheTogyzQumalaq.getButtonSound().play();
                botLevel--;
                if(botLevel == -1)botLevel = 3;
            }else if(difficultyRightArrowRectangle.contains(tmp.x,tmp.y)){
                if(TheTogyzQumalaq.sound)TheTogyzQumalaq.getButtonSound().play();
                botLevel=(botLevel+1)%4;
            }
            //System.out.println("[X:"+tmp.x +"][Y:"+ tmp.y+"]");
        }
    }

    private void accept(){
        TheTogyzQumalaq.getMusic().pause();
        TheTogyzQumalaq.getMusic().dispose();
        pw.println(skinsList[TheTogyzQumalaq.getIndexOfTheme()]);
        pw.println(TheTogyzQumalaq.sound);
        pw.println(selectedLanguage);
        pw.println(botLevel);
        pw.println((int)(TheTogyzQumalaq.getMusic().getVolume()*32));
        pw.flush();
        new TheTogyzQumalaq().reboot();
        gsm.set(new MenuState(gsm,TheTogyzQumalaq.POSTFIX));
    }

    @Override
    public void update(float dt) {

        if(alpha>0) {
            alpha-=0.08f;
            blackSprite.setAlpha(alpha);
        }
        currentTime+=dt;
        if(currentTime>DELAY)handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg,0,0, TheTogyzQumalaq.WIDTH,TheTogyzQumalaq.HEIGHT);
        for(int i = 0;i<4;i++){
            sb.draw(wrapperTexture,120f,440f-i*100f);
        }
            sb.draw(wrapperTexture,734f,33f);
        TheTogyzQumalaq.getBitmapFont().draw(sb,TheTogyzQumalaq.WORDS[15],(wrapperTexture.getWidth()-wDesign)/2+120f,500f);

        TheTogyzQumalaq.getBitmapFont().draw(sb,TheTogyzQumalaq.WORDS[6],420f-wLanguage,300f);
        TheTogyzQumalaq.getBitmapFontLil().draw(sb,ResID.LEFT_ARROW,420f,300f);
        TheTogyzQumalaq.getBitmapFont().draw(sb,ResID.LANGUAGES[selectedLanguage],420f+63f+(getBitmapFont().getSpaceWidth()*2+ResID.LANGUAGE_WIDTH[2] - ResID.LANGUAGE_WIDTH[selectedLanguage])/2,300f);
        TheTogyzQumalaq.getBitmapFontLil().draw(sb,ResID.RIGTH_ARROW,420f+43f+getBitmapFont().getSpaceWidth()*2+ResID.LANGUAGE_WIDTH[2],300f);

        TheTogyzQumalaq.getBitmapFont().draw(sb,TheTogyzQumalaq.WORDS[7],420f-wDifficulty,400f);
        TheTogyzQumalaq.getBitmapFontLil().draw(sb,ResID.LEFT_ARROW,420f,400f);
        TheTogyzQumalaq.getBitmapFont().draw(sb,TheTogyzQumalaq.WORDS[8]+" "+(TheTogyzQumalaq.botLevel+1),420f+getBitmapFont().getSpaceWidth()+43f+ResID.LEVEL_OFFSET[oldSelectedLanguage],400f);
        TheTogyzQumalaq.getBitmapFontLil().draw(sb,ResID.RIGTH_ARROW,420f+43+getBitmapFont().getSpaceWidth()*2+ResID.LANGUAGE_WIDTH[2],400f);

        TheTogyzQumalaq.getBitmapFont().draw(sb,volumeSliderBar,420f,200f);
        TheTogyzQumalaq.getBitmapFont().draw(sb,TheTogyzQumalaq.WORDS[16],420f-wMusic,200f);

        TheTogyzQumalaq.getBitmapFont().draw(sb,"OK",754f,90f);
        sb.draw(homeTexture,822f,475f);
        sb.draw(soundTexture,822f,410f);
        if(alpha>0) {
            blackSprite.draw(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        pw.close();
        bg.dispose();
        homeTexture.dispose();
        soundOffTexture.dispose();
        soundOnTexture.dispose();
        soundTexture.dispose();
        wrapperTexture.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            // Optional back button handling (e.g. ask for confirmation)
            gsm.set(new MenuState(gsm,TheTogyzQumalaq.POSTFIX));
            /*if (shouldReallyQuit)
                Gdx.app.exit();*/
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.ESCAPE){
            this.dispose();
            // Optional back button handling (e.g. ask for confirmation)
            Gdx.app.exit();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 tmp = new Vector3(screenX,screenY,0);
        camera.unproject(tmp);
            if(volumeSlider.contains(tmp.x,tmp.y)){
                System.out.println(Gdx.graphics.getWidth()+" "+screenX+":"+screenY+"   "+(screenX -(Gdx.graphics.getWidth()-TheTogyzQumalaq.WIDTH)/2-420f)/wProgress);
                TheTogyzQumalaq.getMusic().setVolume((screenX -(Gdx.graphics.getWidth()-TheTogyzQumalaq.WIDTH)/2-420f)/wProgress);
                initSlider();
            }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        return false;
    }
}

