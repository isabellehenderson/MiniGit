import java.util.*;
import java.text.SimpleDateFormat;

// This class represents a repository version control systems that manages a series of commits. It
// uses user input to determine how to modify and/or retrieve information about the repository.
public class Repository {
    private String name;
    private Commit head;

    // Constructs a repository with the specified name.
    // Exceptions: 
    // - If the name is null or empty, an IllegalArgumentException is thrown.
    // Parameters:
    // - name: The specified name of the repository.
    public Repository(String name){
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.head = null;
    }

    // Returns the ID of the current head of this repository.
    // Returns:
    // - String: The ID of the current head of the repository. If there are no commits, returns 
    // null.
    public String getRepoHead(){
        if(head == null){
            return null;
        }

        return head.id;
    }

    // Returns the number of commits in the repository.
    // Returns:
    // - int: The size of the repository.
    public int getRepoSize(){
        int size = 0;
        Commit temp = head;
        while(temp != null){
            size++;
            temp = temp.past;
        }

        return size;
    }

    // Returns a message representing the state of the repository.
    // Returns:
    // - String: The message representing the commit information in the repository. If there are 
    // no commits in the repository, it is indicated in the message.
    public String toString(){
        if(getRepoSize() <= 0){
            return name + " - No commits";
        }
        return name + " - Current head: " + head.toString();
    }

    // Returns whether or not a commit with the target ID is in the repository.
    // Parameters:
    // - targetId: The commit ID to check for in the repository.
    // Returns:
    // - boolean: Returns true if the commit with the target ID is found, false if not.
    public boolean contains(String targetId){
        Commit temp = head;
        while (temp != null) {
            if (temp.id.equals(targetId)) {
                return true;
            }
            temp = temp.past;
        }

        return false;
    }

    // Returns a message consisting of the most recent specified amount of commits in this 
    // repository, with the most recent first. If the specified amount is greater than the
    // number of commits, the whole history is returned.
    // Parameters: 
    // - n: The amount of commits from the history to be returned.
    // Returns:
    // - String: The repository history containing the indicated number of commits.
    // Exceptions:
    // - If the specified number of commits is non-positive, an IllegalArgumentException is thrown.
    public String getHistory(int n){
        if(n <= 0){
            throw new IllegalArgumentException();
        }

        if (n > getRepoSize()) {
            n = getRepoSize();
        }

        Commit temp = head;
        String history = "";
        boolean spacer = false;

        for(int i = 0; i < n && temp != null; i++){
            if (spacer) {
                history += "\n";
            } else {
                spacer = true;
            }   
            
            history += temp.toString();
            temp = temp.past;
        }

        return history;
    }

    // Creates a new commit with the given message, adding it to become the head of the
    // repository while preserving the history behind it.
    // Parameters:
    // - message: The message to be associated with the commit.
    // Returns:
    // - String: The ID of the new commit.
    public String commit(String message){
        Commit newCommit = new Commit(message, head);
        head = newCommit;
        return head.id;
    }

    // Removes the commit with the specified ID from this repository, maintaining the rest
    // of the history. If there is no matching commit ID, nothing is changed.
    // Parameters:
    // - targetID: The ID of the commit to be removed from the repository.
    // Returns:
    // - boolean: Returns true if the commit was successfully dropped, and false if commit ID was 
    // not found.
    public boolean drop(String targetId){
        if(getRepoSize() <= 0){
            return false;
        }

        if(head.id.equals(targetId)){
            head = head.past;
            return true;
        }

        Commit temp = head;
        while (temp.past != null && !temp.past.id.equals(targetId)) {
            temp = temp.past;
        }
    
        if (temp.past != null) {
            temp.past = temp.past.past;
            return true; 
        }                                                  
    
        return false;
    }

    // Takes all the commits in the other repository and moves them into this repository,
    // combining the two repository histories such that chronological order is preserved. If the 
    // other repository is empty, nothing is changed.
    // Parameters:
    // - other: The repository to be merged with the current repository.
    public void synchronize(Repository other){
        if(head == null) {
            head = other.head;    
            other.head = null;                                                                                                     
        } else if (head != null && other.head != null) { 

            Commit temp = head;
            // front case
            if (head.timeStamp < other.head.timeStamp) {
                head = other.head;
                other.head = other.head.past;
                head.past = temp;
            }

            Commit curr = head;
            // middle case
            while(other.head != null && curr.past != null) {
                if (curr.past.timeStamp > other.head.timeStamp){
                    curr = curr.past;
                } else {
                    Commit target = other.head;
                    other.head = other.head.past;
                    target.past = curr.past;
                    curr.past = target;
                    target = other.head;
                }
            }

            // end case
            while(other.head != null){
                curr.past = other.head;
                curr = curr.past;
                other.head = other.head.past;
            }
        }
    }  

    /**
     * DO NOT MODIFY
     * A class that represents a single commit in the repository.
     * Commits are characterized by an identifier, a commit message,
     * and the time that the commit was made. A commit also stores
     * a reference to the immediately previous commit if it exists.
     *
     * Staff Note: You may notice that the comments in this 
     * class openly mention the fields of the class. This is fine 
     * because the fields of the Commit class are public. In general, 
     * be careful about revealing implementation details!
     */
    public class Commit {

        private static int currentCommitID;

        /**
         * The time, in milliseconds, at which this commit was created.
         */
        public final long timeStamp;

        /**
         * A unique identifier for this commit.
         */
        public final String id;

        /**
         * A message describing the changes made in this commit.
         */
        public final String message;

        /**
         * A reference to the previous commit, if it exists. Otherwise, null.
         */
        public Commit past;

        /**
         * Constructs a commit object. The unique identifier and timestamp
         * are automatically generated.
         * @param message A message describing the changes made in this commit.
         * @param past A reference to the commit made immediately before this
         *             commit.
         */
        public Commit(String message, Commit past) {
            this.id = "" + currentCommitID++;
            this.message = message;
            this.timeStamp = System.currentTimeMillis();
            this.past = past;
        }

        /**
         * Constructs a commit object with no previous commit. The unique
         * identifier and timestamp are automatically generated.
         * @param message A message describing the changes made in this commit.
         */
        public Commit(String message) {
            this(message, null);
        }

        /**
         * Returns a string representation of this commit. The string
         * representation consists of this commit's unique identifier,
         * timestamp, and message, in the following form:
         *      "[identifier] at [timestamp]: [message]"
         * @return The string representation of this collection.
         */
        @Override
        public String toString() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(timeStamp);

            return id + " at " + formatter.format(date) + ": " + message;
        }

        /**
        * Resets the IDs of the commit nodes such that they reset to 0.
        * Primarily for testing purposes.
        */
        public static void resetIds() {
            Commit.currentCommitID = 0;
        }
    }
}