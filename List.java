/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        if(this.first == null){
            return null;
        }
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        if(this.first == null){
            this.first = new Node(new CharData(chr));
        }
        else{
            Node new_first_node = new Node(new CharData(chr));
            new_first_node.next = this.first;
            this.first = new_first_node;

        }
        size++;
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        // Your code goes here
           if (size == 0) return "()";
        StringBuilder str = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            if(current.next != null){
                str.append(current.cp + " ");
            }
            else{
                str.append(current.cp + ")");
            }
            current = current.next;   
        }
        return str.toString();
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        if(this.first == null){
            return -1;
        }
        Node current = first;
        int index = 0;
        while (current != null) {
            if(current.cp.equals(chr)){
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        if(this.first == null){
            addFirst(chr);
        }
        else{
            int index = indexOf(chr); 
            if (index == -1) {
                addFirst(chr);
            } 
            else { 
                CharData cp = get(index);
                cp.count++;
        }

        }
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        if(indexOf(chr) == -1){
            return false;
        }
        if(indexOf(chr) == 0){
            this.first = this.first.next;
            size--;
            return true;
        }
        int index = indexOf(chr);
        Node current = this.first;
        for(int i = 0; i < index - 1; i++){
            current = current.next;
        }
        current.next = current.next.next;
        size--;
        return true;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if(index >= getSize() || index < 0){
            throw new IndexOutOfBoundsException();
        }
        Node current = this.first;
        for(int i = 0; i < index; i++){
            current = current.next;
        }
        return current.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }




    public static void main(String[] args) {
        List myList = new List();
        myList.addFirst('e');
        myList.addFirst('t');
        myList.addFirst('i');
        myList.addFirst('m');
        myList.addFirst('o');
        myList.addFirst('c');
        System.out.println( myList.toString()); //(c, o, m, i, t, e) if works

        List committeeList = new List();
        String testStr = "committee_";
        for (int i = 0; i < testStr.length(); i++) {
            committeeList.update(testStr.charAt(i));
        }
        // לפי הקובץ: (('c', 1, 0, 0) ('o', 1, 0, 0) ('m', 2, 0, 0) ('i', 1, 0, 0) ('t', 2, 0, 0) ('e', 2, 0, 0) ('_', 1, 0, 0))
        System.out.println("List after 'committee_': " + committeeList.toString());

        System.out.println("Index of 'm' " + committeeList.indexOf('m')); // 2
        System.out.println("Index of 'z' " + committeeList.indexOf('z')); // -1


        try {
            System.out.println("CharData at index 2: " + committeeList.get(2)); // אמור להיות 'm'
            System.out.println("CharData at index 10: " + committeeList.get(10)); // אמור לזרוק חריגה
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Caught expected exception for index 10");
        }

        boolean removed = committeeList.remove('m');
        System.out.println("Removed 'm'? " + removed);
        System.out.println("List after removing 'm': " + committeeList.toString());
        System.out.println("Index of 'm' now  " + committeeList.indexOf('m')); // -1

    }
}
