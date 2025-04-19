import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SimpleFriendRecommender extends JFrame {
    HashMap<String, ArrayList<String>> friends = new HashMap<>();

    JTextField userField = new JTextField(10);
    JTextField user1Field = new JTextField(10);
    JTextField user2Field = new JTextField(10);
    JTextField recommendField = new JTextField(10);
    JTextArea outputArea = new JTextArea(10, 30);

    public SimpleFriendRecommender() {
        setTitle("Friend Recommendation (Simple)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Add user
        add(new JLabel("New User:"));
        add(userField);
        JButton addUserBtn = new JButton("Add User");
        add(addUserBtn);

        // Add friendship
        add(new JLabel("Friendship (User1 & User2):"));
        add(user1Field);
        add(user2Field);
        JButton addFriendBtn = new JButton("Add Friendship");
        add(addFriendBtn);

        // Recommend
        add(new JLabel("Recommend for:"));
        add(recommendField);
        JButton recommendBtn = new JButton("Get Recommendation");
        add(recommendBtn);

        // Output
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea));

        // Button actions
        addUserBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            if (!user.isEmpty() && !friends.containsKey(user)) {
                friends.put(user, new ArrayList<>());
                outputArea.append("User added: " + user + "\n");
                userField.setText("");
            }
        });

        addFriendBtn.addActionListener(e -> {
            String u1 = user1Field.getText().trim();
            String u2 = user2Field.getText().trim();
            if (friends.containsKey(u1) && friends.containsKey(u2)) {
                friends.get(u1).add(u2);
                friends.get(u2).add(u1);
                outputArea.append("Friendship added between " + u1 + " and " + u2 + "\n");
                user1Field.setText("");
                user2Field.setText("");
            } else {
                outputArea.append("Both users must exist!\n");
            }
        });

        recommendBtn.addActionListener(e -> {
            String user = recommendField.getText().trim();
            if (!friends.containsKey(user)) {
                outputArea.append("User not found!\n");
                return;
            }
            HashMap<String, Integer> suggestion = new HashMap<>();

            for (String friend : friends.get(user)) {
                for (String friendOfFriend : friends.get(friend)) {
                    if (!friendOfFriend.equals(user) && !friends.get(user).contains(friendOfFriend)) {
                        suggestion.put(friendOfFriend, suggestion.getOrDefault(friendOfFriend, 0) + 1);
                    }
                }
            }

            outputArea.append("Recommendations for " + user + ":\n");
            for (Map.Entry<String, Integer> entry : suggestion.entrySet()) {
                outputArea.append("- " + entry.getKey() + " (" + entry.getValue() + " mutual friend(s))\n");
            }
            recommendField.setText("");
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleFriendRecommender();
    }
}
