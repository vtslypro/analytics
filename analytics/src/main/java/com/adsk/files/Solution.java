package com.adsk.files;

import java.io.IOException;
/**
 * Created by vatsalya on 9/14/16.
 */
public class Solution {
    public static class LinkedListNode{
        String val;
        LinkedListNode next;

        LinkedListNode(String node_value) {
            val = node_value;
            next = null;
        }
    }

    public static LinkedListNode _insert_node_into_singlylinkedlist(LinkedListNode head, String val){
        if(head == null) {
            head = new LinkedListNode(val);
        }
        else {
            LinkedListNode end = head;
            while (end.next != null) {
                end = end.next;
            }
            LinkedListNode node = new LinkedListNode(val);
            end.next = node;
        }
        return head;
    }
    /*
 * Complete the function below.
 */
/*
For your reference:
LinkedListNode {
    String val;
    LinkedListNode *next;
};
*/

    static LinkedListNode updateRoute(LinkedListNode initialRoute, String[] citiesToSkip) {


        LinkedListNode tempRoute = initialRoute;
        LinkedListNode routeIter = initialRoute;
        LinkedListNode updatedRoute = null;
        String itemStr =  "";

        while(routeIter.next != null){

            while(tempRoute.next != null){
                for(int i =0; i < citiesToSkip.length; i++){

                    if(updatedRoute.val.equals(citiesToSkip[i])){
                        LinkedListNode temp = new LinkedListNode(tempRoute.next.val);
                        temp.next = tempRoute.next;
                        tempRoute.val = tempRoute.next.val;
                        tempRoute.next = temp;
                    } else {
                        continue;
                    }
                }
//                updatedRoute = _insert_node_into_singlylinkedlist(tempRoute,);
                tempRoute = tempRoute.next;
            }

            routeIter = routeIter.next;
        }

//        while(routeIter!=null){
//            tempNext = routeIter.next;
//            for(int i =0; i < citiesToSkip.length; i++){
//                if(routeIter.val.equals(citiesToSkip)){
//                    if(updatedRoute == null){
//                        updatedRoute = new LinkedListNode(tempNext.val);
//                    }
//                    else if(routeIter.next.val!=null){
//                        LinkedListNode node = new LinkedListNode(routeIter.next.val);
//                        updatedRoute.next = node;
//                    }
//                } else {
//                    if(updatedRoute == null){
//                        updatedRoute = new LinkedListNode(routeIter.val);
//                    }
//                    else if(routeIter.next.val!=null){
//                        LinkedListNode node = new LinkedListNode(routeIter.next.val);
//                        updatedRoute.next = node;
//                    }
//                }
//            }
//            routeIter = routeIter.next;
//        }


        return updatedRoute;

    }

    public static void main(String[] args) throws IOException{
//        Scanner in = new Scanner(System.in);
//        final String fileName = System.getenv("OUTPUT_PATH");
//        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        LinkedListNode res;
        String[] initRoute = { "A","B","C","D"};
        int _initialRoute_size = 4;
        int _initialRoute_i;
        String _initialRoute_item;
        LinkedListNode _initialRoute = null;
        for(_initialRoute_i = 0; _initialRoute_i < _initialRoute_size; _initialRoute_i++) {
            try {
                _initialRoute_item = initRoute[_initialRoute_i];
            } catch (Exception e) {
                _initialRoute_item = null;
            }
            _initialRoute = _insert_node_into_singlylinkedlist(_initialRoute, _initialRoute_item);
        }


        int _citiesToSkip_size = 2;
        String[] _citiesToSkip = {"B","C"};
        String _citiesToSkip_item;
        for(int _citiesToSkip_i = 0; _citiesToSkip_i < _citiesToSkip_size; _citiesToSkip_i++) {
            try {
                _citiesToSkip_item = _citiesToSkip[_citiesToSkip_i];
            } catch (Exception e) {
                _citiesToSkip_item = null;
            }
            _citiesToSkip[_citiesToSkip_i] = _citiesToSkip_item;
        }

        res = updateRoute(_initialRoute, _citiesToSkip);
        while (res != null) {
            System.out.println(res.val);
            res = res.next;
        }

    }

}
