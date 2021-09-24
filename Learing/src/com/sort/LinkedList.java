package com.sort;

public class LinkedList {
	Node head;
	Node tail;
	int size;

	class Node{
		int data;
		Node next;
		Node(int data){
			this.data = data;
			this.next =null;
		}
	}
	
	public LinkedList() {
		this.head = null;
		this.tail = null;
		this.size = 0;
	}
	
	public void addFirst(int data) {
		Node node = new Node(data);
		if(head == null) {
			head = node;
			tail = node;
		}else {
			node.next = head;
			head = node;
		}
		size++;
	}
	
	public void removeFirst() {
		if(head==null) {
			System.out.println("List is empty");
		}else if(head == tail){
			head = tail = null;
			size--;
		}else {
			Node node = head;
			head = node.next; 
			node.next = null;
			size--;
		}
	}
	
	public void addLast(int data) {
		Node node = new Node(data);
		if(head == null) {
			head = tail = node;
		}else {
			tail.next = node;
			tail = node;
		}
		size++;
	}
	
	public void removeLast() {
		if(head == null) {
			System.out.println("List is empty");
		}else if(head == tail){
			head = tail = null;
			size--;
		}else {
			Node current = head;
			Node prev = null;
			while(current != tail) {
				prev = current;
				current = current.next;
			}
			tail=prev;
			prev.next =null;
			size--;
		}
	}
	
	public void addAtIndex(int index,int data) {
		Node node = new Node(data);
		if(index < 0 || index > size) {
			System.out.println("Invalid index");
		}else if(index == 0) {
			addFirst(data);
		}else if(index == size) {
			addLast(data);
		}else {
			int count=0;
			Node current = head;
			Node prev = null;
			while(current != null && ++count <= index) {
				prev = current;
				current = current.next;
			}
			node.next = prev.next;
			prev.next = node;
			size++;
		}
	}
	
	public void print() {
		Node node = head;
		if(head == null) {
			System.out.println("List is Empty");
		}else {
			System.out.println("Size:"+size+" Head:"+head.data+" Tail:"+tail.data);
			while(node != null) {
				System.out.println(node.data);
				node = node.next;
			}
		}
	}
	
	public void reverse() {
		if(head == null) {
			System.out.println("List is empty");
		}else {
			Node current = head;
			Node prev = null;
			Node next = null;
			while(current != null) {
				next = current.next;
				current.next = prev;
				prev = current;
				current = next;
			}
			head = prev;
		}
	}
	
	public void removeDuplicate() {
		Node node = head;
		Node prev = null;
		Node next = null;
		while(node != null) {
			prev = node;
			next = node.next;
			while(next != null) {
				if(node.data == next.data) {
					prev.next = next.next;
					size--;
				}else {
					prev = next;
				}
				next = next.next;
			}
			node = node.next;
		}
	}
	
	
	
	public static void main(String[] args) {
		LinkedList list = new LinkedList();
		list.addLast(11);
		list.addLast(11);
		list.addLast(13);
		list.addLast(15);
		list.addLast(18);
		list.addLast(18);
		list.addLast(20);
		list.addLast(23);
		list.addLast(23);
		list.print();
		list.removeDuplicate();
		list.print();
	}
}
