package com.appspot.smartshop.utils;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class ObjectPool<T> {

    private final Queue<T> storedObjs;
    private final Queue<T> usedObjs;

    public ObjectPool() {
        this.storedObjs = new ConcurrentLinkedQueue<T>();
        this.usedObjs = new ConcurrentLinkedQueue<T>();
    }

    public ObjectPool(Collection<? extends T> objects) {
        this.storedObjs = new ConcurrentLinkedQueue<T>(objects);
        this.usedObjs = new ConcurrentLinkedQueue<T>();
    }

    public abstract T createExpensiveObject();

    public T borrow() {
        T t;
        if ((t = storedObjs.poll()) == null) {
            t = createExpensiveObject();
        }
        
        this.usedObjs.add(t);
        return t;
    }

    public void giveBack(T object) {
    	this.usedObjs.remove(object);
        this.storedObjs.offer(object);   // no point to wait for free space, just return
    }
    
    public void giveBackAll(){
    	T t;
    	while ((t = usedObjs.poll())!= null){
    		this.storedObjs.offer(t);
    	}
    }
}
