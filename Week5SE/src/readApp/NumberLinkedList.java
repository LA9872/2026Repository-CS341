package readApp;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A minimal singly linked list of real numbers (doubles), written from scratch
 * Design decisions:
 * - A tail reference is kept so add() is O(1) and never traverses the list.
 * - A running sum is maintained on every add(), so the mean needs no traversal.
 * - Only the standard deviation requires a pass over the list (one pass).
 * 
 * @author Leonardo A
 * @version 3.0
 */
public class NumberLinkedList implements Iterable<Double> {

	/** A single node holding one value and a link to the next node. */
	private static class Node {
		private final double value;
		private Node next;

		private Node(double value) {
			this.value = value;
		}
	}

	private Node head;
	private Node tail;
	private int size;
	private double sum;

	/** Appends a value to the end of the list in constant time. */
	public void add(double value) {
		Node node = new Node(value);
		if (head == null) {
			head = node;
		} else {
			tail.next = node;
		}
		tail = node;
		size++;
		sum += value;
	}

	/** @return number of values stored */
	public int size() {
		return size;
	}

	/** @return true if no values are stored */
	public boolean isEmpty() {
		return size == 0;
	}

	/** @return sum of all stored values (maintained incrementally) */
	public double getSum() {
		return sum;
	}

	/** @return arithmetic mean, or NaN if the list is empty */
	public double mean() {
		return size == 0 ? Double.NaN : sum / size;
	}

	/**
	 * Computes the sample standard deviation (divides by n - 1) using one
	 * traversal of the list. Uses the mean from the running sum.
	 *
	 * @return sample standard deviation, or NaN if fewer than 2 values exist
	 */
	public double sampleStandardDeviation() {
		if (size < 2) {
			return Double.NaN;
		}
		double mean = mean();
		double squaredDiffs = 0;
		for (double value : this) {
			double diff = value - mean;
			squaredDiffs += diff * diff;
		}
		return Math.sqrt(squaredDiffs / (size - 1));
	}

	@Override
	public Iterator<Double> iterator() {
		return new Iterator<Double>() {
			private Node current = head;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public Double next() {
				if (current == null) {
					throw new NoSuchElementException();
				}
				double value = current.value;
				current = current.next;
				return value;
			}
		};
	}
}