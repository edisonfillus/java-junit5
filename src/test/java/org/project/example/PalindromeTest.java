package org.project.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PalindromeTest {

	@Test
	void testIsPalindrome() {
		
		String test1true = "Able was I ere I saw Elba";
		String test2true = "A man, a plan, a canal � Panama";
		String test3false = "This is fake";
		String test4false = "This is false";
		
		Palindrome palindromw = new Palindrome();
		
		assertTrue(palindromw.isPalindrome(test1true));
		assertTrue(palindromw.isPalindrome(test2true));
		assertFalse(palindromw.isPalindrome(test3false));
		assertFalse(palindromw.isPalindrome(test4false));
				
	}

}
