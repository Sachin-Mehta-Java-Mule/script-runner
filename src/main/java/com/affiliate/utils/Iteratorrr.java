package com.affiliate.utils;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Java Iterator interface reference:
// https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html

class PeekingIterator2 implements Iterator<Integer> {

	private int cursor;
	private final Integer[] a;

	public PeekingIterator2(Integer[] iterator) {
		this.a = iterator;

	}

	// Returns the next element in the iteration without advancing the iterator.
	public Integer peek() {
		int i = cursor;
		if (i >= a.length) {
			throw new NoSuchElementException();
		}
		cursor = i;
		return a[i];
	}

	// hasNext() and next() should behave the same as in the Iterator interface.
	// Override them if needed.
	@Override
	public Integer next() {
		int i = cursor;
		if (i >= a.length) {
			throw new NoSuchElementException();
		}
		cursor = i + 1;
		return a[i];
	}

	@Override
	public boolean hasNext() {
		return cursor < a.length;
	}
}

//Java Iterator interface reference:
//https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html
class PeekingIterator implements Iterator<Integer> {

	private int cursor;
	private Integer[] a = new Integer[1000];

	public PeekingIterator(Iterator<Integer> iterator) {
		for (int i = 0; iterator.hasNext(); i++) {
			a[i] = (iterator.next());
		}
	}

	// Returns the next element in the iteration without advancing the iterator.
	public Integer peek() {
		int i = cursor;
		if (i >= a.length) {
			throw new NoSuchElementException();
		}
		cursor = i;
		return a[i];
	}

	// hasNext() and next() should behave the same as in the Iterator interface.
	// Override them if needed.
	@Override
	public Integer next() {
		int i = cursor;
		if (i >= a.length) {
			throw new NoSuchElementException();
		}
		cursor = i + 1;
		return a[i];
	}

	@Override
	public boolean hasNext() {
		return this.peek() != null;
	}
}

public class Iteratorrr {

	public static void main(String args[]) {
		Integer a[] = { 1, 2, 3};
		PeekingIterator peekingIterator = new PeekingIterator(Arrays.asList(a).iterator()); // [1,2,3]
		System.out.println(peekingIterator.next()); // return 1, the pointer moves to the next element [1,2,3].
		System.out.println(peekingIterator.peek()); // return 2, the pointer does not move [1,2,3].
		System.out.println(peekingIterator.next()); // return 2, the pointer moves to the next element [1,2,3]
		System.out.println(peekingIterator.next()); // return 3, the pointer moves to the next element [1,2,3]
//		System.out.println(peekingIterator.next()); // return 3, the pointer moves to the next element [1,2,3]
		System.out.println(peekingIterator.hasNext()); // return False
	}
}