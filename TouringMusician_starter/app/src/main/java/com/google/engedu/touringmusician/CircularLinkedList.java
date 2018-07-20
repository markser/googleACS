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

package com.google.engedu.touringmusician;


import android.graphics.Point;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

        private Node(Point p) {
            this.point = p;
        }

    }

    Node head;

    public void insertBeginning(Point p) {
        Node foo = new Node(p);

        if (head == null) {
            head = foo;
            foo.next = head;
            foo.prev = head;

        } else {
            Node bar = head.prev;
            foo.next = head;
            foo.prev = bar;
            head.prev = foo;
            bar.next = foo;
        }
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y - to.y, 2) + Math.pow(from.x - to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        if (head != null) {
            Node temp = head;
            while (temp.next != head) {
                total += distanceBetween(temp.point, temp.next.point);
                temp = temp.next;
            }
        }
        return total;
    }



    public void insertNearest(Point p) {
//        //No Node at All Case
//        if(head==null)
//        {
//            insertBeginning(p);
//        }
//        //Only One Node Case
//        else if(head.next==head)
//        {
//            insertBeginning(p);
//        }
//        //More than 2 nodes case
//        else
//        {
//            Node temp=head;
//            Node minNode=head;
//            while(temp.next!=head)
//            {
//                float min=Float.MAX_VALUE;
//                float distance=distanceBetween(temp.point,p);
//                if(min>distance)
//                {
//
//                    min=distance;
//                    minNode=temp;
//                }
//                temp=temp.next;
//            }
//            Node node=new Node(p);
//            node.prev = minNode;
//            node.next = minNode.next;
//
//            minNode.next=node;
//            minNode=minNode.next;
//            minNode.prev=node;
//
//        }

        CircularLinkedListIterator iterator = new CircularLinkedListIterator();
        Node newNode = new Node(p);

        if (head == null){
            head = newNode;
            head.next = head;
            head.prev = head;
        }
        else {
            // find which node is the nearest first
            Node nearestNode = null;
            float nearestDist = 999999;
            while (iterator.hasNext()) {
                if (nearestNode == null) {
                    nearestNode = iterator.current;
                    nearestDist = distanceBetween(iterator.current.point, p);
                } else {
                    float distance = distanceBetween(iterator.current.point, p);

                    if (nearestDist > distance) {
                        // you found a new nearest node
                        nearestDist = distance;
                        nearestNode = iterator.current;
                    }
                }
                iterator.next();
            }
            Node tempPrev = nearestNode.prev;

            nearestNode.prev = newNode;
            newNode.next = nearestNode;
            newNode.prev = tempPrev;
            tempPrev.next = newNode;
        }
    }

    public void insertSmallest(Point p) {
        Node temp=head;
        //No Node at All Case
        if(temp==null)
        {
            insertBeginning(p);
        }
        //Only One Node Case
        else if(temp.next==head)
        {
            insertBeginning(p);
        }
        //More than 2 nodes case
        else
        {
            Node prevNode=temp;
            temp=temp.next;

            float prevDist=distanceBetween(prevNode.point,p);
            float nextDist=distanceBetween(p,temp.point);

            //We Need to Minimise the sum of prevDist + nextDist and Insert the new node at the minimum point

            while(temp!=head)
            {
                float presentTotalDistance=distanceBetween(temp.point,p)+distanceBetween(p,temp.next.point);
                if(presentTotalDistance<(prevDist+nextDist))
                {
                    prevNode=temp;
                }
                temp=temp.next;
            }

            Node node=new Node(p);
            node.prev = prevNode;
            node.next = prevNode.next;

            prevNode.next=node;
            prevNode=prevNode.next;
            prevNode.prev=node;
        }
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
