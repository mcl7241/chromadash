package com.linmarina.lab12;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {
    Paint paint = new Paint();
    Sprite sprite = new Sprite();
    List<Sprite> colorSprites = new ArrayList<>();
    Sprite foodSprite, badSprite, sprite3;
    private TextToSpeech textToSpeech;
    private int score;

    public void setTextToSpeech(TextToSpeech textToSpeech) {
        this.textToSpeech = textToSpeech;
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //note, at this point, getWidth() and getHeight() will have access the the dimensions
        foodSprite = generateSprite();
        badSprite = generateSprite();
        sprite3 = generateSprite();
        badSprite.setColor(Color.GREEN);
        sprite3.setColor(Color.BLUE);
        colorSprites.add(generateColorSprite(Color.BLUE));
        colorSprites.add(generateColorSprite(Color.RED));
        colorSprites.add(generateColorSprite(Color.GREEN));
        colorSprites.add(generateColorSprite(Color.YELLOW));
        sprite.grow(250);
        sprite.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.image));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Set the background color
        paint.setColor(Color.parseColor("#54fdd6"));
        canvas.drawRect(getLeft(), 0, getRight(), getBottom(), paint);

        // Update and draw the main sprite
        sprite.update(canvas);
        sprite.draw(canvas);

        // Update and draw the food and bad sprites
        foodSprite.update(canvas);
        foodSprite.draw(canvas);
        badSprite.update(canvas);
        badSprite.draw(canvas);

        // Collision detection with the food sprite
        if (RectF.intersects(sprite, foodSprite)) {
            foodSprite = generateSprite();
            sprite.grow(10);
            handleCollisionEvent();
        }

        // Collision detection with the bad sprite
        if (RectF.intersects(sprite, badSprite)) {
            badSprite = generateSprite();
            badSprite.setColor(Color.GREEN);
            sprite.grow(-5);
        }

        // Draw and collision detection for colored circle sprites
        for (Sprite colorSprite : colorSprites) {
            colorSprite.update(canvas);
            colorSprite.draw(canvas);

            if (RectF.intersects(sprite, colorSprite)) {
                if (sprite.getColor() == colorSprite.getColor()) {
                    // If the colors match, increase the size of the sprite
                    sprite.grow(10);
                    score += 10;
                } else {
                    // If the colors do not match, decrease the size of the sprite
                    sprite.grow(-5);
                    score -= 5;
                }
                // Respawn the color sprite
                int index = colorSprites.indexOf(colorSprite);
                colorSprites.set(index, generateColorSprite(colorSprite.getColor()));
            }
        }

        // Force a redraw
        invalidate();
    }

    private Sprite generateSprite() {
        float x = (float) (Math.random() * (getWidth() - .1 * getWidth()));
        float y = (float) (Math.random() * (getHeight() - .1 * getHeight()));
        int dX = (int) (Math.random() * 12 - 5);
        int dY = (int) (Math.random() * 12 - 5);
        return new Sprite(x, y, x + .1f * getWidth(), y + .1f * getWidth(), dX, dY, Color.MAGENTA);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (badSprite.contains(event.getX(), event.getY())) {
                badSprite = generateSprite();
                badSprite.setColor(Color.GREEN);
            }
        }
        return true;
    }
    private Sprite generateColorSprite(int color) {
        // Generate a sprite with a given color
        Sprite colorSprite = generateSprite();
        colorSprite.setColor(color);
        return colorSprite;
    }

    private void handleCollisionEvent() {
        String textToSpeak = "Collision detected!";
        textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
    }
     public int getScore(){
        return score;
    }
}