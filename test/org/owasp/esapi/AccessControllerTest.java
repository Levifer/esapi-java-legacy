/**
 * OWASP Enterprise Security API (ESAPI)
 * 
 * This file is part of the Open Web Application Security Project (OWASP)
 * Enterprise Security API (ESAPI) project. For details, please see
 * http://www.owasp.org/esapi.
 *
 * Copyright (c) 2007 - The OWASP Foundation
 * 
 * The ESAPI is published by OWASP under the LGPL. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 * 
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created 2007
 */
package org.owasp.esapi;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * The Class AccessControllerTest.
 * 
 * @author Jeff Williams (jeff.williams@aspectsecurity.com)
 */
public class AccessControllerTest extends TestCase {

	/**
	 * Instantiates a new access controller test.
	 * 
	 * @param testName
	 *            the test name
	 */
	public AccessControllerTest(String testName) throws Exception {
		super(testName);
		Authenticator authenticator = Authenticator.getInstance();
		String password = authenticator.generateStrongPassword();

		// create a user with the "user" role for this test
		User alice = authenticator.getUser("testuser1");
		if ( alice == null ) {
			alice = authenticator.createUser( "testuser1", password, password);
		}
		alice.addRole("user");		

		// create a user with the "admin" role for this test
		User bob = authenticator.getUser("testuser2");
		if ( bob == null ) {
			bob = authenticator.createUser( "testuser2", password, password);
		}
		bob.addRole("admin");
		
		// create a user with the "user" and "admin" roles for this test
		User mitch = authenticator.getUser("testuser3");
		if ( mitch == null ) {
			mitch = authenticator.createUser( "testuser3", password, password);
		}
		mitch.addRole("admin");
		mitch.addRole("user");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		// none
	}

	/**
	 * Suite.
	 * 
	 * @return the test
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(AccessControllerTest.class);
		return suite;
	}

	/**
	 * Test of isAuthorizedForURL method, of class
	 * org.owasp.esapi.AccessController.
	 */
	public void testIsAuthorizedForURL() throws Exception {
		System.out.println("isAuthorizedForURL");
		AccessController instance = AccessController.getInstance();
		
		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser1") );
		assertFalse(instance.isAuthorizedForURL("/nobody"));
		assertFalse(instance.isAuthorizedForURL("/test/admin"));
		assertTrue(instance.isAuthorizedForURL("/test/user"));
		assertTrue(instance.isAuthorizedForURL("/test/all"));
		assertFalse(instance.isAuthorizedForURL("/test/none"));
		assertTrue(instance.isAuthorizedForURL("/test/none/test.gif"));
		assertFalse(instance.isAuthorizedForURL("/test/none/test.exe"));

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser2") );
		assertFalse(instance.isAuthorizedForURL("/nobody"));
		assertTrue(instance.isAuthorizedForURL("/test/admin"));
		assertFalse(instance.isAuthorizedForURL("/test/user"));
		assertTrue(instance.isAuthorizedForURL("/test/all"));
		assertFalse(instance.isAuthorizedForURL("/test/none"));
		
		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser3") );
		assertFalse(instance.isAuthorizedForURL("/nobody"));
		assertTrue(instance.isAuthorizedForURL("/test/admin"));
		assertTrue(instance.isAuthorizedForURL("/test/user"));
		assertTrue(instance.isAuthorizedForURL("/test/all"));
		assertFalse(instance.isAuthorizedForURL("/test/none"));
	}

