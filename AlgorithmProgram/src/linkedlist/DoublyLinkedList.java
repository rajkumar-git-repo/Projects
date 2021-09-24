package linkedlist;

public class DoublyLinkedList {

	private class Node {
		int data;
		Node next;
		Node prev;
		
		Node(int data){
			this.data = data;
			this.next = null;
			this.prev = null;
		}
	}
	
	private Node head;
	private Node tail;
	private int size;
	
	public DoublyLinkedList() {
	}
	
	public void addFirst(int data) {
		Node node = new Node(data);
		if(head == null) {
			head = node;
			tail = node;
		}else {
			head.prev = node;
			node.next = head;
			head = node;
		}
		size++;
	}
	
	public void addLast(int data) {
		Node node = new Node(data);
		if(head == null) {
			head = node;
			tail = node;
		}else {
			node.prev = tail;
			tail.next = node;
			tail = node;
		}
		size++;
	}
	
	public void addNodeAtIndex(int data, int index) {
		Node node = new Node(data);
		if(index < 1 || index > size+1) {
			System.out.println("Index out of range!");
		}else {
			if(head == null) {
				head = node;
				tail = node;
			}else if(index == 1) {
				addFirst(data);
			}else if(index == size+1) {
				addLast(data);
			}else {
				Node current = head;
				Node prev = null;
				int count = 1;
				while(current != null && count++ < index) {
					prev = current;
					current = current.next;
				}
				node.next = current;
				current.prev = node;
				prev.next = node;
				node.prev = prev;
			}
			size++;
		}
	}
	
	public void removeFirst() {
		if(head == null) {
			System.out.println("List is empty!"); 
		}else if(head == tail) {
			head = null;
			tail = null;
			size--;
		}else {
			head = head.next;
			head.prev = null;
			size--;
		}
	}
	
	public void removeLast() {
		Node node = head;
		if(head == null) {
			System.out.println("List is empty!"); 
		}else if(head == tail) {
			head = null;
			tail = null;
			size--;
		}else {
			Node prev = null;
			while(node != tail) {
				prev = node;
				node = node.next;
			}
			tail = prev;
			tail.next = null;
			size--;
		}
	}
	
	public void removeAtIndex(int index) {
		Node node = head;
		if(index < 1 || index > size) {
			System.out.println("Inde out of range!");
		}else if(head == tail || index == 1){
			removeFirst();
		}else if(index == size) {
			removeLast();
		}else {
			Node prev = null;
			int count = 1;
			while(node != tail && count++ < index) {
				prev = node;
				node = node.next;
			}
			Node temp = node.next;
			prev.next = temp;
			temp.prev = prev;
			size--;
		}
	}
	
	public void removeDuplicate() {
		Node node = head;
		if(head == null) {
			System.out.println("List is empty!");
		}else {
			Node prev = null;
			Node current = null;
			while(node != null) {
				prev = node;
				current = node.next;
				while(current != null) {
					if(node.data == current.data) {
						Node temp = current.next;
						prev.next = temp;
						if(temp != null) {
						    temp.prev = prev;
						}else {
							tail = prev;
							tail.next = null;
						}
						size--;
					}else {
						prev = current;
					}
					current = current.next;
				}
				node = node.next;
			}
		}
	}
	
	public void reverseDoublyLinkedList() {
		Node node = head;
		if(head == null) {
			System.out.println("List is empty!");
		}else {
			Node temp = null;
			
			while(node != null) {
				temp = node.prev;
				node.prev = node.next;
				node.next = temp;
				node = node.prev;
			}
			
			//if (temp != null) {
			//	head = temp.prev;
			//}	 
			temp = head;
			head = tail;
			tail = temp;
		}
	}
	
	public void printForward() {
		Node node = head;
		if(head == null ) {
			System.out.println("List is empty!");
		}else {
			System.out.println("Size:"+size+"\n");
			while(node != null) {
				System.out.println("DATA ["+node.data+"]");
				node = node.next;
			}
		}
	}
	
	public void printBackward() {
		System.out.println("\n");
		Node node = tail;
		if(head == null ) {
			System.out.println("List is empty!");
		}else {
			while(node != null) {
				System.out.println("DATA ["+node.data+"]");
				node = node.prev;
		    }
		}
	}
	
	public static void main(String[] args) {
		DoublyLinkedList list = new DoublyLinkedList();
		list.addLast(1);
		list.addLast(2);
		list.addLast(3);
		list.addLast(4);
		list.addLast(5);
		list.printForward();
		list.reverseDoublyLinkedList();
		list.printForward();
		list.printBackward();
	}
	
}
