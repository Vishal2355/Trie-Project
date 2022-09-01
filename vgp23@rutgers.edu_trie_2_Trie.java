package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Vishal Patel Email: vgp23@scarletmail.rutgers.edu NetID: vgp23
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	private static ArrayList<TrieNode> Method(TrieNode Tn,  ArrayList<TrieNode> List){

		while(Tn != null){
			if(Tn.firstChild == null){
				List.add(Tn);
			} else {
				List = Method(Tn.firstChild, List);
			}
			Tn = Tn.sibling;
		}
		return List;
	}
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {

		TrieNode rt = new TrieNode(null,null,null);
		int size = allWords.length;
		if(size == 0){
			return null;
		}
		for(int a = 0; a < size; a++){
			if(a == 0){
				short start = 0;
				short end = 0;
				for(short b = 0; b < allWords[a].length(); b++){
					if((b+1) == allWords[a].length()){
						end = b;
					}
				}
				Indexes i = new Indexes(0, start, end);
				TrieNode t = new TrieNode(i,null,null);
				rt.firstChild = t;
				continue;
			}
			TrieNode r = rt.firstChild, 
			p = rt;
			String s = allWords[a];
			short d = 0;
			while(r != null){
				if(s.charAt(d) == allWords[r.substr.wordIndex].charAt(d)){
					short end = 0;
					for(short b = d; b < s.length(); b++){
						if(s.charAt(b) != allWords[r.substr.wordIndex].charAt(b)){
							break;
						}
						if(b > r.substr.endIndex){
							break;
						}
						end = b;
					}
					if(end == r.substr.endIndex){
						d = (short) ((short) end + 1);
						p = r;
						r = r.firstChild;
						continue;
					} else {
						Indexes i = new Indexes(r.substr.wordIndex, d, end);
						TrieNode Tn = new TrieNode(i,null,null);
						Indexes i2 = new Indexes(r.substr.wordIndex, (short)((short) end + 1), r.substr.endIndex);
						TrieNode Tn2 = new TrieNode(i2,null,null);
						Indexes i3 = new Indexes(a,(short) ((short) end + 1), (short) ((short)s.length() - 1 ));
						TrieNode Tn3 = new TrieNode(i3,null,null);
						if(p == rt || p.firstChild == r){
							p.firstChild = Tn;
						} else {
							p.sibling = Tn;
						}
						Tn2.sibling = Tn3;
						Tn.firstChild = Tn2;
						Tn.sibling = r.sibling;
						if(r.firstChild != null){
							Tn2.firstChild = r.firstChild;
						}
						break;
					}
				} else if(r.sibling == null && s.charAt(d) != allWords[r.substr.wordIndex].charAt(d)){
					short end = 0;
					for(short b = d; b < s.length(); b++){
						end = b;
					}
					Indexes i = new Indexes(a,d,end);
					TrieNode Tn = new TrieNode(i,null,null);
					r.sibling = Tn;
					break;
				} else {
					p = r;
					r = r.sibling;
				}
			}
		}		
		return rt;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		
		ArrayList<TrieNode> List = new ArrayList<TrieNode>();
		TrieNode r = root.firstChild;
		String s = "";
		if(r == null || allWords.length == 0 || prefix.length() == 0){
			return null;
		}
		while(r != null){
			if(prefix.charAt(r.substr.startIndex) == allWords[r.substr.wordIndex].charAt(r.substr.startIndex)){
				for(int a = r.substr.startIndex; a <= r.substr.endIndex; a++){
					if(a == prefix.length()){
						break;
					}
					if(prefix.charAt(a) != allWords[r.substr.wordIndex].charAt(a)){
						return null;	
					}
					s += prefix.charAt(a);
					if(s.equals(prefix)){
						break;
					}
				}
				if(s.equals(prefix)){
					if(r.firstChild == null){
						List.add(r);
						break;
					}
					TrieNode Tn = r.firstChild;
					List = Method(Tn, List);
					break;
				} else {
					r = r.firstChild;
					continue;
				}
			} else {
				r = r.sibling;
			}
		}
		if(s == "" || List == null || (s.equals(prefix) == false)){
			return null;
		}
		return List;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