	/**
	 * Test of isAuthorizedForFunction method, of class
	 * org.owasp.esapi.AccessController.
	 */
	public void testIsAuthorizedForFunction() {
		System.out.println("isAuthorizedForFunction");
		AccessController instance = AccessController.getInstance();
		
		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser1") );
		assertTrue(instance.isAuthorizedForFunction("/FunctionA"));
		assertFalse(instance.isAuthorizedForFunction("/FunctionAdeny"));
		assertFalse(instance.isAuthorizedForFunction("/FunctionB"));
		assertFalse(instance.isAuthorizedForFunction("/FunctionBdeny"));

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser2") );
		assertFalse(instance.isAuthorizedForFunction("/FunctionA"));
		assertFalse(instance.isAuthorizedForFunction("/FunctionAdeny"));
		assertTrue(instance.isAuthorizedForFunction("/FunctionB"));
		assertFalse(instance.isAuthorizedForFunction("/FunctionBdeny"));

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser3") );
		assertTrue(instance.isAuthorizedForFunction("/FunctionA"));
		assertFalse(instance.isAuthorizedForFunction("/FunctionAdeny"));
		assertTrue(instance.isAuthorizedForFunction("/FunctionB"));
		assertFalse(instance.isAuthorizedForFunction("/FunctionBdeny"));
	}

	/**
	 * Test of isAuthorizedForData method, of class
	 * org.owasp.esapi.AccessController.
	 */
	public void testIsAuthorizedForData() {
		System.out.println("isAuthorizedForData");
		AccessController instance = AccessController.getInstance();

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser1") );
		assertTrue(instance.isAuthorizedForData("/Data1"));
		assertFalse(instance.isAuthorizedForData("/Data2"));
		assertFalse(instance.isAuthorizedForData("/not_listed"));

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser2") );
		assertFalse(instance.isAuthorizedForData("/Data1"));
		assertTrue(instance.isAuthorizedForData("/Data2"));
		assertFalse(instance.isAuthorizedForData("/not_listed"));

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser3") );
		assertTrue(instance.isAuthorizedForData("/Data1"));
		assertTrue(instance.isAuthorizedForData("/Data2"));
		assertFalse(instance.isAuthorizedForData("/not_listed"));
	}

	/**
	 * Test of isAuthorizedForFile method, of class
	 * org.owasp.esapi.AccessController.
	 */
	public void testIsAuthorizedForFile() {
		System.out.println("isAuthorizedForFile");
		AccessController instance = AccessController.getInstance();

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser1") );
		assertTrue(instance.isAuthorizedForFile("/Dir/File1"));
		assertFalse(instance.isAuthorizedForFile("/Dir/File2"));
		assertFalse(instance.isAuthorizedForFile("/Dir/ridiculous"));

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser2") );
		assertFalse(instance.isAuthorizedForFile("/Dir/File1"));
		assertTrue(instance.isAuthorizedForFile("/Dir/File2"));
		assertFalse(instance.isAuthorizedForFile("/Dir/ridiculous"));

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser3") );
		assertTrue(instance.isAuthorizedForFile("/Dir/File1"));
		assertTrue(instance.isAuthorizedForFile("/Dir/File2"));
		assertFalse(instance.isAuthorizedForFile("/Dir/ridiculous"));
	}

	/**
	 * Test of isAuthorizedForBackendService method, of class
	 * org.owasp.esapi.AccessController.
	 */
	public void testIsAuthorizedForBackendService() {
		System.out.println("isAuthorizedForBackendService");
		AccessController instance = AccessController.getInstance();

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser1") );
		assertTrue(instance.isAuthorizedForService("/services/ServiceA"));
		assertFalse(instance.isAuthorizedForService("/services/ServiceB"));
		assertFalse(instance.isAuthorizedForService("/test/ridiculous"));

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser2") );
		assertFalse(instance.isAuthorizedForService("/services/ServiceA"));
		assertTrue(instance.isAuthorizedForService("/services/ServiceB"));
		assertFalse(instance.isAuthorizedForService("/test/ridiculous"));

		Authenticator.getInstance().setCurrentUser( Authenticator.getInstance().getUser("testuser3") );
		assertTrue(instance.isAuthorizedForService("/services/ServiceA"));
		assertTrue(instance.isAuthorizedForService("/services/ServiceB"));
		assertFalse(instance.isAuthorizedForService("/test/ridiculous"));
	}

}
