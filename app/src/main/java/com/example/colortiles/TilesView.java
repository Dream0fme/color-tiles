package com.example.colortiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

class Card {
    private Paint p = new Paint();
    int color = Color.RED, backColor = Color.LTGRAY;
    boolean isOpen = false;
    private float x, y, width, height;
    int row, column;

    public Card(float x, float y, float width, float height, int row, int column) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.row = row;
        this.column = column;
    }


    public boolean flip(float touchX, float touchY) {
        if (touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height) {
            isOpen = !isOpen;
            return true;
        } else return false;
    }

    public void draw(Canvas c) {
        if (isOpen) {
            p.setColor(color);
        } else p.setColor(backColor);
        c.drawRect(x, y, x + width, y + height, p);
    }
}

public class TilesView extends View {
    Context context;
    Random rand = new Random();

    int n = 4;
    static ArrayList<Card> listCards = new ArrayList<>();
    int widthCard = 200;
    int heightCard = 300;
    int distance = 55;

    int width, height; // ширина и высота канвы

    public TilesView(Context context) {
        super(context);
    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                listCards.add(new Card((widthCard * j + distance * j) + distance, heightCard * i + distance * i, widthCard, heightCard, i, j));
                Collections.shuffle(listCards);
            }
        }

        for (int i = 0; i < listCards.size(); i++) {
            if (rand.nextInt() % 2 == 0) listCards.get(i).isOpen = true;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();

        for (Card c : listCards) {
            c.draw(canvas);
        }
        int openedCards = 0;
        for (int i = 1; i < listCards.size(); i++) {
            if (listCards.get(i).isOpen) openedCards++;
        }

        if (openedCards == 15) {
            Toast.makeText(getContext(), "Вы выиграли!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 3) получить координаты касания
        int x = (int) event.getX();
        int y = (int) event.getY();
        // 4) определить тип события
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // палец коснулся экрана
            for (int i = 0; i < listCards.size(); i++) {
                if (listCards.get(i).flip(x, y)) {
                    listCards.get(i).isOpen = !listCards.get(i).isOpen;
                    for (int j = 0; j < listCards.size(); j++) {
                        if (listCards.get(j).row == listCards.get(i).row || listCards.get(j).column == listCards.get(i).column) {
                            if (listCards.get(j).isOpen) {
                                listCards.get(j).isOpen = false;
                                listCards.get(j).color = Color.LTGRAY;
                            } else {
                                listCards.get(j).isOpen = true;
                                listCards.get(j).color = Color.RED;
                            }
                        }
                    }
                }
            }
            int openedCards = 0;
            for (int i = 1; i < listCards.size(); i++) {
                if (listCards.get(i).isOpen) openedCards++;
            }

            if (openedCards == 15) {
                Toast.makeText(getContext(), "Вы выиграли", Toast.LENGTH_SHORT).show();
            }
        }
        invalidate(); // заставляет экран перерисоваться
        return super.onTouchEvent(event);
    }


    public void onClick() {
        listCards.clear();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                listCards.add(new Card((widthCard * j + distance * j) + distance, heightCard * i + distance * i, widthCard, heightCard, i, j));
                Collections.shuffle(listCards);
            }
        }
        for (int i = 0; i < listCards.size(); i++) {
            if (rand.nextInt() % 2 == 0) listCards.get(i).isOpen = true;
        }
        invalidate();
        Toast.makeText(getContext(), "Новая игра!", Toast.LENGTH_SHORT).show();

    }
}
