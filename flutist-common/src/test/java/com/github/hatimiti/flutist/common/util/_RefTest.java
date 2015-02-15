package com.github.hatimiti.flutist.common.util;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class _RefTest {

	@Test
	public void test_getMethod() {
		assertTrue(_Ref.getMethod(Person.class, "getName", new Class[]{}).isPresent());
		assertTrue(_Ref.getMethod(Person.class, "getAge", new Class[]{}).isPresent());
		assertFalse(_Ref.getMethod(Person.class, "getage", new Class[]{}).isPresent());
		assertFalse(_Ref.getMethod(Person.class, "thinkX", new Class[]{}).isPresent());
		assertFalse(_Ref.getMethod(Person.class, "thinkY", Integer.class).isPresent());
		assertTrue(_Ref.getMethod(Person.class, "thinkY", int.class).isPresent());
	}
	
	@Test
	public void test_getAllFields() {
		List<Field> p = _Ref.getAllFields(Person.class);
		assertFalse(p.isEmpty());
		
		List<Field> e = _Ref.getAllFields(Empty.class);
		assertTrue(e.isEmpty());
	}
	
	@Data
	public static abstract class Animal {
		private String name;
		private int age;
	}
	
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class Person extends Animal {
		private int thinkingPower;
		
		protected String thinkX() {
			return "thinkX";
		}
		
		public String thinkY(int y) {
			return "thinkY" + y;
		}
	}
	
	public static class Empty {
	}
}
