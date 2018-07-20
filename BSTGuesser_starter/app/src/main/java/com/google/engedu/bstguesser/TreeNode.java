/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.bstguesser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static java.lang.Math.max;


public class TreeNode {
    private static final int SIZE = 60;
    private static final int MARGIN = 20;
    private int value, height;
    protected TreeNode left, right;
    private boolean showValue;
    private int x, y;
    private int color = Color.rgb(150, 150, 250);

    public TreeNode(int value) {
        this.value = value;
        this.height = 0;
        showValue = false;
        left = null;
        right = null;
    }

    private int getHeight(TreeNode curr) {
        if (curr == null)
            return -1;
        return curr.height;
    }

    private TreeNode rotateRight(TreeNode z) {
        TreeNode x = z.left;
        TreeNode y = x.right;

        x.right = z;
        z.left = y;

        z.height = max(getHeight(z.left), getHeight(z.right)) + 1;
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    private TreeNode rotateLeft(TreeNode z) {
        TreeNode x = z.right;
        TreeNode y = x.left;

        x.left = z;
        z.right = y;

        z.height = max(getHeight(z.left), getHeight(z.right)) + 1;
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    private int getBalance(TreeNode curr) {
        return (getHeight(curr.left) - getHeight(curr.right));
    }

    public TreeNode insert(TreeNode input, int valueToInsert) {
        int balance;

        if (input == null)
            return new TreeNode(valueToInsert);
        if (input.value > valueToInsert)
            input.left = insert(input.left, valueToInsert);
        else
            input.right = insert(input.right, valueToInsert);


        input.height = max(getHeight(input.left), getHeight(input.right)) + 1;
        balance = getBalance(input);


        if (balance > 1 && valueToInsert < input.left.value)
            return rotateRight(input);
        if (balance < -1 && valueToInsert > input.right.value)
            return rotateLeft(input);
        if (balance > 1) {
            input.left = rotateLeft(input.left);
            return rotateRight(input);
        }
        if (balance < -1) {
            input.right = rotateRight(input.right);
            return rotateLeft(input);
        }

        return input;
    }
    public int getValue(){
        return value;
    }
    public void positionSelf(int x0, int x1, int y) {
        this.y = y;
        x = (x0 + x1) / 2;

        if (left != null) {
            left.positionSelf(x0, right == null ? x1 - 2 * MARGIN : x, y + SIZE + MARGIN);
        }
        if (right != null) {
            right.positionSelf(left == null ? x0 + 2 * MARGIN : x, x1, y + SIZE + MARGIN);
        }
    }

    public void draw(Canvas c) {
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.GRAY);
        if (left != null)
            c.drawLine(x, y + SIZE / 2, left.x, left.y + SIZE / 2, linePaint);
        if (right != null)
            c.drawLine(x, y + SIZE / 2, right.x, right.y + SIZE / 2, linePaint);

        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(color);
        c.drawRect(x - SIZE / 2, y, x + SIZE / 2, y + SIZE, fillPaint);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(SIZE * 2 / 3);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(showValue ? String.valueOf(value) : "?", x, y + SIZE * 3 / 4, paint);

        if (height > 0) {
            Paint heightPaint = new Paint();
            heightPaint.setColor(Color.MAGENTA);
            heightPaint.setTextSize(SIZE * 2 / 3);
            heightPaint.setTextAlign(Paint.Align.LEFT);
            c.drawText(String.valueOf(height), x + SIZE / 2 + 10, y + SIZE * 3 / 4, heightPaint);
        }

        if (left != null)
            left.draw(c);
        if (right != null)
            right.draw(c);
    }

    public int click(float clickX, float clickY, int target) {
        int hit = -1;
        if (Math.abs(x - clickX) <= (SIZE / 2) && y <= clickY && clickY <= y + SIZE) {
            if (!showValue) {
                if (target != value) {
                    color = Color.RED;
                } else {
                    color = Color.GREEN;
                }
            }
            showValue = true;
            hit = value;
        }
        if (left != null && hit == -1)
            hit = left.click(clickX, clickY, target);
        if (right != null && hit == -1)
            hit = right.click(clickX, clickY, target);
        return hit;
    }

    public void invalidate() {
        color = Color.CYAN;
        showValue = true;
    }
}