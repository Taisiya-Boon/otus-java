package ru.otus;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.Arrays;
import java.util.Comparator;

public class DIYarrayList<T> implements List<T> {

    private int size;

    private Object[] baseArrayList;

    public DIYarrayList() {
        this.baseArrayList = new Object[0];
        this.size = 0;
    }

    public DIYarrayList(int sizeNewDIYarrayList) {
        if ( sizeNewDIYarrayList >= 0 ) {
            this.baseArrayList = new Object[sizeNewDIYarrayList];
            this.size = sizeNewDIYarrayList;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public DIYarrayList(T... elemens) {
        for (T element : elemens) {
            Object[] buffer = this.baseArrayList;
            baseArrayList = new Object[size+1];
            for ( int i = 0 ; i < ( size + 1 ) ; i++ ) {
                if ( i != size ) {
                    baseArrayList[i] = buffer[i];
                } else {
                    baseArrayList[i] = element;
                }
            }
            size++;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return  size == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(baseArrayList , size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        Object[] buffer = this.toArray();
        baseArrayList = new Object[size+1];
        for ( int i = 0 ; i < ( size + 1 ) ; i++ ) {
            if ( i != size ) {
                baseArrayList[i] = buffer[i];
            } else {
                baseArrayList[i] = t;
            }
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        int cSize = c.toArray().length;
        Object[] cArray = c.toArray();
        Object[] buffer = this.toArray();
        baseArrayList = new Object[size+cSize];
        for ( int i = 0 ; i < ( size + cSize ) ; i++ ) {
            if ( i < size ) {
                baseArrayList[i] = buffer[i];
            } else {
                baseArrayList[i] = cArray[i-size];
            }
        }
        size += cSize;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Arrays.sort(baseArrayList, (Comparator)c );
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index) {
        if ( index >= 0 && index < size ) {
            return (T) baseArrayList[index];
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public T set(int index, T element) {
        if ( index >= 0 && index < size ) {
            baseArrayList[index] = element;
            return (T)baseArrayList;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void add(int index, T element) {
        Object[] buffer = this.toArray();
        baseArrayList = new Object[size+1];
        for ( int i = 0 ; i < ( size + 1 ) ; i++ ) {
            if ( i != index ) {
                baseArrayList[i] = buffer[i];
            } else {
                baseArrayList[i] = element;
            }
        }
        size++;
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new listItrerator(0);
    }

    private class listItrerator<E> implements ListIterator<E> {

        private int itr;
        private boolean firstIterable = false;

        public listItrerator(int index) {
            this.itr = index;
        }

        @Override
        public boolean hasNext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public E next() {
            if ( itr < size && itr >= 0) {
                if ( itr == 0 && firstIterable == false ) {
                    firstIterable = true;
                    return (E) baseArrayList[itr];
                } else if ( (itr + 1) == size ) {
                    return (E) baseArrayList[itr-1];
                } else {
                    itr = itr + 1;
                    return (E) baseArrayList[itr];
                }
            } else {
                throw new UnsupportedOperationException();
            }
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public E previous() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(E e) {
            if ( itr <= size ) {
                baseArrayList[itr] = e;
            } else {
                throw new UnsupportedOperationException();
            }
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException();
    }

    //Метод выводит на экран размер DITarrayList и его содержимое

    public void listString() {
        System.out.print("size: " + this.size + " DIYarrayList: ");
        for (int i = 0; i < this.size(); i++) {
            System.out.print(this.get(i) + " ");
        }
        System.out.println();
    }
}
