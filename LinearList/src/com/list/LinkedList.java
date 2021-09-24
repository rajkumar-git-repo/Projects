package com.list;

public class LinkedList {

	Node head;
	Node tail;
	int size;

	public void addLast(int data) {
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

	public void addAtIndex(int index , int data) {
		Node node = new Node(data);
		if(size+1 == index) {
			throw new RuntimeException("Index out of range");
		}
		if(index == 0) {
			addFirst(data);
		}else if(index == size) {
			addLast(data);
		}else {
			int count =0;
			Node temp  = head;
			while(temp != null && ++count < index) {
				temp = temp.next;
			}
			node.next = temp.next;
			temp.next = node;
			size++;
		}
	}

	public int removeFirst() {
		Node node = head;
		if(head == null) {
			return 0;
		}
		if(head == tail) {
			head = null;
			tail = null;
		}else {
			head = head.next;
		}
		size--;
		return node.data;
	}

	public int removeLast() {
		Node node = head;
		if(head == null) {
			return 0;
		}
		if(head == tail) {
			return removeFirst();
		}else {
			Node prev = null;
			while(node != tail) {
				prev = node;
				node = node.next;
			}
			prev.next = null;
			tail = prev;
			size--;
		}
		return tail.data;
	}

	public int remove(int index) {
		Node node = head;
		if(node == null) {
			return 0;
		}
		if(head == tail || index == 1) {
			return removeFirst();
		}
		if(index == size) {
			return removeLast();
		}
		Node prev = null;
		int count = 0;
		while(++count < index) {
			prev = node;
			node = node.next;
		}
		prev.next = node.next;
		node.next = null;
		size--;
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
		while(prev != null) {
			System.out.print("->"+prev.data);
			prev = prev.next;
		}
	}
	
	public void search(int searchKey) {
		Node node = head;
		int count = 0;
		boolean flag = true;
		while(node != null) {
			count++;
			if(node.data == searchKey){
				flag = false;
				System.out.println("Item "+searchKey+" found at position :"+count);
			}
			node =node.next;
		}
		if(flag) {
			System.out.println("No item found in linked list");
		}
	}
	
	public void findMiddle() {
		//first way
		Node node = head;
		int count = 0;
		while(++count <= size/2) {
			node = node.next;
		}
		System.out.println("Middle node is : "+node.data);
		
		//second way
		Node slow = head;
		Node fast = head;
		while(slow != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		System.out.println("Middle node is : "+slow.data);
	}
	
	public void getIndexValueFromEnd(int index) {
		if(index < 1 || index > size) {
			System.out.println("Invalid position...");
		}
		Node first = head;
		Node second = head;
		int count =0;
		while(count < index) {
			first = first.next;
			count++;
		}
		
		while(first != null) {
			first = first.next;
			second = second.next;
		}
		System.out.println("Node of given index from end is :"+second.data);
	}
	
	public void removeDuplicate() {
		Node node = head;
		while(node != null && node.next != null) {
			if(node.data == node.next.data) {
				node.next = node.next.next;
				size--;
			}else {
				node = node.next;
			}
		}
	}

	public void addItemIntoSortedList(int data) {
		Node newNode = new Node(data);
		Node node = head;
		Node prev = null;
		while(node != null && node.data < newNode.data) {
			prev = node;
			node = node.next;
		}
		newNode.next = prev.next;
		prev.next = newNode;
		size++;
	}
	
	public void getOccurenceOfKey(int key) {
		Node node = head;
		int count =0;
		boolean flag = false;
		while(node != null) {
			if(node.data == key) {
				count++;
				flag = true;
			}
			node = node.next;
		}
		if(flag)
		   System.out.println("Occurence of "+key+" is:"+count);
		else {
			System.out.println("Given key not found");
		}
	}
	
	public void isPalindrome() {
		int[] temp = new int[size];
		Node node = head;
		int i = 0;
		while(node != null) {
			temp[i] = node.data;
			node = node.next;
			i++;
		}
		
		int j = 0;
		Node newNode = head;
		boolean flag = true;
		while(newNode != null && j<size/2) {
			if(temp[size-1-j] != newNode.data) {
				flag = false;
			}
			newNode = newNode.next;
			j++;
		}
		if(flag) {
			System.out.println("List is palindrome...");
		}else {
			System.out.println("List is not palindrome...");
		}
	}
	
	public void print(){
		System.out.print("Node :");
		Node node = head;
		while(node != null) {
			System.out.print("->"+node.data);
			node = node.next;
		}
		if(head != null)
			System.out.println("\nHead:"+head.data+"  Tail:"+tail.data+" Size:"+size);
	}

	public static void main(String[] args) {
		LinkedList list = new LinkedList();
		list.addLast(4);
		list.addLast(6);
		list.addLast(9);
		list.addLast(4);
		list.addLast(9);
		list.addLast(6);
		list.addLast(4);
		list.print();
		list.addItemIntoSortedList(4);
		list.print();
	}
}
