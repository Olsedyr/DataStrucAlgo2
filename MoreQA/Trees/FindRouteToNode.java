package MoreQA.Trees;// BinarySearchTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x
// boolean contains( x )  --> Return true if x is present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

/**
 * Implements an unbalanced binary search tree.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 */
public class FindRouteToNode<AnyType extends Comparable<? super AnyType>>
{
    /**
     * Construct the tree.
     */
    public FindRouteToNode( )
    {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * @param x the item to insert.
     */
    public void insert( AnyType x )
    {
        root = insert( x, root );
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     * @param x the item to remove.
     */
    public void remove( AnyType x )
    {
        root = remove( x, root );
    }

    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     */
    public AnyType findMin( )
    {
        if( isEmpty( ) )
            throw new RuntimeException();
        return findMin( root ).element;
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     */
    public AnyType findMax( )
    {
        if( isEmpty( ) )
            throw new RuntimeException();
        return findMax( root ).element;
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if not found.
     */
    public boolean contains( AnyType x )
    {
        return contains( x, root );
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty( )
    {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( )
    {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree( )
    {
        if( isEmpty( ) )
            System.out.println( "Empty tree" );
        else
            printTree( root );
    }

    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<AnyType> insert( AnyType x, BinaryNode<AnyType> t )
    {
        if( t == null )
            return new BinaryNode<>( x, null, null );

        int compareResult = x.compareTo( t.element );

        if( compareResult < 0 )
            t.left = insert( x, t.left );
        else if( compareResult > 0 )
            t.right = insert( x, t.right );
        else
            ;  // Duplicate; do nothing
        return t;
    }

    /**
     * Internal method to remove from a subtree.
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<AnyType> remove( AnyType x, BinaryNode<AnyType> t )
    {
        if( t == null )
            return t;   // Item not found; do nothing

        int compareResult = x.compareTo( t.element );

        if( compareResult < 0 )
            t.left = remove( x, t.left );
        else if( compareResult > 0 )
            t.right = remove( x, t.right );
        else if( t.left != null && t.right != null ) // Two children
        {
            t.element = findMin( t.right ).element;
            t.right = remove( t.element, t.right );
        }
        else
            t = ( t.left != null ) ? t.left : t.right;
        return t;
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the subtree.
     * @return node containing the smallest item.
     */
    private BinaryNode<AnyType> findMin( BinaryNode<AnyType> t )
    {
        if( t == null )
            return null;
        else if( t.left == null )
            return t;
        return findMin( t.left );
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the subtree.
     * @return node containing the largest item.
     */
    private BinaryNode<AnyType> findMax( BinaryNode<AnyType> t )
    {
        if( t != null )
            while( t.right != null )
                t = t.right;

        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * @return node containing the matched item.
     */
    private boolean contains( AnyType x, BinaryNode<AnyType> t )
    {
        if( t == null )
            return false;

        int compareResult = x.compareTo( t.element );

        if( compareResult < 0 )
            return contains( x, t.left );
        else if( compareResult > 0 )
            return contains( x, t.right );
        else
            return true;    // Match
    }

    /**
     * Internal method to print a subtree in sorted order.
     * @param t the node that roots the subtree.
     */
    private void printTree( BinaryNode<AnyType> t )
    {
        if( t != null )
        {
            printTree( t.left );
            System.out.println( t.element );
            printTree( t.right );
        }
    }

    /**
     * Internal method to compute height of a subtree.
     * @param t the node that roots the subtree.
     */
    private int height( BinaryNode<AnyType> t )
    {
        if( t == null )
            return -1;
        else
            return 1 + Math.max( height( t.left ), height( t.right ) );
    }

    // Basic node stored in unbalanced binary search trees
    private static class BinaryNode<AnyType>
    {
        // Constructors
        BinaryNode( AnyType theElement )
        {
            this( theElement, null, null );
        }

        BinaryNode( AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt )
        {
            element  = theElement;
            left     = lt;
            right    = rt;
        }

        AnyType element;            // The data in the node
        BinaryNode<AnyType> left;   // Left child
        BinaryNode<AnyType> right;  // Right child
    }


    /** The tree root. */
    private BinaryNode<AnyType> root;

    /**
     * Returnerer ruten fra roden til den node, der indeholder x.
     * Hvis x ikke findes, returneres en fejlmeddelelse.
     */
    public String findRoute(AnyType x) {
        String route = findRoute(root, x);
        if (route == null) {
            return "Værdi " + x + " findes ikke i træet.";
        }
        return route;
    }

    /**
     * Privat rekursiv hjælpe-metode, der finder ruten fra 'node' til værdien x.
     */
    private String findRoute(BinaryNode<AnyType> node, AnyType x) {
        if (node == null) {
            return null;
        }

        int cmp = x.compareTo(node.element);
        if (cmp == 0) {
            return node.element.toString();
        }

        // Søg i venstre eller højre deltræ afhængigt af sammenligningen
        if (cmp < 0) {
            String leftRoute = findRoute(node.left, x);
            if (leftRoute != null) {
                return node.element + " " + leftRoute;
            }
        } else {
            String rightRoute = findRoute(node.right, x);
            if (rightRoute != null) {
                return node.element + " " + rightRoute;
            }
        }
        return null;
    }


    // Test program
    public static void main( String [ ] args )
    {
        FindRouteToNode<Integer> t = new FindRouteToNode<>( );
        final int NUMS = 4000;
        final int GAP  =   37;

        // Indsæt værdier i træet
        t.insert(45);
        t.insert(15);
        t.insert(79);
        t.insert(10);
        t.insert(20);
        t.insert(55);
        t.insert(90);
        t.insert(12);
        t.insert(50);

        System.out.println("Route til 50: " + t.findRoute(50));

        // Udskriv træet i sorteret rækkefølge
        t.printTree();


        // Test the findMin method
        try {
            System.out.println("Minimum value in the tree: " + t.findMin());
        } catch (RuntimeException e) {
            System.out.println("Tree is empty, no minimum value.");
        }

        // Test the findMax method
        try {
            System.out.println("Maximum value in the tree: " + t.findMax());
        } catch (RuntimeException e) {
            System.out.println("Tree is empty, no maximum value.");
        }

        // Test the contains method
        System.out.println("Does the tree contain 55? " + t.contains(55));
        System.out.println("Does the tree contain 100? " + t.contains(100));

        // Test the printTree method
        System.out.println("Tree in sorted order:");
        t.printTree();

        // Now, let's test removing some nodes
        t.remove(55);
        System.out.println("After removing 55:");
        t.printTree();

        // Test the insert method by adding a new element
        t.insert(42);
        System.out.println("After inserting 42:");
        t.printTree();

        // Test the isEmpty method
        System.out.println("Is the tree empty? " + t.isEmpty());

        // Test the makeEmpty method
        t.makeEmpty();
        System.out.println("After making the tree empty:");
        t.printTree();
    }
}