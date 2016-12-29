package hcmut.its.algorithm;

public interface Queue<T> {

	/**
	 * Adds a value to the priority queue.
	 */
	public void add(T value);

	/**
	 * Tests if the priority queue is empty.
	 */
	public boolean isEmpty();

	/**
	 * Returns, but does not delete the element at the top of the priority
	 * queue.
	 * 
	 * @return the element at the top of the priority queue
	 * @throws IllegalStateException
	 *             if priority queue is empty
	 */
	public T peek();

	/**
	 * Deletes and returns the element at the top of the priority queue.
	 * 
	 * @return the element at the top of the priority queue
	 * @throws IllegalStateException
	 *             if priority queue is empty
	 */
	public T remove();
}
