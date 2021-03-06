package kz.enu.states.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import kz.enu.TheTogyzQumalaq;
import kz.enu.ai.AI;
import kz.enu.states.model.GameStateManager;
import kz.enu.states.model.LocalGame;
/**
 * Single Player
 * Created by Meirkhan on 03.09.2018.
 */

public class SinglePlayerGame extends LocalGame {
    private static boolean isThinking;
    private static String sThinkingBar = "";

    // AI stuff
    private boolean isAIMoveTuzdykMaker = false;
    private boolean bAIHasTuzdyk = false;
    private boolean bAIMadeMove = false;

    // Time stuff
    private static final float DELAY = 1.2f;
    private static float currentTime;
    private static float deltaTime;
    private static float fThinkingDuration = 0;

    public SinglePlayerGame(GameStateManager gsm, boolean isNewGame) {
        super(gsm, isNewGame);
        isThinking = false;
        sThinkingBar = "";
        fThinkingDuration = 0;
    }

    @Override
    public void update(float dt) {
        animateThinkingBar(dt);
        super.update(dt);
    }

    @Override
    protected void handleInput() {
        super.handleInput();
        if (turn) {
            if (Gdx.input.justTouched()) {
                for (int i = 0; i < slots.length; i++) {
                    if (slots[i].isToched(camera)) {

                        move = slots[i].slotNumber;
                        bAnimationStarted = true;
                        bPlayerMadeMove = true;
                        if (slots[i].side == turn && slots[i].currentStonesNumber != 0)
                            regShot();
                        logic();
                        printState();
                    }
                }
            }
        } else {
            currentTime += deltaTime;
            isThinking = true;
            if (currentTime > DELAY) {
                switch (TheTogyzQumalaq.botLevel) {
                    case AI.EASY_AI:
                        move = AI.makeMoveEasyAI(slots);
                        break;
                    case AI.NORMAL_AI:
                        move = AI.makeMoveNormalAI(slots);
                        break;
                    case AI.HARD_AI:
                        move = AI.makeMoveHardAI(slots);
                        break;
                    case AI.EFFECTIVE_AI:
                        move = AI.makeMoveEffectiveAI(slots);
                        break;
                    default:
                        move = AI.makeMoveNormalAI(slots);
                }
                bAIMadeMove = true;
                bAnimationStarted = true;
                logic();
                currentTime = 0;
                printState();
                if (!bAIHasTuzdyk) for (int i = 0; i < 9; i++) {
                    if (slots[i].isTuzdyk) {
                        isAIMoveTuzdykMaker = true;
                        bAIHasTuzdyk = true;
                    }
                }
            }
        }
    }

    @Override
    protected void logic() {
        super.logic();
        if (bAIMadeMove) bAIMadeMove = false;
    }

    @Override
    protected int makeMove(int slotIndex) {
        bNeedErrorSound = !turn;
        return super.makeMove(slotIndex);
    }

    @Override
    protected void undo() {
        bUndoTurn = !turn;
        super.undo();
    }

    @Override
    protected void renderThinkingBar(SpriteBatch sb) {
        if (isThinking) TheTogyzQumalaq.getMainFont().draw(sb, sThinkingBar, 822f, 440f);
    }

    @Override
    protected void specificCorrectMoveAction() {
        if (bPlayerMadeMove && isMoveTuzdykMaker) {
            isMoveTuzdykMaker = false;
        }
        if (bAIMadeMove && isAIMoveTuzdykMaker) {
            isAIMoveTuzdykMaker = false;
        }
        if (!turn) {
            isThinking = false;
        }
    }

    @Override
    protected void specificUndoAction() {
        if (isAIMoveTuzdykMaker) {
            for (int i = 0; i < slots.length; i++) {
                if (slots[i].side == turn) {
                    slots[i].texture = slotTexture;
                    slots[i].isTuzdyk = false;
                    isAIMoveTuzdykMaker = false;
                    bAIHasTuzdyk = false;
                }
            }
        }
    }

    private void animateThinkingBar(float dt) {
        deltaTime = dt;
        if (isThinking) fThinkingDuration += dt;
        if (sThinkingBar.length() > 2) {
            sThinkingBar = "";
        } else if (fThinkingDuration > 0.3f) {
            sThinkingBar += ".";
            fThinkingDuration = 0;
        }
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        sSaveFile = "saveAI.txt";
    }

    @Override
    protected void initResource() {
        super.initResource();

    }


    @Override
    public String getGameOverWords() {
        if (isAtsyrau()) {
            if (!turn) return TheTogyzQumalaq.LOCALE[9];
            else return TheTogyzQumalaq.LOCALE[10];
        } else if (stoneBanks[0].currentStonesNumber == 81 && stoneBanks[1].currentStonesNumber == 81) {
            return TheTogyzQumalaq.LOCALE[11];
        } else if (stoneBanks[0].currentStonesNumber > stoneBanks[1].currentStonesNumber) {
            return TheTogyzQumalaq.LOCALE[9];
        } else {
            return TheTogyzQumalaq.LOCALE[10];
        }
    }
}