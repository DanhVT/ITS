package hcmut.its.algorithm;

import java.util.Arrays;

public class FIFOQueue <T extends Comparable<T>> implements Queue<T> {

	private static final int DEFAULT_CAPACITY = 2000;
	protected T[] array;
	protected int front;
	protected int end;

	/**
	 * Constructs a new BinaryHeap.
	 */
	@SuppressWarnings("unchecked")
	public FIFOQueue() {
		// Java doesn't allow construction of arrays of placeholder data types
		array = (T[]) new Comparable[DEFAULT_CAPACITY];
		front = 0;
		end = 0;
	}

	@Override
	public void add(T value) {
		// grow array if needed
		if (end >= array.length - 1) {
			array = this.resize();
		}
		array[end] = value;
		end++;
	}


	@Override
	public boolean isEmpty() {
		return end == front ;
	}

	@Override
	public T peek() {
		if (this.isEmpty()) {
			throw new IllegalStateException();
		}
		return array[front];
	}


	@Override
	public T remove() {
		T result = peek();
		front++;
		return result;
	}

	protected T[] resize() {
		return Arrays.copyOf(array, array.length * 2);
	}

}
