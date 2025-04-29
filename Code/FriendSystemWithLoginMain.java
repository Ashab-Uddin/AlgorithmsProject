import java.util.*;

public class FriendSystemWithLoginMain {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<User> users = new ArrayList<>();
    static User currentUser = null;  // Variable to hold the logged-in user

    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
                // Main menu if no user is logged in
                System.out.println("\n==== Main Menu ====");
                System.out.println("1. Login");
                System.out.println("2. Add User");
                System.out.println("3. Exit");
                System.out.print("Choose option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> login();  // Log in
                    case 2 -> addUser(); // Add new user
                    case 3 -> {
                        System.out.println("Goodbye!");
                        return;  // Exit program
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } else {
                // User is logged in, show options for the logged-in user
                showLoggedInMenu();
            }
        }
    }

    static class User {
        String name;
        ArrayList<String> friends = new ArrayList<>();
        ArrayList<String> requests = new ArrayList<>();

        User(String name) {
            this.name = name;
        }
    }

    // Find user by name
    static User findUser(String name) {
        for (User u : users) {
            if (u.name.equals(name)) return u;
        }
        return null;
    }

    // Login method
    static void login() {
        System.out.print("Enter username: ");
        String name = sc.nextLine().trim();
        currentUser = findUser(name);

        if (currentUser == null) {
            System.out.println("User not found. Please create an account first.");
        } else {
            System.out.println("Login successful. Welcome, " + name + "!");
        }
    }

    // Logout method
    static void logout() {
        System.out.println("Logging out...");
        currentUser = null;
    }

    // Add new user
    static void addUser() {
        System.out.print("Enter new username: ");
        String name = sc.nextLine().trim();
        if (findUser(name) == null) {
            users.add(new User(name));
            System.out.println("User '" + name + "' added.");
        } else {
            System.out.println("User already exists.");
        }
    }

    // Show the menu when a user is logged in
    static void showLoggedInMenu() {
        System.out.println("\n==== " + currentUser.name + " Menu ====");
        System.out.println("1. Send Friend Request");
        System.out.println("2. Accept Request");
        System.out.println("3. Show All Friends");
        System.out.println("4. Show Recommended Friends (BFS)");
        System.out.println("5. Show Recommended Friends (DFS)");
        System.out.println("6. Logout");
        System.out.println("7. Return to Main Menu");
        System.out.print("Choose option: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (choice) {
            case 1 -> sendRequest();
            case 2 -> acceptRequests();
            case 3 -> showFriends();
            case 4 -> recommendBFS();
            case 5 -> recommendDFS();
            case 6 -> logout();  // Log out
            case 7 -> currentUser = null;  // Return to main menu
            default -> System.out.println("Invalid choice.");
        }
    }

    // Send friend request
    static void sendRequest() {
        System.out.print("To: ");
        String toName = sc.nextLine().trim();

        if (toName.equals(currentUser.name)) {
            System.out.println("You can't send a friend request to yourself.");
            return;
        }

        User to = findUser(toName);
        if (to == null) {
            System.out.println("User not found.");
            return;
        }

        if (!currentUser.friends.contains(toName)) {
            if (!to.requests.contains(currentUser.name)) {
                to.requests.add(currentUser.name);
                System.out.println("Friend request sent to " + toName);
            } else {
                System.out.println("Friend request already sent.");
            }
        } else {
            System.out.println("You are already friends with " + toName);
        }
    }

    // Accept all requests
    static void acceptRequests() {
        if (currentUser.requests.isEmpty()) {
            System.out.println("You have no friend requests.");
            return;
        }

        // Show all users who sent requests
        System.out.println("You have received friend requests from:");
        for (String requester : currentUser.requests) {
            System.out.println(requester);
        }

        // Accept all requests
        for (String req : currentUser.requests) {
            if (!currentUser.friends.contains(req)) {
                currentUser.friends.add(req);
                User sender = findUser(req);
                if (sender != null && !sender.friends.contains(currentUser.name)) {
                    sender.friends.add(currentUser.name);
                }
                System.out.println(currentUser.name + " accepted friend request from " + req);
            }
        }

        currentUser.requests.clear(); // Clear all the accepted requests
    }

    // Show friends of the logged-in user
    static void showFriends() {
        System.out.println(currentUser.name + "'s friends: " + currentUser.friends);
    }

    // Recommend friends using BFS
    static void recommendBFS() {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        ArrayList<String> recommendations = new ArrayList<>();

        visited.add(currentUser.name);
        queue.add(currentUser.name);

        while (!queue.isEmpty()) {
            String currentName = queue.poll();
            User current = findUser(currentName);

            if (current == null) continue;

            for (String friendName : current.friends) {
                if (!visited.contains(friendName)) {
                    visited.add(friendName);
                    queue.add(friendName);
                    User friendUser = findUser(friendName);

                    if (friendUser != null) {
                        for (String fof : friendUser.friends) {
                            if (!fof.equals(currentUser.name) && !currentUser.friends.contains(fof)) {
                                if (!recommendations.contains(fof)) {
                                    recommendations.add(fof);
                                }
                            }
                        }
                    }
                }
            }
        }

        printRecommendations(recommendations);
    }

    // Recommend friends using DFS
    static void recommendDFS() {
        Set<String> visited = new HashSet<>();
        ArrayList<String> recommendations = new ArrayList<>();
        dfs(currentUser.name, currentUser.name, visited, recommendations);
        printRecommendations(recommendations);
    }

    // Helper DFS method for friend recommendations
    static void dfs(String origin, String currentName, Set<String> visited, ArrayList<String> recommendations) {
        visited.add(currentName);
        User current = findUser(currentName);

        if (current == null) return;

        for (String friend : current.friends) {
            if (!visited.contains(friend)) {
                User friendUser = findUser(friend);
                if (friendUser != null) {
                    for (String fof : friendUser.friends) {
                        if (!fof.equals(origin) && !findUser(origin).friends.contains(fof)) {
                            if (!recommendations.contains(fof)) {
                                recommendations.add(fof);
                            }
                        }
                    }
                }
                dfs(origin, friend, visited, recommendations);
            }
        }
    }

    // Print friend recommendations
    static void printRecommendations(ArrayList<String> recs) {
        if (recs.isEmpty()) {
            System.out.println("No recommendations.");
            return;
        }

        System.out.println("Recommended friends:");
        for (String rec : recs) {
            System.out.println("- " + rec);
        }
    }
}
