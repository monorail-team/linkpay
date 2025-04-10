package monorail.linkpay.algo.trie;


import monorail.linkpay.algo.JamoUtils;

import java.util.*;

public class PrefixTrie implements Trie {
    private static final PrefixTrie instance = new PrefixTrie();
    private static Node root = new Node();

    public static PrefixTrie getInstance() {
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
            for (String jamo : JamoUtils.splitOne(val)) {
                if (!node.childMap.containsKey(jamo)) {
                    node.childMap.put(jamo, new Node());
                }
                node = node.childMap.get(jamo);
            }
        }
        node.isEnd = true;
        node.val = sb.toString();
    }

    public List<String> searchAll(String keyword) {
        Optional<Node> nodeOptional = find(keyword);
        if (nodeOptional.isEmpty()) {
            return Collections.emptyList();
        }

        Node node = nodeOptional.get();
        // node를 루트로 하는 트리 탐색하면서 isWord가 true인 경우 val 추출
        List<String> result = new ArrayList<>();
        findTree(node, result);
        return result;
    }

    /**
     * DFS로 node의 하위 노드들을 탐색한 뒤 검색 대상(글 제목, 관광지명 등)인 경우(node.isEnd == true) result에 그 값(val:String)를 담는다.
     *
     * @param node
     * @param result
     */
    private void findTree(Node node, List<String> result) {
        if (node.isEnd) result.add(node.val);
        node.childMap.values().stream().forEach(child -> findTree(child, result));
    }

    private Optional<Node> find(String keyword) {
        Node node = root;
        String[] values = keyword.split("");

        for (String val : values) {
            for (String jamo : JamoUtils.splitOne(val)) {
                if (!node.childMap.containsKey(jamo)) {
                    return Optional.empty();
                }
                node = node.childMap.get(jamo);
            }
        }
        return Optional.of(node);
    }

    static class Node {
        private String val;
        private boolean isEnd;
        private Map<String, Node> childMap = new HashMap<>();

        public Node() {
        }
    }

}
