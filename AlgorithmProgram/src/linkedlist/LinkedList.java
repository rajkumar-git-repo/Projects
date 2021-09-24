package linkedlist;


public class LinkedList {

	private class Node{
		Node next;
		int data;
		
		Node(int data){
			this.data = data;
			this.next = null;
		}
	}
	
	private Node head;
	private Node tail;
	private int size;
	
	LinkedList(){
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
	
	public void addAtIndex(int index, int data) {
		Node node = new Node(data);
		if(size < index) {
			throw new RuntimeException("Index out of range");
		}
		if(index == 0) {
			addFirst(data);
		}else if(size == index) {
			addLast(data);
		}else {
			int count = 0;
			Node temp = head;
			while(temp != null && ++count < index) {
				System.out.println("count:"+count);
				temp = temp.next;
			}
			node.next  = temp.next;
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
		}
		size--;
		return node.data;
	}
	
	public int removeAtIndex(int index) {
		Node node = head;
		if(index > size-1 && index > 0) {
			 throw new RuntimeException("Index out of range!");
		}
		if(head == null) {
			return 0;
		}
		if(head == tail || index == 0) {
			return removeFirst();
		}
		if(size-1 == index) {
			return removeLast();
		}else {
			Node prev = null;
			int count = 0;
			while(node != null && ++count <= index) {
				prev = node;
				node = node.next;
			}
			prev.next = node.next;
			node.next = null;
		}
		size--;
		return node.data;
	}
	
	public void reverse() {
		Node node = head;
		Node next = null;
		Node prev = null;
		while(node != null) {
			next = node.next;
			node.next = prev;
			prev = node;
			node = next;
		}
		Node temp = head;
		head = tail;
		tail = temp;
	}
	
	public void search(int searchItem) {
		Node node = head;
		int count =1;
		boolean isExist = false;
		while(node != null) {
			if(node.data == searchItem) {
				isExist = true;
				System.out.println("Search Item "+ searchItem+ " found at position "+count);
				return;
			}
			node = node.next;
			count++;
		}
		if(!isExist) {
			System.out.println("Item not found!");
		}
	}
	
	public void findMiddle() {
		Node node = head;
		int count = 0;
		System.out.println("Size :"+size+", middle: "+size/2);
		while(++count <= size/2) {
			node = node.next;
		}
		System.out.println("Midlle Item :"+node.data);
	}
	
	//1 -> 2 -> 3 -> 2 -> 2 ->4 ->1
	public void deleteData(int data) {
		Node node = head;
		Node prev = null;
		if(head == null) {
			System.out.println("List is empty !");
		}
		if(head.data == data) {
			removeFirst();
			deleteData(data);
			return;
		}
		if(tail.data == data) {
			removeLast();
			deleteData(data);
			return;
		} else {
			while (node != null && node.data != data) {
				prev = node;
				node = node.next;
			}
			if (node.data == data) {
				prev.next = node.next;
				node.next = null;
				size--;
			}
		}
		boolean flag = false;
		while( prev != null) {
			if(prev.data == data) {
				flag =true;
			}
			prev = prev.next;
		}
		if(flag) {
			deleteData(data);
		}
	}
	
	//1 -> 2 -> 3 -> 2 -> 2 ->4 ->1
	public void removeDuplicate() {
		Node node = head;
		Node nextNode = null;
		Node prevNode = null;
		while(node != null) {
			nextNode = node.next;
			prevNode = node;
			while(nextNode != null) {
				if(nextNode.data == node.data) {
					//this line skip duplicate node
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
	
	public void addItemInSortedList(int data) {
		Node node = new Node(data);
		Node current = head;
		Node prev = null;
		if(head == null) {
			head = node;
			return;
		}else {
			while(current != null && current.data <= data) {
				prev = current;
				current = current.next;
			}
			if(current == null) {
				prev.next = node;
			    tail = node;
			}
			if(prev == null) {
			   node.next = head;
			   head = node;
			}else {
			  node.next = current;
			  prev.next = node;
			}
		}
		 size++;
	}
	
	
	public Node reverseList(Node reverse) {
		Node node = reverse;
		Node next = null;
		Node prev = null;
		while(node != null) {
			next = node.next;
			node.next = prev;
			prev = node;
			node = next;
		}
		return prev;
	}
	
	// 1->2->3->4->3->2->1
	public void isPalindrome() {
		Node node = head;
		boolean flag = false;

		int mid = size%2==0 ? size/2 :(size+1)/2;
		System.out.println("Mid : "+mid);
		for(int i=1;i<mid;i++) {
			node = node.next;
		}
		Node reverse = reverseList(node.next);
		while (head != null && reverse != null) {
			if (head.data != reverse.data) {
				flag = true;
				break;
			}
			head = head.next;
			reverse = reverse.next;
		}

		if(flag) {
			System.out.println("Given linked list is not palindrome");
		}else {
			System.out.println("Given linked list is palindrome");
		}
		
	}
	
	public void print() {
		Node node = head;
		while(node != null) {
			System.out.println("["+node.data+"]");
			node = node.next;
		}
		if(head != null) {
			System.out.println("Size : "+size);
			System.out.println("Head Data: "+head.data+"  Tail Data: "+tail.data);
		}else {
			System.err.println("Linked list is empty!");
		}
	}
	
	public void reversePrint(Node current) {  
        if(head == null) {  
            System.out.println("List is empty");  
            return;  
        }  
        else {  
            //Checks if the next node is null, if yes then prints it.  
            if(current.next == null) {  
                System.out.print(current.data + " ");  
                return;  
            }  
            //Recursively calls the reverse function  
            reversePrint(current.next);  
            System.out.print(current.data + " ");  
        }  
    } 
	
	//compare two list if size know in advance
	public static void compareTwoList(LinkedList list1 , LinkedList list2) {
		Node head1 = list1.head;
		Node head2 = list2.head;
        boolean flag = false;
		if(head1 == null && head2 == null) {
			System.out.println("Identical");
		}else if(list1.size != list2.size) {
			System.out.println("Not Identical");
		}else {
			while( head1 != null && head2 != null) {
				if(head1.data != head2.data) {
					flag =true;
					break;
				}
				head1 = head1.next;
				head2 = head2.next;
			}
			if(flag) {
				System.out.println("Not Identical");
			}else {
				System.out.println("Identical");
			}
		}
	}
	
	//compare two list if we dont know size in advance
	static boolean compareLists(Node head1, Node head2) {
		if (head1 == null && head2 == null) {
			return true;
		} else if (head1 == null && head2 != null) {
			return false;
		} else if (head1 != null && head2 == null) {
			return false;
		} else {
			boolean flag = false;
			while (head1 != null && head2 != null) {
				if (head1.data != head2.data) {
					flag = true;
					break;
				}
				head1 = head1.next;
				head2 = head2.next;
			}
			if (head1 == null && head2 != null) {
				return false;
			} else if (head1 != null && head2 == null) {
				return false;
			} else if (flag) {
				return false;
			} else {
				return true;
			}
		}

	}
	
	/**
	 * please refer  {@link MyLinkedList} mergeTwoSortedList() method for best approach
	 * @param head1
	 * @param head2
	 */
	public static void mergeTwoSortedList(Node head1,Node head2) {
		LinkedList list = new LinkedList();
		Node head = list.head;
		Node tail = list.tail;
		int size = list.size;
		while (head1 != null) {

			while (head2 != null) {
				if (head1.data < head2.data) {
					if (head == null) {
						head = head1;
						tail = head1;
						size++;
					} else {
						tail.next = head1;
						tail = head1;
						size++;
					}
					break;
				} else {
					if (head == null) {
						head = head2;
						tail = head2;
						size++;
					} else {
						tail.next = head2;
						tail = head2;
						size++;
					}
				}
				head2 = head2.next;
			}
			if (head2== null && head1 != null) {
				if (head == null) {
					head = head1;
					tail = head1;
					size++;
				} else {
					tail.next = head1;
					tail = head1;
					size++;
				}
			} 
			head1 = head1.next;
		}

		if (head1 == null && head2 != null) {
			while (head2 != null) {
				if (head == null) {
					head = head2;
					tail = head2;
					size++;
				} else {
					tail.next = head2;
					tail = head2;
					size++;
				}
				head2 = head2.next;
			}
		}
		list.size = size;
		Node node = head;
		while (node != null) {
			System.out.println("Node [" + node.data + "]");
			node = node.next;
		}

		if(head != null) {
			System.out.println("Size : "+list.size+", Head: "+head.data+",  Tail: "+tail.data);
		}else {
			System.err.println("Linked list is empty!");
		}
	}
	
	public static void main(String[] s) {
		LinkedList list1 = new LinkedList();
		
		list1.print();
		
		LinkedList list2 = new LinkedList();
		list2.addLast(3);
		list2.addLast(6);
		list2.addLast(3);
		list2.addLast(11);
		list2.print();
		//mergeTwoSortedList(list1.head, list2.head);
		list2.removeDuplicate();
		list2.print();
	}
}



