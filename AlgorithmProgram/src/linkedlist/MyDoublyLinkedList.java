package linkedlist;

public class MyDoublyLinkedList {
	
	Node head;
	Node tail;
	int size;

	private class Node{
		int data;
		Node next;
		Node prev;
		
		Node(int data){
			this.data = data;
			this.next = null;
			this.prev = null;
		}
	}
	
	public MyDoublyLinkedList() {
		this.head = null;
		this.tail = null;
		this.size =0;
	}
	
	private void addFirst(int data) {
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
	
	private void addLast(int data) {
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
	
	private void removeFirst() {
		if(head == null) {
			System.out.println("List is empty");
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
	
	private void removeLast() {
		if(head == null) {
			System.out.println("List is empty");
		}else if(head == tail){
			head = null;
			tail = null;
		}else {
			Node node = head;
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
	
	private void reverse() {
		if(head == null) {
			System.out.println("List is empty");
		}else {
			Node node = head;
			Node temp = null;
			while(node != null) {
				temp = node.prev;
				node.prev = node.next;
				node.next = temp;
				node = node.prev;
			}
		}
	}
	
	public void printFarword() {
		if(head == null) {
			System.out.println("List is empty");
		}else {
			Node node = head;
			while(node != null) {
				System.out.println("Node:"+node.data);
				node = node.next;
			}
		}
	}
	
	private void printBackword() {
		if(head == null) {
			System.out.println("List is empty");
		}else {
			Node node = tail;
			while(node != null) {
				System.out.println("Data:"+node.data);
				node = node.prev;
			}
		}
	}
	
	public static void main(String[] args) {
		MyDoublyLinkedList list = new MyDoublyLinkedList();
	}
}
