package linkedlist;


public class MyLinkedList {
	
	private Node head;
	private Node tail;
	private int size;
	
	private class Node{
		int data;
		Node next;
		
		Node(int data){
			this.data = data;
			this.next = null;
		}
	}

	public MyLinkedList() {
		this.head = null;
		this.tail = null;
		this.size = 0;
	}
	
	private void addFirst(int data) {
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
	
	private void removeFirst() {
		Node node = head;
		if(head == null) {
			System.out.println("Linked list is empty");
		}else if(head == tail){
			head = null;
			tail = null;
		}else {
			head = head.next;
			node.next = null;
			size--;
		}
	}
	
	private void addLast(int data) {
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
	
	private void removeLast() {
		Node node = head;
		if(head == null) {
			System.out.println("Linked list is empty");
		}else if(head == tail) {
			head = null;
			tail = null;
			size--;
		}else {
			Node temp = null;
			while(node != tail) {
				temp = node;
				node = node.next;
			}
			temp.next = null;
			tail = temp;
			size--;
		}
	}
	
	private void addAtIndex(int index, int data) {
		Node node = head;
		if(!(0 <= index && index <= size)) {
			System.out.println("Enter valid index");
		}else {
			if(head == null) {
				System.out.println("Linked List is empty");
			}else if(index == 0){
				addFirst(data);
			}else if(index == size) {
				addLast(data);
			}else {
				Node addNode = new Node(data);
				Node temp = null;
				int count =0;
				while(node != tail && ++count <= index) {
					temp = node;
					node = node.next;
				}
				addNode.next = node;
				temp.next = addNode;
				size++;
			}
		}
	}
	
	private void removeAtIndex(int index) {
		Node node = head;
		if(!(0 <= index && index < size)) {
			System.out.println("Enter valid index");
		}else {
			if(head == null) {
				System.out.println("Linked List is empty");
			}else if(index == 0){
				removeFirst();
			}else if(index+1 == size) {
				removeLast();
			}else {
				Node temp = null;
				int count =0;
				while(node != tail && ++count <= index) {
					temp = node;
					node = node.next;
				}
				temp.next = node.next;
				node.next = null;
				size--;
			}
		}
	}
	
	private void reverse() {
		Node current = head;
		if(head == null) {
			System.out.println("Linked list is empty");
		}else {
			Node prev = null;
			Node next = null;
			while(current != null) {
				next = current.next;
				current.next = prev;
				prev = current;
				current = next;
			}
			
			tail = head;
			head = prev;
		}
	}
	
	private Node reverseGivenList(Node list) {
		Node current = list;
		if(head == null) {
			System.out.println("Linked list is empty");
			return null;
		}else {
			Node prev = null;
			Node next = null;
			while(current != null) {
				next = current.next;
				current.next = prev;
				prev = current;
				current = next;
			}
			return prev;
		}
	}
	
	private void palindrome() {
		Node current  = head;
		boolean flag = true;
		if(head == null) {
			System.out.println("Linked list is empty");
		}else {
			Node node  = head;
			int mid = size%2 ==0? size/2 : (size+1)/2;
			for(int i=1;i<=mid;i++) {
				node =node.next;
			}
			Node reverse = reverseGivenList(node);
			
			while(reverse != null) {
				System.out.println(reverse.data+"----"+current.data);
				if(reverse.data != current.data) {
					flag = false;
					break;
				}
				reverse = reverse.next;
				current = current.next;
			}
		}
		if(flag) {
			System.out.println("Palindrome");
		}else {
			System.out.println("Not palindrome");
		}
	}
	
	private void removeDuplicate() {
		Node node = head;
		Node prevNode = null;
		Node nextNode = null;
		while(node != null) {
			prevNode = node;
			nextNode = node.next;
			while(nextNode != null) {
				if(node.data == nextNode.data) {
					 System.out.println("Duplicate:"+node.data);
					 prevNode.next  = nextNode.next;
					 size--;
				}else {
					prevNode = nextNode;
				}
				nextNode = nextNode.next;
			}
			node = node.next;
		}
	}
	
	private void addItemInSortedList(int data) {
		Node node = new Node(data);
		if(head == null) {
			head = node;
			tail = node;
			size++;
		}else {
			Node current = head;
			Node prev = null;
			while(current != null && current.data < data) {
				prev = current;
				current = current.next;
			}
			if(prev != null) {
				node.next = current;
				prev.next = node;
			}else {
				node.next = head;
				head= node;
			}
			if(current == null) {
				tail = node;
			}
			size++;
		}
	}
	
	private void reversePrint(Node current) {
		if (head == null) {
			System.out.println("Linked list is empty");
			return;
		} else {
			if (current.next == null) {
				System.out.println(current.data);
				return;
			}
			reversePrint(current.next);
			System.out.println(current.data);
		}
	}
	
	private void print() {
		System.out.println("***********************");
		Node node = head;
		if(head == null && tail == null) {
			System.out.println("Linked List is Empty.....");
		} else {
			while (node != null) {
				System.out.println("Data:"+node.data);
				node = node.next;
			}
			System.out.println("Size:"+size+" Head:"+head.data+" Tail:"+tail.data);
		}
	}
	
	private static boolean compareTwoList(MyLinkedList firstList, MyLinkedList secondList) {
		Node head1 = firstList.head;;
		Node head2 = secondList.head;
		if(head1 == null && head2 == null) {
			return true;
		}else if(head1 != null && head2 == null) {
			return false;
		}else if(head1 == null && head2 != null) {
			return false;
		}else {
			Node node1 = head1;
			Node node2 = head2;
			while(node1 != null && node2 !=null) {
				if(node1.data != node2.data) {
					return false;
				}
				node1 = node1.next;
				node2 = node2.next;
			}
			if(node1 == null && node2 == null) {
				return true;
			}
		}
		return false;
	}
	
	private static void mergeTwoSortedList(MyLinkedList firstList, MyLinkedList secondList) {
		MyLinkedList list = new MyLinkedList();
		Node head1 = firstList.head;;
		Node head2 = secondList.head;
		if(head1 == null && head2 == null) {
			System.out.println("Both list is empty");
		}else if(head1 != null && head2 == null){
			list.head = head1;
		}else if(head1 == null && head2 != null) {
			list.head = head2;
		}else {
			Node node1 = head1;
			Node node2 = head2;
			while(node1 != null && node2 != null) {
				if(node1.data <= node2.data) {
					list.addLast(node1.data);
					node1 = node1.next;
				}else {
					list.addLast(node2.data);
					node2 = node2.next;
				}
			}
			if(node1 != null) {
				while(node1 != null) {
					list.addLast(node1.data);
					node1 = node1.next;
				}
			}
			
			if(node2 != null) {
				while(node2 != null) {
					list.addLast(node2.data);
					node2 = node2.next;
				}
			}
		}
		System.out.println("After merge:");
		list.print();
	}
	
	
	public static void main(String[] args) {
		MyLinkedList list = new MyLinkedList();
		list.addLast(20);
		list.addLast(10);
		list.addLast(20);
		list.addLast(30);
		list.addLast(20);
		list.print();
		list.removeDuplicate();
		list.print();
		
	}
}
