import java.util.NoSuchElementException;

public class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T obj) {
        this.item = obj;
        this.next = null;
    }
    
    public Node(T obj, Node<T> next) {
    	this.item = obj;
    	this.next = next;
    }
    
    public final T getItem() {
    	return item;
    }
    
    public final void setItem(T item) {
    	this.item = item;
    }
    
    public final void setNext(Node<T> next) {
    	this.next = next;
    }
    
    public Node<T> getNext() {
    	return this.next;
    }
    
    public final void insertNext(T obj) {
      Node<T> nextNode = this.next;
      this.next = new Node<T>(obj);
      this.next.next = nextNode;
    }
    
    public final void removeNext() {
      if(this.next == null)
        throw new NoSuchElementException();
      else
        this.next = this.next.next;
    }
}