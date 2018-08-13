package kz.enu.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import kz.enu.ResID;
import kz.enu.TheTogyzQumalaq;


/**
 * Created by SLUX on 17.05.2017.
 */

public class GameOver extends State {

    private Texture background;
    //private Texture wrapperTexture;
    private static final float DELAY = 1.2f;
    private static float currentTime;
    private Sound win;
    float w, w1;

    public GameOver(GameStateManager gsm) {
        super(gsm);
        background = new Texture(ResID.BACKGROUND + TheTogyzQumalaq.POSTFIX + ".png");
        //wrapperTexture = new Texture(ResID.WRAPPER + TheTogyzQumalaq.POSTFIX+".png");

        win = Gdx.audio.newSound(Gdx.files.internal(ResID.WIN));
        if (TheTogyzQumalaq.sound) win.play();
        //bitmapFont.getData().setScale(2f);
        camera.setToOrtho(false, TheTogyzQumalaq.WIDTH, TheTogyzQumalaq.HEIGHT);
        currentTime = 0;
        GlyphLayout glyphLayout = new GlyphLayout();
        String item = PlayState.getResult();
        glyphLayout.setText(TheTogyzQumalaq.getBitmapFont(), item);
        w = glyphLayout.width;
        glyphLayout.reset();
        glyphLayout.setText(TheTogyzQumalaq.getBitmapFont(), PlayState.getGameOverWords());
        w1 = glyphLayout.width;
        //if (PlayState.getMode() == ResID.INTERNET) PlayState.getSocket().disconnect();

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set(new MenuState(gsm, TheTogyzQumalaq.POSTFIX));
        }
    }

    @Override
    public void update(float dt) {
        currentTime += dt;
        if (currentTime > DELAY)
            handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background, 0, 0, TheTogyzQumalaq.WIDTH, TheTogyzQumalaq.HEIGHT);
        //sb.draw(wrapperTexture,(TheTogyzQumalaq.WIDTH-wrapperTexture.getWidth())/2,290f);
        TheTogyzQumalaq.getBitmapFont().draw(sb, PlayState.getGameOverWords(), (TheTogyzQumalaq.WIDTH - w1) / 2, 350f);
        if (!PlayState.isTurn()) {
            TheTogyzQumalaq.getBitmapFont().draw(sb, PlayState.getResult(), (TheTogyzQumalaq.WIDTH / 2) - (w / 2), TheTogyzQumalaq.HEIGHT / 2);
        } else {
            TheTogyzQumalaq.getBitmapFont().draw(sb, PlayState.getResult(), (TheTogyzQumalaq.WIDTH / 2) - (w / 2), TheTogyzQumalaq.HEIGHT / 2);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        win.dispose();
        //wrapperTexture.dispose();
        System.out.println("GameOver Disposed");
    }


}
