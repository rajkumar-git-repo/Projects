package linkedlist;

public class FNLinkedList {
	
	private Node head;
	private Node tail;
	private int size;

	class Node{
		int data;
		Node next;
		
		Node(int data){
			this.data=data;
			this.next = null;
		}
	}
	
	public FNLinkedList() {
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
	
	public void removeFirst() {
		Node node = head;
		if(head == null) {
			System.out.println("List is empty!");
		}else if(head == tail){
			head = null;
			tail = null;
			size--;
		}else {
			head = head.next;
			node.next = null;
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
			prev.next = null;
			tail = prev;
			size--;
		}
	}

	public void addAtIndex(int index,int data) {
		Node node = head;
		if(!(0 <= index && index <= size)) {
			System.out.println("Invalid Index!");
		}else if(index == 0){
			addFirst(data);
		}else if(index == size) {
			addLast(data);
		}else {
			int count = 0;
			Node prev = null;
			Node addNode = new Node(data);
			while(node != tail && ++count <= index) {
				prev = node;
				node = node.next;
			}
			addNode.next = node;
			prev.next = addNode;
			size++;
			
		}
	}
	
	public void removeAtIndex(int index) {
		Node node = head;
		if(head == null) {
			System.out.println("List is Empty!");
		}else {
			if(!(0<=index && index <=size)) {
				System.out.println("Invalid index");
			}else if(index == 0) {
				removeFirst();
			}else if(index == size) {
				removeLast();
			}else {
				int count = 0;
				Node prev = null;
				while(node != tail && ++ count <= index) {
					prev = node;
					node = node.next;
				}
				prev.next = node.next;
				node.next = null;
				size--;
			}
		}
	}
	
	public void reverse() {
		if(head == null) {
			System.out.println("List is Emty!");
		}else {
			Node prev = null;
			Node next = null;
			Node current = head;
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
	
	public void isPalindrome(){
		if(head == null) {
			System.out.println("List is empty!");
		}else {
			Node node = head;
			int mid = size%2 == 0 ? size/2 : (size/2)+1;
			for(int i=1;i<mid;i++) {
				node = node.next;
			}
			Node node1 = reverseHalf(node.next);
			Node node2 = head;
			boolean flag = false;
			while(node1 != null && node2 != null) {
				if(node1.data != node2.data) {
					flag = true;
					break;
				}
				node1 = node1.next;
				node2 = node2.next;
			}
			
			if(flag) {
				System.out.println("Not Palindrome");
			}else {
				System.out.println("Palindrome");
			}
			
		}
	}
	
	private Node reverseHalf(Node node) {
		Node prev = null;
		Node next = null;
		Node current = node;
		while(current != null) {
			next = current.next;
			current.next = prev;
			prev = current;
			current = next;
		}
		return prev;
	}
	
	public void removeDuplicate() {
		if(head == null) {
			System.out.println("List is empty!");
		}else {
			Node node = head;
			Node nextNode = null;
			Node prevNode = null;
			while(node != null) {
				prevNode = node;
				nextNode = node.next;
				while(nextNode != null) {
					if(node.data == nextNode.data) {
						prevNode.next = nextNode.next;
						size--;
					}else {
						prevNode = nextNode;
					}
					nextNode = nextNode.next;
				}
				node = node.next;
			}
		}
	}
	
	public void addItemInSortedList(int data) {
		Node node = new Node(data);
		if(head == null) {
			head = node;
			tail = node;
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
			   head = node;
			} 
			if(current == null){
				tail = node;
			}
			size++;
		}
	}
	
	public void reversePrint(Node current) {
		if(head == null) {
			System.out.println("List is empty");
		}else {
			if(current.next == null) {
				System.out.println(current.data);
				return;
			}
			reversePrint(current.next);
			System.out.println(current.data);
		}
	}
	
	private static boolean compareTwoList(FNLinkedList firstList, FNLinkedList secondList) {
		Node head1 = firstList.head;
		Node head2 = secondList.head;
		boolean flag = true;
		if(head1 == null && head2 == null) {
			flag = true;
		}else if(head1 == null && head2 != null) {
			flag = false;
		}else if(head1 != null && head2 == null) {
			flag = false;
		}else if(head1 != null && head2 != null) {
			Node current1 = head1;
			Node current2 = head2;
			while(current1 != null && current2 != null) {
				if(current1.data != current2.data) {
					flag = false;
					break;
				}
				current1 = current1.next;
				current2 = current2.next;
			}
			if(current1 != null || current2 != null) {
				flag = false;
			}
		}
		return flag;
	}
	
	private static void mergeTwoSortedList(FNLinkedList firstList, FNLinkedList secondList) {
		FNLinkedList list = new FNLinkedList();
		Node head1 = firstList.head;
		Node head2 = secondList.head;
		if(head1 == null && head2 != null) {
			list = secondList;
		}else if(head1 != null && head2 == null) {
			list = firstList;
		}else if(head1 != null && head2 != null){
			Node node1 = head1;
			Node node2 = head2;
			while(node1 != null && node2 != null) {
				if(node1.data <= node2.data) {
					list.addLast(node1.data);
					node1 = node1.next;
				}else if(node1.data > node2.data) {
					list.addLast(node2.data);
					node2 = node2.next;
				}
			}
		}
	}

	public void print() {
		if(head == null) {
			System.out.println("List is Empty!!");
		}else {
			Node node = head;
			while(node != null) {
				System.out.println("DATA["+node.data+"]");
				node = node.next;
			}
			System.out.println("Head:"+head.data+" Tail:"+tail.data);
		}
	}
	
	public static void main(String[] args) {
		FNLinkedList list = new FNLinkedList();
		list.addLast(1);
		list.addLast(2);
		list.addLast(5);
		list.addLast(6);
		list.addLast(5);
		list.addLast(2);
		list.addLast(1);
		list.isPalindrome();
		//list.print();
	}
}
