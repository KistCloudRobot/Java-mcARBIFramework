package kr.ac.uos.ai.arbi.monitor.model;

import java.util.Collection;


public class SequenceModel<T> extends AbstractListModel<T> {
	private static final int DEFAULT_BUFFER_SIZE = 100;
	private int bufferSize;

	public SequenceModel() {
		setBufferSize(100);
	}

	public SequenceModel(int bufferSize) {
		setBufferSize(bufferSize);
	}

	public void setBufferSize(int size) {
		this.bufferSize = size;
		if (this.bufferSize >= 0)
			return;
		this.bufferSize = 100;
	}

	public void add(int index, T element) {
		int size = size() + 1;
		if (size > this.bufferSize)
			remove(0);
		super.add(index, element);
	}

	public boolean add(T element) {
		if (element == null)
			return false;
		int size = size() + 1;
		if (size > this.bufferSize)
			remove(0);
		return super.add(element);
	}

	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}
}