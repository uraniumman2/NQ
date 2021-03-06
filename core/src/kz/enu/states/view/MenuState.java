package kz.enu.states.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import kz.enu.TheTogyzQumalaq;
import kz.enu.system.Registry;
import kz.enu.states.model.GameStateManager;
import kz.enu.states.view.menu.MultiplayerModeState;
import kz.enu.states.view.menu.LoadModeState;
import kz.enu.states.model.State;
import kz.enu.states.view.settings.SettingState;
import kz.enu.system.Util;

/**
 * Created by SLUX on 17.05.2017.
 */

public class MenuState extends State implements InputProcessor {

    private Texture background;

    public Rectangle rectanglePvp;
    public Rectangle rectanglePvc;
    public Rectangle rectangleSettings;
    public Rectangle rectangleTraining;

    private boolean backAnimatino;
    private int selected;

    float wPvp, wPvc, wSettings, wTraining;
    static float offset;
    private static final float boundX = 10f, boundY = 10f;

    public MenuState(GameStateManager gsm, String POSTFIX) {
        super(gsm);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        initTextures(POSTFIX);
        backAnimatino = false;
        offset = TheTogyzQumalaq.WIDTH * 0.56f;
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(TheTogyzQumalaq.getMainFont(), TheTogyzQumalaq.LOCALE[0]);
        wPvc = glyphLayout.width;
        glyphLayout.reset();
        glyphLayout.setText(TheTogyzQumalaq.getMainFont(), TheTogyzQumalaq.LOCALE[1]);
        wPvp = glyphLayout.width;
        glyphLayout.reset();
        glyphLayout.setText(TheTogyzQumalaq.getMainFont(), TheTogyzQumalaq.LOCALE[2]);
        wSettings = glyphLayout.width;
        glyphLayout.reset();
        glyphLayout.setText(TheTogyzQumalaq.getMainFont(), TheTogyzQumalaq.LOCALE[18]);
        wTraining = glyphLayout.width;
        rectanglePvc = new Rectangle(TheTogyzQumalaq.WIDTH * 0.44f - boundX, 348f - boundY, wPvc + boundX * 2, 50f + boundY * 2);
        rectanglePvp = new Rectangle(TheTogyzQumalaq.WIDTH * 0.44f - boundX, 256f - boundY, wPvp + boundX * 2, 50f + boundY * 2);
        rectangleTraining = new Rectangle(TheTogyzQumalaq.WIDTH * 0.44f - boundX, 164f - boundY, wPvp + boundX * 2, 50f + boundY * 2);
        rectangleSettings = new Rectangle(TheTogyzQumalaq.WIDTH * 0.44f - boundX, 72f - boundY, wSettings + boundX * 2, 50f + boundX * 2);
        camera.setToOrtho(false, TheTogyzQumalaq.WIDTH, TheTogyzQumalaq.HEIGHT);

    }

    private void initTextures(String POSTFIX) {
        background = Util.getTexture(Registry.MAIN_MENU);

    }

    @Override
    protected void handleInput() {

        Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(tmp);
        if (Gdx.input.justTouched()) {

            if (rectanglePvc.contains(tmp.x, tmp.y)) {
                backAnimatino = true;
                if (TheTogyzQumalaq.bPlaySound) TheTogyzQumalaq.getButtonSound().play();
                selected = 0;
            } else if (rectanglePvp.contains(tmp.x, tmp.y)) {
                backAnimatino = true;
                if (TheTogyzQumalaq.bPlaySound) TheTogyzQumalaq.getButtonSound().play();
                selected = 1;
            } else if (rectangleSettings.contains(tmp.x, tmp.y)) {
                backAnimatino = true;
                if (TheTogyzQumalaq.bPlaySound) TheTogyzQumalaq.getButtonSound().play();
                selected = 2;
            } else if (rectangleTraining.contains(tmp.x, tmp.y)) {
                backAnimatino = true;
                if (TheTogyzQumalaq.bPlaySound) TheTogyzQumalaq.getButtonSound().play();
                selected = 3;
            }
        }
    }

    @Override
    public void update(float dt) {
        if (offset > 0 && backAnimatino == false) offset -= 18f;
        if (backAnimatino) offset += 18f;
        handleInput();
        if (offset >= TheTogyzQumalaq.WIDTH * 0.8f && backAnimatino) {
            switch (selected) {
                case 0:
                    gsm.set(new LoadModeState(gsm, 0));
                    break;
                case 1:
                    gsm.set(new MultiplayerModeState(gsm, 1));
                    break;
                case 2:
                    gsm.set(new SettingState(gsm));
                    break;
                case 3:
                    gsm.set(new LoadingState(gsm, Registry.SINGLE_PLAYER, false));
                    break;
            }
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background, 0, 0, TheTogyzQumalaq.WIDTH, TheTogyzQumalaq.HEIGHT);
        TheTogyzQumalaq.getMainFont().draw(sb, TheTogyzQumalaq.LOCALE[0], TheTogyzQumalaq.WIDTH * 0.44f + offset, 398f);
        TheTogyzQumalaq.getMainFont().draw(sb, TheTogyzQumalaq.LOCALE[1], TheTogyzQumalaq.WIDTH * 0.44f + offset, 306f);
        TheTogyzQumalaq.getMainFont().draw(sb, TheTogyzQumalaq.LOCALE[18], TheTogyzQumalaq.WIDTH * 0.44f + offset, 214f);
        TheTogyzQumalaq.getMainFont().draw(sb, TheTogyzQumalaq.LOCALE[2], TheTogyzQumalaq.WIDTH * 0.44f + offset, 122f);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        System.out.println("MenuState Disposed");
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            // Optional back button handling (e.g. ask for confirmation)
            Gdx.app.exit();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
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
