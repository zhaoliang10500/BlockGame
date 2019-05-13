package ca.mcgill.ecse223.block.controller;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.mcgill.ecse223.block.application.*;
import ca.mcgill.ecse223.block.model.*;
import ca.mcgill.ecse223.block.persistence.*;

public class Block223ControllerTest {
	
	
private static int nextBlockID = 1;
	
	@BeforeClass
	public static void setUpOnce() {
		String filename = "testdata.block223";
		Block223Persistence.setFilename(filename);
		File f = new File(filename);
		f.delete();
	}
	
	@Before
	public void setUp() {
		// clear all data
		Block223 block223 = Block223Application.getBlock223();
		block223.delete(); 
	}
	
	public static void checkUser(String username, Block223 block223, int numberOfUsers, String passwordPlayer, String passwordAdmin) {
		assertEquals(block223.getUsers().size(), numberOfUsers); 
		if (numberOfUsers > 0) {
			assertEquals(block223.getUser(0).getUsername(), username); 
			if (passwordAdmin != null && passwordAdmin.length() > 0) {
				assertEquals(block223.getUser(0).getRole(1).getPassword(), passwordAdmin); 
				assertEquals(block223.getUser(0).getRole(0).getPassword(), passwordPlayer); 
			} else if (passwordPlayer != null && passwordPlayer.length() > 0) {
				assertEquals(block223.getUser(0).getRole(0).getPassword(), passwordPlayer); 
			} else {
				fail(); 
			}
		}
	}
	
	@Test
	public void testSignUp() {
		// add fake user
		try {
			Block223Controller.register("Donald Peter Davis", "letyouescape", "curvedown");
		} catch (Exception e) {
			fail(); 
		}
		checkUser("Donald Peter Davis", Block223Application.getBlock223(), 1, "letyouescape", "curvedown"); 
	}
	
	@Test
	public void testSignUpAdmin() {
		// add fake user
		try {
			Block223Controller.register("Donald Peter Davis", null, "curvedown");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "The player password needs to be specified."); 
		}
	}
	
	@Test
	public void testSignUpPlayer() {
		// add fake user
		try {
			Block223Controller.register("Donald Peter Davis", "letyouescape", null);
		} catch (Exception e) {
			fail(); 
		}
	}
	
	@Test
	public void testSignUpDuplicatedUsername() {
		try {
			Block223Controller.register("Donald Peter Davis", "letyouescape", "curvedown");
			Block223Controller.register("Donald Peter Davis", "letyouescape", "curvedown");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "The username has already been taken."); 
		}
		checkUser("Donald Peter Davis", Block223Application.getBlock223(), 1, "letyouescape", null); 
	}

}
