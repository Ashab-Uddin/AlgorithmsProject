import java.util.*;

public class fr {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==== Simple Friend System ====");
            System.out.println("1. Add User");
            System.out.println("2. Send Friend Request");
            System.out.println("3. Accept All Requests");
            System.out.println("4. Show Friends");
            System.out.println("5. Recommend Friends (BFS)");
            System.out.println("6. Recommend Friends (DFS)");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addUser();
                case 2 -> sendRequest();
                case 3 -> acceptRequests();
                case 4 -> showFriends();
                case 5 -> recommendBFS();
                case 6 -> recommendDFS();
                case 7 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
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

    static User findUser(String name) {
        for (User u : users) {
            if (u.name.equals(name)) return u;
        }
        return null;
    }

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

        if (fromName.equals(toName) || from.friends.contains(toName)) {
            System.out.println("Invalid or already friends.");
            return;
        }

        if (!to.requests.contains(fromName)) {
            to.requests.add(fromName);
            System.out.println("Friend request sent.");
        } else {
            System.out.println("Request already sent.");
        }
    }

    static void acceptRequests() {
        System.out.print("Enter your username: ");
        String name = sc.nextLine().trim();
        User user = findUser(name);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        if (user.requests.isEmpty()) {
            System.out.println("You have no friend requests.");
            return;
        }

        // Show all users who sent requests
        System.out.println("You have received friend requests from:");
        for (String requester : user.requests) {
            System.out.println(requester);
        }

        // Accept all requests
        for (String req : user.requests) {
            if (!user.friends.contains(req)) {
                user.friends.add(req);
                User sender = findUser(req);
                if (sender != null && !sender.friends.contains(name)) {
                    sender.friends.add(name);
                }
                System.out.println(name + " accepted friend request from " + req);
            }
        }

        user.requests.clear(); // Clear all the accepted requests
    }

    static void showFriends() {
        System.out.print("Enter username: ");
        String name = sc.nextLine().trim();
        User user = findUser(name);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println(name + "'s friends: " + user.friends);
    }

    static void recommendBFS() {
        System.out.print("Enter username: ");
        String name = sc.nextLine().trim();
        User start = findUser(name);

        if (start == null) {
            System.out.println("User not found.");
            return;
        }

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        ArrayList<String> recommendations = new ArrayList<>();

        visited.add(name);
        queue.add(name);

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
                            if (!fof.equals(name) && !start.friends.contains(fof)) {
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

    static void recommendDFS() {
        System.out.print("Enter username: ");
        String name = sc.nextLine().trim();
        User user = findUser(name);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        Set<String> visited = new HashSet<>();
        ArrayList<String> recommendations = new ArrayList<>();
        dfs(name, name, visited, recommendations);
        printRecommendations(recommendations);
    }

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
