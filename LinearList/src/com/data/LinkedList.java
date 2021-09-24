package com.data;


public class LinkedList {

	Node head;
	Node tail;
	int size;
	
	public int size() {
		return this.size;
	}
	
	public void add(int data) {
		Node node = new Node(data);
		if(head == null) {
			head = node;
			tail = node;
		}else {
			tail.next = node;
			tail = node;
		}
		size++;
	}
	
	public void addNodeAtStart(int data) {
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
	
	public void addNodeAtEnd(int data) {
		Node node = new Node(data);
		if(head == null) {
			head = node;
			tail = node;
		}else {
			tail.next = node;
			tail = node;
		}
		size++;
	}
	
	public void insertAt(int data , int index) {
		Node node = new Node(data);
		if(head == null) {
			head = node;
			tail = node;
		}else if(index == 0){
			node.next  = head;
			head = node;
		}else if(size+1 == index){
			tail.next = node;
			tail = node;
		}else {
			int count = 0;
			Node temp = head;
			while(temp != null && ++count < index) {
				temp = temp.next;
			}
			node.next = temp.next;
			temp.next = node;
		}
		size++;
	}
	
	public void print() {
		Node node = head;
		while(node != null) {
			System.out.println("Data ->"+node.data);
			node = node.next;
		}
	}
	
	public int removeFirst() {
		Node node = head;
		if(head == null)
			return 0;
		if(head == tail)
			head = tail = null;
		else
			head = head.next;
		size--;
		return node.data;
	}
	
	public int removeLast() {
		Node node = head;
		if(head == null)
			return 0;
		if(head == tail)
			return removeFirst();
		Node prev = null;
			while(node != tail) {
				prev  = node;
				node = node.next;
			}
			prev.next = null;
			tail = prev;
		size--;
		return node.data;
	}
	
	public int remove(int data) {
		Node current  = head;
		Node previous = null;
		while(current != null) {
			if(((Comparable<Integer>)data).compareTo(current.data) == 0) {
				System.out.println("middle :"+current.data);
				if(current == head)
					return removeFirst();
				if(current == tail)
					return removeLast();
				size--;
				previous.next = current.next;
				return current.data;
			}
			previous = current;
			current = current.next;
		}
		return 0;
	}
	
	public int removeAt(int index) {
		Node node = head;
		if(index < 1 || index > size) {
			System.out.println("Invalid position...");
			return head.data;
		}
		if(head == null)
			return 0;
		if(head == tail || index == 1)
			return removeFirst();
		if(index == size)
			return removeLast();
		Node prev = null;
		int count = 0;
		while(++count != index) {
			prev = node;
			node = node.next;
		}
		prev.next = node.next;
		node.next = null;
		return node.data;
	}
	
	public void reverse() {
		Node current = head;
		Node next = null;
		Node prev = null;
		while(current != null) {
			next = current.next;
			current.next = prev;
			prev = current;
			current = next;
		}
		head = prev;
	}
	
	public static void main(String[] s) {
		LinkedList linkedList = new LinkedList();
		linkedList.add(11);
		linkedList.add(12);
		linkedList.add(13);
		linkedList.addNodeAtStart(5);
		linkedList.addNodeAtEnd(8);
		linkedList.insertAt(90, 4);
		linkedList.print();
		System.out.println("Size:"+linkedList.size);
	    linkedList.reverse();
		linkedList.print();
	}
}

