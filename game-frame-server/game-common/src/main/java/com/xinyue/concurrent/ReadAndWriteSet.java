package com.xinyue.concurrent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadAndWriteSet<E> {
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	private Set<E> set = new HashSet<>();

	public void add(E e) {
		lock.writeLock().lock();
		try {
			set.add(e);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public boolean contains(E e) {
		lock.readLock().lock();
		try {
			return set.contains(e);
		} finally {
			lock.readLock().unlock();
		}
	}

	public void remove(E e) {
		lock.writeLock().lock();
		try {
			set.remove(e);
		} finally {
			lock.writeLock().unlock();
		}
	}
}
