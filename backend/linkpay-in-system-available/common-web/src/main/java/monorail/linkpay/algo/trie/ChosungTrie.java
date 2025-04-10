package monorail.linkpay.algo.trie;


import monorail.linkpay.algo.JamoUtils;

import java.util.*;

public class ChosungTrie implements Trie {
    private static final ChosungTrie instance = new ChosungTrie();
    private static Node root = new Node();

    public static ChosungTrie getInstance() {
        return instance;
    }

    public void init(List<String> inputs) {
        inputs.stream().forEach(input -> insert(input));
    }

    public void insert(String input) {
        Node node = root;
        StringBuilder sb = new StringBuilder();
        String[] values = input.split("");

        for (String val : values) {
            sb.append(val);
            String chosung = JamoUtils.splitOne(val).get(0);

            if (!node.childMap.containsKey(chosung)) {
                node.childMap.put(chosung, new Node());
            }
            node = node.childMap.get(chosung);
        }
        if (!node.isEnd) {
            node.values = new ArrayList<>();
        }
        node.isEnd = true;
        node.values.add(sb.toString());
//        if(input.equals("어느 누구와 함께해도 남부럽지 않은 코스")){
//            System.out.println("node.values = " + node.values);
//        }
    }

    public List<String> searchAll(String keyword) {
        if (!JamoUtils.isChosungString(keyword)) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        String[] chosungs = keyword.split("");

        findTree(root, chosungs, 0, result);
        return result;
    }

    private void findTree(Node node, String[] chosungs, int depth, List<String> result) {
        if (depth >= chosungs.length) {
            findTree(node, result);
            return;
        }
        node.childMap.keySet().stream().filter(chosungs[depth]::equals).forEach(key -> findTree(node.childMap.get(key), chosungs, depth + 1, result));
    }

    private void findTree(Node node, List<String> result) {
        if (node.isEnd) result.addAll(node.values);
        node.childMap.values().stream().forEach(child -> findTree(child, result));
    }

    static class Node {
        private List<String> values;
        private boolean isEnd;
        private Map<String, Node> childMap = new HashMap<>();

        public Node() {
        }
    }

}
