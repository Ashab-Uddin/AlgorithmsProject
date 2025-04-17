import java.util.*;

public class FriendSystemConsole {
    static Scanner sc = new Scanner(System.in);
    static HashMap<String, ArrayList<String>> friends = new HashMap<>();
    static HashMap<String, ArrayList<String>> requests = new HashMap<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n====== Social Network Menu ======");
            System.out.println("1. Add User");
            System.out.println("2. Send Friend Request");
            System.out.println("3. Accept All Requests");
            System.out.println("4. Recommend Friends (BFS)");
            System.out.println("5. Recommend Friends (DFS)");
            System.out.println("6. Shortest Path (Dijkstra)");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addUser();
                case 2 -> sendRequest();
                case 3 -> acceptRequests();
                case 4 -> recommendBFS();
                case 5 -> recommendDFS();
                case 6 -> shortestPath();
                case 7 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    static void addUser() {
        System.out.print("Enter username: ");
        String user = sc.nextLine().trim();
        if (!friends.containsKey(user)) {
            friends.put(user, new ArrayList<>());
            requests.put(user, new ArrayList<>());
            System.out.println("User added: " + user);
        } else {
            System.out.println("User already exists!");
        }
    }

    static void sendRequest() {
        System.out.print("From: ");
        String from = sc.nextLine().trim();
        System.out.print("To: ");
        String to = sc.nextLine().trim();

        if (friends.containsKey(from) && friends.containsKey(to)) {
            if (!from.equals(to) && !friends.get(from).contains(to)) {
                if (!requests.get(to).contains(from)) {
                    requests.get(to).add(from);
                    System.out.println("Friend request sent from " + from + " to " + to);
                } else {
                    System.out.println("Request already sent.");
                }
            } else {
                System.out.println("Already friends or invalid request.");
            }
        } else {
            System.out.println("Both users must exist.");
        }
    }

    static void acceptRequests() {
        System.out.print("Enter your username: ");
        String user = sc.nextLine().trim();
        if (!friends.containsKey(user)) {
            System.out.println("User not found.");
            return;
        }
        for (String sender : requests.get(user)) {
            friends.get(user).add(sender);
            friends.get(sender).add(user);
            System.out.println(user + " accepted friend request from " + sender);
        }
        requests.get(user).clear();
    }

    static void recommendBFS() {
        System.out.print("Enter username: ");
        String user = sc.nextLine().trim();
        if (!friends.containsKey(user)) {
            System.out.println("User not found.");
            return;
        }

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> mutual = new HashMap<>();

        visited.add(user);
        queue.add(user);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : friends.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    for (String fof : friends.get(neighbor)) {
                        if (!fof.equals(user) && !friends.get(user).contains(fof)) {
                            mutual.put(fof, mutual.getOrDefault(fof, 0) + 1);
                        }
                    }
                }
            }
        }

        printRecommendations(mutual);
    }

    static void recommendDFS() {
        System.out.print("Enter username: ");
        String user = sc.nextLine().trim();
        if (!friends.containsKey(user)) {
            System.out.println("User not found.");
            return;
        }

        Set<String> visited = new HashSet<>();
        Map<String, Integer> mutual = new HashMap<>();
        dfs(user, visited, mutual, user);

        printRecommendations(mutual);
    }

    static void dfs(String current, Set<String> visited, Map<String, Integer> mutual, String origin) {
        visited.add(current);
        for (String neighbor : friends.getOrDefault(current, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                for (String fof : friends.get(neighbor)) {
                    if (!fof.equals(origin) && !friends.get(origin).contains(fof)) {
                        mutual.put(fof, mutual.getOrDefault(fof, 0) + 1);
                    }
                }
                dfs(neighbor, visited, mutual, origin);
            }
        }
    }

    static void shortestPath() {
        System.out.print("From: ");
        String start = sc.nextLine().trim();
        System.out.print("To: ");
        String end = sc.nextLine().trim();

        if (!friends.containsKey(start) || !friends.containsKey(end)) {
            System.out.println("One or both users not found.");
            return;
        }

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));

        for (String user : friends.keySet()) {
            dist.put(user, Integer.MAX_VALUE);
        }
        dist.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (current.equals(end)) break;

            for (String neighbor : friends.get(current)) {
                int alt = dist.get(current) + 1;
                if (alt < dist.get(neighbor)) {
                    dist.put(neighbor, alt);
                    prev.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        if (!prev.containsKey(end)) {
            System.out.println("No path found.");
            return;
        }

        LinkedList<String> path = new LinkedList<>();
        for (String at = end; at != null; at = prev.get(at)) {
            path.addFirst(at);
        }
        System.out.println("Shortest path: " + String.join(" -> ", path));
    }

    static void printRecommendations(Map<String, Integer> mutual) {
        if (mutual.isEmpty()) {
            System.out.println("No recommendations found.");
            return;
        }

        System.out.println("Recommended Friends:");
        mutual.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .forEach(entry -> System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " mutual friends)"));
    }
}