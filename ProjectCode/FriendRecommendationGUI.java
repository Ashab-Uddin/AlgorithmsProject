import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

public class FriendRecommendationGUI extends JFrame {
    private Map<String, Set<String>> graph = new HashMap<>();
    private JTextArea outputArea;

    public FriendRecommendationGUI() {
        setTitle("Social Network Friend Recommendation");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        JTextField userField = new JTextField();
        JTextField friendField = new JTextField();
        JTextField recommendUserField = new JTextField();

        JButton addUserBtn = new JButton("Add User");
        JButton addFriendBtn = new JButton("Add Friendship");
        JButton recommendBFSBtn = new JButton("Recommend (BFS)");
        JButton recommendDFSBtn = new JButton("Recommend (DFS)");

        inputPanel.add(new JLabel("User:"));
        inputPanel.add(userField);
        inputPanel.add(new JLabel("Friend:"));
        inputPanel.add(friendField);
        inputPanel.add(new JLabel("Recommend For:"));
        inputPanel.add(recommendUserField);
        inputPanel.add(addUserBtn);
        inputPanel.add(addFriendBtn);

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(recommendBFSBtn);
        buttonPanel.add(recommendDFSBtn);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addUserBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            if (!user.isEmpty()) {
                graph.putIfAbsent(user, new HashSet<>());
                outputArea.append("User added: " + user + "\n");
            }
        });

        addFriendBtn.addActionListener(e -> {
            String u1 = userField.getText().trim();
            String u2 = friendField.getText().trim();
            if (!u1.isEmpty() && !u2.isEmpty()) {
                graph.putIfAbsent(u1, new HashSet<>());
                graph.putIfAbsent(u2, new HashSet<>());
                graph.get(u1).add(u2);
                graph.get(u2).add(u1);
                outputArea.append("Friendship added: " + u1 + " ↔ " + u2 + "\n");
            }
        });

        recommendBFSBtn.addActionListener(e -> {
            String user = recommendUserField.getText().trim();
            outputArea.append("BFS Recommendations for " + user + ":\n");
            for (String rec : recommendFriendsBFS(user)) {
                outputArea.append("- " + rec + "\n");
            }
        });

        recommendDFSBtn.addActionListener(e -> {
            String user = recommendUserField.getText().trim();
            outputArea.append("DFS Recommendations for " + user + ":\n");
            for (String rec : recommendFriendsDFS(user)) {
                outputArea.append("- " + rec + "\n");
            }
        });
    }

    private List<String> recommendFriendsBFS(String user) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> mutual = new HashMap<>();
        visited.add(user);
        queue.add(user);

        while (!queue.isEmpty()) {
            String curr = queue.poll();
            for (String friend : graph.getOrDefault(curr, new HashSet<>())) {
                if (!friend.equals(user)) {
                    if (!graph.get(user).contains(friend)) {
                        mutual.put(friend, mutual.getOrDefault(friend, 0) + 1);
                    }
                    if (!visited.contains(friend)) {
                        visited.add(friend);
                        queue.add(friend);
                    }
                }
            }
        }
        return sortByMutual(mutual);
    }

    private List<String> recommendFriendsDFS(String user) {
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();
        Map<String, Integer> mutual = new HashMap<>();
        visited.add(user);
        stack.push(user);

        while (!stack.isEmpty()) {
            String curr = stack.pop();
            for (String friend : graph.getOrDefault(curr, new HashSet<>())) {
                if (!friend.equals(user)) {
                    if (!graph.get(user).contains(friend)) {
                        mutual.put(friend, mutual.getOrDefault(friend, 0) + 1);
                    }
                    if (!visited.contains(friend)) {
                        visited.add(friend);
                        stack.push(friend);
                    }
                }
            }
        }
        return sortByMutual(mutual);
    }

    private List<String> sortByMutual(Map<String, Integer> mutual) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(mutual.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> e : list) {
            result.add(e.getKey() + " (" + e.getValue() + " mutual)");
        }
        return result;
    }

    public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            new FriendRecommendationGUI().setVisible(true);
        }
    });
}

}
