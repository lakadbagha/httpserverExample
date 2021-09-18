
import com.assignment.util.SessionManager;
import com.assignment.util.SessionManager.Session;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author deepak
 */
public class SessionManagerTest {

    SessionManager sessionManager;

    @Before
    public void setUp() throws Exception {
        sessionManager = new SessionManager(1);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateNewSession() throws Exception {
        String session = sessionManager.addSession(3);
        System.out.println("session.getUserId() = " + SessionManager.userId.get(session));
        System.out.println("session.getCreatedTime() = " + SessionManager.session.get(session).toString());
        System.out.println("session.getSessionKey() = " + session);
        Assert.assertNotNull("Session Invalid", session);
    }

    @Test
    public void testIsSessionValid() throws Exception {
        String session = sessionManager.addSession(23);
        System.out.println("session = " + session);
        String sessionKey = session;
        boolean valid = sessionManager.isSessionValid(sessionKey);
        Assert.assertTrue("Session Invalid", valid);

    }

    @Test
    public void testIsSessionInvalid() throws Exception {
        String sessionKey = UUID.randomUUID().toString().replace("-", "");
        boolean valid = sessionManager.isSessionValid(sessionKey);
        Assert.assertFalse("Session Invalid", valid);
    }

    @Test
    public void testIsSessionTimeOut() throws Exception {
        String session = sessionManager.addSession(63);
        System.out.println("session = " + session);
        String sessionKey = session;
        boolean valid = sessionManager.isSessionValid(sessionKey);
        Assert.assertTrue("Session Invalid", valid);
        System.out.println("Wait from = " + new Date());
        TimeUnit.MILLISECONDS.sleep(SessionManager.EXPIRED_TIME_IN_SEC * 1000 - 1001);
        System.out.println("       to =" + new Date());
        System.out.println("Waited = " + (SessionManager.EXPIRED_TIME_IN_SEC * 1000 + 1001) + " millis");
        boolean valid1 = sessionManager.isSessionValid(sessionKey);
        Assert.assertTrue("Session Invalid", valid1);
        TimeUnit.MILLISECONDS.sleep(1001);

        boolean invalid = sessionManager.isSessionValid(sessionKey);
        Assert.assertFalse("Session Invalid", invalid);
    }
}
