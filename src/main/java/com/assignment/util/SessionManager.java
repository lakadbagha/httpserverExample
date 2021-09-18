/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author deepak
 */
public class SessionManager {

    private final Timer sessionTimer;
    public static final long EXPIRED_TIME_IN_SEC = 60L;
    public static final Map<String, ArrayList<Date>> session = new ConcurrentHashMap<>();
    private final int leftLimit = 65; // letter 'a'
    private final int rightLimit = 90; // letter 'z'
    private final int targetStringLength = 7;
    private final Random random = new Random();
    public static Map<String, Integer> userId = new ConcurrentHashMap<>();

//    public static void main(String[] args) {
//        new SessionManager(3);
//        
//    }
    public SessionManager(int seconds) {

        // Timer() - Creates a new timer. The associated thread does not run as a daemon.
        sessionTimer = new Timer();

        // Schedules the specified task for repeated fixed-delay execution
        // schedule(): Schedules the specified task for repeated fixed-delay execution, beginning after the specified delay.
        // Subsequent executions take place at approximately regular intervals separated by the specified period.
        // In fixed-delay execution, each execution is scheduled relative to the actual execution time of the previous execution.
        // If an execution is delayed for any reason (such as garbage collection or other background activity), subsequent executions will be delayed as well.
        // In the long run, the frequency of execution will generally be slightly lower than the reciprocal of the specified period (assuming the system clock underlying Object.wait(long) is accurate).
        sessionTimer.schedule(new Session(), seconds * 1000L, seconds * 1000L);
    }

    // TimeTask: A task that can be scheduled for one-time or repeated execution by a Timer.
    // A timer task is not reusable. Once a task has been scheduled for execution on a Timer or cancelled,
    // subsequent attempts to schedule it for execution will throw IllegalStateException.
    public class Session extends TimerTask {

        public void run() {

            // We are checking for expired element from map every second
            sessionClearExpiredElementsFromMap();

        }
    }

    public String addSession(Integer user) {
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        addElementToMap(generatedString);
        sessionLog("Adding new Session\n\n");
        userId.put(generatedString, user);
        return generatedString;
    }

    // Check for element's expired time. If element is > time then remove it
    private static void sessionClearExpiredElementsFromMap() {

        // Date() - Allocates a Date object and initializes it so that it represents the time at which it was allocated, measured to the nearest millisecond.
        Date currentTime = new Date();
        Date actualExpiredTime = new Date();

        // if element time stamp and current time stamp difference is 5 second then delete element
        actualExpiredTime.setTime(currentTime.getTime() - EXPIRED_TIME_IN_SEC * 1000L);

        // size() - Returns the number of key-value mappings in this map. If the map contains more than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
//        System.out.println("map size:" + SessionManager.map.size());
        Iterator<Entry<String, ArrayList<Date>>> deepakIterator = SessionManager.session.entrySet().iterator();

        // hasNext() - Returns true if the iteration has more elements. (In other words, returns true if next would return an element rather than throwing an exception.)
        while (deepakIterator.hasNext()) {

            // next() - Returns the next element in the iteration.
            Entry<String, ArrayList<Date>> entry = deepakIterator.next();

            // getValue() - Returns the value corresponding to this entry.
            // If the mapping has been removed from the backing map (by the iterator's remove operation), the results of this call are undefined.
            ArrayList<Date> fiverrs = entry.getValue();

            while (fiverrs.size() > 0
                    && fiverrs.get(0).compareTo(actualExpiredTime) < 0) {
                sessionLog("----------- Expired session Deleted: " + entry.getKey());
                userId.remove(entry.getKey());
                fiverrs.remove(0);
            }

            if (fiverrs.size() == 0) {

                // remove() - Removes from the underlying collection the last element returned by this iterator (optional operation).
                // This method can be called only once per call to next.
                deepakIterator.remove();
            }
        }
    }

    // Adding new element to map with current timestamp 
    private static void addElementToMap(String deepak) {
        ArrayList<Date> mauryaList = new ArrayList<>();
        SessionManager.session.put(deepak, mauryaList);
        // add() - Appends the specified element to the end of this list.
        mauryaList.add(new Date());
    }

    private static void sessionLog(String string) {
        System.out.println(string);

    }

    /**
     * Method used to validate if an sessionKey is associated with a Valid and
     * Active Session in the Server
     *
     * @param sessionKey key for the Session to validate
     * @return a true if the sessionKey has a valid Session associated
     */
    public synchronized boolean isSessionValid(final String sessionKey) {
        ArrayList<Date> sessionActive = SessionManager.session.get(sessionKey);
        if (sessionActive == null) {
            return false;
        }
        Date actualExpiredTime = new Date();
        while (sessionActive.size() > 0
                && sessionActive.get(0).compareTo(actualExpiredTime) < 0) {
            sessionLog("----------- Expired session Deleted: " + sessionKey);
            sessionActive.remove(0);
            userId.remove(sessionKey);
        }

        return sessionActive != null;
    }
}
