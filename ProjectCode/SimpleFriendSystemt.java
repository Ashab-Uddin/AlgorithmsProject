import java.util.*;

public class SimpleFriendSystemt {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==== Simple Social Network ====");
            System.out.println("1. Add User");
            System.out.println("2. Send Friend Request");
            System.out.println("3. Accept All Requests");
            System.out.println("4. Show Friends");
            System.out.println("5. Recommend Friends (BFS)");
            System.out.println("6. Recommend Friends (DFS)");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine();  // consume newline

            switch (choice) {
                case 1 -> addUser();
                case 2 -> sendRequest();
                case 3 -> acceptRequests();
                case 4 -> showFriends();
                case 5 -> recommendFriendsBFS();
                case 6 -> recommendFriendsDFS();
                case 7 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    // User class
    static class User {
        String name;
        ArrayList<User> friends = new ArrayList<>();
        ArrayList<User> requests = new ArrayList<>();

        User(String name) {
            this.name = name;
        }
    }

    // Find user by name
    static User findUser(String name) {
        for (User u : users) {
            if (u.name.equalsIgnoreCase(name)) return u;
        }
        return null;
    }

    // Add a new user
    static void addUser() {
        System.out.print("Enter username: ");
        String name = sc.nextLine().trim();
        if (findUser(name) == null) {
            users.add(new User(name));
            System.out.println("User added: " + name);
        } else {
            System.out.println("User already exists!");
        }
    }

    // Send friend request
    static void sendRequest() {
        System.out.print("From: ");
        String fromName = sc.nextLine().trim();
        System.out.print("To: ");
        String toName = sc.nextLine().trim();

        User from = findUser(fromName);
        User to = findUser(toName);

        if (from == null || to == null) {
            System.out.println("User not found.");
            return;
        }

        if (from == to || from.friends.contains(to)) {
            System.out.println("Already friends or invalid request.");
            return;
        }

        if (!to.requests.contains(from)) {
            to.requests.add(from);
            System.out.println("Friend request sent.");
        } else {
            System.out.println("Request already sent.");
        }
    }

    // Accept all pending requests
    static void acceptRequests() {
        System.out.print("Enter your username: ");
        String name = sc.nextLine().trim();
        User user = findUser(name);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        for (User sender : user.requests) {
            if (!user.friends.contains(sender)) {
                user.friends.add(sender);
                sender.friends.add(user);
                System.out.println(user.name + " accepted request from " + sender.name);
            }
        }
        user.requests.clear();
    }

    // Show friends of a user
    static void showFriends() {
        System.out.print("Enter username: ");
        String name = sc.nextLine().trim();
        User user = findUser(name);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        if (user.friends.isEmpty()) {
            System.out.println(user.name + " has no friends yet.");
            return;
        }

        System.out.println(user.name + "'s friends:");
        for (User friend : user.friends) {
            System.out.println("- " + friend.name);
        }
    }

    // Recommend friends using BFS
    static void recommendFriendsBFS() {
        System.out.print("Enter username: ");
        String name = sc.nextLine().trim();
        User start = findUser(name);

        if (start == null) {
            System.out.println("User not found.");
            return;
        }

        Set<User> visited = new HashSet<>();
        Queue<User> queue = new LinkedList<>();
        Map<User, Integer> mutual = new HashMap<>();

        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            User current = queue.poll();
            for (User friend : current.friends) {
                if (!visited.contains(friend)) {
                    visited.add(friend);
                    queue.add(friend);
                    for (User fof : friend.friends) {
                        if (fof != start && !start.friends.contains(fof)) {
                            mutual.put(fof, mutual.getOrDefault(fof, 0) + 1);
                        }
                    }
                }
            }
        }

        if (mutual.isEmpty()) {
            System.out.println("No recommendations found.");
            return;
        }

        System.out.println("Recommended friends (BFS):");
        for (Map.Entry<User, Integer> entry : mutual.entrySet()) {
            System.out.println("- " + entry.getKey().name + " (" + entry.getValue() + " mutual friends)");
        }
    }

    // Recommend friends using DFS
    static void recommendFriendsDFS() {
        System.out.print("Enter username: ");
        String name = sc.nextLine().trim();
        User start = findUser(name);

        if (start == null) {
            System.out.println("User not found.");
            return;
        }

        Set<User> visited = new HashSet<>();
        Map<User, Integer> mutual = new HashMap<>();
        dfsHelper(start, visited, mutual, start);

        if (mutual.isEmpty()) {
            System.out.println("No recommendations found.");
            return;
        }

        System.out.println("Recommended friends (DFS):");
        for (Map.Entry<User, Integer> entry : mutual.entrySet()) {
            System.out.println("- " + entry.getKey().name + " (" + entry.getValue() + " mutual friends)");
        }
    }

    // Helper function for DFS
    static void dfsHelper(User current, Set<User> visited, Map<User, Integer> mutual, User origin) {
        visited.add(current);

        for (User friend : current.friends) {
            if (!visited.contains(friend)) {
                for (User fof : friend.friends) {
                    if (fof != origin && !origin.friends.contains(fof)) {
                        mutual.put(fof, mutual.getOrDefault(fof, 0) + 1);
                    }
                }
                dfsHelper(friend, visited, mutual, origin);
            }
        }
    }
}
